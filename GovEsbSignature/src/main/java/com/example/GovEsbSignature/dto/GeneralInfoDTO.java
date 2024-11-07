package com.example.GovEsbSignature.dto;

import lombok.Data;

@Data
public class GeneralInfoDTO {
    private String applicationNumber;
    private int versionNumber;
    private String orgCode;
    private String applicationCode;
    private String submissionDate;
    private String approvalDate;
    private String status;
    private String validDate;
    private String masterNumber;
    private String applicantCode;
    private String applicantCountry;
    private String applicantPostalAddress;
    private String applicantPhysicalAddress;
    private String applicantContactName;
    private String applicantTelephoneNumber;
    private String applicantEmail;

    // Getters and Setters
}

