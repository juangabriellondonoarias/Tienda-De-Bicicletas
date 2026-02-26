package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.service.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (útil en desarrollo)
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    // GET: http://localhost:8080/api/bicicletas
    @GetMapping
    public List<Bicicleta> listar() {
        return bicicletaService.listarTodas();
    }

    // GET por ID: http://localhost:8080/api/bicicletas/1
    @GetMapping("/{id}")
    public ResponseEntity<Bicicleta> obtenerPorId(@PathVariable Integer id) {
        return bicicletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/api/bicicletas (Cuerpo: JSON con datos de la bici)
    @PostMapping
    public Bicicleta crear(@RequestBody Bicicleta bicicleta) {
        return bicicletaService.guardar(bicicleta);
    }

    // DELETE: http://localhost:8080/api/bicicletas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        // Primero verificamos si la bicicleta existe para dar una mejor respuesta
        if (bicicletaService.buscarPorId(id).isPresent()) {
            bicicletaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (Éxito, sin cuerpo)
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found si no existe
    }

    // PUT: http://localhost:8080/api/bicicletas/1
    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta> actualizar(@PathVariable Integer id, @RequestBody Bicicleta bicicleta) {
        Bicicleta bicicletaActualizada = bicicletaService.actualizar(id, bicicleta);

        if (bicicletaActualizada != null) {
            return ResponseEntity.ok(bicicletaActualizada); // Retorna 200 OK con los datos nuevos
        }
        return ResponseEntity.notFound().build(); // Retorna 404 si el ID no existe
    }
}