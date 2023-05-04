package com.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest extends PrepareTest {

    @Test
    @Order(1)
    void userLogin() {
        assertThat(login("user","456")).hasURL(gatewayURL + "/");
    }


    @Test
    void userCanNotGetUserAccount() {
        assertEquals(getUserInfo().status(),403);
    }

}
