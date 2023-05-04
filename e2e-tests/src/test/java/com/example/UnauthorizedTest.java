package com.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)



public class UnauthorizedTest extends PrepareTest {

    final String idpURL = "http://" + System.getenv("IDP_HOST") + ":" + System.getenv("IDP_PORT");
    @Test
    @Order(1)
    void unauthorizedLogin() {
        assertThat(login("NotExistingUser","NotValidPassword")).hasURL(idpURL + "/login?error");
    }

    @Test
    void unauthorizedCanNotGetUserAccount() {
        assertEquals(getUserInfo().status(),401);
    }

}
