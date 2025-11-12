package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegisterTest extends BaseTest{

    @Test(priority=1, description = "Validar creación exitosa de cuenta")
    public void Register(){
        SoftAssert softAssert = new SoftAssert();

        // Inicializar Página
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        System.out.println("=== Iniciando pruebas de Registro ===");

        // Navegar a la página
        homePage.navigateTo(Constants.BASE_URL);

        //Ir a la sección de registro
        homePage.menuAccount();
        homePage.registerMyAccount();

        //Crear cuenta utilizando excel
        registerPage.createAccount(1);

        boolean resultadoRegistro = registerPage.isCreatedSuccessful();

        if (resultadoRegistro) {
            System.out.println("Registro correctamente validado");
            ExcelUtils.escribirLogRegistro(
                    registerPage.getEmail(),
                    registerPage.getNombre(),
                    "ÉXITO",
                    "Cuenta creada correctamente."
            );
        } else {
            System.out.println("❌ Falló la creación de la cuenta");
            ExcelUtils.escribirLogRegistro(
                    registerPage.getEmail(),
                    registerPage.getNombre(),
                    "ERROR",
                    "No se encontró mensaje de confirmación."
            );
        }

        Assert.assertTrue(resultadoRegistro,
                "El mensaje no fue exitoso, No se encontró mensaje de confirmación");
        System.out.println("Registro correctamente validado");

        softAssert.assertAll();

    }

    @Test(priority = 2, description = "Validar intento de registro con email duplicado")
    public void DuplicateEmailTest() {
        SoftAssert softAssert = new SoftAssert();

        // Inicializar Página
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        System.out.println("=== Iniciando email registro ===");

        // Navegar a la página
        homePage.navigateTo(Constants.BASE_URL);

        //Ir a la sección de registro
        homePage.menuAccount();
        homePage.registerMyAccount();

        //Validar mensaje de email duplicado
        registerPage.createAccount(2);

        Assert.assertTrue(registerPage.isWarningEmailDisplayed(),
                "No se encontró mensaje de advertencia por email duplicado.");
        System.out.println("Mensaje de advertencia por email duplicado validado correctamente.");

        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Validar mensaje por contraseña insegura")
    public void testPasswordInsegura() {
        SoftAssert softAssert = new SoftAssert();

        // Inicializar Página
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        // Navegar a la página
        homePage.navigateTo(Constants.BASE_URL);

        //Ir a la sección de registro
        homePage.menuAccount();
        homePage.registerMyAccount();

        // Validar mensaje de contraseña segura.
        registerPage.createAccount(3); // Fila 3 = contraseña insegura

        Assert.assertTrue(registerPage.isSecurePasswordDisplayed(),
                "No se encontró mensaje de contraseña insegura.");
        System.out.println("Mensaje de contraseña insegura validado correctamente.");

        softAssert.assertAll();
    }
    }
