package com.exemplo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    // Salvar a mensagem no banco de dados
    Mensagem mensagem = new Mensagem();
    mensagem.setNome(nome);
    mensagem.setDataNascimento(dataNascimento);
    mensagem.setTelefone(telefone); // Novo campo
    mensagem.setDescricao(descricao);
    mensagem.setMensagemGerada(mensagemGerada);
    mensagem.setDataEnvio(new Date());
    mensagemRepository.save(mensagem);
}
}