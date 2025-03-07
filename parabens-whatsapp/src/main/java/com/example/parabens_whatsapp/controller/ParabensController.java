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
        // Lê o conteúdo do PDF
        String textoPdf = pdfService.lerPdf("C:/Aniversariante1.pdf");

        // Remove cabeçalho inicial se existir
        textoPdf = textoPdf.replace("Lista de Aniversariantes", "").trim();

        // Dividindo cada pessoa a partir do "Nome: "
        String[] blocos = textoPdf.split("(?=Nome: )");

        System.out.println("Total de pessoas encontradas: " + blocos.length);

        for (String bloco : blocos) {
            try {
                String[] dados = bloco.split("\n");

                String nome = "";
                String dataStr = "";
                String telefone = "";
                StringBuilder descricao = new StringBuilder();

                for (String linha : dados) {
                    linha = linha.trim();
                    if (linha.startsWith("Nome: ")) {
                        nome = linha.replace("Nome: ", "").trim();
                    } else if (linha.startsWith("Data de Nascimento: ")) {
                        dataStr = linha.replace("Data de Nascimento: ", "").trim();
                    } else if (linha.startsWith("Telefone: ")) {
                        telefone = linha.replace("Telefone: ", "").trim();
                    } else if (linha.startsWith("Descrição: ")) {
                        descricao.append(linha.replace("Descrição: ", "").trim());
                    } else if (!linha.isEmpty()) {
                        descricao.append(" ").append(linha);
                    }
                }

                if (nome.isEmpty() || dataStr.isEmpty() || telefone.isEmpty() || descricao.isEmpty()) {
                    System.err.println("⚠️ Dados incompletos para:\n" + bloco);
                    continue;
                }

                Date dataNascimento;
                try {
                    dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
                } catch (ParseException e) {
                    System.err.println("⚠️ Data inválida para " + nome + ": " + dataStr);
                    continue;
                }

                // Gera mensagem personalizada
                String mensagem = openAIService.gerarMensagem(descricao.toString());

                System.out.println("✅ Enviando mensagem para " + nome + " (" + telefone + ")");

                try {
                    String resposta = whatsAppService.enviarMensagem(
                        telefone,
                        nome,
                        dataNascimento,
                        descricao.toString(),
                        mensagem
                    );
                    System.out.println("📩 Resposta da API para " + nome + ": " + resposta);
                } catch (Exception e) {
                    System.err.println("❌ Falha ao enviar mensagem para " + nome + ": " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }

            } catch (Exception e) {
                System.err.println("❌ Erro inesperado ao processar pessoa:\n" + bloco);
                e.printStackTrace();
            }
        }

        System.out.println("\n✅ Processo concluído!");
        return "Mensagens enviadas e processadas com sucesso!";
    }
}
