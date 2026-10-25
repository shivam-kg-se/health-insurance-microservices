package com.ish.aaa.services;

import com.ish.aaa.bindings.PlanData;
import com.ish.aaa.entity.PlanCategory;
import com.ish.aaa.entity.PlanEntity;
import com.ish.aaa.repository.IPlanCategoryRepository;
import com.ish.aaa.repository.IPlanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanManagementServiceImpl implements IPlanMgmtService {

    @Autowired
    private IPlanRepository planRepository;
    @Autowired
    private IPlanCategoryRepository planCategoryRepository;

    @Override
    public String registerPlan(PlanData plan) {
//        convert plan data binding obj to planEntity
        PlanEntity entity = new PlanEntity();
        BeanUtils.copyProperties(plan,entity);
//        save the object
        PlanEntity saveEntity = planRepository.save(entity);
        Integer planId= saveEntity.getPlanId();
        if(planId!=null){
            return "register paln with id; " +planId;
        }
        throw new RuntimeException("user not registered successfully");
    }

    @Override
    public Map<Integer, String> getPlanCategories() {
        List<PlanCategory> list = planCategoryRepository.findAll();
        Map<Integer, String> categoryMap = new HashMap<>();
        list.forEach(category->{
            categoryMap.put(category.getCategoryId(),category.getCategoryName());
        });
        return categoryMap;
    }

    @Override
    public List<PlanData> showAllPlans() {
        List<PlanEntity> list = planRepository.findAll();
        List<PlanData> planDataList = new ArrayList<>();
        list.forEach(plan->{
            PlanData planData = new PlanData();
            BeanUtils.copyProperties(plan,planData);
            planDataList.add(planData);
        });
        return planDataList;
    }

    @Override
    public PlanData showPlanById(Integer plan) {
        Optional<PlanEntity> optPlandId = planRepository.findById(plan);
        if(optPlandId.isPresent()){
           PlanData data = new PlanData();
           BeanUtils.copyProperties(optPlandId.get(),data);
           return data;
        }
        return null;
    }

    @Override
    public String updatePlan(PlanData plan) {
        Optional<PlanEntity> optEntity = planRepository.findById(plan.getPlanId());
        if(optEntity.isPresent()){
            PlanEntity entity = optEntity.get();
            BeanUtils.copyProperties(plan,entity);
            planRepository.save(entity);
            return "plan updated successfulley";
        }
        return "plan not found";
    }

    @Override
    public String changePlanStatus(Integer planId, String status) {
        Optional<PlanEntity> optEntity = planRepository.findById(planId);
        if (optEntity.isPresent()) {
            PlanEntity entity = optEntity.get();
            entity.setActiveSw(status);
            planRepository.save(entity);
            return "status change sucessfulley "+planId;
        }
        return "no changes in plan status";
    }

    @Override
    public String deletePlan(Integer planId) {
        Optional<PlanEntity> optEntity = planRepository.findById(planId);
        if(optEntity.isPresent()){
            planRepository.deleteById(planId);
            return "plan id "+planId +"delete sucessfulley";
        }
        return "plan id delete failure";
    }
}
