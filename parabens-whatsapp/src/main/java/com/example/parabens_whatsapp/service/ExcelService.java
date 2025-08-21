package com.exemplo.service;

import com.exemplo.model.Contato;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<Contato> lerExcel(File file) {
        List<Contato> contatos = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Pular cabeçalho

                // Ler células com segurança, mesmo que estejam vazias
                Cell nomeCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell telefoneCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                String nome = nomeCell.getStringCellValue().trim();

                String telefone = "";
                if (telefoneCell.getCellType() == CellType.STRING) {
                    telefone = telefoneCell.getStringCellValue().trim();
                } else if (telefoneCell.getCellType() == CellType.NUMERIC) {
                    telefone = String.valueOf((long) telefoneCell.getNumericCellValue());
                }

                // Limpar espaços e caracteres que não sejam números
                telefone = telefone.replaceAll("[^0-9]", "");

                // Adiciona apenas contatos válidos
                if (!nome.isEmpty() && !telefone.isEmpty()) {
                    Contato contato = new Contato();
                    contato.setNome(nome);
                    contato.setTelefone(telefone);
                    contatos.add(contato);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }
}

