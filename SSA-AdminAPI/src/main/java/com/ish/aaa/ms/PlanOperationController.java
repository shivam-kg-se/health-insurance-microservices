package com.ish.aaa.ms;

import com.ish.aaa.bindings.PlanData;
import com.ish.aaa.services.IPlanMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan-api")
public class PlanOperationController {

    @Autowired
    private IPlanMgmtService planServices;

    @GetMapping("/categories")
    public ResponseEntity<?> showPlanCategory(){
        Map<Integer,String> category = planServices.getPlanCategories();
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("/register")
    public ResponseEntity<String> savePlan(@RequestBody PlanData plan){
        String msg = planServices.registerPlan(plan);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlanData>> getAllPlans(){
        List<PlanData> list = planServices.showAllPlans();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/find/{planId}")
    public ResponseEntity<PlanData> getPlanById(@PathVariable Integer planId){
        PlanData plan= planServices.showPlanById(planId);
        return ResponseEntity.ok().body(plan);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updatePlan(@RequestBody PlanData plan){
        String msg = planServices.updatePlan(plan);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{planId}")
    public ResponseEntity<String> removePlanByPlanId(@PathVariable Integer planId){
        String msg = planServices.deletePlan(planId);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/status-change/{planId}/{status}")
    public ResponseEntity<String> changeStatusByPlanId(@PathVariable Integer planId,@PathVariable String status){
        String msg = planServices.changePlanStatus(planId,status);
        return ResponseEntity.ok(msg);
    }

}
