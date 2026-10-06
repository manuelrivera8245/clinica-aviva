package com.clinicaaviva.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller SPA (Single Page Application) para produccion.
 *
 * CONTEXTO: Angular usa HTML5 PathLocationStrategy (URLs sin #).
 * En DESARROLLO, el dev-server de Angular (pnpm start) maneja el routing
 * internamente y el F5 funciona sin configuracion adicional.
 *
 * En PRODUCCION (Angular compilado y servido desde Spring Boot):
 * cuando el usuario recarga /admin/dashboard, Spring Boot no tiene ningun
 * handler y devuelve 404, impidiendo que Angular inicialice.
 *
 * ESTRATEGIA IMPLEMENTADA (dos capas):
 *
 * 1. Rutas conocidas (@RequestMapping explicito):
 *    Captura las rutas SPA conocidas con patrones Ant y hace forward a index.html.
 *
 * 2. Rutas desconocidas (ErrorController):
 *    Captura los 404 de Spring Boot (rutas que no coincidieron con ningun handler)
 *    y los reenvía a index.html. Esto permite que Angular reciba la URL y la maneje.
 *    Excluye rutas /api/** para que los 404 de API devuelvan JSON real.
 *
 * PREREQUISITO: el build de Angular (ng build) debe estar en:
 *   backend/src/main/resources/static/
 * Spring Boot sirve ese directorio automaticamente como recursos estaticos.
 *
 * PARA PRODUCCION: copiar el contenido de frontend/dist/[nombre-app]/browser/
 * a backend/src/main/resources/static/ antes de hacer mvn package.
 */
@Controller
public class SpaController implements ErrorController {

    // === CAPA 1: Rutas SPA conocidas ===
    // Estas rutas se mapean directamente. Spring Boot las intercepta
    // antes de que lleguen al ResourceHttpRequestHandler.
    @RequestMapping(value = {
            "/",
            "/inicio",
            "/nosotros",
            "/especialidades",
            "/contacto",
            "/auth/**",
            "/paciente/**",
            "/medico/**",
            "/admin/**"
    })
    public String redirigirAlFrontend() {
        return "forward:/index.html";
    }

    // === CAPA 2: Catch-all para rutas no mapeadas ===
    // Spring Boot envía los 404 a /error. Este metodo los captura.
    // Si la ruta original NO es /api/**, se sirve index.html (Angular la manejara).
    // Si es /api/**, se deja pasar para que el GlobalExceptionHandler devuelva JSON.
    @RequestMapping("/error")
    public Object manejarError(HttpServletRequest request) {
        String uri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        if (uri != null && uri.startsWith("/api/")) {
            // Dejar que Spring Boot devuelva el JSON de error por defecto para APIs
            return "forward:/api/error-json";
        }
        // Para cualquier otra ruta (Angular routes desconocidas), servir index.html
        return "forward:/index.html";
    }
}

