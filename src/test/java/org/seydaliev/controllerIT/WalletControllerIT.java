package org.seydaliev.controllerIT;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.seydaliev.controller.WalletController;
import org.seydaliev.repository.WalletRepository;
import org.seydaliev.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", 63527);
        jsonObject.put("operationType", "DEPOSIT");
        jsonObject.put("amount", 1000);
        mockMvc.perform(patch("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpectAll(status().isOk(),
                        jsonPath("$.uuid").value(63527),
                        jsonPath("$.operationType").value("DEPOSIT"),
                        jsonPath("$.amount").value(1000));
    }

    @Test
    public void getBalance_isOk() throws Exception {
        UUID walletUUID = UUID.randomUUID();
        BigDecimal expectedBalance = new BigDecimal("12345");
        WalletService mockWalletService = mock(WalletService.class);
        when(mockWalletService.getBalanceByUUID(walletUUID)).thenReturn(expectedBalance);
        WalletController controller = new WalletController(mockWalletService);
        BigDecimal actualBalance = controller.getBalance(walletUUID);
        assertEquals(expectedBalance,actualBalance);
    }
}
