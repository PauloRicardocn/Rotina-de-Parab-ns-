package com.exemplo.service;

import com.exemplo.model.Mensagem;
import com.exemplo.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class WhatsAppService {

    @Value("${zapi.session.id}")
    private String sessionId;

    @Value("${zapi.token}")
    private String token;

    @Value("${zapi.client.token}")
    private String clientToken;

    @Autowired
    private MensagemRepository mensagemRepository;

    private static final String API_URL = "https://api.z-api.io/";

    List<String> midiasUrl = List.of(
        "https://drive.google.com/uc?export=download&id=1iV8pD5Tdcv2P89iNf4S_xhuTPVZaXxlo",
        "https://drive.google.com/uc?export=download&id=1F7MTcsxVCEaCdVlgRE7EKHBoeepY3WzV"
    );

    public List<String> getMidiasUrl() {
        return this.midiasUrl;
    }

    public String enviarMensagem(String telefone, String nome, String mensagemGerada) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "instances/" + sessionId + "/token/" + token + "/send-text";

        telefone = formatarNumero(telefone);
        if (telefone == null) {
            return "❌ Número inválido!";
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("phone", telefone);
        requestBody.put("message", mensagemGerada);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Client-Token", clientToken);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Salvar a mensagem no banco de dados
        Mensagem mensagem = new Mensagem();
        mensagem.setNome(nome);
        mensagem.setTelefone(telefone);
        mensagem.setMensagemGerada(mensagemGerada);
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagemRepository.save(mensagem);

        return response.getBody();
    }

    public String enviarVideoParaContato(String telefone, String nome, String legendaPersonalizada, List<String> midiasUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "instances/" + sessionId + "/token/" + token + "/send-image";

        telefone = formatarNumero(telefone);
        if (telefone == null) {
            return "❌ Número inválido!";
        }

        StringBuilder respostas = new StringBuilder();

        for (String midiaUrl : midiasUrl) {
            try {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("phone", telefone);
                requestBody.put("image", midiaUrl);
                if (legendaPersonalizada != null && !legendaPersonalizada.isEmpty()) {
                    requestBody.put("caption", legendaPersonalizada);
                }
                requestBody.put("viewOnce", false);

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                headers.set("Client-Token", clientToken);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                respostas.append("✅ Enviado: ").append(midiaUrl).append("\n");

                // Salva no banco
                Mensagem mensagem = new Mensagem();
                mensagem.setNome(nome);
                mensagem.setTelefone(telefone);
                mensagem.setMensagemGerada((legendaPersonalizada != null ? legendaPersonalizada : "") + midiaUrl);
                mensagem.setDataEnvio(LocalDateTime.now());
                mensagemRepository.save(mensagem);

                Thread.sleep(2000);

            } catch (Exception e) {
                respostas.append("❌ Erro ao enviar ").append(midiaUrl).append(": ").append(e.getMessage()).append("\n");
            }
        }

        return respostas.toString();
    }

    public static String formatarNumero(String telefone) {
    if (telefone == null || telefone.trim().isEmpty()) return null;

    // Remove tudo que não for dígito
    telefone = telefone.replaceAll("[^\\d]", "");

    // Adiciona 55 se não começar
    if (!telefone.startsWith("55")) {
        telefone = "55" + telefone;
    }

    // Remove o 55 inicial para processar o DDD e número
    String numeroSem55 = telefone.substring(2);

    if (numeroSem55.length() < 10) {
        return null;
    }

    String ddd = numeroSem55.substring(0, 2);
    String numero = numeroSem55.substring(2);

    // Adiciona 9 se número tiver 8 dígitos
    if (numero.length() == 8) {
        numero = "9" + numero;
    }

    telefone = "55" + ddd + numero;

    if (telefone.length() != 13) {
        return null;
    }

    return "+" + telefone;
}


    public static boolean isNumeroValido(String telefone) {
        return formatarNumero(telefone) != null;
    }
}


