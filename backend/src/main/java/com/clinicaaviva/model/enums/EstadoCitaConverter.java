package com.clinicaaviva.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter JPA para EstadoCita.
 *
 * Problema resuelto: @Enumerated(EnumType.STRING) persiste el nombre Java del enum
 * (e.g. 'No_Asistio'), pero MySQL espera exactamente 'No Asistio' (con espacio).
 * Este converter usa EstadoCita.getValue() para obtener el literal correcto de BD
 * y EstadoCita.fromValue() para reconstruir el enum al leer.
 *
 * Se aplica automaticamente (autoApply = true) a todos los atributos de tipo
 * EstadoCita en cualquier @Entity del proyecto.
 *
 * Para que funcione, la entidad Cita NO debe tener @Enumerated en el campo estado.
 */
@Converter(autoApply = true)
public class EstadoCitaConverter implements AttributeConverter<EstadoCita, String> {

    @Override
    public String convertToDatabaseColumn(EstadoCita attribute) {
        if (attribute == null) return null;
        return attribute.getValue();   // 'No Asistio', 'Programada', etc.
    }

    @Override
    public EstadoCita convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return EstadoCita.fromValue(dbData);
    }
}
