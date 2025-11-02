package com.example.oreo.sales;

import com.example.oreo.sales.domain.Sale;
import com.example.oreo.sales.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SalesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaleRepository salesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Instant now = Instant.parse("2024-05-01T00:00:00Z");

    @BeforeEach
    void setup() {
        salesRepository.deleteAll();

        // Ventas simuladas
        Sale s1 = new Sale();
        s1.setSku("A001");
        s1.setUnits(10);
        s1.setPrice(BigDecimal.valueOf(10.0));
        s1.setBranch("Lima");
        s1.setSoldAt(Instant.parse("2024-04-01T00:00:00Z"));
        s1.setCreatedBy("admin");

        Sale s2 = new Sale();
        s2.setSku("B002");
        s2.setUnits(5);
        s2.setPrice(BigDecimal.valueOf(20.0));
        s2.setBranch("Arequipa");
        s2.setSoldAt(Instant.parse("2024-04-10T00:00:00Z"));
        s2.setCreatedBy("admin");

        Sale s3 = new Sale();
        s3.setSku("A001");
        s3.setUnits(8);
        s3.setPrice(BigDecimal.valueOf(12.0));
        s3.setBranch("Lima");
        s3.setSoldAt(Instant.parse("2024-05-10T00:00:00Z"));
        s3.setCreatedBy("admin");

        salesRepository.save(s1);
        salesRepository.save(s2);
        salesRepository.save(s3);
    }

    @Test
    void testListarVentasDatosValidos() throws Exception {
        mockMvc.perform(get("/sales")
                        .param("from", "2024-01-01T00:00:00Z")
                        .param("to", "2024-12-31T23:59:59Z")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].sku").value("A001"));
    }

    @Test
    void testListaVacia() throws Exception {
        salesRepository.deleteAll();

        mockMvc.perform(get("/sales")
                        .param("from", "2024-01-01T00:00:00Z")
                        .param("to", "2024-12-31T23:59:59Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void testFiltradoPorSucursal() throws Exception {
        mockMvc.perform(get("/sales")
                        .param("from", "2024-01-01T00:00:00Z")
                        .param("to", "2024-12-31T23:59:59Z")
                        .param("branch", "Lima"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].branch").value("Lima"));
    }

    @Test
    void testFiltradoPorFechas() throws Exception {
        mockMvc.perform(get("/sales")
                        .param("from", "2024-04-01T00:00:00Z")
                        .param("to", "2024-04-30T23:59:59Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}
