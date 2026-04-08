package com.tienda.bicicletas.util;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.tienda.bicicletas.dto.reporte.ReporteFiltroDTO;
import com.tienda.bicicletas.exception.ReporteGeneracionException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfReporteStrategy implements ReporteStrategy {

    @Override
    public byte[] generar(String titulo, String[] columnas, List<Object[]> filas, ReporteFiltroDTO filtro) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // --- HEADER ---
            document.add(new Paragraph(titulo)
                    .setFont(PdfEstilos.getFontNegrita())
                    .setFontSize(18)
                    .setFontColor(PdfEstilos.COLOR_PRIMARIO)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));

            if (filtro.getFechaInicio() != null && filtro.getFechaFin() != null) {
                document.add(new Paragraph("Rango: " + filtro.getFechaInicio() + " al " + filtro.getFechaFin())
                        .setFontSize(10));
            }

            document.add(new Paragraph("\n"));

            // --- TABLE ---
            Table table = new Table(UnitValue.createPercentArray(columnas.length)).useAllAvailableWidth();

            // Headers
            for (String col : columnas) {
                table.addHeaderCell(new Cell().add(new Paragraph(col))
                        .setBackgroundColor(PdfEstilos.COLOR_PRIMARIO)
                        .setFontColor(PdfEstilos.COLOR_BLANCO)
                        .setFont(PdfEstilos.getFontNegrita())
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // Body
            if (filas.isEmpty()) {
                table.addCell(new Cell(1, columnas.length).add(new Paragraph("Sin datos para este periodo"))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setItalic());
            } else {
                for (Object[] fila : filas) {
                    for (Object celda : fila) {
                        table.addCell(new Cell().add(new Paragraph(celda != null ? celda.toString() : ""))
                                .setFontSize(10));
                    }
                }
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new ReporteGeneracionException("Error al generar el PDF del reporte", e);
        }
    }

    @Override
    public String getFormato() {
        return "PDF";
    }
}
