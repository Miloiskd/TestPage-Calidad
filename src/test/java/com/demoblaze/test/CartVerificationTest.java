package com.demoblaze.test;

import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.SearchPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public class CartVerificationTest extends BaseTest {

    @Test(description = "Buscar productos, agregarlos al carrito y verificar que estén presentes")
    public void testSearchAddAndVerifyProductsInCart() {
        SoftAssert softAssert = new SoftAssert();
        HomePage homePage = new HomePage(driver);
        CartPage cartPage = new CartPage(driver);
        SearchPage searchPage = new SearchPage(driver);

        System.out.println("\n=== Agregando productos al carrito ===");

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Leer productos desde Excel
        List<Map<String, String>> productos = searchPage.leerProductosDesdeExcel();

        System.out.println("Total de productos a agregar: " + productos.size());

        // Iterar sobre cada producto para agregarlo al carrito
        for (int i = 0; i < productos.size(); i++) {
            Map<String, String> producto = productos.get(i);
            String categoria = producto.get("categoria");
            String subcategoria = producto.get("subcategoria");
            String nombreProducto = producto.get("producto");
            int cantidad = Integer.parseInt(producto.get("cantidad"));

            System.out.println("\n--- Agregando Producto " + (i + 1) + " de " + productos.size() + " ---");
            System.out.println("Categoría: " + categoria);
            System.out.println("Subcategoría: " + subcategoria);
            System.out.println("Producto: " + nombreProducto);
            System.out.println("Cantidad: " + cantidad);

            try {
                // Buscar el producto
                searchPage.searchProduct(nombreProducto);

                // Verificar que aparece en los resultados
                boolean productoEncontrado = searchPage.isProductInResults(nombreProducto);
                softAssert.assertTrue(productoEncontrado,
                        "El producto '" + nombreProducto + "' debería aparecer en los resultados");

                // Hacer clic en el producto
                searchPage.clickOnProduct(nombreProducto);

                // Establecer la cantidad
                searchPage.setQuantity(cantidad);

                // Agregar al carrito
                searchPage.addToCart();

                // Verificar mensaje de éxito
                boolean mensajeExito = searchPage.isSuccessMessageDisplayed();
                softAssert.assertTrue(mensajeExito,
                        "Debería mostrarse el mensaje de éxito al agregar '" + nombreProducto + "' al carrito");

                // Escribir en el log
                ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ÉXITO",
                        "Producto agregado al carrito");

                // Volver a la página principal para el siguiente producto
                homePage.navigateTo(Constants.BASE_URL);

            } catch (Exception e) {
                System.err.println("✗ Error al agregar el producto: " + nombreProducto);
                System.err.println("Error: " + e.getMessage());

                // Escribir error en el log
                ExcelUtils.escribirLogProductos(categoria, subcategoria, nombreProducto, cantidad, "ERROR",
                        e.getMessage());

                // Volver a la página principal
                homePage.navigateTo(Constants.BASE_URL);

                // Continuar con el siguiente producto sin lanzar excepción
            }
        }

        System.out.println("\n=== Verificando productos en el carrito ===");

        // Ahora verificar los productos en el carrito
        cartPage.goToCart();
        cartPage.printCartContents();

        System.out.println("Total de productos esperados: " + productos.size());

        int productosVerificados = 0;
        int productosFallidos = 0;

        for (int i = 0; i < productos.size(); i++) {
            Map<String, String> producto = productos.get(i);
            String categoria = producto.get("categoria");
            String subcategoria = producto.get("subcategoria");
            String nombreProducto = producto.get("producto");
            int cantidadEsperada = Integer.parseInt(producto.get("cantidad"));

            System.out.println("\n--- Verificando Producto " + (i + 1) + " ---");
            System.out.println("Producto: " + nombreProducto);
            System.out.println("Cantidad esperada: " + cantidadEsperada);

            try {
                boolean estaEnCarrito = cartPage.isProductInCart(nombreProducto);

                if (estaEnCarrito) {
                    int cantidadEnCarrito = cartPage.getProductQuantityInCart(nombreProducto);
                    System.out.println("Cantidad en carrito: " + cantidadEnCarrito);

                    if (cantidadEnCarrito == cantidadEsperada) {
                        System.out.println("✓ Producto verificado correctamente");
                        ExcelUtils.escribirLogCarrito(categoria, subcategoria, nombreProducto,
                                cantidadEsperada, cantidadEnCarrito, "ÉXITO",
                                "Producto encontrado en el carrito con la cantidad correcta");
                        productosVerificados++;
                    } else {
                        System.out.println("✗ La cantidad no coincide");
                        ExcelUtils.escribirLogCarrito(categoria, subcategoria, nombreProducto,
                                cantidadEsperada, cantidadEnCarrito, "ERROR",
                                "La cantidad en el carrito (" + cantidadEnCarrito + ") no coincide con la esperada ("
                                        + cantidadEsperada + ")");
                        productosFallidos++;
                        softAssert.fail("La cantidad del producto '" + nombreProducto + "' en el carrito (" +
                                cantidadEnCarrito + ") no coincide con la esperada (" + cantidadEsperada + ")");
                    }
                } else {
                    System.out.println("✗ Producto NO encontrado en el carrito");
                    ExcelUtils.escribirLogCarrito(categoria, subcategoria, nombreProducto,
                            cantidadEsperada, 0, "ERROR",
                            "Producto no encontrado en el carrito");
                    productosFallidos++;
                    softAssert.fail("El producto '" + nombreProducto + "' no fue encontrado en el carrito");
                }

            } catch (Exception e) {
                System.err.println("✗ Error al verificar el producto: " + e.getMessage());
                ExcelUtils.escribirLogCarrito(categoria, subcategoria, nombreProducto,
                        cantidadEsperada, 0, "ERROR", e.getMessage());
                productosFallidos++;
                // No lanzar excepción, continuar con soft asserts
            }
        }

        System.out.println("\n=== Resumen de Verificación ===");
        System.out.println("Total de productos esperados: " + productos.size());
        System.out.println("Productos verificados correctamente: " + productosVerificados);
        System.out.println("Productos con errores: " + productosFallidos);
        System.out.println("Total en carrito: " + cartPage.getTotalProductsInCart());

        softAssert.assertEquals(productosFallidos, 0, "Algunos productos no fueron verificados correctamente");
        System.out.println("\n✓ Todos los productos del Excel están en el carrito con las cantidades correctas");
        softAssert.assertAll();
    }
}
