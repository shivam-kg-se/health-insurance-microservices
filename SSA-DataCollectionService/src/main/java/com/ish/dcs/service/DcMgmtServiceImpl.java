package com.ish.dcs.service;

import com.ish.dcs.bindings.*;
import com.ish.dcs.entity.*;
import com.ish.dcs.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DcMgmtServiceImpl implements IDcMgmtService{

    @Autowired
    private IDcCaseRepository caseRepository;
    @Autowired
    private IApplicationRegistrationRepository citizenAppRepo;

    @Autowired
    private IDcIncomeRepository incomeRepository;

    @Autowired
    private IDcEducationRepository educationRepository;
//    load citizen data
    @Autowired
    private IDcChildernRepository childernRepository;
    @Autowired
    private IPlanRepository planRepository;




    @Override
    public Integer generateCaseNo(Integer appId) {
        Optional<CitizenApplicationEntity> appCitizen = citizenAppRepo.findById(appId);
        if(appCitizen.isPresent()){
            DcCaseEntity caseEntity = new DcCaseEntity();
            caseEntity.setAppId(appId);
            return caseRepository.save(caseEntity).getCaseNo();
        }
        return null;
    }

    @Override
    public List<String> showAllPlanNames() {
        return List.of();
    }

    @Override
    public Integer savePlanSelection(PlanSectionInputs plan) {
//        load DccaseEntity Object
        Optional<DcCaseEntity> opt = caseRepository.findById(plan.getCaseNo());
        if(opt.isPresent()){
            DcCaseEntity caseEntity = opt.get();
           caseEntity.setPlanId(plan.getPlanId());
//           update the DcCaseEntity with plan id
            caseRepository.save(caseEntity);
            return caseEntity.getCaseNo();
        }
        return 0;
    }

    @Override
    public Integer saveIncomeDetails(IncomeInputs income) {
//        convert binding object data to entity class obj
        DcIncomeEntity incomeEntity = new DcIncomeEntity();
        BeanUtils.copyProperties(income,incomeEntity);
//        save the income details
        incomeRepository.save(incomeEntity);
        return income.getCaseNo();
    }

    @Override
    public Integer saveEducationDetails(EducationInputs education) {
//        convert binding obj to entity obj
        DcEducationEntity educationEntity = new DcEducationEntity();
        BeanUtils.copyProperties(education,educationEntity);
        educationRepository.save(educationEntity);
        return education.getCaseNo();
    }

    @Override
    public Integer saveChildernDetails(List<ChildInputs> children) {
//        convert each binding class obj to each entity obj
        children.forEach(child -> {
            DcChildernEntity childEntity = new DcChildernEntity();
            BeanUtils.copyProperties(child,childEntity);
//            ?save each entity
            childernRepository.save(childEntity);
        });
//        return case no
        return children.get(0).getCaseNo();
    }

    @Override
    public DcSummaryReport showSCSummary(Integer caseNo) {
//        get multiple entity objs based on caseNo
        DcIncomeEntity incomeEntity = incomeRepository.findByCaseNo(caseNo);
        DcEducationEntity educationEntity = educationRepository.findByCaseNo(caseNo);
        List<DcChildernEntity> childernEntities = childernRepository.findByCaseNo(caseNo);
        Optional<DcCaseEntity> optCaseEntity = caseRepository.findById(caseNo);
        String planName=null;
        Integer appId =null;
        if(optCaseEntity.isPresent()) {
            DcCaseEntity caseEntity = optCaseEntity.get();
            Integer planId = optCaseEntity.get().getPlanId();
             appId = caseEntity.getAppId();
            Optional<PlanEntity> optPlanEntity = planRepository.findById(planId);
            if(optPlanEntity.isPresent()) {
                planName = optPlanEntity.get().getPlanName();
            }
        }
        Optional<CitizenApplicationEntity> optcitizenEntity = citizenAppRepo.findById(appId);
        CitizenApplicationEntity citizenEntity =null;
        if(optcitizenEntity.isPresent()) {
            citizenEntity = optcitizenEntity.get();
        }

//        convert entity obj to binding obj
        IncomeInputs incomeInputs = new IncomeInputs();
        BeanUtils.copyProperties(incomeEntity,incomeInputs);
        EducationInputs educationInputs = new EducationInputs();
        BeanUtils.copyProperties(educationEntity,educationInputs);
        List<ChildInputs> listchild = new ArrayList<>();
        childernEntities.forEach(childEntity -> {
            ChildInputs child = new ChildInputs();
            BeanUtils.copyProperties(childEntity,child);
            listchild.add(child);
        });
        CitizenAppRegistrationInputs citizen = new CitizenAppRegistrationInputs();
        BeanUtils.copyProperties(citizenEntity,citizen);
//        prepare Dcsummary report
        DcSummaryReport report = new DcSummaryReport();
        report.setPlanName(planName);
        report.setIncomeDetails(incomeInputs);
        report.setEducationDetails(educationInputs);
        report.setChildrenDetails(listchild);
        report.setCitizenDetails(citizen);
        return report;
    }
}
