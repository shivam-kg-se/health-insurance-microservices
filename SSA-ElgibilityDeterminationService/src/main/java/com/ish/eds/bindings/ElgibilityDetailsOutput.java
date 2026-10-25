package com.ish.eds.bindings;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ElgibilityDetailsOutput {
    private String holderName;
    private String planName;
    private String planStatus;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private Double benifitAmt;
    private String denialReson;
}
