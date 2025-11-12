package com.demoblaze.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.demoblaze.utils.Constants.RUTA_LOG_CARRITO;
import static com.demoblaze.utils.Constants.RUTA_LOG_PRODUCTOS;

public class ExcelUtils {


     //Escribe los resultados del login en un archivo de log

    public static void escribirLogLogin(String email, String resultado, String mensaje) {
        String rutaArchivo = "excel/LoginLog.xlsx";
        Workbook workbook;
        Sheet sheet;

        try {
            // Intentar abrir el archivo existente
            try (FileInputStream file = new FileInputStream(rutaArchivo)) {
                workbook = new XSSFWorkbook(file);
                sheet = workbook.getSheetAt(0);
            } catch (IOException e) {
                // Si no existe, crear uno nuevo
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Login Log");

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Fecha/Hora");
                headerRow.createCell(1).setCellValue("Email");
                headerRow.createCell(2).setCellValue("Resultado");
                headerRow.createCell(3).setCellValue("Mensaje");

                // Aplicar estilos
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < 4; i++) {
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
            }

            // Agregar nueva fila con los datos
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Fecha y hora actual
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            newRow.createCell(0).setCellValue(timestamp);
            newRow.createCell(1).setCellValue(email);
            newRow.createCell(2).setCellValue(resultado);
            newRow.createCell(3).setCellValue(mensaje);

            // Aplicar estilo - resultado
            CellStyle resultStyle = workbook.createCellStyle();
            Font resultFont = workbook.createFont();

            if (resultado.equalsIgnoreCase("ÉXITO")) {
                resultFont.setColor(IndexedColors.GREEN.getIndex());
            } else {
                resultFont.setColor(IndexedColors.RED.getIndex());
            }

            resultStyle.setFont(resultFont);
            newRow.getCell(2).setCellStyle(resultStyle);

            // Ancho de columnas
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar el archivo
            try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
                workbook.write(outputStream);
            }

            workbook.close();
            System.out.println("✓ Log escrito exitosamente");

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            e.printStackTrace();
        }
    }


      //Escribe los resultados del registro en un archivo de log

    public static void escribirLogRegistro(String email, String nombre, String resultado, String mensaje) {
        String rutaArchivo = "excel/RegistroLog.xlsx";
        Workbook workbook;
        Sheet sheet;

        try {
            try (FileInputStream file = new FileInputStream(rutaArchivo)) {
                workbook = new XSSFWorkbook(file);
                sheet = workbook.getSheetAt(0);
            } catch (IOException e) {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Registro Log");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Fecha/Hora");
                headerRow.createCell(1).setCellValue("Nombre Completo");
                headerRow.createCell(2).setCellValue("Email");
                headerRow.createCell(3).setCellValue("Resultado");
                headerRow.createCell(4).setCellValue("Mensaje");

                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < 5; i++) {
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
            }

            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            newRow.createCell(0).setCellValue(timestamp);
            newRow.createCell(1).setCellValue(nombre);
            newRow.createCell(2).setCellValue(email);
            newRow.createCell(3).setCellValue(resultado);
            newRow.createCell(4).setCellValue(mensaje);

            CellStyle resultStyle = workbook.createCellStyle();
            Font resultFont = workbook.createFont();

            if (resultado.equalsIgnoreCase("ÉXITO")) {
                resultFont.setColor(IndexedColors.GREEN.getIndex());
            } else {
                resultFont.setColor(IndexedColors.RED.getIndex());
            }

            resultStyle.setFont(resultFont);
            newRow.getCell(3).setCellStyle(resultStyle);

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
                workbook.write(outputStream);
            }

            workbook.close();
            System.out.println("✓ Log de registro escrito exitosamente");

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void escribirLogProductos(String categoria, String subcategoria, String producto, int cantidad, String resultado, String mensaje) {
        String rutaArchivo = RUTA_LOG_PRODUCTOS;
        Workbook workbook;
        Sheet sheet;

        try {
            // Intentar abrir el archivo existente
            try (FileInputStream file = new FileInputStream(rutaArchivo)) {
                workbook = new XSSFWorkbook(file);
                sheet = workbook.getSheetAt(0);
            } catch (IOException e) {
                // Si no existe, crear uno nuevo
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Productos Log");

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Fecha/Hora");
                headerRow.createCell(1).setCellValue("Categoría");
                headerRow.createCell(2).setCellValue("Subcategoría");
                headerRow.createCell(3).setCellValue("Producto");
                headerRow.createCell(4).setCellValue("Cantidad");
                headerRow.createCell(5).setCellValue("Resultado");
                headerRow.createCell(6).setCellValue("Mensaje");

                // Aplicar estilos
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < 7; i++) {
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
            }

            // Agregar nueva fila con los datos
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Fecha y hora actual
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            newRow.createCell(0).setCellValue(timestamp);
            newRow.createCell(1).setCellValue(categoria);
            newRow.createCell(2).setCellValue(subcategoria);
            newRow.createCell(3).setCellValue(producto);
            newRow.createCell(4).setCellValue(cantidad);
            newRow.createCell(5).setCellValue(resultado);
            newRow.createCell(6).setCellValue(mensaje);

            // Aplicar estilo al resultado
            CellStyle resultStyle = workbook.createCellStyle();
            Font resultFont = workbook.createFont();

            if (resultado.equalsIgnoreCase("ÉXITO")) {
                resultFont.setColor(IndexedColors.GREEN.getIndex());
            } else {
                resultFont.setColor(IndexedColors.RED.getIndex());
            }

            resultStyle.setFont(resultFont);
            newRow.getCell(5).setCellStyle(resultStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar el archivo
            try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
                workbook.write(outputStream);
            }

            workbook.close();

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Escribe los resultados de la verificación del carrito en un archivo de log
     */
    public static void escribirLogCarrito(String categoria, String subcategoria, String producto, int cantidadEsperada, int cantidadEnCarrito, String resultado, String mensaje) {
        String rutaArchivo = RUTA_LOG_CARRITO;
        Workbook workbook;
        Sheet sheet;

        try {
            // Intentar abrir el archivo existente
            try (FileInputStream file = new FileInputStream(rutaArchivo)) {
                workbook = new XSSFWorkbook(file);
                sheet = workbook.getSheetAt(0);
            } catch (IOException e) {
                // Si no existe, crear uno nuevo
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Carrito Log");

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Fecha/Hora");
                headerRow.createCell(1).setCellValue("Categoría");
                headerRow.createCell(2).setCellValue("Subcategoría");
                headerRow.createCell(3).setCellValue("Producto");
                headerRow.createCell(4).setCellValue("Cantidad Esperada");
                headerRow.createCell(5).setCellValue("Cantidad en Carrito");
                headerRow.createCell(6).setCellValue("Resultado");
                headerRow.createCell(7).setCellValue("Mensaje");

                // Aplicar estilos
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < 8; i++) {
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
            }

            // Agregar nueva fila con los datos
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Fecha y hora actual
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            newRow.createCell(0).setCellValue(timestamp);
            newRow.createCell(1).setCellValue(categoria);
            newRow.createCell(2).setCellValue(subcategoria);
            newRow.createCell(3).setCellValue(producto);
            newRow.createCell(4).setCellValue(cantidadEsperada);
            newRow.createCell(5).setCellValue(cantidadEnCarrito);
            newRow.createCell(6).setCellValue(resultado);
            newRow.createCell(7).setCellValue(mensaje);

            // Aplicar estilo al resultado
            CellStyle resultStyle = workbook.createCellStyle();
            Font resultFont = workbook.createFont();

            if (resultado.equalsIgnoreCase("ÉXITO")) {
                resultFont.setColor(IndexedColors.GREEN.getIndex());
            } else {
                resultFont.setColor(IndexedColors.RED.getIndex());
            }

            resultStyle.setFont(resultFont);
            newRow.getCell(6).setCellStyle(resultStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar el archivo
            try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
                workbook.write(outputStream);
            }

            workbook.close();

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log del carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
