package com.tienda.bicicletas.controller;


import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService){
        this.ventaService = ventaService;
    }

    // para crear
    @PostMapping
    public ResponseEntity<VentaResponseDTO>crearVenta(@RequestBody VentaRequestDTO request){
        VentaResponseDTO response = ventaService.registrarVenta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // para leer todos
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>>leer(){
        return new ResponseEntity<>(ventaService.obtenerLasVentas(), HttpStatus.OK);
    }


    //obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO>leerId(@PathVariable Integer id){
        return  new ResponseEntity<>(ventaService.obtenerPorId(id), HttpStatus.OK);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO>actualizar(@PathVariable Integer id , @RequestBody VentaRequestDTO requestDTO){
            return new ResponseEntity<>(ventaService.actualizarVenta(id , requestDTO) , HttpStatus.OK);
    }

    //Eliminar

    @DeleteMapping("/{id}")
    public  ResponseEntity<VentaResponseDTO>eliminar(@PathVariable Integer id ){
        ventaService.eliminarLaVenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
