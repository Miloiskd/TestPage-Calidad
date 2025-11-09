package com.demoblaze.pages;

import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver){
        this.driver = driver;
    }

    public void navigateTo(String url){
        driver.get(url);
    }
}
