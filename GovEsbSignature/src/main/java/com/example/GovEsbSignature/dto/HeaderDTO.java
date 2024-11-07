package com.example.GovEsbSignature.dto;

import lombok.Data;

@Data
public class HeaderDTO {
    private String interfaceId;
    private String sendDateAndTime;
    private String senderId;
    private String receiverId;
    private String referenceNumber;
    private String ucrNumber;

    // Getters and Setters
}

