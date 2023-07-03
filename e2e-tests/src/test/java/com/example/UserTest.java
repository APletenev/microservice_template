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
    void userLogin() {
        assertThat(login("user","456")).hasURL(GATEWAY_URL + "/");
    }


    @Test
    void authorizedUserCanNotSignUp() {
        APIResponse response = signUp("authorizedUserSignUp"+ UUID.randomUUID());
        assertEquals(response.status(), HttpStatus.FORBIDDEN.value(),"Request response: " + response.text());
    }

    @Test
    void userCanNotGetUserAccount() {
        assertEquals(getUserInfo().status(),HttpStatus.FORBIDDEN.value());
    }

}
