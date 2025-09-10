package com.nttdata.dockerized.postgresql.controller;

import com.nttdata.dockerized.postgresql.model.dto.*;
import com.nttdata.dockerized.postgresql.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listAll() {
        return ResponseEntity.ok(pedidoService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDto> create(@Valid @RequestBody PedidoCreateRequestDto request) {
        PedidoResponseDto created = pedidoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PostMapping("/{pedidoId}/detalles")
    public ResponseEntity<PedidoResponseDto> addDetalle(
            @PathVariable Long pedidoId,
            @Valid @RequestBody DetallePedidoCreateRequestDto detalleDto) {
        PedidoResponseDto updated = pedidoService.addDetalle(pedidoId, detalleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PutMapping("/{pedidoId}/detalles/{detalleId}")
    public ResponseEntity<PedidoResponseDto> updateDetalle(
            @PathVariable Long pedidoId,
            @PathVariable Long detalleId,
            @Valid @RequestBody DetallePedidoUpdateRequestDto request) {
        PedidoResponseDto updated = pedidoService.updateDetalle(pedidoId, detalleId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{pedidoId}/detalles/{detalleId}")
    public ResponseEntity<PedidoResponseDto> removeDetalle(
            @PathVariable Long pedidoId,
            @PathVariable Long detalleId) {
        PedidoResponseDto updated = pedidoService.removeDetalle(pedidoId, detalleId);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoUpdateRequestDto request) {
        return ResponseEntity.ok(pedidoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}