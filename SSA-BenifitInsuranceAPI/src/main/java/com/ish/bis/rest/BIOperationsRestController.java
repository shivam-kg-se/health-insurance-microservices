package com.ish.bis.rest;

import com.ish.bis.service.IBenifitInsuranceMgmtService;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bi-api")
public class BIOperationsRestController {

    @Autowired
    private IBenifitInsuranceMgmtService biService;

    @GetMapping("/send")
    public ResponseEntity<String> sendAmount() throws Exception {
//        use service
        JobExecution execution = biService.sendAmountBenificiries();
        return new ResponseEntity<>(execution.getExitStatus().toString(), HttpStatus.OK);
    }
}
