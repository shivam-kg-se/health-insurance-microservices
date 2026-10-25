package com.ish.cas.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CO_TRIGGERS")
@Data
public class CoTriggersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer triggerId;
    private Integer caseNo;
    @Lob
    private byte[] coNoticePdf;
    private String triggerStatus="pending";
}
