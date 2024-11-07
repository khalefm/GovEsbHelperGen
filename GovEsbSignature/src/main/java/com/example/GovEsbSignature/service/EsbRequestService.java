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

//    @Value("${esb.private-key}")
//    private String privateKeyPem;

    @Value("${esb.public-key}")
    private String esbPublicKey;

    @Value("${esb.client-id}")
    private String clientId;

    @Value("${esb.client-secret}")
    private String clientSecret;

    @Value("${esb.token-url}")
    private String esbTokenUrl;

    @Value("${esb.engine-url}")
    private String esbEngineUrl;


    @Autowired
    private ObjectMapper objectMapper;

    public String createEsbRequest(EsbRequestDTO requestDTO, String apiCode) throws IOException, GeneralSecurityException {
        // Map DTO to JSON nodes
        ObjectNode esbBodyNode = objectMapper.createObjectNode();

        // Convert header
        ObjectNode headerNode = objectMapper.convertValue(requestDTO.getHeader(), ObjectNode.class);

        // Include apiCode in the header or body as needed
        headerNode.put("apiCode", apiCode); // Add apiCode to the header

        // Add client ID and public key to the header (example usage)
        headerNode.put("clientId", clientId);
        headerNode.put("publicKey", esbPublicKey);
        headerNode.put("clientSecret", clientSecret);
        headerNode.put("esbTokenUrl", esbTokenUrl);
        headerNode.put("esbEngineUrl", esbEngineUrl);

        // Convert other sections
        ObjectNode messageInfoNode = objectMapper.createObjectNode();
        messageInfoNode.set("general_info", objectMapper.convertValue(requestDTO.getGeneralInfo(), ObjectNode.class));
        messageInfoNode.set("transport_info", objectMapper.convertValue(requestDTO.getTransportInfo(), ObjectNode.class));
        messageInfoNode.set("invoice_info", objectMapper.convertValue(requestDTO.getInvoiceInfo(), ObjectNode.class));

        // Convert list of items
        ArrayNode itemsNode = objectMapper.valueToTree(requestDTO.getItems());
        messageInfoNode.set("item_info", itemsNode);

        esbBodyNode.set("header", headerNode);
        esbBodyNode.set("message_info", messageInfoNode);

        // Create final data structure
        ObjectNode dataNode = objectMapper.createObjectNode();
        dataNode.set("esbBody", esbBodyNode);

        String dataString = objectMapper.writeValueAsString(dataNode);
        String signature = signText(dataString, loadPrivateKey());

        // Wrap with final request node
        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.set("data", dataNode);
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

    private PrivateKey loadPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Use your private key directly instead of loading it from a file
        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                "MGACAQAwEAYHKoZIzj0CAQYFK4EEACMESTBHAgEBBEIA69+H+HhhiWbk+deFRVMvJlKnUiuPFVlpckZPcXWki6+JWTmzehtzQrdwglCUIPq0JXZp6cfDgzNGNf8+ZEtBmk0=\n" +
                "-----END PRIVATE KEY-----";

        // Convert PEM to PrivateKey
        byte[] keyBytes = Base64.decode(privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""));

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

}

