package com.clinicaaviva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicacion Spring Boot.
 * Inicia el servidor backend del Sistema de Gestion de Citas Medicas de Clinica Aviva.
 */
@SpringBootApplication
public class ClinicaAvivaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicaAvivaApplication.class, args);
    }
}
