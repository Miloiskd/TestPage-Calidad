package com.demoblaze.test;

import com.demoblaze.pages.BasePage;
import org.testng.annotations.Test;

public class OpenURLTest extends BaseTest{

    @Test
    public void OpenUrl(){
        BasePage basePage = new BasePage(driver);

        basePage.navigateTo("https://opencart.abstracta.us/");
    }

}
