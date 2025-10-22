package com.example.oreo.sales.service;

import com.example.oreo.sales.domain.Sale;
import com.example.oreo.sales.dto.SalesCreateDto;
import com.example.oreo.sales.dto.SalesResponseDto;
import com.example.oreo.sales.repository.SaleRepository;
import com.example.oreo.user.repository.UserRepository;
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

  
    public SalesResponseDto create(SalesCreateDto req) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        if (user.getRole().name().equals("BRANCH")) {
            if (!user.getBranch().equalsIgnoreCase(req.branch())) {
                throw new AccessDeniedException("Solo puedes crear ventas de tu propia sucursal");
            }
        }

        Sale s = new Sale();
        s.setId(UUID.randomUUID().toString());
        s.setSku(req.sku());
        s.setUnits(req.units());
        s.setPrice(req.price());
        s.setBranch(req.branch());
        s.setSoldAt(req.soldAt());
        s.setCreatedBy(user.getUsername());

        saleRepository.save(s);

        return new SalesResponseDto(
                s.getId(),
                s.getSku(),
                s.getUnits(),
                s.getPrice(),
                s.getBranch(),
                s.getSoldAt(),
                s.getCreatedBy()
        );
    }

    public SalesResponseDto get(String id) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        checkBranchAccess(sale.getBranch());

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

    public Page<SalesResponseDto> list(Instant from, Instant to, String branch, Pageable pageable) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (user.getRole().name().equals("BRANCH")) {
            branch = user.getBranch();
        }

        List<Sale> sales = saleRepository.findByDateRangeAndBranch(from, to, branch);
        List<SalesResponseDto> content = sales.stream().map(s ->
                new SalesResponseDto(
                        s.getId(), s.getSku(), s.getUnits(), s.getPrice(),
                        s.getBranch(), s.getSoldAt(), s.getCreatedBy()
                )).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        List<SalesResponseDto> sub = content.subList(start, end);
        return new PageImpl<>(sub, pageable, content.size());
    }

    public SalesResponseDto update(String id, SalesCreateDto req) {
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

    public void delete(String id) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!user.getRole().name().equals("CENTRAL")) {
            throw new AccessDeniedException("Solo los usuarios CENTRAL pueden eliminar ventas");
        }

        saleRepository.deleteById(id);
    }

    private void checkBranchAccess(String branch) {
        var auth = getAuth();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (user.getRole().name().equals("BRANCH") &&
                !user.getBranch().equalsIgnoreCase(branch)) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta sucursal");
        }
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void ensureEmailPresent(String email) {
        if (email == null || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailTo es obligatorio y debe ser válido");
        }
    }

    public void ensureBranchPermission(String branch) {
        checkBranchAccess(branch);
    }
}
