package com.tienda.bicicletas.controller;


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

    // para crear
    @Operation(summary = "CrearVenta" ,  description = "se crea una venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "Se creo correctamente"),
            @ApiResponse(responseCode = "400" , description = "No se creo correctamente")
    })
    @PostMapping
    public ResponseEntity<VentaResponseDTO>crearVenta(@RequestBody VentaRequestDTO request){
        VentaResponseDTO response = ventaService.registrarVenta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // para leer todos
    @Operation(summary = "obtener Todas las ventas",  description = "Obtiene una lista de las ventas")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200" , description = "Se encontro correctamente"),
            @ApiResponse (responseCode = "404" , description = "No se encontro correctamente")
    })
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>>leer(){
        return new ResponseEntity<>(ventaService.obtenerLasVentas(), HttpStatus.OK);
    }


    //obtener por id
    @Operation(summary = "Obtiene por Id" , description = "Obtiene una sola venta por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Se encontro correctamente"),
            @ApiResponse( responseCode = "404" , description = "No se encontro")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO>leerId(@PathVariable Integer id){
        return  new ResponseEntity<>(ventaService.obtenerPorId(id), HttpStatus.OK);
    }


    // Actualizar
    @Operation(summary = "Actualiza por Id" ,  description = "Se actualiza una venta por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Se actualizo correctamente"),
            @ApiResponse(responseCode = "404" , description = "No se actualizo correctamente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO>actualizar(@PathVariable Integer id , @RequestBody VentaRequestDTO requestDTO){
            return new ResponseEntity<>(ventaService.actualizarVenta(id , requestDTO) , HttpStatus.OK);
    }

    //Eliminar
    @Operation(summary = "Eliminar por Id", description = "Se elimina una venta por su Id ")
    @ApiResponse(responseCode = "204" , description = "se desactivo correctamente")
    @DeleteMapping("/{id}")
    public  ResponseEntity<VentaResponseDTO>eliminar(@PathVariable Integer id ){
        ventaService.eliminarLaVenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
