package com.demoblaze.pages;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.io.IOException;
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

    public Map<String, String> leerDatosDesdeExcel() {
        String rutaArchivo = "excel/Users.xlsx";
        Map<String, String> datosUsuario = new HashMap<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream file = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row fila = sheet.getRow(1);

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

    public void createAccount(){
        Map<String, String> datos = leerDatosDesdeExcel();

        driver.findElement(firstName).sendKeys(datos.get("firstName"));
        driver.findElement(lastName).sendKeys(datos.get("lastName"));
        driver.findElement(email).sendKeys(datos.get("email"));
        driver.findElement(password).sendKeys(datos.get("password"));
        driver.findElement(telephone).sendKeys(datos.get("telephone"));
        driver.findElement(confirmPassword).sendKeys(datos.get("password"));

        driver.findElement(privacyPolicy).click();
        driver.findElement(ContinueButton).click();
    }
}
