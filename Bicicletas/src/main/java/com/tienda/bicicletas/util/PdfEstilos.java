package com.tienda.bicicletas.util;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import java.io.IOException;

public class PdfEstilos {
    public static final Color COLOR_PRIMARIO = new DeviceRgb(49, 130, 206); // #3182ce
    public static final Color COLOR_SECUNDARIO = new DeviceRgb(226, 232, 240); // #e2e8f0
    public static final Color COLOR_TEXTO = new DeviceRgb(45, 55, 72); // #2d3748
    public static final Color COLOR_BLANCO = new DeviceRgb(255, 255, 255);
    
    public static PdfFont getFontNormal() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }
    
    public static PdfFont getFontNegrita() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    }
}
