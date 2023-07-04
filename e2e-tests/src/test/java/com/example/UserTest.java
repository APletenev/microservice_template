package com.example;

import com.microsoft.playwright.APIResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTest extends PrepareTest {

    @Test
    @Order(1)
    void userSignup() {
        unauthorizedSignUp();
    }

    @Test
    @Order(2)
    void userLogin() {
        assertThat(login(username, TESTPASSWORD)).hasURL(GATEWAY_URL + "/");
    }


    @Test
    void authorizedUserCanNotSignUp() {
        APIResponse response = signUp("authorizedUserSignUp" + UUID.randomUUID());
        assertEquals(HttpStatus.FORBIDDEN.value(), response.status(), "Request response: " + response.text());
    }

    @Test
    void userCanNotGetUserAccount() {
        assertEquals(HttpStatus.FORBIDDEN.value(), getUserInfo().status());
    }

}
