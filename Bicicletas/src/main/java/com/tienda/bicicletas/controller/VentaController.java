package com.tienda.bicicletas.controller;


import com.tienda.bicicletas.dto.request.CompraClienteRequestDTO;
import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ventas")
@Tag(name = "Venta", description = "mostrar ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService){
        this.ventaService = ventaService;
    }

    // 2. REGISTRO POR VENDEDOR (Venta Asistida)
    @Operation(summary = "Venta por Vendedor", description = "El vendedor registra la venta incluyendo su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta creada por vendedor"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/vendedor")
    public ResponseEntity<VentaResponseDTO> crearVentaVendedor(@RequestBody VentaRequestDTO request){
        VentaResponseDTO response = ventaService.registrarVenta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- Los demás métodos se mantienen igual ---

    @Operation(summary = "obtener Todas las ventas",  description = "Obtiene una lista de las ventas")
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> leer(){
        return new ResponseEntity<>(ventaService.obtenerLasVentas(), HttpStatus.OK);
    }

    @Operation(summary = "Obtiene por Id", description = "Obtiene una sola venta por su Id")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> leerId(@PathVariable Integer id){
        return new ResponseEntity<>(ventaService.obtenerPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualiza por Id",  description = "Se actualiza una venta por su Id")
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizar(@PathVariable Integer id, @RequestBody VentaRequestDTO requestDTO){
        return new ResponseEntity<>(ventaService.actualizarVenta(id, requestDTO), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar por Id", description = "Se elimina una venta por su Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        ventaService.eliminarLaVenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
