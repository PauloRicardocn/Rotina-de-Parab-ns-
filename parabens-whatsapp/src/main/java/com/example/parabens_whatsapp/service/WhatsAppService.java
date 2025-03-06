package com.exemplo.service;

import com.exemplo.model.Mensagem;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

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
        Message.creator(
            new PhoneNumber("whatsapp:" + telefone),
            new PhoneNumber("whatsapp:" + fromNumber),
            mensagemGerada
        ).create();

    }
}