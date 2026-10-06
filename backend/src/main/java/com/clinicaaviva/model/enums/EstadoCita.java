package com.clinicaaviva.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Estados posibles de una cita medica.
 *
 * El campo 'value' contiene el literal exacto del ENUM de MySQL
 * en la tabla `cita` (columna `estado`):
 *   ENUM('Programada','Atendida','No Asistio','Cancelada')
 *
 * Se usa un AttributeConverter (EstadoCitaConverter) en lugar de
 * @Enumerated(EnumType.STRING) para controlar exactamente el string
 * que se escribe y se lee desde la base de datos.
 *
 * @JsonValue garantiza que Jackson serializa/deserializa usando 'value',
 * por lo que el JSON entre FE y BE tambien usa 'No Asistio' (con espacio),
 * consistente con lo que el frontend envia en ActualizarEstadoRequest.estado.
 */
public enum EstadoCita {

    Programada("Programada"),
    Atendida("Atendida"),
    No_Asistio("No Asistio"),
    Cancelada("Cancelada");

    private final String value;

    EstadoCita(String value) {
        this.value = value;
    }

    /** Valor exacto persistido en MySQL y serializado en JSON. */
    @JsonValue
    public String getValue() {
        return value;
    }

    /** Permite que Jackson deserialice desde el string de BD/JSON. */
    @JsonCreator
    public static EstadoCita fromValue(String value) {
        for (EstadoCita e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("EstadoCita desconocido: " + value);
    }
}

