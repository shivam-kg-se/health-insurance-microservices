package com.ish.aaa.bindings;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanCategoryData {
    private Integer categoryId;
    private String categoryName;
    private String active_SW="active";
}
