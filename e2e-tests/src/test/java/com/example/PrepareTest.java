package com.example;


import com.example.common.coreapi.UserDTO;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepareTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    APIRequestContext request;
    final String gatewayURL = "https://" + System.getenv("GATEWAY_HOST") + ":" + System.getenv("GATEWAY_PORT");
    final String idpURL = "http://" + System.getenv("IDP_HOST") + ":" + System.getenv("IDP_PORT");
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
        APIResponse response=signUp();
        assertEquals(response.status(),200);
    }

    Page login(String username, String password) {

        Page page = context.newPage();

        page.navigate(gatewayURL + "/oauth2/authorization/gateway");
        page.getByPlaceholder("Username").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByPlaceholder("Password").press("Enter");

        return page;
    }

    APIResponse getUserInfo() {
        return request.get(
                gatewayURL +
                        System.getenv("ACCOUNT_API") +
                        "/"+ this.username);
    }

    APIResponse signUp() {
        return request.post(
                idpURL +
                        System.getenv("IDP_API") +
                        "/signup", RequestOptions.create().setData(new UserDTO(this.username, "testpassword", "test@email.com")));
    }

    APIResponse tryEndPointOtherThenSignup() {
        return request.get(
                idpURL +
                        System.getenv("IDP_API") +
                        "/");
    }

}
