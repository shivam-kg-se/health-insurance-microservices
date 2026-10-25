package com.ish.cas.ms;

import com.ish.cas.bindings.CoSummary;
import com.ish.cas.service.ICorrespondenceMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/co-triggers-api")
public class CoTriggersOpertionsController {
    @Autowired
    private ICorrespondenceMgmtService correspondenceMgmtService;

    @GetMapping("/process")
    public ResponseEntity<CoSummary> processAndUpdateTriggers(){
        CoSummary summary = correspondenceMgmtService.processPendingTriggers();
        return ResponseEntity.ok(summary);
    }
}
