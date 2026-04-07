package com.tienda.bicicletas.util;

import com.tienda.bicicletas.dto.reporte.ReporteFiltroDTO;
import com.tienda.bicicletas.exception.ReporteGeneracionException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelReporteStrategy implements ReporteStrategy {

    @Override
    public byte[] generar(String titulo, String[] columnas, List<Object[]> filas, ReporteFiltroDTO filtro) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(titulo);

            // --- ESTILOS ---
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // --- HEADER ---
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- BODY ---
            int rowIdx = 1;
            for (Object[] fila : filas) {
                Row row = sheet.createRow(rowIdx++);
                for (int colIdx = 0; colIdx < fila.length; colIdx++) {
                    Cell cell = row.createCell(colIdx);
                    Object val = fila[colIdx];
                    if (val instanceof Number) {
                        cell.setCellValue(((Number) val).doubleValue());
                    } else {
                        cell.setCellValue(val != null ? val.toString() : "");
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ReporteGeneracionException("Error al generar el Excel del reporte", e);
        }
    }

    @Override
    public String getFormato() {
        return "EXCEL";
    }
}
