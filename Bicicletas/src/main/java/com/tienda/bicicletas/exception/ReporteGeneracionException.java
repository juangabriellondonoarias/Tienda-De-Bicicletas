package com.tienda.bicicletas.exception;

public class ReporteGeneracionException extends RuntimeException {
    public ReporteGeneracionException(String mensaje) {
        super(mensaje);
    }

    public ReporteGeneracionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
