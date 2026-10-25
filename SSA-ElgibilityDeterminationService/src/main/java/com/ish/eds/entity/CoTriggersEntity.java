package com.ish.eds.entity;

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
    private Byte[] coNoticePdf;
    private String triggerStatus="pending";
}
