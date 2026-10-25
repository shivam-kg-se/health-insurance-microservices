package com.ish.bis.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Date;
import java.util.Map;

@Service
public class BenifitInsuranceMgmtServiceImpl implements IBenifitInsuranceMgmtService {

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job job;

    @Override
    public JobExecution sendAmountBenificiries() throws Exception {
        JobParameter<Date> param = new JobParameter<>(new Date(),Date.class);
        Map<String,JobParameter<?>> map =Map.of("param1",param);
        JobParameters params = new JobParameters(map);
        JobExecution execution = launcher.run(job,params);
        System.out.println("jobExecution status :"+ execution.getExitStatus());
         return execution;
    }
}