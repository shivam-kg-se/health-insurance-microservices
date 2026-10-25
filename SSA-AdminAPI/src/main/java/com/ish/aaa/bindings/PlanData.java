package com.ish.aaa.bindings;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanData {
    private Integer planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String activeSw;
}
