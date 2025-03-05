package com.exemplo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String gerarMensagem(String descricao) {
        String url = "https://api.openai.com/v1/completions";
        String prompt = "Escreva uma mensagem de parabéns para alguém que " + descricao;

        Map<String, Object> request = new HashMap<>();
        request.put("model", "text-davinci-003");
        request.put("prompt", prompt);
        request.put("max_tokens", 100);
        request.put("temperature", 0.7);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        headers.put("Content-Type", "application/json");

        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class, headers);
        return response.get("choices").toString(); // Extraia a mensagem da resposta
    }
}