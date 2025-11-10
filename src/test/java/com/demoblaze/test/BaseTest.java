package com.demoblaze.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod  // Cambiar de @BeforeTest a @BeforeMethod
    public void setup(){
        //Configurar el Driver
        WebDriverManager.chromedriver().setup();

        //Crear una instancia de WebDriver para Chrome
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod  // Cambiar de @AfterTest a @AfterMethod
    public void tearDown(){
        if(driver != null){
            driver.quit();  // Descomentar esta línea
        }
    }
}
