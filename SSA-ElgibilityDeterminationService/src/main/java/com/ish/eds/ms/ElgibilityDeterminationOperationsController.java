package com.ish.eds.ms;

import com.ish.eds.bindings.ElgibilityDetailsOutput;
import com.ish.eds.services.IElgibilityDeterminationMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ed-api")
public class ElgibilityDeterminationOperationsController {

    @Autowired
    private IElgibilityDeterminationMgmtService elgibilityDeterminationMgmtService;

    @GetMapping("/determine/{caseNo}")
    public ResponseEntity<ElgibilityDetailsOutput> showPlanElgibility(@PathVariable Integer caseNo) {
//        use service
        ElgibilityDetailsOutput output = elgibilityDeterminationMgmtService.determineElgibility(caseNo);
//        return ResponseEntity.accepted().body(output);
        return  new ResponseEntity<>(output, HttpStatus.CREATED);
    }
}
