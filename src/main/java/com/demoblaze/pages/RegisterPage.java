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
import java.util.HashMap;
import java.util.Map;

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
    private By ConfirmationAccount = By.xpath("//div[@id='content' and contains(@class, 'col-sm-9')]//p[contains(text(), 'Congratulations!')]");
    private By WarningEmail = By.xpath("//div[contains(@class, 'alert') and contains(@class, 'alert-danger') and contains(@class, 'alert-dismissible') and contains(., 'Warning')]");
    private By SecurePassword = By.xpath("//div[contains(@class, 'text-danger') and contains(., 'Password must be between 4 and 20 characters')]");

    public Map<String, String> leerDatosDesdeExcel(int rowIndex) {
        String rutaArchivo = "excel/Users.xlsx";
        Map<String, String> datosUsuario = new HashMap<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream file = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row fila = sheet.getRow(rowIndex);

            datosUsuario.put("firstName", formatter.formatCellValue(fila.getCell(0)));
            datosUsuario.put("lastName", formatter.formatCellValue(fila.getCell(1)));
            datosUsuario.put("email", formatter.formatCellValue(fila.getCell(2)));
            datosUsuario.put("telephone", formatter.formatCellValue(fila.getCell(3)));
            datosUsuario.put("password", formatter.formatCellValue(fila.getCell(4)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return datosUsuario;
    }

    public void createAccount(int rowIndex) {
        Map<String, String> datos = leerDatosDesdeExcel(rowIndex);

        driver.findElement(firstName).clear();
        driver.findElement(firstName).sendKeys(datos.get("firstName"));
        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(datos.get("lastName"));
        driver.findElement(email).clear();
        driver.findElement(email).sendKeys(datos.get("email"));
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(datos.get("password"));
        driver.findElement(telephone).clear();
        driver.findElement(telephone).sendKeys(datos.get("telephone"));
        driver.findElement(confirmPassword).clear();
        driver.findElement(confirmPassword).sendKeys(datos.get("password"));

        driver.findElement(privacyPolicy).click();
        driver.findElement(ContinueButton).click();
    }

    public boolean isCreatedSuccessful(){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(ConfirmationAccount));
            return driver.findElement(ConfirmationAccount).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWarningEmailDisplayed(){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(WarningEmail));
            return driver.findElement(WarningEmail).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSecurePasswordDisplayed(){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(SecurePassword));
            return driver.findElement(SecurePassword).isDisplayed();
        }  catch (Exception e) {
            return false;
        }
    }
}
