package com.example;


import com.example.common.coreapi.UserDTO;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepareTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    APIRequestContext request;
    final String GATEWAY_URL = "https://" + System.getenv("GATEWAY_HOST") + ":" + System.getenv("GATEWAY_PORT");
    final String IDP_URL = "http://" + System.getenv("IDP_HOST") + ":" + System.getenv("IDP_PORT");
    private String username;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        request = context.request();
        username = "testuser" + UUID.randomUUID();

        unauthorizedCanSignUp(); //Create user and account for using by another tests

    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
    }

    void unauthorizedCanSignUp() {
        APIResponse response = signUp(this.username);
        assertTrue(response.ok(), "Request response: " + response.text());
    }

    Page login(String username, String password) {

        Page page = context.newPage();

        page.navigate(GATEWAY_URL + "/oauth2/authorization/gateway");
        page.getByPlaceholder("Username").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByPlaceholder("Password").press("Enter");

        return page;
    }

    APIResponse getUserInfo() {
        return request.get(
                GATEWAY_URL +
                        System.getenv("ACCOUNT_API") +
                        "/" + this.username);
    }

    APIResponse signUp(String username) {

        return request.post(
                IDP_URL +
                        System.getenv("IDP_API") +
                        "/signup", RequestOptions.create().setData(new UserDTO(username, "testpassword", "test@email.com")));
    }

    APIResponse tryEndPointOtherThenSignup() {
        return request.get(
                IDP_URL +
                        System.getenv("IDP_API") +
                        "/");
    }

}
