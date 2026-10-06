package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.ActualizarEstadoCitaRequest;
import com.clinicaaviva.dto.request.CancelacionCitaRequest;
import com.clinicaaviva.dto.request.ReservaCitaRequest;
import com.clinicaaviva.dto.response.CitaResponse;
import com.clinicaaviva.entity.Cita;
import com.clinicaaviva.entity.Notificacion;
import com.clinicaaviva.entity.Turno;
import com.clinicaaviva.model.enums.EstadoCita;
import com.clinicaaviva.model.enums.EstadoEnvio;
import com.clinicaaviva.model.enums.EstadoTurno;
import com.clinicaaviva.model.enums.TipoNotificacion;
import com.clinicaaviva.repository.CitaRepository;
import com.clinicaaviva.repository.NotificacionRepository;
import com.clinicaaviva.repository.PacienteRepository;
import com.clinicaaviva.repository.TurnoRepository;
import com.clinicaaviva.service.CitaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

// Servicio de citas
@Slf4j
@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final TurnoRepository turnoRepository;
    private final NotificacionRepository notificacionRepository;
    private final PacienteRepository pacienteRepository;

    // Reserva una cita
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reservarCita(Integer idPaciente, ReservaCitaRequest request) {
        try {
            // Obtener turno con bloqueo pesimista (evita condiciones de carrera)
            Turno turno = turnoRepository.findByIdWithLock(request.getIdTurno())
                    .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado"));

            // Verificar disponibilidad
            if (turno.getEstado() != EstadoTurno.Libre) {
                log.warn("Turno {} no disponible. Estado actual: {}", request.getIdTurno(), turno.getEstado());
                return "TURNO_NO_DISPONIBLE";
            }

            // Ocupar el turno
            turno.setEstado(EstadoTurno.Ocupado);
            turno = turnoRepository.save(java.util.Objects.requireNonNull(turno));

            // Crear la cita
            Cita cita = Cita.builder()
                    .paciente(pacienteRepository.getReferenceById(java.util.Objects.requireNonNull(idPaciente)))
                    .turno(turno)
                    .estado(EstadoCita.Programada)
                    .build();

            Cita citaGuardada = citaRepository.save(java.util.Objects.requireNonNull(cita));
            log.info("Cita creada: {} para paciente: {} en turno: {}",
                    citaGuardada.getIdCita(), idPaciente, request.getIdTurno());

            // Notificacion de confirmacion (inmediata)
            Notificacion confirmacion = Notificacion.builder()
                    .cita(citaGuardada)
                    .tipo(TipoNotificacion.Confirmacion)
                    .fechaProgramada(LocalDateTime.now())
                    .estadoEnvio(EstadoEnvio.Pendiente)
                    .build();
            confirmacion = notificacionRepository.save(java.util.Objects.requireNonNull(confirmacion));

            // Notificacion de recordatorio (24h antes de la cita)
            LocalDateTime fechaHoraCita = LocalDateTime.of(
                    turno.getFecha(), turno.getHoraInicio());
            LocalDateTime fechaRecordatorio = fechaHoraCita.minusHours(24);

            Notificacion recordatorio = Notificacion.builder()
                    .cita(citaGuardada)
                    .tipo(TipoNotificacion.Recordatorio)
                    .fechaProgramada(fechaRecordatorio)
                    .estadoEnvio(EstadoEnvio.Pendiente)
                    .build();
            recordatorio = notificacionRepository.save(java.util.Objects.requireNonNull(recordatorio));

            log.info("Notificaciones programadas para cita: {}", citaGuardada.getIdCita());
            return "RESERVA_EXITOSA";

        } catch (Exception e) {
            log.error("Error al reservar cita: {}", e.getMessage(), e);
            throw new RuntimeException("ERROR_INTERNO: " + e.getMessage());
        }
    }

    // Cancela una cita
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelarCita(Integer idPaciente, CancelacionCitaRequest request) {
        // Buscar la cita programada del paciente
        Cita cita = citaRepository.findCitaProgramadaByIdAndPaciente(
                        request.getIdCita(), idPaciente)
                .orElse(null);

        if (cita == null) {
            log.warn("Cita programada no encontrada: {} para paciente: {}",
                    request.getIdCita(), idPaciente);
            return "CITA_NO_ENCONTRADA";
        }

        // Calcular fecha/hora de la cita
        Turno turno = cita.getTurno();
        LocalDateTime fechaHoraCita = LocalDateTime.of(turno.getFecha(), turno.getHoraInicio());

        // Validar plazo minimo de 24 horas
        long horasRestantes = java.time.Duration.between(LocalDateTime.now(), fechaHoraCita).toHours();
        if (horasRestantes < 24) {
            log.warn("Cancelacion fuera de plazo. Cita: {} - Horas restantes: {}",
                    request.getIdCita(), horasRestantes);
            return "FUERA_DE_PLAZO";
        }

        // Cancelar la cita
        cita.setEstado(EstadoCita.Cancelada);
        cita = citaRepository.save(java.util.Objects.requireNonNull(cita));

        // Liberar el turno
        turno.setEstado(EstadoTurno.Libre);
        turno = turnoRepository.save(java.util.Objects.requireNonNull(turno));

        // Notificacion de cancelacion
        Notificacion cancelacion = Notificacion.builder()
                .cita(cita)
                .tipo(TipoNotificacion.Cancelacion)
                .fechaProgramada(LocalDateTime.now())
                .estadoEnvio(EstadoEnvio.Pendiente)
                .build();
        cancelacion = notificacionRepository.save(java.util.Objects.requireNonNull(cancelacion));

        log.info("Cita cancelada exitosamente: {}", cita.getIdCita());
        return "CANCELACION_EXITOSA";
    }

    // Actualiza el estado de una cita
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String actualizarEstadoCita(ActualizarEstadoCitaRequest request) {
        Cita cita = citaRepository.findById(java.util.Objects.requireNonNull(request.getIdCita()))
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (cita.getEstado() != EstadoCita.Programada) {
            log.warn("Cita {} no valida para actualizacion. Estado actual: {}",
                    request.getIdCita(), cita.getEstado());
            return "CITA_NO_VALIDA";
        }

        // Actualizar estado
        cita.setEstado(request.getEstado());
        cita = citaRepository.save(java.util.Objects.requireNonNull(cita));

        // Si no asistio, liberar el turno
        if (request.getEstado() == EstadoCita.No_Asistio) {
            Turno turno = cita.getTurno();
            turno.setEstado(EstadoTurno.Libre);
            turno = turnoRepository.save(java.util.Objects.requireNonNull(turno));
            log.info("Turno liberado por inasistencia: {}", turno.getIdTurno());
        }

        log.info("Estado de cita {} actualizado a: {}", request.getIdCita(), request.getEstado());
        return "ACTUALIZADO";
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarMisCitasProgramadas(Integer idPaciente) {
        return citaRepository.findCitasProgramadasByPaciente(idPaciente)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarHistorialPaciente(Integer idPaciente) {
        return citaRepository.findHistorialByPaciente(idPaciente)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarAgendaDelDia(Integer idMedico) {
        return citaRepository.findCitasDelDiaByMedico(idMedico)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarCitasDelDiaAdmin() {
        return citaRepository.findCitasDelDia()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Historial de todas las citas de un medico
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarHistorialMedico(Integer idMedico) {
        return citaRepository.findHistorialByMedico(idMedico)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CitaResponse mapToResponse(Cita cita) {
        return CitaResponse.builder()
                .idCita(cita.getIdCita())
                .fechaReserva(cita.getFechaReserva())
                .estado(cita.getEstado())
                .idPaciente(cita.getPaciente().getIdPaciente())
                .nombrePaciente(cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos())
                .dniPaciente(cita.getPaciente().getDni())
                .telefonoPaciente(cita.getPaciente().getTelefono())
                .idTurno(cita.getTurno().getIdTurno())
                .fechaCita(cita.getTurno().getFecha())
                .horaInicio(cita.getTurno().getHoraInicio())
                .horaFin(cita.getTurno().getHoraFin())
                .idMedico(cita.getTurno().getMedico().getIdMedico())
                .nombreMedico(cita.getTurno().getMedico().getNombres() + " " + cita.getTurno().getMedico().getApellidos())
                .idEspecialidad(cita.getTurno().getMedico().getEspecialidad().getIdEspecialidad())
                .nombreEspecialidad(cita.getTurno().getMedico().getEspecialidad().getNombre())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarComprobantePdf(Integer idCita, Integer idPaciente) {
        Cita cita = citaRepository.findById(java.util.Objects.requireNonNull(idCita))
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        // Validar que la cita pertenezca al paciente (por seguridad)
        if (!cita.getPaciente().getIdPaciente().equals(idPaciente)) {
            throw new IllegalArgumentException("No tiene permisos para descargar este comprobante");
        }

        // Solo permitir descargar de citas que no estén canceladas, o puedes permitir de todas
        if (cita.getEstado() == EstadoCita.Cancelada) {
            throw new IllegalStateException("No se puede generar comprobante de una cita cancelada");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // HEADER: Clinic Info + Receipt Info
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{60f, 40f});

            // Columna Izquierda: Logo / Nombre Clinica
            PdfPCell leftHeader = new PdfPCell();
            leftHeader.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            Font fontClinicName = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new java.awt.Color(0, 102, 204));
            leftHeader.addElement(new Paragraph("CLÍNICA AVIVA", fontClinicName));
            Font fontClinicSub = FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Color.GRAY);
            leftHeader.addElement(new Paragraph("Centro Médico de Especialidades\nAv. Los Jazmines 123, Lima\nTel: (01) 555-1234 | www.clinicaaviva.com", fontClinicSub));
            headerTable.addCell(leftHeader);

            // Columna Derecha: Recibo
            PdfPCell rightHeader = new PdfPCell();
            rightHeader.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            rightHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            PdfPTable receiptInfoTable = new PdfPTable(1);
            receiptInfoTable.setWidthPercentage(100);
            PdfPCell cellReceiptTitle = new PdfPCell(new Phrase("RESERVA DE CITA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, java.awt.Color.WHITE)));
            cellReceiptTitle.setBackgroundColor(new java.awt.Color(33, 150, 243));
            cellReceiptTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellReceiptTitle.setPadding(8);
            cellReceiptTitle.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            receiptInfoTable.addCell(cellReceiptTitle);

            PdfPCell cellReceiptNo = new PdfPCell(new Phrase("TICKET N°: TKT-" + String.format("%06d", cita.getIdCita()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, java.awt.Color.RED)));
            cellReceiptNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellReceiptNo.setPadding(5);
            cellReceiptNo.setBorder(com.lowagie.text.Rectangle.BOX);
            cellReceiptNo.setBorderColor(new java.awt.Color(200, 200, 200));
            receiptInfoTable.addCell(cellReceiptNo);

            rightHeader.addElement(receiptInfoTable);
            headerTable.addCell(rightHeader);

            document.add(headerTable);
            
            // Línea separadora
            Paragraph separator = new Paragraph("______________________________________________________________________________");
            separator.getFont().setColor(java.awt.Color.LIGHT_GRAY);
            separator.setSpacingAfter(15);
            document.add(separator);

            // Título de la sección
            Font fontSectionTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new java.awt.Color(0, 51, 102));
            
            // DATOS DEL PACIENTE
            document.add(new Paragraph("1. DATOS DEL PACIENTE", fontSectionTitle));
            document.add(new Paragraph("\n"));
            
            PdfPTable patientTable = new PdfPTable(2);
            patientTable.setWidthPercentage(100);
            patientTable.setWidths(new float[]{30f, 70f});
            
            Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new java.awt.Color(80, 80, 80));
            Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 11, java.awt.Color.BLACK);
            
            addRowToTable(patientTable, "DNI:", cita.getPaciente().getDni(), fontLabel, fontValue, true);
            addRowToTable(patientTable, "Nombre Completo:", cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos(), fontLabel, fontValue, false);
            addRowToTable(patientTable, "Correo Electrónico:", cita.getPaciente().getCorreo() != null ? cita.getPaciente().getCorreo() : "No registrado", fontLabel, fontValue, true);
            
            document.add(patientTable);
            document.add(new Paragraph("\n"));

            // DETALLES DE LA CITA
            document.add(new Paragraph("2. DETALLES DE LA CITA", fontSectionTitle));
            document.add(new Paragraph("\n"));
            
            PdfPTable citaTable = new PdfPTable(2);
            citaTable.setWidthPercentage(100);
            citaTable.setWidths(new float[]{30f, 70f});
            
            addRowToTable(citaTable, "Especialidad:", cita.getTurno().getMedico().getEspecialidad().getNombre(), fontLabel, fontValue, true);
            addRowToTable(citaTable, "Médico Tratante:", "Dr(a). " + cita.getTurno().getMedico().getNombres() + " " + cita.getTurno().getMedico().getApellidos(), fontLabel, fontValue, false);
            addRowToTable(citaTable, "Fecha de Atención:", cita.getTurno().getFecha().toString(), fontLabel, fontValue, true);
            addRowToTable(citaTable, "Hora Programada:", cita.getTurno().getHoraInicio().toString() + " - " + cita.getTurno().getHoraFin().toString(), fontLabel, fontValue, false);
            addRowToTable(citaTable, "Estado de Cita:", cita.getEstado().name(), fontLabel, fontValue, true);
            
            String fechaReservaStr = cita.getFechaReserva() != null ? cita.getFechaReserva().toString().replace("T", " ") : "N/A";
            if (fechaReservaStr.contains(".")) fechaReservaStr = fechaReservaStr.substring(0, fechaReservaStr.indexOf('.'));
            addRowToTable(citaTable, "Fecha de Emisión:", fechaReservaStr, fontLabel, fontValue, false);
            
            document.add(citaTable);

            // Barcode and Terms
            document.add(new Paragraph("\n"));
            
            PdfPTable footerContentTable = new PdfPTable(2);
            footerContentTable.setWidthPercentage(100);
            footerContentTable.setWidths(new float[]{65f, 35f});

            // Terms
            PdfPCell termsCell = new PdfPCell();
            termsCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            Font termsFont = FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Color.DARK_GRAY);
            termsCell.addElement(new Paragraph("RECOMENDACIONES IMPORTANTES:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, java.awt.Color.DARK_GRAY)));
            termsCell.addElement(new Paragraph("• Presentarse en recepción 15 minutos antes de su hora programada.", termsFont));
            termsCell.addElement(new Paragraph("• Portar obligatoriamente su DNI físico vigente.", termsFont));
            termsCell.addElement(new Paragraph("• En caso de no asistir, realizar la cancelación con 24 horas de anticipación.", termsFont));
            footerContentTable.addCell(termsCell);

            // Barcode
            PdfPCell barcodeCell = new PdfPCell();
            barcodeCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            barcodeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            barcodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            try {
                com.lowagie.text.pdf.Barcode128 barcode = new com.lowagie.text.pdf.Barcode128();
                barcode.setCode("AVIVA" + String.format("%05d", cita.getIdCita()) + cita.getPaciente().getDni());
                com.lowagie.text.Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), java.awt.Color.BLACK, java.awt.Color.BLACK);
                barcodeImage.setAlignment(com.lowagie.text.Image.RIGHT);
                barcodeImage.scalePercent(130);
                barcodeCell.addElement(barcodeImage);
            } catch (Exception e) {
                log.warn("No se pudo generar el codigo de barras", e);
            }
            
            footerContentTable.addCell(barcodeCell);
            document.add(footerContentTable);

            // Pie de página
            document.add(new Paragraph("\n\n"));
            PdfPTable officialFooterTable = new PdfPTable(1);
            officialFooterTable.setWidthPercentage(100);
            PdfPCell footerOfficialCell = new PdfPCell(new Phrase("Este documento es un comprobante electrónico válido generado por el sistema automatizado de Clínica Aviva.", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, java.awt.Color.GRAY)));
            footerOfficialCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerOfficialCell.setBorder(com.lowagie.text.Rectangle.TOP);
            footerOfficialCell.setBorderColor(new java.awt.Color(200, 200, 200));
            footerOfficialCell.setPaddingTop(10);
            officialFooterTable.addCell(footerOfficialCell);
            document.add(officialFooterTable);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error al generar el comprobante PDF para la cita {}", idCita, e);
            throw new RuntimeException("Error al generar el comprobante PDF");
        }
    }

    private void addRowToTable(PdfPTable table, String label, String value, Font fontLabel, Font fontValue, boolean isZebra) {
        java.awt.Color bgColor = isZebra ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;

        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBorder(com.lowagie.text.Rectangle.BOTTOM);
        cellLabel.setBorderColor(new java.awt.Color(220, 220, 220));
        cellLabel.setBackgroundColor(bgColor);
        cellLabel.setPadding(12);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, fontValue));
        cellValue.setBorder(com.lowagie.text.Rectangle.BOTTOM);
        cellValue.setBorderColor(new java.awt.Color(220, 220, 220));
        cellValue.setBackgroundColor(bgColor);
        cellValue.setPadding(12);
        table.addCell(cellValue);
    }
}
