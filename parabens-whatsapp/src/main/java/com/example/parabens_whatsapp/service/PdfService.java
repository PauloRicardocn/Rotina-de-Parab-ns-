package com.exemplo.service;


import com.exemplo.model.Contato;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;



@Service
public class PdfService {

    public List<Contato> lerPdf(File file) {
        List<Contato> contatos = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(file);
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String textoPdf = pdfStripper.getText(document);
            textoPdf = textoPdf.replace("Lista de Contatos", "").trim();

            String[] blocos = textoPdf.split("(?=Nome: )");

            for (String bloco : blocos) {
                String[] dados = bloco.split("\n");

                Contato contato = new Contato();
                String dataStr = "";

                for (String linha : dados) {
                    linha = linha.trim();
                    if (linha.startsWith("Nome: ")) {
                        contato.setNome(linha.replace("Nome: ", "").trim());
                    } else if (linha.startsWith("Data de Nascimento: ")) {
                        dataStr = linha.replace("Data de Nascimento: ", "").trim();
                    } else if (linha.startsWith("Telefone: ")) {
                        contato.setTelefone(linha.replace("Telefone: ", "").trim());
                    } else if (linha.startsWith("Descrição: ")) {
                        contato.setDescricao(linha.replace("Descrição: ", "").trim());
                    } else if (!linha.isEmpty()) {
                        contato.setDescricao(contato.getDescricao() + " " + linha);
                    }
                }

                try {
                    Date dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
                    contato.setDataNascimento(dataNascimento);
                } catch (Exception e) {
                    System.err.println("Erro ao converter data para: " + contato.getNome());
                    continue;
                }

                contatos.add(contato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }
}
