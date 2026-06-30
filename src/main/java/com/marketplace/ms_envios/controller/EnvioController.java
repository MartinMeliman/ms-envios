package com.marketplace.ms_envios.controller;
import com.marketplace.ms_envios.model.Envio;
import com.marketplace.ms_envios.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/envios") @RequiredArgsConstructor
public class EnvioController {
    private final EnvioService envioService;
    @GetMapping public List<Envio> obtenerTodos(){ return envioService.obtenerTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id){ return envioService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @GetMapping("/pedido/{pid}") public ResponseEntity<Envio> porPedido(@PathVariable Long pid){ return envioService.obtenerPorPedido(pid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public ResponseEntity<Envio> crear(@Valid @RequestBody Envio e){ return ResponseEntity.status(HttpStatus.CREATED).body(envioService.guardar(e)); }
    @PutMapping("/{id}/estado") public ResponseEntity<Envio> actualizarEstado(@PathVariable Long id, @RequestParam String estado, @RequestParam(required=false) String descripcion){ return envioService.actualizarEstado(id,estado,descripcion).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
}
