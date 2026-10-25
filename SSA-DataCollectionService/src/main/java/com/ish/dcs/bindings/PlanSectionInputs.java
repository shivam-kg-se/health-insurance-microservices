package com.ish.dcs.bindings;

import lombok.Data;

@Data
public class PlanSectionInputs {
    private Integer caseNo;
    private Integer appId;
    private Integer planId;
    private String planName;
}
