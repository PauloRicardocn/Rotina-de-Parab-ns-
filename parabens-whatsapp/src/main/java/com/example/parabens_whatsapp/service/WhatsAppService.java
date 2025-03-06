package com.exemplo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    public void enviarMensagem(String telefone, String nome, Date dataNascimento, String descricao, String mensagemGerada) {
        Twilio.init(accountSid, authToken);

        // Validar e formatar número de telefone
        telefone = formatarNumero(telefone);
        if (telefone == null) {
            System.err.println("Número de telefone inválido para " + nome);
            return;
        }

        try {
            Message message = Message.creator(
                new PhoneNumber("whatsapp:" + telefone),
                new PhoneNumber("whatsapp:" + fromNumber),
                mensagemGerada
            ).create();

            System.out.println("Mensagem enviada com sucesso para " + nome + " (" + telefone + ")");
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para " + nome + ": " + e.getMessage());
        }
    }

    private String formatarNumero(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return null;
        }

        // Remover espaços e caracteres especiais
        telefone = telefone.replaceAll("[^\\d+]", "");

        // Adicionar código do país se necessário
        if (!telefone.startsWith("+")) {
            telefone = "+55" + telefone;
        }

        // Verificar se o número tem entre 12 e 15 dígitos após formatação
        if (!Pattern.matches("\\+\\d{11,14}", telefone)) {
            return null;
        }

        return telefone;
    }
}
