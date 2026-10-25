package com.ish.dcs.bindings;

import lombok.Data;

import java.util.List;


@Data
public class DcSummaryReport {
    private String planName;
    private IncomeInputs incomeDetails;
    private EducationInputs educationDetails;
    private List<ChildInputs> childrenDetails;
    private CitizenAppRegistrationInputs citizenDetails;
//    private PlanSectionInputs planDetails;

}
