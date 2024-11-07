package com.example.GovEsbSignature.dto;

import lombok.Data;

import java.util.List;

@Data
public class EsbRequestDTO {
    private HeaderDTO header;
    private GeneralInfoDTO generalInfo;
    private TransportInfoDTO transportInfo;
    private InvoiceInfoDTO invoiceInfo;
    private List<ItemInfoDTO> items;
    // Add similar fields for vehicleInfo, seedInfo, weaponInfo, attachmentsInfo if needed

    // Getters and Setters
}

