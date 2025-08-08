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

import java.util.Date;
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
    "https://drive.google.com/uc?export=download&id=1-Wte_kePo1TDQsvrobDKrBqvDIsZDmJE",
    "https://drive.google.com/uc?export=download&id=1HbYo6mBSmRev_UU-DyH5WBJTQzh1JchZ",
    "https://drive.google.com/uc?export=download&id=1v8bwC4zQMdt2OpDvAYDSoqk2B_XGsWuR",
    "https://drive.google.com/uc?export=download&id=1jIfmO2ASKPqmZ8hCtgnjEC88idrrnbwn",
    "https://drive.google.com/uc?export=download&id=1NZIvPHS-GrqN8bomUhh7qrrrU-UXadm2",
    "https://drive.google.com/uc?export=download&id=14HU6PP5Am95AdBOc_KsbEfIxSL7c3OMp",
    "https://drive.google.com/uc?export=download&id=1QnPBLoJDqR7gM8aZtpzqdqGOWI1eGg0n",
    "https://drive.google.com/uc?export=download&id=16B_RxSa2U0Z6L1WMgXcDOcTpeeO7pFmH",
    "https://drive.google.com/uc?export=download&id=1yOt3pnCNSt0984ZIYAQnvzWkuvi1xz5S",
    "https://drive.google.com/uc?export=download&id=1jmsRBmN2NGlv0AhhmB16koRuDo0-quoh",
    "https://drive.google.com/uc?export=download&id=1rzJjH0zgsvLtXFiCY0vAWCXSoNgXBclV",
    "https://drive.google.com/uc?export=download&id=115-gqKixL0cD7vMx2WD-cV7autRTJ7xZ"

);

public List<String> getMidiasUrl() {
    return this.midiasUrl;
}




    public String enviarMensagem(String telefone, String nome, Date dataNascimento, String mensagemGerada) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "instances/" + sessionId + "/token/" + token + "/send-text";

        telefone = formatarNumero(telefone);

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
        mensagem.setDataNascimento(dataNascimento);
        mensagem.setTelefone(telefone);
        mensagem.setMensagemGerada(mensagemGerada);
        mensagem.setDescricao("Mensagem automática enviada pelo sistema da Otica.");
        mensagem.setDataEnvio(new Date());
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
            // Só adiciona legenda se não for vazia
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
            mensagem.setDescricao("Mídia promocional enviada via Z-API.");
            mensagem.setDataEnvio(new Date());
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
        telefone = telefone.replaceAll("[^\\d]", "");
        if (telefone.length() >= 12 && telefone.startsWith("0")) {
            telefone = telefone.substring(1);
        }
        if (telefone.length() == 10) {
            String ddd = telefone.substring(0, 2);
            String numero = telefone.substring(2);
            if (numero.length() == 8) {
                numero = "9" + numero;
            }
            telefone = ddd + numero;
        }
        if (!telefone.startsWith("55")) {
            telefone = "55" + telefone;
        }
        if (telefone.length() != 13) {
            System.err.println("❌ Número inválido: " + telefone);
            return null;
        }
        return "+" + telefone;
    }

    public static boolean isNumeroValido(String telefone) {
        return formatarNumero(telefone) != null;
    }
}
