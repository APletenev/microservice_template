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

    @BeforeAll
    void prepareUserAccountForTests() {
        unauthorizedSignUp();
    }
    @Test
    @Order(1)
    void adminLogin() {
        assertThat(login(System.getenv("IDP_ADMIN"),System.getenv("IDP_ADMIN_PASSWORD"))).hasURL(GATEWAY_URL + "/");
    }

    @Test
    void authorizedAdminCanNotSignUp() {
        APIResponse response = signUp("authorizedAdminSignUp"+ UUID.randomUUID());
        assertEquals(HttpStatus.FORBIDDEN.value(), response.status(), "Request response: " + response.text());
    }

    @Test
    void adminCanGetUserAccount() {
        APIResponse response = getUserInfo();
        assertTrue(response.ok(),"Request response: " + response.text());
    }

}
