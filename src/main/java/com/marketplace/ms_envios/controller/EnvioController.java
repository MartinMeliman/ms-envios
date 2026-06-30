package com.marketplace.ms_envios.controller;

import com.marketplace.ms_envios.model.Envio;
import com.marketplace.ms_envios.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Envíos", description = "Gestión de envíos y seguimiento de pedidos del marketplace EcoTrade")
@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @Operation(summary = "Listar todos los envíos",
               description = "Retorna la lista completa de envíos registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public List<Envio> obtenerTodos() {
        return envioService.obtenerTodos();
    }

    @Operation(summary = "Obtener envío por ID",
               description = "Busca un envío por su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(
            @Parameter(description = "ID del envío") @PathVariable Long id) {
        return envioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener envío por pedido",
               description = "Busca el envío asociado a un pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío del pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe envío para ese pedido")
    })
    @GetMapping("/pedido/{pid}")
    public ResponseEntity<Envio> porPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long pid) {
        return envioService.obtenerPorPedido(pid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo envío",
               description = "Registra un envío para un pedido y genera su código de seguimiento. Solo se permite un envío por pedido.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Ya existe un envío para ese pedido")
    })
    @PostMapping
    public ResponseEntity<Envio> crear(@Valid @RequestBody Envio e) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.guardar(e));
    }

    @Operation(summary = "Actualizar estado del envío",
               description = "Actualiza el estado del envío y agrega una descripción del seguimiento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(
            @Parameter(description = "ID del envío") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del envío") @RequestParam String estado,
            @Parameter(description = "Descripción del seguimiento (opcional)") @RequestParam(required = false) String descripcion) {
        return envioService.actualizarEstado(id, estado, descripcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}