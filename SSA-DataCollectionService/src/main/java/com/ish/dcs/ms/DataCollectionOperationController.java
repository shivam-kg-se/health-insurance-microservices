package com.ish.dcs.ms;

import com.ish.dcs.bindings.*;
import com.ish.dcs.service.IDcMgmtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dc-api")
@Tag(name = "Data Collection Management", description = "Operations related to data collection management")
public class DataCollectionOperationController {

    @Autowired
    private IDcMgmtService dcService;

    @GetMapping("/planNames")
    public ResponseEntity<List<String>> displayPlanName(){
        List<String> listPlanNames = dcService.showAllPlanNames();
//        return ResponseEntity.ok().body(listPlanNames);
        return new ResponseEntity<>(listPlanNames, HttpStatus.OK);

    }

    @PostMapping("/generateCaseNo/{appId}")
    public ResponseEntity<Integer> generteCaseNo(@PathVariable Integer appId){
        Integer caseNo = dcService.generateCaseNo(appId);
        return new ResponseEntity<>(caseNo, HttpStatus.OK);
    }

    @PutMapping("/updatePlanSelection")
    public ResponseEntity<Integer>savePlanSelection(@RequestBody PlanSectionInputs inputs){
        Integer caseNo = dcService.savePlanSelection(inputs);
        return new ResponseEntity<>(caseNo, HttpStatus.OK);
    }
    @PostMapping("/saveIncome")
    public ResponseEntity<Integer> saveIncomeDetails(@RequestBody IncomeInputs income){
        Integer caseNo = dcService.saveIncomeDetails(income);
        return new ResponseEntity<>(caseNo, HttpStatus.CREATED);
    }
    @PostMapping("/saveEducation")
    public ResponseEntity<Integer> saveEducationDetails(@RequestBody EducationInputs education){
        Integer caseNo = dcService.saveEducationDetails(education);
        return new ResponseEntity<>(caseNo, HttpStatus.CREATED);
    }
    @PostMapping("/saveChildren")
    public ResponseEntity<Integer> saveChildernDetails(@RequestBody List<ChildInputs> children){
        Integer caseNo = dcService.saveChildernDetails(children);
        return new ResponseEntity<>(caseNo, HttpStatus.CREATED);
    }
    @GetMapping("/summary")
    public ResponseEntity<DcSummaryReport> showReport(@PathVariable Integer CaseNo){
            DcSummaryReport report = dcService.showSCSummary(CaseNo);
            return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
