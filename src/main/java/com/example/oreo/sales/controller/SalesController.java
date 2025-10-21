package com.example.oreo.sales.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.time.*;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final ApplicationEventPublisher events;

    // Crear nueva venta
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse create(@RequestBody SaleCreateRequest req) {
        return salesService.create(req);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public SaleResponse get(@PathVariable String id) {
        return salesService.get(id);
    }

    // Listar todas las ventas (con filtros)
    @GetMapping
    public Page<SaleResponse> list(
            @RequestParam Instant from,
            @RequestParam Instant to,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return salesService.list(from, to, branch, PageRequest.of(page, size));
    }

    // Actualizar una venta
    @PutMapping("/{id}")
    public SaleResponse update(@PathVariable String id, @RequestBody SaleCreateRequest req) {
        return salesService.update(id, req);
    }

    // Eliminar venta (solo CENTRAL)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        salesService.delete(id);
    }

    // Generar resumen semanal (asíncrono)
    @PostMapping("/summary/weekly")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SummaryAck summary(@RequestBody SummaryRequest req) {
        var now = Instant.now();
        var from = (req.from() == null ? LocalDate.now().minusDays(7) : req.from())
                .atStartOfDay().toInstant(ZoneOffset.UTC);
        var to = (req.to() == null ? LocalDate.now() : req.to())
                .plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        String requestId = "req_" + UUID.randomUUID();
        events.publishEvent(new ReportRequestedEvent(
                requestId, from, to, req.branch(), req.emailTo(), "system"
        ));

        return new SummaryAck(
                requestId,
                "PROCESSING",
                "Su solicitud de reporte está siendo procesada. Recibirá el resumen en " + req.emailTo(),
                "30-60 segundos",
                now
        );
    }
}
