package com.nttdata.dockerized.postgresql.controller;

import com.nttdata.dockerized.postgresql.model.dto.PedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoUpdateRequestDto;
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

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody PedidoUpdateRequestDto request) {
        return ResponseEntity.ok(pedidoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
