package com.example.GovEsbSignature.dto;

import lombok.Data;

@Data
public class InvoiceInfoDTO {
    private String deliveryTermCode;
    private String currencyCode;
    private String exchangeRate;
    private String fobForeignCurrency;
    private String freightForeignCurrency;
    private String insuranceForeignCurrency;
    private String otherForeignCurrency;
    private String totalForeignCurrency;
    private String fobNationalCurrency;
    private String freightNationalCurrency;
    private String insuranceNationalCurrency;
    private String otherNationalCurrency;
    private String totalNationalCurrency;

    // Getters and Setters
}
