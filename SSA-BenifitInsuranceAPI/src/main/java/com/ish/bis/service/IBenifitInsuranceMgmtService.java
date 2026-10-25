package com.ish.bis.service;

import org.springframework.batch.core.JobExecution;

public interface IBenifitInsuranceMgmtService  {
    public JobExecution sendAmountBenificiries() throws  Exception;
}
