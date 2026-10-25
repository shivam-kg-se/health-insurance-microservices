package com.ish.aaa.services;

import com.ish.aaa.bindings.PlanData;

import java.util.List;
import java.util.Map;

public interface IPlanMgmtService {
    public String registerPlan(PlanData plan);
    public Map<Integer,String > getPlanCategories();
    public List<PlanData> showAllPlans();
    public PlanData showPlanById(Integer plan);
    public String updatePlan(PlanData plan);
    public String changePlanStatus(Integer planId,String status);
    public String deletePlan(Integer planId);
}
