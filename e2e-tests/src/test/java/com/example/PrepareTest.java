package com.example;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepareTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    APIRequestContext request;
    final String gatewayURL = "https://" + System.getenv("GATEWAY_HOST") + ":" + System.getenv("GATEWAY_PORT");

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        request = context.request();
    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
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
                        "/user");
    }

}
