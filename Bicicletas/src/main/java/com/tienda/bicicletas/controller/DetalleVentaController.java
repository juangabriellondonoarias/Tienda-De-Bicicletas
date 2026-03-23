package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.DetalleVentaResponseDTO;
import com.tienda.bicicletas.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
//import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.swing.plaf.SeparatorUI;
import java.util.List;


@RestController
@RequestMapping("/api/detalles-ventas")
@AllArgsConstructor
@Tag(name = "DetalleVenta" , description = "los detalles de las ventas")
public class DetalleVentaController {
    private final DetalleVentaService service;

    // se hace el contructor de DetalleVentaService pero lo hacemos con la anotacion @AllArgsConstructor

    /*@Operation(summary = "Crear" , description = "se crea un detalle de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "Se creo correctamente"),
            @ApiResponse(responseCode = "400" , description = "Datos de entrada invalidos")
    })
    @PostMapping("/{idVenta}/detalles")
    public ResponseEntity<DetalleVentaResponseDTO> crear(
            @PathVariable Integer idVenta,
            @Valid @RequestBody DetalleVentaRequestDTO requestDTO){

        // ¡Aquí está la magia! Le pasamos los DOS parámetros al servicio
        return new ResponseEntity<>(service.registrarDetalle(idVenta, requestDTO), HttpStatus.CREATED);
    }*/

    @Operation(summary = "Obtener todos los detalles" , description = "se obtinen todos los detalles de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Se obtienen correctamente todos los detalles de venta"),
            @ApiResponse(responseCode = "404" , description = "No se encontraron los detalles de ventas")
    })

    @GetMapping
    public ResponseEntity<List<DetalleVentaResponseDTO>> obtenerTodo(){
        return new ResponseEntity<>(service.obtenerTodo(), HttpStatus.OK);
    }

    @Operation(summary = "obtener detalle venta" , description = "se obtiene el detalle de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Se obtuvo correctamente el detalle de venta"),
            @ApiResponse(responseCode = "404" , description = "no se encontro el detalle de venta")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> obtenerPorId(@PathVariable Integer id){
        return new ResponseEntity<>(service.obtenerPorId(id) , HttpStatus.OK);
    }

   /* @Operation(summary = "Actualizar detalle venta" ,  description = "se actualiza el detalle de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Se actualizo correctamente el detalle venta"),
            @ApiResponse(responseCode = "404" , description = "No se encontro detalle venta para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO>actualizar(@PathVariable Integer id , @RequestBody DetalleVentaRequestDTO requestDTO){
        return new ResponseEntity<>(service.actualizarDetalle(id,requestDTO), HttpStatus.OK);
    }*/

    @Operation(summary = "Eliminar por Id" , description = "Se elimina un detalle venta por Id")
    @ApiResponse(responseCode = "204" , description = "Se desactivo correctamente el detalle venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO>eliminar(@PathVariable Integer id){
        service.eliminarDetalle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
