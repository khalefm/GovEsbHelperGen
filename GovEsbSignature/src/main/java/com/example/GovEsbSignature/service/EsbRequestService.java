package com.example.GovEsbSignature.service;

import com.example.GovEsbSignature.dto.EsbRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Service

public class EsbRequestService {

    @Value("${ESB_PUBLIC_KEY}")
    private String esbPublicKey;

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;


    @Value("${PRIVATE_KEY}")
    private String privateKeyPem;


    @Autowired
    private ObjectMapper objectMapper;

    public String createEsbRequest(ObjectNode payload, String apiCode, String codeType) throws IOException, GeneralSecurityException {

        // Ensure the payload has the required structure
        if (!payload.has("header") || !payload.has("message_info")) {
            throw new IllegalArgumentException("Payload must contain 'header' and 'message_info'");
        }

        // Map DTO to JSON nodes
        ObjectNode esbBodyNode = objectMapper.createObjectNode();
        // Construct the esbBody structure
        esbBodyNode.set("header", payload.get("header"));
        esbBodyNode.set("message_info", payload.get("message_info"));

        // Create the complete payload structure
        ObjectNode completePayload = objectMapper.createObjectNode();
        completePayload.put(codeType,apiCode);
        completePayload.set("esbBody", esbBodyNode);

        // Convert the complete payload to a string signing
        String dataString = objectMapper.writeValueAsString(completePayload);


        // Create signature using provided signing method
        String signature = signText(dataString, loadPrivateKey());

        // Assemble the final request with the signature
        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.set("data", completePayload);
        requestNode.put("signature", signature);

        return objectMapper.writeValueAsString(requestNode);
    }

    private String signText(String plainText, PrivateKey privateKey) throws GeneralSecurityException {
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();
        return Base64.toBase64String(signature);
    }

    private PrivateKey loadPrivateKey() throws GeneralSecurityException {

        // Convert PEM to PrivateKey
        byte[] keyBytes = Base64.decode(privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""));


        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

}

