package com.example;

import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class UnauthorizedTest extends PrepareTest {

    @Test
    @Order(1)
    void unauthorizedLogin() {
        assertThat(login("NotExistingUser","NotValidPassword")).hasURL(idpURL + "/login?error");
    }

    @Test
    void unauthorizedCanNotGetUserAccount() {
        assertEquals(getUserInfo().status(),401);
    }

    @Test
    void unauthorizedCanNotAccessOtherEndpoints() {
        assertEquals(tryEndPointOtherThenSignup().url(),idpURL+"/login");
    }

}
