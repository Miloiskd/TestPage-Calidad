package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.SearchPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public class ProductSearchTest extends BaseTest {

    @Test(priority = 1, description = "Buscar productos y agregar al carrito con HardAssert - Primeros 2 productos")
    public void testSearchAndAddToCart() {
        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = new SearchPage(driver);

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Leer productos desde Excel
        List<Map<String, String>> todosLosProductos = searchPage.leerProductosDesdeExcel();

        // Tomar solo los primeros 2 productos
        int inicio = 0;
        int fin = Math.min(2, todosLosProductos.size()); // Asegurar que no exceda el tamaño
        List<Map<String, String>> productos = todosLosProductos.subList(inicio, fin);

        System.out.println("=== TEST 1: HardAssert - Procesando primeros 2 productos ===");
        System.out.println("Total de productos a buscar: " + productos.size());

        // Iterar sobre cada producto
        for (int i = 0; i < productos.size(); i++) {
            Map<String, String> producto = productos.get(i);
            String categoria = producto.get("categoria");
            String subcategoria = producto.get("subcategoria");
            String nombreProducto = producto.get("producto");
            int cantidad = Integer.parseInt(producto.get("cantidad"));

            System.out.println("\n--- Producto " + (i + 1) + " de " + productos.size() + " ---");
            System.out.println("Categoría: " + categoria);
            System.out.println("Subcategoría: " + subcategoria);
            System.out.println("Producto: " + nombreProducto);
            System.out.println("Cantidad: " + cantidad);

            try {
                // Buscar el producto
                searchPage.searchProduct(nombreProducto);

                // Verificar que aparece en los resultados
                boolean productoEncontrado = searchPage.isProductInResults(nombreProducto);
                Assert.assertTrue(productoEncontrado,
                        "El producto '" + nombreProducto + "' debería aparecer en los resultados");
                System.out.println("✓ Producto encontrado en los resultados");

                // Hacer clic en el producto
                searchPage.clickOnProduct(nombreProducto);

                // Establecer la cantidad
                searchPage.setQuantity(cantidad);

                // Agregar al carrito
                searchPage.addToCart();

                // Verificar mensaje de éxito
                boolean mensajeExito = searchPage.isSuccessMessageDisplayed();
                Assert.assertTrue(mensajeExito,
                        "Debería mostrarse el mensaje de éxito al agregar '" + nombreProducto + "' al carrito");
                System.out.println("✓ Producto agregado al carrito exitosamente");
                System.out.println("Mensaje: " + searchPage.getSuccessMessage());

                // Escribir en el log
                ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ÉXITO", "Producto agregado al carrito (HardAssert)");

                // Volver a la página principal para el siguiente producto
                homePage.navigateTo(Constants.BASE_URL);

            } catch (Exception e) {
                System.err.println("✗ Error al procesar el producto: " + nombreProducto);
                System.err.println("Error: " + e.getMessage());

                // Escribir error en el log
                ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ERROR", e.getMessage());

                // Volver a la página principal
                homePage.navigateTo(Constants.BASE_URL);

                // Lanzar la excepción para que falle el test
                throw e;
            }
        }

        System.out.println("\n=== TEST 1: HardAssert finalizado - Se procesaron " + productos.size() + " productos ===");
    }

    @Test(priority = 2, description = "Buscar productos y agregar al carrito con SoftAssert - Últimos 2 productos")
    public void testSearchAndAddToCartWithSoftAssert() {
        SoftAssert softAssert = new SoftAssert();

        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = new SearchPage(driver);

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Leer productos desde Excel
        List<Map<String, String>> todosLosProductos = searchPage.leerProductosDesdeExcel();

        // Tomar los productos desde el índice 2 hasta el final
        int inicio = 2;
        int fin = todosLosProductos.size();

        // Validar que hay productos para procesar
        if (inicio >= todosLosProductos.size()) {
            System.out.println("⚠ No hay productos para procesar en el Test 2");
            return;
        }

        List<Map<String, String>> productos = todosLosProductos.subList(inicio, fin);

        System.out.println("\n=== TEST 2: SoftAssert - Procesando últimos " + productos.size() + " productos ===");
        System.out.println("Productos del índice " + (inicio + 1) + " al " + fin);

        // Iterar sobre cada producto
        for (int i = 0; i < productos.size(); i++) {
            Map<String, String> producto = productos.get(i);
            String categoria = producto.get("categoria");
            String subcategoria = producto.get("subcategoria");
            String nombreProducto = producto.get("producto");
            int cantidad = Integer.parseInt(producto.get("cantidad"));

            System.out.println("\n--- Producto " + (i + 1) + " de " + productos.size() + ": " + nombreProducto + " ---");
            System.out.println("Categoría: " + categoria);

            try {
                // Buscar el producto
                searchPage.searchProduct(nombreProducto);

                // Verificar que aparece en los resultados (con SoftAssert)
                boolean productoEncontrado = searchPage.isProductInResults(nombreProducto);
                softAssert.assertTrue(productoEncontrado,
                        "El producto '" + nombreProducto + "' debería aparecer en los resultados");

                if (productoEncontrado) {
                    System.out.println("✓ Producto encontrado");

                    // Hacer clic en el producto
                    searchPage.clickOnProduct(nombreProducto);

                    // Establecer la cantidad
                    searchPage.setQuantity(cantidad);

                    // Agregar al carrito
                    searchPage.addToCart();

                    // Verificar mensaje de éxito (con SoftAssert)
                    boolean mensajeExito = searchPage.isSuccessMessageDisplayed();
                    softAssert.assertTrue(mensajeExito,
                            "Debería mostrarse el mensaje de éxito para '" + nombreProducto + "'");

                    if (mensajeExito) {
                        ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ÉXITO", "Producto agregado al carrito (SoftAssert)");
                        System.out.println("✓ Producto agregado exitosamente");
                    } else {
                        ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ERROR", "No se mostró mensaje de éxito");
                        System.out.println("✗ No se mostró mensaje de éxito");
                    }
                } else {
                    ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ERROR", "Producto no encontrado en resultados");
                    System.out.println("✗ Producto no encontrado");
                }

                // Volver a la página principal
                homePage.navigateTo(Constants.BASE_URL);

            } catch (Exception e) {
                System.err.println("✗ Error: " + e.getMessage());
                ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ERROR", e.getMessage());
                homePage.navigateTo(Constants.BASE_URL);
            }
        }

        // Reportar todos los errores al final
        softAssert.assertAll();
        System.out.println("\n=== TEST 2: SoftAssert finalizado - Se procesaron " + productos.size() + " productos ===");
    }
}