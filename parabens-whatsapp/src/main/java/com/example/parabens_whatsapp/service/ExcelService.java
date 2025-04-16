package com.exemplo.service;

import com.exemplo.model.Contato;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
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
                contato.setNome(row.getCell(0).getStringCellValue());

                // Tratando a data de nascimento
                Cell dataCell = row.getCell(1);
                if (dataCell != null && dataCell.getCellType() == CellType.NUMERIC) {
                    contato.setDataNascimento(dataCell.getDateCellValue());
                } else if (dataCell != null && dataCell.getCellType() == CellType.STRING) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    contato.setDataNascimento(format.parse(dataCell.getStringCellValue()));
                }

                contato.setTelefone(row.getCell(2).getStringCellValue());
                contato.setDescricao(row.getCell(3).getStringCellValue());

                contatos.add(contato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contatos;
    }
}
