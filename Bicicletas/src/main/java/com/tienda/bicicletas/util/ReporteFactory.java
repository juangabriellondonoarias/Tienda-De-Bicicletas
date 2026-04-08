package com.tienda.bicicletas.util;

import com.tienda.bicicletas.exception.ReporteGeneracionException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ReporteFactory {
    private final Map<String, ReporteStrategy> strategies;

    public ReporteFactory(List<ReporteStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(s -> s.getFormato().toUpperCase(), Function.identity()));
    }

    public ReporteStrategy getStrategy(String formato) {
        ReporteStrategy strategy = strategies.get(formato.toUpperCase());
        if (strategy == null) {
            throw new ReporteGeneracionException("Formato de reporte no soportado: " + formato);
        }
        return strategy;
    }
}
