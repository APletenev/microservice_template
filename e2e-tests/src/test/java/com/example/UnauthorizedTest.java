package com.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class UnauthorizedTest extends PrepareTest {

    @Test
    @Order(1)
    void unauthorizedLogin() {
        assertThat(login("NotExistingUser","NotValidPassword")).hasURL(IDP_URL + "/login?error");
    }
    @Test
    void unauthorizedCanNotGetUserAccount() {
        assertEquals(getUserInfo().status(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void unauthorizedCanNotAccessOtherEndpoints() {
        assertEquals(tryEndPointOtherThenSignup().url(), IDP_URL +"/login");
    }

}
