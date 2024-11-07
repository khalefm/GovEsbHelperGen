package com.example.GovEsbSignature.dto;

import lombok.Data;

@Data
public class ItemInfoDTO {
    private String itemNumber;
    private String hsCode;
    private String itemDescription;
    private String itemGoodsStateCode;
    private String itemUnitQuantityCode;
    private String itemUnitQuantity;
    private String itemSupplementaryQuantityCode;
    private String itemSupplementaryQuantity;
    private String itemPackageCode;
    private String itemPackageQuantity;
    private String itemOriginCountryCode;
    private String itemGrossWeight;
    private String itemNetWeight;
    private String itemUnitFobForeignCurrency;
    private String itemPriceForeignCurrency;
    private String itemFobForeignCurrency;
    private String itemUnitFobNationalCurrency;
    private String itemPriceNationalCurrency;
    private String itemFobNationalCurrency;

    // Getters and Setters
}

