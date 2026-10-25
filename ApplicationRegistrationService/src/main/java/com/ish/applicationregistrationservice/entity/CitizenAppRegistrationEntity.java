package com.ish.applicationregistrationservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "CITIZEN_APPLICATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenAppRegistrationEntity {

    @Id
    @SequenceGenerator(name = "gen1_seq", sequenceName = "App_id-seq", initialValue = 1000,allocationSize = 1)
    @GeneratedValue(generator = "gen1_seq",strategy = GenerationType.SEQUENCE)
    private Integer appId;
    @Column(length = 30)
    private String fullname;
    private String email;
    private String gender;
    private long ssn;
    private Long phoneNo;
    private String StateName;
    private LocalDate dob;
    private String cerateBy;
    private String updateby;
    private LocalDateTime updationDate;
    private LocalDateTime creationDate;
}
