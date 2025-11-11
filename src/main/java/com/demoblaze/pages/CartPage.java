package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Elementos de la página
    private By viewCartLink() {
        return By.cssSelector("a[title='Shopping Cart']");
    }

    private By cartPageTable() {
        return By.cssSelector("div.table-responsive");
    }

    private By cartPageProductRows() {
        return By.cssSelector("#content form tbody tr");
    }
    /**
     * Busca un producto en el carrito por su nombre
     */
    private WebElement findProductRowByName(String productName) {
        List<WebElement> rows = driver.findElements(cartPageProductRows());
        for (WebElement row : rows) {
            try {
                WebElement nameElement = row.findElement(By.cssSelector("td:nth-child(2) a"));
                String nombre = nameElement.getText().trim();
                if (nombre.toLowerCase().contains(productName.toLowerCase())) {
                    return row;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    /**
     * Navega a la página completa del carrito
     */
    public void goToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(viewCartLink()));
        cart.click();
        wait.until(ExpectedConditions.urlContains("checkout/cart"));
    }

    /**
     * Verifica si el carrito está vacío (en la página completa)
     */
    public boolean isCartEmpty() {
        try {
            WebElement emptyMessage = driver.findElement(By.cssSelector("#content p"));
            return emptyMessage.getText().contains("cart is empty");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un producto específico está en el carrito
     */
    public boolean isProductInCart(String productName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(cartPageTable()));

            WebElement productRow = findProductRowByName(productName);
            return productRow != null && productRow.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene la cantidad de un producto específico en el carrito
     */
    public int getProductQuantityInCart(String productName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(cartPageTable()));

            WebElement productRow = findProductRowByName(productName);
            if (productRow != null) {
                WebElement quantityInput = productRow.findElement(By.cssSelector("input[name*='quantity']"));
                String cantidad = quantityInput.getAttribute("value");
                return Integer.parseInt(cantidad);
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Obtiene la lista de productos en el carrito
     * Retorna una lista de mapas con: nombre, cantidad, precio, total
     */
    public List<Map<String, String>> getProductsInCart() {
        List<Map<String, String>> productos = new ArrayList<>();

        try {
            if (isCartEmpty()) {
                return productos;
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(cartPageTable()));

            List<WebElement> rows = driver.findElements(cartPageProductRows());

            for (WebElement row : rows) {
                try {
                    Map<String, String> producto = new HashMap<>();

                    WebElement nameElement = row.findElement(By.cssSelector("td:nth-child(2) a"));
                    String nombre = nameElement.getText().trim();

                    WebElement quantityInput = row.findElement(By.cssSelector("input[name*='quantity']"));
                    String cantidad = quantityInput.getAttribute("value");

                    WebElement priceElement = row.findElement(By.cssSelector("td:nth-child(4)"));
                    String precio = priceElement.getText().trim();

                    WebElement totalElement = row.findElement(By.cssSelector("td:nth-child(5)"));
                    String total = totalElement.getText().trim();

                    producto.put("nombre", nombre);
                    producto.put("cantidad", cantidad);
                    producto.put("precio", precio);
                    producto.put("total", total);

                    productos.add(producto);

                } catch (Exception e) {
                    System.err.println("Error al procesar una fila del carrito: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener productos del carrito: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Obtiene el número total de productos en el carrito
     */
    public int getTotalProductsInCart() {
        if (isCartEmpty()) {
            return 0;
        }

        try {
            List<WebElement> rows = driver.findElements(cartPageProductRows());
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Imprime el contenido del carrito en consola
     */
    public void printCartContents() {
        System.out.println("\n=== Contenido del Carrito ===");

        if (isCartEmpty()) {
            System.out.println("El carrito está vacío");
            return;
        }

        List<Map<String, String>> productos = getProductsInCart();
        System.out.println("Total de productos: " + productos.size());

        for (int i = 0; i < productos.size(); i++) {
            Map<String, String> producto = productos.get(i);
            System.out.println("\nProducto " + (i + 1) + ":");
            System.out.println("  Nombre: " + producto.get("nombre"));
            System.out.println("  Cantidad: " + producto.get("cantidad"));
            System.out.println("  Precio: " + producto.get("precio"));
            System.out.println("  Total: " + producto.get("total"));
        }

        System.out.println("=============================\n");
    }
}
