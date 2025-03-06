package com.example.parabens_whatsapp.controller;

import com.exemplo.service.PdfService;
import com.exemplo.service.OpenAIService;
import com.exemplo.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    public String enviarParabens() {
        try {
            // Caminho do arquivo PDF (substitua pelo caminho correto)
            String caminhoPdf = "C:/aniversariantes.pdf";

            // Ler o PDF
            String textoPdf = pdfService.lerPdf(caminhoPdf);

            // Dividir o texto em blocos (um para cada aniversariante)
            String[] pessoas = textoPdf.split("\n\n");

            // Obter a data atual
            LocalDate dataAtual = LocalDate.now();

            // Processar cada aniversariante
            for (String pessoa : pessoas) {
                try {
                    // Dividir os dados do aniversariante
                    String[] dados = pessoa.split("\n");

                    // Validar se há dados suficientes
                    if (dados.length < 4) {
                        System.err.println("Formato inválido no PDF: " + pessoa);
                        continue; // Pula para o próximo aniversariante
                    }

                    // Extrair os dados
                    String nome = null;
                    LocalDate dataNascimento = null;
                    String telefone = null;
                    String descricao = null;

                    for (String linha : dados) {
                        if (linha.startsWith("Nome: ")) {
                            nome = linha.replace("Nome: ", "").trim();
                        } else if (linha.startsWith("Data de Nascimento: ")) {
                            String dataNascimentoStr = linha.replace("Data de Nascimento: ", "").trim();
                            try {
                                // Converter a data de nascimento para LocalDate
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
                            } catch (Exception e) {
                                System.err.println("Formato de data inválido: " + dataNascimentoStr);
                                continue; // Pula para o próximo aniversariante
                            }
                        } else if (linha.startsWith("Telefone: ")) {
                            telefone = linha.replace("Telefone: ", "").trim();
                            // Remover espaços e hífens do número de telefone
                            telefone = telefone.replaceAll("[\\s-]", "");
                            // Adicionar o código do país (+55) se necessário
                            if (!telefone.startsWith("+55")) {
                                telefone = "+55" + telefone;
                            }
                        } else if (linha.startsWith("Descrição: ")) {
                            descricao = linha.replace("Descrição: ", "").trim();
                        }
                    }

                    // Validar se todos os campos foram extraídos
                    if (nome == null || dataNascimento == null || telefone == null || descricao == null) {
                        System.err.println("Dados incompletos no PDF: " + pessoa);
                        continue; // Pula para o próximo aniversariante
                    }

                    // Verificar se a data de aniversário já passou
                    LocalDate proximoAniversario = dataNascimento.withYear(dataAtual.getYear());
                    if (proximoAniversario.isBefore(dataAtual)) {
                        // Se o aniversário já passou este ano, ajuste para o próximo ano
                        proximoAniversario = proximoAniversario.plusYears(1);
                    }

                    if (proximoAniversario.isAfter(dataAtual)) {
                        System.out.println("Aniversário de " + nome + " já passou. Ignorando.");
                        continue; // Pula para o próximo aniversariante
                    }

                    // Gerar a mensagem personalizada
                    String mensagem = openAIService.gerarMensagem(descricao);

                    // Enviar a mensagem no WhatsApp
                    whatsAppService.enviarMensagem(telefone, nome, Date.from(dataNascimento.atStartOfDay(ZoneId.systemDefault()).toInstant()), descricao, mensagem);

                } catch (Exception e) {
                    System.err.println("Erro ao processar aniversariante: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return "Mensagens enviadas e salvas com sucesso!";

        } catch (Exception e) {
            System.err.println("Erro ao processar o PDF: " + e.getMessage());
            e.printStackTrace();
            return "Erro ao enviar mensagens. Verifique os logs para mais detalhes.";
        }
    }
}