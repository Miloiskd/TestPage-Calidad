package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Elementos de la página
    private By myAccount = By.xpath("//li[contains(@class, 'dropdown')]//a");
    private By registerAccount = By.xpath("//ul[@class='dropdown-menu dropdown-menu-right']/li/a[text()='Register']");
    private By loginOption = By.xpath("//*[@id=\"top-links\"]/ul/li[2]/ul/li[2]/a");

    // Métodos de acción
    public void menuAccount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(myAccount)).click();
    }

    public void registerMyAccount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(registerAccount)).click();
    }

    //Hacer click en el botón del login
    public void clickLoginOption() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(loginOption)).click();
    }
}
