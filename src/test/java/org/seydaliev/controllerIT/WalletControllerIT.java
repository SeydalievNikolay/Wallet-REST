package org.seydaliev.controllerIT;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.seydaliev.controller.WalletController;
import org.seydaliev.dto.WalletDTO;
import org.seydaliev.model.OperationType;
import org.seydaliev.repository.WalletRepository;
import org.seydaliev.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WalletControllerIT {
    @Autowired
    private DataSource dataSource;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletController walletController;
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testPostgresql() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
        }
    }

    @AfterEach
    void cleanDB() {
        walletRepository.deleteAll();
    }

    @Test
    public void getWallet_isOk() throws Exception {

        WalletDTO walletDTO = new WalletDTO();
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        walletDTO.setUuid(fixedUUID);
        walletDTO.setOperationType(OperationType.DEPOSIT);
        walletDTO.setAmount(BigDecimal.valueOf(10));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(walletDTO);

        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Future<String> future = executor.submit(() -> {
                return mockMvc.perform(patch("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andReturn().getResponse().getContentAsString();
            });
            futures.add(future);
        }

        for (Future<String> future : futures) {
            String response = future.get();
        }

        executor.shutdown();
    }

    @Test
    public void getBalance_isOk() throws Exception {
        UUID walletUUID = UUID.randomUUID();
        BigDecimal expectedBalance = new BigDecimal("12345");
        WalletService mockWalletService = mock(WalletService.class);
        when(mockWalletService.getBalanceByUUID(walletUUID)).thenReturn(expectedBalance);
        WalletController controller = new WalletController(mockWalletService);
        BigDecimal actualBalance = controller.getBalance(walletUUID);
        assertEquals(expectedBalance, actualBalance);
    }
}
