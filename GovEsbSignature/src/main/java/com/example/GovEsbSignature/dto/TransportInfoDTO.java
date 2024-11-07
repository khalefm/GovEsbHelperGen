package com.example.GovEsbSignature.dto;

import lombok.Data;

@Data
public class TransportInfoDTO {
    private String transportModeCode;
    private String transportationName;
    private String transportationNumber;
    private String remark;
    private String arrivalPortCode;
    private String departurePortCode;
    private String customerOfficeCode;
    private String blNumber;

    // Getters and Setters
}

