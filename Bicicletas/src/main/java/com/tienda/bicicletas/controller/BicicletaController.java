package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.service.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicleta")
@CrossOrigin(origins = "*")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @GetMapping
    public List<Bicicleta> listar(){
        return bicicletaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bicicleta> obtenerPorId(@PathVariable Integer id){
        return bicicletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bicicleta crear(@RequestBody Bicicleta bicicleta){
        return  bicicletaService.guardar(bicicleta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        if (bicicletaService.buscarPorId(id).isPresent()){
            bicicletaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta> actualizar(@PathVariable Integer id, @RequestBody Bicicleta bicicleta){
        Bicicleta bicicletaActualizada = bicicletaService.actualizar(id, bicicleta);
        if (bicicletaActualizada != null){
            return ResponseEntity.ok(bicicletaActualizada);
        }
        return ResponseEntity.notFound().build();
    }
}
