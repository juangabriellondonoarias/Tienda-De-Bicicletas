package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.request.MovimientoRequestDTO;
import com.tienda.bicicletas.dto.response.MovimientoResponseDTO;
import com.tienda.bicicletas.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
@Tag(name = "Movimientos", description = "Operaciones para gestionar entradas y salidas de inventario")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Operation(summary = "Registrar un movimiento", description = "Registra una entrada o salida de stock.El trigger de la DB actualizara el stock de la bicicleta automaticamente")
    @PreAuthorize("hasRole('ADMIN')") // solo entra si el token dice ROLE_ADMIN
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> registrar(@Valid @RequestBody MovimientoRequestDTO request){
        return ResponseEntity.ok(movimientoService.registrar(request));
    }

    @Operation(summary = "Listar historial de moviminetos", description = "Obtiene una lista de todos los movimientos (entradas y salidas) registrados")
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listar(){
        return ResponseEntity.ok(movimientoService.listarTodos());
    }
}
