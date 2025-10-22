package com.example.oreo.sales.service;

import com.example.oreo.sales.domain.Sale;
import com.example.oreo.sales.dto.SalesCreateDto;
import com.example.oreo.sales.dto.SalesResponseDto;
import com.example.oreo.sales.repository.SaleRepository;
import com.example.oreo.user.repository.UserRepository;
import com.example.oreo.sales.dto.SaleCreateRequest;
import com.example.oreo.sales.dto.SaleResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    public SaleResponse create(SaleCreateRequest req) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        // Validar sucursal si es BRANCH
        if (user.getRole().name().equals("BRANCH") &&
            !user.getBranch().equalsIgnoreCase(req.branch())) {
            throw new AccessDeniedException("Solo puedes crear ventas de tu sucursal");
        }

        Sale sale = new Sale();
        sale.setId(UUID.randomUUID().toString());
        sale.setSku(req.sku());
        sale.setUnits(req.units());
        sale.setPrice(req.price());
        sale.setBranch(req.branch());
        sale.setSoldAt(req.soldAt());
        sale.setCreatedBy(user.getUsername());

        saleRepository.save(sale);

        return new SaleResponse(
                sale.getId(),
                sale.getSku(),
                sale.getUnits(),
                sale.getPrice(),
                sale.getBranch(),
                sale.getSoldAt(),
                sale.getCreatedBy()
        );
    }

    // 🔍 Obtener detalle
    public SaleResponse get(String id) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        checkBranchAccess(sale.getBranch());
        return new SaleResponse(
                sale.getId(),
                sale.getSku(),
                sale.getUnits(),
                sale.getPrice(),
                sale.getBranch(),
                sale.getSoldAt(),
                sale.getCreatedBy()
        );
    }

    // 📋 Listar ventas
    public Page<SaleResponse> list(Instant from, Instant to, String branch, Pageable pageable) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (user.getRole().name().equals("BRANCH")) {
            branch = user.getBranch();
        }

        List<Sale> sales = saleRepository.findByDateRangeAndBranch(from, to, branch);
        List<SaleResponse> content = sales.stream().map(s -> new SaleResponse(
                s.getId(),
                s.getSku(),
                s.getUnits(),
                s.getPrice(),
                s.getBranch(),
                s.getSoldAt(),
                s.getCreatedBy()
        )).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        List<SaleResponse> sublist = content.subList(start, end);

        return new PageImpl<>(sublist, pageable, content.size());
    }

    // ✏️ Actualizar venta
    public SaleResponse update(String id, SaleCreateRequest req) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        checkBranchAccess(sale.getBranch());

        sale.setSku(req.sku());
        sale.setUnits(req.units());
        sale.setPrice(req.price());
        sale.setSoldAt(req.soldAt());
        saleRepository.save(sale);

        return new SalesResponseDto(
                sale.getId(),
                sale.getSku(),
                sale.getUnits(),
                sale.getPrice(),
                sale.getBranch(),
                sale.getSoldAt(),
                sale.getCreatedBy()
        );
    }

    // 🗑️ Eliminar venta (solo CENTRAL)
    public void delete(String id) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!user.getRole().name().equals("CENTRAL")) {
            throw new AccessDeniedException("Solo CENTRAL puede eliminar ventas");
        }

        saleRepository.deleteById(id);
    }

    // 🧠 Métodos auxiliares
    private void checkBranchAccess(String branch) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (user.getRole().name().equals("BRANCH") &&
            !user.getBranch().equalsIgnoreCase(branch)) {
            throw new AccessDeniedException("No tienes permiso para esta sucursal");
        }
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
