package com.exemplo.controller;

import com.exemplo.service.PdfService;
import com.exemplo.service.OpenAIService;
import com.exemplo.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/parabens")
public class ParabensController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping("/enviar")
    public String enviarParabens() throws Exception {
        String textoPdf = pdfService.lerPdf("C:/Aniversariante1.pdf");
        String[] pessoas = textoPdf.split("\n\n"); // Divide o texto em blocos de pessoas

        for (String pessoa : pessoas) {
            String[] dados = pessoa.split("\n");
            // Remover espaços em branco das linhas
            for (int i = 0; i < dados.length; i++) {
                dados[i] = dados[i].trim();
            }
            
            // Debug: imprimir os dados extraídos
            System.out.println("Processando bloco:");
            for (String linha : dados) {
                System.out.println(">" + linha + "<");
            }
            
            String nome = "";
            String dataStr = "";
            String telefone = "";
            String descricao = "";
            
            for (String linha : dados) {
                if (linha.startsWith("Nome: ")) {
                    nome = linha.replace("Nome: ", "").trim();
                } else if (linha.startsWith("Data de Nascimento: ")) {
                    dataStr = linha.replace("Data de Nascimento: ", "").trim();
                } else if (linha.startsWith("Telefone: ")) {
                    telefone = telefone.replaceAll("\\s+", ""); // Remove espaços
                    if (!telefone.startsWith("+")) {
                 telefone = "+55" + telefone; // Adiciona código do Brasil caso não tenha
}
                } else if (linha.startsWith("Descrição: ")) {
                    descricao = linha.replace("Descrição: ", "").trim();
                }
            }
            
            if (nome.isEmpty() || dataStr.isEmpty() || telefone.isEmpty() || descricao.isEmpty()) {
                System.err.println("Dados incompletos para: " + pessoa);
                continue; // pula para o próximo
            }
            
            Date dataNascimento;
            try {
                dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
            } catch (ParseException e) {
                System.err.println("Data inválida para " + nome + ": " + dataStr);
                continue; // pula para o próximo
            }
            
            String mensagem = openAIService.gerarMensagem(descricao);
            whatsAppService.enviarMensagem(telefone, nome, dataNascimento, descricao, mensagem);
        }

        return "Mensagens enviadas e salvas com sucesso!";
    }
}
