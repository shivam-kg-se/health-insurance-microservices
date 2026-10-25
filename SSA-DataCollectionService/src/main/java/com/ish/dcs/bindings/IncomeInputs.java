package com.ish.dcs.bindings;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public class IncomeInputs {
    private Integer incomeId;
    private Integer caseNo;
    private Double empIncome;
    private Double propertyIncome;
}
