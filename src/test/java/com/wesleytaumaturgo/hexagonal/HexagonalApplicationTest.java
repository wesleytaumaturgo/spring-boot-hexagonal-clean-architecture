package com.wesleytaumaturgo.hexagonal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Smoke test to verify application context loads correctly
@SpringBootTest
@ActiveProfiles("test")
class HexagonalApplicationTest {

    @Test
    void contextLoads() {
        // verifies Spring context starts without errors
    }
}
