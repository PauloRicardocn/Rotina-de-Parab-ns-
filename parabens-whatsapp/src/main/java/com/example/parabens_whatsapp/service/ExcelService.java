package com.exemplo.service;

import com.exemplo.model.Contato;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;

@Service
public class ExcelService {

    public List<Contato> lerExcel(File file) {
        List<Contato> contatos = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Pular cabeçalho

                Contato contato = new Contato();

                // Nome
                Cell nomeCell = row.getCell(0);
                contato.setNome(nomeCell != null ? nomeCell.getStringCellValue().trim() : "");


                // Telefone (tratamento para evitar notação científica)
                Cell telefoneCell = row.getCell(1);
                if (telefoneCell != null) {
                    if (telefoneCell.getCellType() == CellType.STRING) {
                        contato.setTelefone(telefoneCell.getStringCellValue().trim());
                    } else if (telefoneCell.getCellType() == CellType.NUMERIC) {
                        contato.setTelefone(String.valueOf((long) telefoneCell.getNumericCellValue()));
                    }
                }

                contatos.add(contato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }
}
