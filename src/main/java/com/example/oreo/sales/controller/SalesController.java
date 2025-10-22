package com.example.oreo.sales.controller;

import com.example.oreo.sales.service.SalesService;
import com.example.oreo.sales.dto.SaleCreateRequest;
import com.example.oreo.sales.dto.SaleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    // Crear nueva venta (POST /sales)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse create(@RequestBody SaleCreateRequest req) {
        return salesService.create(req);
    }

    // Obtener detalle (GET /sales/{id})
    @GetMapping("/{id}")
    public SaleResponse get(@PathVariable String id) {
        return salesService.get(id);
    }

    // Listar ventas con filtros (GET /sales)
    @GetMapping
    public Page<SaleResponse> list(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return salesService.list(from, to, branch, pageable);
    }

    // Actualizar venta (PUT /sales/{id})
    @PutMapping("/{id}")
    public SaleResponse update(@PathVariable String id, @RequestBody SaleCreateRequest req) {
        return salesService.update(id, req);
    }

    // Eliminar venta (DELETE /sales/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        salesService.delete(id);
    }
}
