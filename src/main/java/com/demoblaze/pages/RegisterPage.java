package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    private By firstName = By.name("firstname");
    private By lastName = By.name("lastname");
    private By email = By.name("email");
    private By password = By.name("password");
    private By telephone = By.name("telephone");
    private By confirmPassword = By.name("confirm");
    private By privacyPolicy = By.name("agree");
    private By ContinueButton = By.xpath("//input[contains(@class, 'btn') and contains(@class, 'btn-primary')]");


    public void createAccount(){
        driver.findElement(firstName).sendKeys(Account[0]);
        driver.findElement(lastName).sendKeys(Account[1]);
        driver.findElement(email).sendKeys(Account[2]);
        driver.findElement(password).sendKeys(Password[0]);
        driver.findElement(telephone).sendKeys(Account[3]);
        driver.findElement(confirmPassword).sendKeys(Password[1]);

        driver.findElement(privacyPolicy).click();
        driver.findElement(ContinueButton).click();
    }
}
