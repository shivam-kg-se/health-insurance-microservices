package com.ish.dcs.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Entity
@Data
@Table(name = "DC_EDUCATION")
public class DcEducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer educationId;
    private Integer caseNo;
    @Column(length = 40)
    private Integer highestQlfy;
    private Integer passOutYear;


}
