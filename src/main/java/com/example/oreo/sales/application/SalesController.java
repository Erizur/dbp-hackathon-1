package com.example.oreo.sales.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.oreo.sales.dto.SalesCreateDto;
import com.example.oreo.sales.dto.SalesResponseDto;
import com.example.oreo.sales.dto.SummaryAckDto;
import com.example.oreo.sales.dto.SummaryRequestDto;
import com.example.oreo.sales.event.ReportEvent;
import com.example.oreo.sales.service.SalesService;

import org.springframework.data.domain.*;
import java.time.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {
    
    private final ModelMapper modelMapper;
    private final SalesService salesService;
    private final ApplicationEventPublisher events;

    // Crear nueva venta
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalesResponseDto create(@RequestBody SalesCreateDto req) {
        return salesService.create(req);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public SalesResponseDto get(@PathVariable String id) {
        return salesService.get(id);
    }

    // Listar todas las ventas (con filtros)
    @GetMapping
    public Page<SalesResponseDto> list(
            @RequestParam Instant from,
            @RequestParam Instant to,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return salesService.list(from, to, branch, PageRequest.of(page, size));
    }

    // Actualizar una venta
    @PutMapping("/{id}")
    public SalesResponseDto update(@PathVariable String id, @RequestBody SalesCreateDto req) {
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
    public SummaryAckDto summary(@RequestBody SummaryRequestDto req) {
        var now = Instant.now();
        var from = (req.getFrom() == null ? LocalDate.now().minusDays(7) : req.getFrom())
                .atStartOfDay().toInstant(ZoneOffset.UTC);
        var to = (req.getTo() == null ? LocalDate.now() : req.getTo())
                .plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        
        ReportEvent ev = salesService.buildReport(from, to, req.getBranch());
        events.publishEvent(ev);
        SummaryAckDto ack = modelMapper.map(ev, SummaryAckDto.class);
        ack.setStatus("PROCESANDO");
        ack.setMessage("Su solicitud de reporte está siendo procesada. Recibirá el resumen en " + req.getEmailTo());
        ack.setEstimatedTime("30-60 segundos");
        ack.setRequestedAt(now);

        return ack;
    }
}