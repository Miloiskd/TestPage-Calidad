package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest{

    @Test
    public void Register(){
        //Pages
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        homePage.menuAccount();
        homePage.registerMyAccount();
        registerPage.createAccount();


    }
}
