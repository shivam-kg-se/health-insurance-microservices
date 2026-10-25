package com.ish.bis.bindings;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ElgibilityDetails {
    private Integer caseNo;
    private String holderName;
    private Long holderSSN;
    private String planName;
    private String planStatus;
    private Double benifitAmt;
    private String bankName;
    private String accountNumber;
    private LocalDateTime planEndDate;

}
