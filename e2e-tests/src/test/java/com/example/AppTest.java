package com.example;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private APIRequestContext request;
    private final String gatewayURL = "https://" + System.getenv("GATEWAY_HOST") + ":" + System.getenv("GATEWAY_PORT");

    @Test
    void adminCanGetUserInfo() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()))
        {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));

            Page page = context.newPage();

            page.navigate(gatewayURL + "/oauth2/authorization/gateway");
            page.getByPlaceholder("Username").fill("admin");
            page.getByPlaceholder("Password").fill("123");
            page.getByPlaceholder("Password").press("Enter");
            assertThat(page).hasURL(gatewayURL + "/");

            request = context.request();
            APIResponse response = request.get(
                    gatewayURL +
                            System.getenv("ACCOUNT_API") +
                            "/user");
            assertTrue(response.ok());
        }
    }
}
