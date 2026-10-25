package com.ish.eds.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "DC_INCOME")
public class DcIncomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer incomeId;
    private Integer caseNo;
    private Double empIncome;
    private Double propertyIncome;
}
