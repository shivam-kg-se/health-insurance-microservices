package com.ish.dcs.bindings;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChildInputs {
    private Integer childId;
    private Integer CaseNo;
    private LocalDate childDOB;
    private Long childSSN;
}

