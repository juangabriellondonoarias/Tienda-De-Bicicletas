package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.DetalleVentaResponseDTO;
import com.tienda.bicicletas.service.DetalleVentaService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/detalles-ventas")
@AllArgsConstructor
public class DetalleVentaController {
    private final DetalleVentaService service;

    // se hace el contructor de DetalleVentaService pero lo hacemos con la anotacion @AllArgsConstructor

    @PostMapping
    public ResponseEntity<DetalleVentaResponseDTO> crear (@RequestBody DetalleVentaRequestDTO requestDTO){
        return new ResponseEntity<>(service.registrarDetalle(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DetalleVentaResponseDTO>> obtenerTodo(){
        return new ResponseEntity<>(service.obtenerTodo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> obtenerPorId(@PathVariable Integer id){
        return new ResponseEntity<>(service.obtenerPorId(id) , HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO>actualizar(@PathVariable Integer id , @RequestBody DetalleVentaRequestDTO requestDTO){
        return new ResponseEntity<>(service.actualizarDetalle(id,requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO>eliminar(@PathVariable Integer id){
        service.eliminarDetalle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
