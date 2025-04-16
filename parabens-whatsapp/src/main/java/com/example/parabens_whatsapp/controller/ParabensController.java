package com.exemplo.controller;

import com.exemplo.model.Contato;
import com.exemplo.service.ExcelService;
import com.exemplo.service.PdfService;
import com.exemplo.service.OpenAIService;
import com.exemplo.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/parabens")
public class ParabensController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping("/enviar")
    public String enviarParabens() throws Exception {
        // Caminho do arquivo local
        String filePath = "C:/Amigos_novo1.xlsx"; 

        // Verificar se o arquivo existe
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "Arquivo não encontrado no caminho: " + filePath;
        }

        // Verificar o tipo do arquivo (PDF ou Excel)
        String contentType = getContentType(file);
        List<Contato> contatos;

        if ("application/pdf".equals(contentType)) {
            contatos = pdfService.lerPdf(file); // Passe o File diretamente
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            contatos = excelService.lerExcel(file); // Passe o File diretamente
        } else {
            return "Formato de arquivo não suportado!";
        }

        System.out.println("Total de pessoas encontradas: " + contatos.size());

        // Processa cada contato
        for (Contato contato : contatos) {
            if (contato.getNome().isEmpty() || contato.getTelefone().isEmpty()) {
                System.err.println("Dados incompletos para: " + contato);
                continue;
            }

            String mensagem = openAIService.gerarMensagem(contato.getNome(), contato.getDescricao());

            System.out.println("Enviando mensagem para " + contato.getNome() + " (" + contato.getTelefone() + ")");

            try {
                String resposta = whatsAppService.enviarMensagem(
                    contato.getTelefone(),
                    contato.getNome(),
                    contato.getDataNascimento(),
                    contato.getDescricao(),
                    mensagem
                );
                System.out.println("Resposta da API para " + contato.getNome() + ": " + resposta);
            } catch (Exception e) {
                System.err.println("Falha ao enviar mensagem para " + contato.getNome() + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }

        System.out.println("\nProcesso concluído!");
        return "Mensagens enviadas e processadas com sucesso!";
    }

    private String getContentType(File file) {
        // Método para determinar o tipo de arquivo (PDF ou Excel) com base na extensão do arquivo
        String fileName = file.getName();
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        return "";
    }
}
 
