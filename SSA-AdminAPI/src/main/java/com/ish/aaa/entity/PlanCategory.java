package com.ish.aaa.entity;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "PLAN_CATEGORY")
public class PlanCategory {
    @Id
    @SequenceGenerator(name = "gen1" ,sequenceName = "category_seq",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoryId;
    private String categoryName;
    private String active_SW="active";
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updatedDate;
    private String createdBy;
    private String updatedBy;
}
