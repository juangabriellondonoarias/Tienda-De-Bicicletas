package com.tienda.bicicletas.controller;

//import com.tienda.bicicletas.dto.request.CompradoLoteRequestDTO;
import com.tienda.bicicletas.dto.request.MovimientoRequestDTO;
import com.tienda.bicicletas.dto.response.MovimientoResponseDTO;
import com.tienda.bicicletas.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@Tag(name = "Movimientos", description = "Operaciones para gestionar entradas y salidas de inventario")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Operation(summary = "Registrar un movimiento", description = "Registra una entrada o salida de stock.El trigger de la DB actualizara el stock de la bicicleta automaticamente")
    @PreAuthorize("hasRole('ADMIN')") // solo entra si el token dice ROLE_ADMIN
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "Se creo exitosamente"),
            @ApiResponse(responseCode = "400" , description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<String> registrar(@Valid @RequestBody MovimientoRequestDTO request){
        movimientoService.registrar(request);
        return ResponseEntity.ok("Movimiento registrado correctamente");
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listar(){
        return ResponseEntity.ok(movimientoService.listarTodos());
    }



    @GetMapping("/cantidad-comprada")
    public ResponseEntity<List<Object[]>> consultar(@RequestParam String proveedor) {
        return ResponseEntity.ok(movimientoService.consultarProveedor(proveedor));
    }

    /*
    @PostMapping("/lote")
    public ResponseEntity<String>movimientosLote(@RequestBody MovimientoRequestDTO loteRequestDTO){
            movimientoService.movimientoLote(loteRequestDTO);
            return ResponseEntity.ok("Se registra lote correctamente");
    }*/
}
