package com.exemplo.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public String lerPdf(String caminhoArquivo) throws IOException {
        File file = new File(caminhoArquivo);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String texto = stripper.getText(document);
        document.close();
        return texto;
    }
}