package com.ish.eds.services;


import com.ish.eds.bindings.ElgibilityDetailsOutput;
import com.ish.eds.entity.*;
import com.ish.eds.globalexception.ResourceNotFoundException;
import com.ish.eds.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ElgibilityDeterminationMgmtServiceImpl implements IElgibilityDeterminationMgmtService {

    private final IDcCaseRepository dcCaseRepository;
    private final IPlanRepository planRepository;
    private final IDcIncomeRepository incomeRepository;
    private final IDcChildernRepository childernRepository;
    private final IApplicationRegistrationRepository applicationRegistrationRepository;
    private final IDcEducationRepository educationRepository;
    private final IElgibilityDeterminationRepository elgibilityRepository;
    private final ICOTriggerRepository triggerRepository;

    public ElgibilityDeterminationMgmtServiceImpl(
            IDcCaseRepository dcCaseRepository,
            IPlanRepository planRepository,
            IDcIncomeRepository incomeRepository,
            IDcChildernRepository childernRepository,
            IApplicationRegistrationRepository applicationRegistrationRepository,
            IDcEducationRepository educationRepository,
            IElgibilityDeterminationRepository elgibilityRepository,
            ICOTriggerRepository triggerRepository) {
        this.dcCaseRepository = dcCaseRepository;
        this.planRepository = planRepository;
        this.incomeRepository = incomeRepository;
        this.childernRepository = childernRepository;
        this.applicationRegistrationRepository = applicationRegistrationRepository;
        this.educationRepository = educationRepository;
        this.elgibilityRepository = elgibilityRepository;
        this.triggerRepository = triggerRepository;
    }

    @Override
    public ElgibilityDetailsOutput determineElgibility(Integer caseNo) {
        DcCaseEntity caseEntity = dcCaseRepository.findById(caseNo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No eligibility case was found with case number " + caseNo + "."));
        Integer appId = caseEntity.getAppId();
        Integer planId = caseEntity.getPlanId();

        if (planId == null) {
            throw new ResourceNotFoundException(
                    "Case " + caseNo + " is not linked to an eligibility plan.");
        }
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The eligibility plan for case " + caseNo + " could not be found."));
        String planName = planEntity.getPlanName();

        if (appId == null) {
            throw new ResourceNotFoundException(
                    "Case " + caseNo + " is not linked to a citizen application.");
        }
        CitizenApplicationEntity citizenEntity = applicationRegistrationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The citizen application for case " + caseNo + " could not be found."));
        Integer citizenAge = Period.between(citizenEntity.getDob(), LocalDate.now()).getYears();
        String citizenName = citizenEntity.getFullName();
//        call helper method to plan condition
        ElgibilityDetailsOutput eligoutput = applyPlanCondition(caseNo,planName,citizenAge);
//        save eligibility entity object
//        yaha per ElgibilityDetailsOutput ke sare entity ko ElgibilityDetailsEntity ke matched entity obj mai map karta hai
        ElgibilityDetailsEntity elgibilitydetailsEntity = new ElgibilityDetailsEntity();
        eligoutput.setHolderName(citizenName);
        BeanUtils.copyProperties(eligoutput,elgibilitydetailsEntity);
        elgibilityRepository.save(elgibilitydetailsEntity);

//        save to cotrigger object
        CoTriggersEntity triggersEntity = new CoTriggersEntity();
        triggersEntity.setCaseNo(caseNo);
        triggersEntity.setTriggerStatus("pending");
        triggerRepository.save(triggersEntity);
        return eligoutput;
    }
    private ElgibilityDetailsOutput applyPlanCondition(Integer caseNo, String planName,Integer citizenAge) {
        ElgibilityDetailsOutput eligoutput = new ElgibilityDetailsOutput();
        eligoutput.setPlanName(planName);
//        get income details of the citizen
        DcIncomeEntity incomeEntity = incomeRepository.findByCaseNo(caseNo);
        if (incomeEntity == null) {
            throw new ResourceNotFoundException(
                    "Income information for case " + caseNo + " could not be found.");
        }
        double empIncome = incomeEntity.getEmpIncome();
        double propertyIncome = incomeEntity.getPropertyIncome();
        if (planName == null || planName.isBlank()) {
            throw new ResourceNotFoundException(
                    "The eligibility plan for case " + caseNo + " does not have a valid plan name.");
        }
//        For SNAP
        if(planName.equalsIgnoreCase("SNAP")){
            if(empIncome<=300){
                eligoutput.setPlanStatus("approved");
                eligoutput.setBenifitAmt(200.0);
            }
            else {
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("emp income is less than 300");
            }
        }
        else if (planName.equalsIgnoreCase("CCAP")) {

            boolean kidsCountCondition = false;
            boolean kidAgeCondition = false;

            List<DcChildernEntity> listchild =
                    childernRepository.findByCaseNo(caseNo);

            if (!listchild.isEmpty()) {
                kidsCountCondition = true;
                for (DcChildernEntity child : listchild) {
                    int kidAge = Period.between(child.getChildDOB(), LocalDate.now()).getYears();
                    if (kidAge < 16) {
                        kidAgeCondition = true;
                        break;
                    }
                }
            }

            if (empIncome <=300 && kidsCountCondition && kidAgeCondition) {
                // Approved
                eligoutput.setPlanStatus("approved");
                eligoutput.setBenifitAmt(300.0);
            } else {
                // Denied
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("emp income is not within the limit or child conditions are not met");
            }
        }
        else if (planName.equalsIgnoreCase("MEDCARE")) {
            if(citizenAge>=60){
                eligoutput.setPlanStatus("approved");
                eligoutput.setBenifitAmt(200.0);
            }
            else{
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("citizen age is less than 65");
            }

        }

        else if (planName.equalsIgnoreCase("MEDICAID")) {
            if(empIncome<=300 && propertyIncome==0){
                eligoutput.setPlanStatus("approved");
                eligoutput.setBenifitAmt(200.0);
            } else {
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("emp income is not within the limit or property income should be zero");
            }
        }
        else if(planName.equalsIgnoreCase("CAJW")){
            Optional<DcEducationEntity> optEducationEntity = educationRepository.findById(caseNo);
            DcEducationEntity educationEntity = optEducationEntity
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Education information for case " + caseNo + " could not be found."));
            Integer passOutYear = educationEntity.getPassOutYear();
            if(empIncome==0&&passOutYear<LocalDate.now().getYear() ){
                eligoutput.setPlanStatus("approved");
                eligoutput.setBenifitAmt(300.0);
            }
            else {
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("emp income should be zero and pass out year should be less than current year");
            }
        } else if (planName.equalsIgnoreCase("QHP")) {
            if(citizenAge>1){
                eligoutput.setPlanStatus("approved");
            }
            else {
                eligoutput.setPlanStatus("denied");
                eligoutput.setDenialReson("QHP rules are denied");
            }
        } else {
            throw new IllegalArgumentException(
                    "The eligibility plan '" + planName + "' is not supported.");
        }
//        set the common properties for eliginput only if the plan is approved
        if(eligoutput.getPlanStatus().equalsIgnoreCase("approved")){
            eligoutput.setPlanStartDate(LocalDate.now());
            eligoutput.setPlanEndDate(LocalDate.now().plusYears(2));
        }
        return eligoutput;
    }
}
