package com.tienda.bicicletas.controller;
import com.tienda.bicicletas.dto.request.BicicletaRequestDTO;
import com.tienda.bicicletas.dto.response.BicicletaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.service.BicicletaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Base64;

@RestController
@RequestMapping("/api/bicicletas") //Permiso explícito para Angular
@CrossOrigin(origins = {"http://localhost:4200", "https://frontend-tienda-bicicletas-s3b8-zeta.vercel.app"})
@Tag(name = "Bicicletas", description = "Operaciones relacionadas con la gestion de bicicletas en la tienda ")
public class    BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    // Obtener todas las bicicletas
    @Operation(summary = "Listar todas las bicicletas", description = "Obtiene una lista de todas las bicicletas registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<BicicletaResponseDTO>> listar(){
        return ResponseEntity.ok(bicicletaService.listarTodas());
    }

    // Obtener una bicileta por su Id
    @Operation(summary = "Obtener una bicicleta por ID", description = "Busca una bicicleta específica utilizando su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bicicleta encontrada"),
            @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BicicletaResponseDTO> obtenerPorId(@PathVariable Integer id){
        return bicicletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva bicicleta
   /* @Operation(summary = "Crear una nueva bicicleta", description = "Registra una nueva bicicleta en el inventario. El stock inicial será 0.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bicicleta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<BicicletaResponseDTO> crear(@Valid @RequestBody BicicletaRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bicicletaService.guardar(request));
    }*/

    @Operation(summary = "Crear una nueva bicicleta", description = "Registra una nueva bicicleta en el inventario. El stock inicial será 0.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bicicleta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })

    @PostMapping("/crear")
    public ResponseEntity<Bicicleta> registrarBicicletas(@RequestBody BicicletaRequestDTO biciDto) {
        // Si la imagen viene en el DTO, el service se encargará de pasarla a la Entity
        Bicicleta nuevaBici = bicicletaService.registrarUnaSolaBicicleta(biciDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaBici);
    }

    // Actualizar una bicicleta existente
    @Operation(summary = "Actualizar una bicicleta", description = "Actualiza la información de una bicicleta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bicicleta actualizada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la bicicleta para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BicicletaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody BicicletaRequestDTO request){
        return bicicletaService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Desactiva una Bicicleta", description = "Desactiva una bicicleta")
    @ApiResponse(responseCode = "204", description = "Bicicleta desactivada correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){
        bicicletaService.eliminar(id);
        return ResponseEntity.ok("Bicicleta marcada como inactiva");
    }
}
