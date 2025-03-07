package com.exemplo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${zapi.session.id}")
    private String sessionId;

    @Value("${zapi.token}")
    private String token;

    @Value("${zapi.client.token}")
    private String clientToken;

    private static final String API_URL = "https://api.z-api.io/";

    public String enviarMensagem(String telefone, String mensagem) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "instances/" + sessionId + "/token/" + token + "/send-text";

        // Formatar número (remover espaços e garantir código do país)
        telefone = formatarNumero(telefone);

        // Criar corpo da requisição
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("phone", telefone);
        requestBody.put("message", mensagem);

        // Configurar cabeçalhos da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Client-Token", clientToken);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Enviar requisição POST
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    private String formatarNumero(String telefone) {
        telefone = telefone.replaceAll("[^\\d+]", "");
        if (!telefone.startsWith("+")) {
            telefone = "+55" + telefone; // Adiciona o código do Brasil caso não tenha
        }
        return telefone;
    }
}
