package com.exemplo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String gerarMensagem(String descricao) {
        String url = "https://api.openai.com/v1/chat/completions";
        String prompt = "Escreva uma mensagem de parabéns para alguém que " + descricao;

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", List.of(message));
        request.put("max_tokens", 300);
        request.put("temperature", 1.00);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> choice = choices.get(0);
        Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
        return messageResponse.get("content").toString().trim();
    }
}
