package com.clinicaaviva.model.enums;

/**
 * Representacion de los dias de la semana para los horarios medicos.
 * 1 = Lunes, 7 = Domingo
 */
public enum DiaSemana {
    LUNES(1),
    MARTES(2),
    MIERCOLES(3),
    JUEVES(4),
    VIERNES(5),
    SABADO(6),
    DOMINGO(7);

    private final int valor;

    DiaSemana(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static DiaSemana fromValor(int valor) {
        for (DiaSemana dia : values()) {
            if (dia.valor == valor) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Valor de dia invalido: " + valor);
    }
}
