package com.ish.dcs.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "PLAN_MASTER")
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(length = 30)
    private String description;
    private String activeSw;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate creationDate;
    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDate updationDate;
    @Column(length = 30)
    private String createdBy;
    @Column(length = 30)
    private String updatedBy;
}
