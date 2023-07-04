package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UnauthorizedTest extends PrepareTest {

    @Test
    void unauthorizedCanSignUp() {
        unauthorizedSignUp();
    }

    @Test
    void unauthorizedCanNotLogin() {
        assertThat(login("NotExistingUser","NotValidPassword")).hasURL(IDP_URL + "/login?error");
    }
    @Test
    void unauthorizedCanNotGetUserAccount() {
        assertEquals(HttpStatus.UNAUTHORIZED.value(), getUserInfo().status());
    }

    @Test
    void unauthorizedCanNotAccessOtherEndpoints() {
        assertEquals(IDP_URL +"/login", tryEndPointOtherThenSignup().url()); //Should be redirected то login form
    }

}
