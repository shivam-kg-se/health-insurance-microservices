package com.ish.cas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Table(name = "ELGIBILITY_DETAILS")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ElgibilityDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer edTraceId;
    private Integer caseNo;
    @Column(length = 30)
    private String holderName;
    private Long holderSSN;
    @Column(length = 30)
    private String planName;
    @Column(length = 30)
    private String planStatus;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private Double benifitAmt;
    @Column(length = 30)
    private String denialReson;



}
