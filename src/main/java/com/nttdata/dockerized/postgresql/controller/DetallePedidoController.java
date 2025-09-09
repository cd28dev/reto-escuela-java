package com.nttdata.dockerized.postgresql.controller;

import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoUpdateRequestDto;
import com.nttdata.dockerized.postgresql.service.DetallePedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedidos")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @GetMapping
    public ResponseEntity<List<DetallePedidoResponseDto>> listAll() {
        List<DetallePedidoResponseDto> detalles = detallePedidoService.listAll();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDto> findById(@PathVariable Long id) {
        DetallePedidoResponseDto detalle = detallePedidoService.findById(id);
        return ResponseEntity.ok(detalle);
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponseDto> create(@Valid @RequestBody DetallePedidoCreateRequestDto request) {
        DetallePedidoResponseDto created = detallePedidoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DetallePedidoUpdateRequestDto request
    ) {
        DetallePedidoResponseDto updated = detallePedidoService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detallePedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
