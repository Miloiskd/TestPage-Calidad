package com.demoblaze.pages;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPage extends BasePage {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    private By searchInput = By.cssSelector("input[name='search']");
    private By productName = By.cssSelector("h4 a");
    private By noResultsMessage = By.xpath("//p[contains(text(), 'There is no product')]");

    private By addToCartButton = By.cssSelector("#button-cart");
    private By quantityInput = By.cssSelector("#input-quantity");

    private By successMessage = By.cssSelector("div.alert.alert-success");


    /**
     * Lee los productos desde el archivo Excel
     */
    public List<Map<String, String>> leerProductosDesdeExcel() {
        String rutaArchivo = "excel/ProductosBusqueda.xlsx";
        List<Map<String, String>> productos = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream file = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);

                if (fila != null) {
                    Map<String, String> producto = new HashMap<>();
                    producto.put("categoria", formatter.formatCellValue(fila.getCell(0)));
                    producto.put("subcategoria", formatter.formatCellValue(fila.getCell(1)));
                    producto.put("producto", formatter.formatCellValue(fila.getCell(2)));
                    producto.put("cantidad", formatter.formatCellValue(fila.getCell(3)));
                    productos.add(producto);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return productos;
    }

    /**
     * Busca un producto usando el campo de búsqueda
     */
    public void searchProduct(String productName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
            searchField.clear();
            searchField.sendKeys(productName);
            searchField.sendKeys(Keys.ENTER);

            // Esperar a que se carguen los resultados o mensaje de no resultados
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(this.productName),
                    ExpectedConditions.presenceOfElementLocated(noResultsMessage)
            ));

        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica si el producto aparece en los resultados
     */
    public boolean isProductInResults(String productName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            try {
                if (driver.findElement(noResultsMessage).isDisplayed()) {
                    return false;
                }
            } catch (Exception ignored) {}

            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(this.productName));

            for (WebElement product : products) {
                String productText = product.getText().toLowerCase().trim();
                String searchText = productName.toLowerCase().trim();

                if (productText.contains(searchText) || searchText.contains(productText)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hace clic en un producto específico de los resultados
     */
    public void clickOnProduct(String productName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(this.productName));

            for (WebElement product : products) {
                String productText = product.getText().toLowerCase().trim();
                String searchText = productName.toLowerCase().trim();

                if (productText.contains(searchText) || searchText.contains(productText)) {
                    product.click();

                    wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al hacer clic en el producto: " + productName);
            e.printStackTrace();
        }
    }

    /**
     * Establece la cantidad del producto
     */
    public void setQuantity(int quantity) {
        try {
            System.out.println("Estableciendo cantidad: " + quantity);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement quantityField = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));

            String currentValue = quantityField.getAttribute("value");
            if (!currentValue.equals(String.valueOf(quantity))) {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].value = '" + quantity + "';", quantityField);
            }

            wait.until(ExpectedConditions.attributeToBe(quantityField, "value", String.valueOf(quantity)));
            System.out.println("✓ Cantidad establecida correctamente");

        } catch (Exception e) {
            System.err.println("Error al establecer la cantidad");
            e.printStackTrace();
        }
    }

    /**
     * Agrega el producto al carrito
     */
    public void addToCart() {
        try {
            System.out.println("Agregando producto al carrito...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", addButton);

            try {
                addButton.click();
            } catch (Exception e) {
                System.out.println("Clic normal falló, usando JavaScript...");
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", addButton);
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            System.out.println("✓ Producto agregado al carrito correctamente");

        } catch (Exception e) {
            System.err.println("Error al agregar al carrito");
            System.err.println("URL actual: " + driver.getCurrentUrl());
            e.printStackTrace();
        }
    }

    /**
     * Verifica si el mensaje de éxito se muestra
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el texto del mensaje de éxito
     */
    public String getSuccessMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
