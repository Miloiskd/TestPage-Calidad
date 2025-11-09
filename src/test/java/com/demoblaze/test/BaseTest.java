package com.demoblaze.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    protected WebDriver driver;

    @BeforeTest
    public void setup(){
        //Configurar el Driver
        WebDriverManager.chromedriver().setup();

        //Crear una instancia de WebDriver para Chrome
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterTest
    public void teaDown(){
        if(driver != null){
            //driver.quit();
        }
    }
}
