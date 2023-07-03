package com.example;

import com.microsoft.playwright.APIResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminTest extends PrepareTest {

    @Test
    @Order(1)
    void adminLogin() {
        assertThat(login("admin","123")).hasURL(GATEWAY_URL + "/");
    }

    @Test
    void authorizedAdminCanNotSignUp() {
        APIResponse response = signUp("authorizedAdminSignUp"+ UUID.randomUUID());
        assertEquals(response.status(), HttpStatus.FORBIDDEN.value(),"Request response: " + response.text());
    }

    @Test
    void adminCanGetUserAccount() {
        assertTrue(getUserInfo().ok());
    }

}
