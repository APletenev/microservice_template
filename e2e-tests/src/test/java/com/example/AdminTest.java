package com.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminTest extends PrepareTest {

    @Test
    @Order(1)
    void adminLogin() {
        assertThat(login("admin","123")).hasURL(gatewayURL + "/");
    }


    @Test
    void adminCanGetUserAccount() {
        assertTrue(getUserInfo().ok());
    }

}
