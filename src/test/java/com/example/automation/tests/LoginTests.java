package com.example.automation.tests;

import com.example.automation.pages.HomePage;
import com.example.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.login("admin", "admin123");
        String message = homePage.getWelcomeMessage();

        Assert.assertTrue(message.contains("Blaze"));
    }
}
