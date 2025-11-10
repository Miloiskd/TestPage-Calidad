package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Validar login con credenciales válidas e inválidas")
    public void testLogin() {
        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Navegar a la página
        homePage.navigateTo(Constants.BASE_URL);

        // Leer datos del Excel
        List<Map<String, String>> datosLogin = loginPage.leerDatosLogin();

        System.out.println("=== Iniciando pruebas de Login ===");
        System.out.println("Total de casos a probar: " + datosLogin.size());

        // Iterar sobre cada conjunto de datos
        for (int i = 0; i < datosLogin.size(); i++) {
            Map<String, String> datos = datosLogin.get(i);
            String email = datos.get("email");
            String password = datos.get("password");
            String expectedResult = datos.get("expectedResult");

            System.out.println("\n--- Caso " + (i + 1) + " ---");
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Resultado esperado: " + expectedResult);

            // Ir a la página de login
            homePage.menuAccount();
            homePage.clickLoginOption();

            // Realizar login
            loginPage.login(email, password);

            // Validar el resultado
            if (expectedResult.equalsIgnoreCase("success")) {
                // Validar login exitoso
                Assert.assertTrue(loginPage.isLoginSuccessful(),
                        "El login debería ser exitoso para: " + email);
                System.out.println("✓ Login exitoso verificado");

                // Hacer logout para el siguiente caso
                homePage.menuAccount();
                // Agregar aquí el logout si es necesario

            } else if (expectedResult.equalsIgnoreCase("failure")) {
                // Validar que aparezca mensaje de error
                Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                        "Debería mostrarse mensaje de error para credenciales inválidas: " + email);
                System.out.println("✓ Mensaje de error verificado");
                System.out.println("Mensaje: " + loginPage.getErrorMessage());
            }

            // Volver a la página principal para el siguiente intento
            homePage.navigateTo(Constants.BASE_URL);
        }

        System.out.println("\n=== Pruebas de Login finalizadas ===");
    }

    @Test(priority = 2, description = "Validar login con SoftAssert para reportar todos los errores")
    public void testLoginWithSoftAssert() {
        SoftAssert softAssert = new SoftAssert();

        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Navegar a la página
        homePage.navigateTo(Constants.BASE_URL);

        // Leer datos del Excel
        List<Map<String, String>> datosLogin = loginPage.leerDatosLogin();

        System.out.println("\n=== Iniciando pruebas de Login con SoftAssert ===");

        // Iterar sobre cada conjunto de datos
        for (int i = 0; i < datosLogin.size(); i++) {
            Map<String, String> datos = datosLogin.get(i);
            String email = datos.get("email");
            String password = datos.get("password");
            String expectedResult = datos.get("expectedResult");

            System.out.println("\n--- Caso " + (i + 1) + " ---");
            System.out.println("Email: " + email);

            // Ir a la página de login
            homePage.menuAccount();
            homePage.clickLoginOption();

            // Realizar login
            loginPage.login(email, password);

            // Validar el resultado con SoftAssert
            if (expectedResult.equalsIgnoreCase("success")) {
                softAssert.assertTrue(loginPage.isLoginSuccessful(),
                        "El login debería ser exitoso para: " + email);
            } else {
                softAssert.assertTrue(loginPage.isErrorMessageDisplayed(),
                        "Debería mostrarse mensaje de error para: " + email);
            }

            // Volver a la página principal
            homePage.navigateTo(Constants.BASE_URL);
        }

        // Reportar todos los errores al final
        softAssert.assertAll();
        System.out.println("\n=== Pruebas de Login con SoftAssert finalizadas ===");
    }
}