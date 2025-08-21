package com.exemplo.controller;

import com.exemplo.model.Contato;
import com.exemplo.service.ExcelService;
import com.exemplo.service.PdfService;
import com.exemplo.service.OpenAIService;
import com.exemplo.service.MensagemService;
import com.exemplo.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/parabens")
public class ParabensController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private WhatsAppService whatsAppService;


    private String getContentType(File file) throws IOException {
        Path path = file.toPath();
        return Files.probeContentType(path);
    }

    @GetMapping("/tempo")
    public String Envio() throws Exception {
        String filePath = "C:/Feedback.xlsx";

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "Arquivo não encontrado no caminho: " + filePath;
        }

        String contentType = getContentType(file);
        List<Contato> contatos;

        if ("application/pdf".equals(contentType)) {
            contatos = pdfService.lerPdf(file);
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            contatos = excelService.lerExcel(file);
        } else {
            return "Formato de arquivo não suportado!";
        }

        int totalMensagens = contatos.size();
        System.out.println("Total de pessoas encontrados: " + totalMensagens);

        int tempoMinimoPorEnvio = 55000;
        int tempoMaximoPorEnvio = 65000;

        long tempoInicio = System.currentTimeMillis();
        Random random = new Random();

        for (int i = 0; i < totalMensagens; i++) {
            Contato contato = contatos.get(i);

            if (contato.getNome().isEmpty() || contato.getTelefone().isEmpty()) {
                System.err.println("Dados incompletos para: " + contato);
                continue;
            }

            String mensagem = mensagemService.mensagemFeedback(contato.getNome());

            try {
                String resposta = whatsAppService.enviarMensagem(
                    contato.getTelefone(),
                    contato.getNome(),
                    mensagem
                );
                System.out.printf("[%d/%d] Enviado para: %s\n", i + 1, totalMensagens, contato.getNome());
            } catch (Exception e) {
                System.err.println("Falha ao enviar mensagem para " + contato.getNome() + ": " + e.getMessage());
            }

            if (i < totalMensagens - 1) {
                if (i > 0 && i % 20 == 0) {
                    int pausaLonga = 1000 * (10 + random.nextInt(10));
                    System.out.println("⏸ Pausa prolongada entre blocos...");
                    Thread.sleep(pausaLonga);
                } else {
                    int delayAleatorio = tempoMinimoPorEnvio + random.nextInt(tempoMaximoPorEnvio - tempoMinimoPorEnvio + 1);
                    Thread.sleep(delayAleatorio);
                }

                long tempoAtual = System.currentTimeMillis();
                long tempoDecorrido = tempoAtual - tempoInicio;
                int restantes = totalMensagens - (i + 1);
                long estimativaRestante = (tempoDecorrido / (i + 1)) * restantes;

                int barraTamanho = 30;
                int preenchido = (int) ((i + 1) * barraTamanho / totalMensagens);
                String barra = "[" + "=".repeat(preenchido) + " ".repeat(barraTamanho - preenchido) + "]";

                System.out.printf("Progresso: %s %d%% | ⏱ Estimativa restante: ~%d min\n",
                        barra,
                        (i + 1) * 100 / totalMensagens,
                        estimativaRestante / 60000
                );
            }
        }

        System.out.println("✅ Processo concluído!");
        return totalMensagens + " mensagens enviadas com sucesso!";
    }

    @GetMapping("/enviar")
    public String enviarParabens() throws Exception {
        String filePath = "C:/Contatos_otica.xlsx";

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "Arquivo não encontrado no caminho: " + filePath;
        }

        String contentType = getContentType(file);
        List<Contato> contatos;

        if ("application/pdf".equals(contentType)) {
            contatos = pdfService.lerPdf(file);
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            contatos = excelService.lerExcel(file);
        } else {
            return "Formato de arquivo não suportado!";
        }

        System.out.println("Total de pessoas encontradas: " + contatos.size());

        for (Contato contato : contatos) {
            if (contato.getNome().isEmpty() || contato.getTelefone().isEmpty()) {
                System.err.println("Dados incompletos para: " + contato);
                continue;
            }

            String mensagem = mensagemService.mensagemPosCompra(contato.getNome());

            System.out.println("Enviando mensagem para " + contato.getNome() + " (" + contato.getTelefone() + ")");

            try {
                String resposta = whatsAppService.enviarMensagem(
                    contato.getTelefone(),
                    contato.getNome(),
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

    @GetMapping("/video")
public String enviarComVideo() throws Exception {
    String filePath = "/root/disparo/Rotina-de-Parab-ns-/parabens-whatsapp/lista4.xlsx";
    int limiteDiario = 900;

    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
        return "Arquivo não encontrado no caminho: " + filePath;
    }

    String contentType = getContentType(file);
    List<Contato> contatos;
    if ("application/pdf".equals(contentType)) {
        contatos = pdfService.lerPdf(file);
    } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
        contatos = excelService.lerExcel(file);
    } else {
        return "Formato de arquivo não suportado!";
    }

    int totalMensagens = contatos.size();
    System.out.println("Total de pessoas encontradas: " + totalMensagens);

        // Ler índice do último enviado
    Path controlePath = Paths.get("/root/disparo/ultimo_enviado.txt");
    int indiceInicio = 0;
    if (Files.exists(controlePath)) {
        String linha = Files.readString(controlePath).trim();
        if (!linha.isEmpty()) {
            indiceInicio = Integer.parseInt(linha);
        }
    }

    System.out.println("Iniciando do contato: " + (indiceInicio + 1));

    Random random = new Random();
    long tempoInicio = System.currentTimeMillis();


    int enviadoHoje = 0;

    int tempoMinimoPorEnvio = 55000;
    int tempoMaximoPorEnvio = 65000;

    for (int i = indiceInicio; i < totalMensagens && enviadoHoje < limiteDiario; i++) {
        Contato contato = contatos.get(i);
        if (!StringUtils.hasText(contato.getTelefone())) continue;

        String mensagemPersonalizada = mensagemService.mensagemVideo(contato.getNome());

        try {
            // Envia a mensagem usando somente o enviarMensagem
            String resposta = whatsAppService.enviarMensagem(
                contato.getTelefone(),
                contato.getNome(),
                mensagemPersonalizada
            );
            System.out.println("✅ Mensagem enviada para: " + contato.getNome());
            System.out.println("Resposta da API para " + contato.getNome() + ": " + resposta);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar texto para " + contato.getNome() + ": " + e.getMessage());
        }

        enviadoHoje++;

         // 🔹 Atualiza o último índice logo após o envio
        Files.writeString(controlePath, String.valueOf(i + 1),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);


        // Pausas entre envios
        if (i > 0 && i % 50 == 0) {
            int pausaLonga = 1000 * (60 + random.nextInt(180)); // entre 60s e 240s
            int minutos = pausaLonga / 60000;
            System.out.println("⏸ Pausa prolongada de " + minutos + " minuto(s) entre blocos...");
            Thread.sleep(pausaLonga);
        } else {
            int delayAleatorio = tempoMinimoPorEnvio + random.nextInt(tempoMaximoPorEnvio - tempoMinimoPorEnvio + 1);
            Thread.sleep(delayAleatorio);
        }

        // Exibir progresso
        long tempoAtual = System.currentTimeMillis();
        long tempoDecorrido = tempoAtual - tempoInicio;
        int restantes = totalMensagens - (i + 1);
        long estimativaRestante = (tempoDecorrido / (i + 1)) * restantes;

        int barraTamanho = 30;
        int preenchido = (int) ((i + 1) * barraTamanho / totalMensagens);
        String barra = "[" + "=".repeat(preenchido) + " ".repeat(barraTamanho - preenchido) + "]";

        System.out.printf("Progresso: %s %d%% | ⏱ Estimativa restante: ~%d min\n",
                barra,
                (i + 1) * 100 / totalMensagens,
                estimativaRestante / 60000
        );
    }

    System.out.println("✅ Enviadas " + enviadoHoje + " mensagens hoje!");
    return enviadoHoje + " mensagens enviadas com sucesso!";
}



}
