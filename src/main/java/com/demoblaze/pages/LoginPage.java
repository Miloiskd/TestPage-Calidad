package com.demoblaze.pages;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Elementos de la página de login
    private By emailInput = By.xpath("//*[@id=\"input-email\"]");
    private By passwordInput = By.xpath("//*[@id=\"input-password\"]");
    private By loginButton = By.xpath("//input[@value='Login']");
    private By successMessage = By.xpath("//h2[text()='My Account']");
    private By errorMessage = By.xpath("//div[contains(@class, 'alert-danger')]");

    // Métodos para leer datos del Excel
    public List<Map<String, String>> leerDatosLogin() {
        String rutaArchivo = "excel/LoginData.xlsx";
        List<Map<String, String>> datosLogin = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream file = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Leer desde la fila 1 (YA QUE LA 0 SON LOS ENCABEZADOS)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);

                if (fila != null) {
                    Map<String, String> datos = new HashMap<>();
                    datos.put("email", formatter.formatCellValue(fila.getCell(0)));
                    datos.put("password", formatter.formatCellValue(fila.getCell(1)));
                    datos.put("expectedResult", formatter.formatCellValue(fila.getCell(2)));
                    datosLogin.add(datos);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return datosLogin;
    }

    // Método para realizar login
    public void login(String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // Método para verificar login exitoso
    public boolean isLoginSuccessful() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return driver.findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Método para verificar mensaje de error
    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Método para obtener el texto del mensaje de error
    public String getErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }
}