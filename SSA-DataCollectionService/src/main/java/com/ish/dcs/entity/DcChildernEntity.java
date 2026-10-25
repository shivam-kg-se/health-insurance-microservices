package com.ish.dcs.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "DC_CHILDREN")
@Data
public class DcChildernEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer childId;
    private Integer caseNo;
    private LocalDate childDOB;
    private Long childSSN;
}
