package com.exemplo.controller;

import com.exemplo.service.PdfService;
import com.exemplo.service.OpenAIService;
import com.exemplo.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parabens")
public class ParabensController {

    @Autowired
    private PDF pdfService;

    @Autowired
    private ChatGPT openAIService;

    @Autowired
    private WhatsApp whatsAppService;

    @GetMapping("/enviar")
    public String enviarParabens() throws Exception {
        String textoPdf = pdfService.lerPdf("caminho/do/arquivo.pdf");
        String[] pessoas = textoPdf.split("\n\n"); // Divide o texto em blocos de pessoas

        for (String pessoa : pessoas) {
            String[] dados = pessoa.split("\n");
            String nome = dados[0].replace("Nome: ", "");
            String descricao = dados[2].replace("Descrição: ", "");

            String mensagem = openAIService.gerarMensagem(descricao);
            whatsAppService.enviarMensagem("+5511999999999", mensagem); // Número do destinatário
        }

        return "Mensagens enviadas com sucesso!";
    }
}