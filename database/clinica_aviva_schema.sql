-- ============================================================
-- CLINICA AVIVA - Sistema de Gestion de Citas Medicas
-- Script de Creacion de Base de Datos Completo
-- MySQL 8.0+
-- Generado: 2026-05-27
-- ============================================================

DROP DATABASE IF EXISTS clinica_aviva;
CREATE DATABASE clinica_aviva
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE clinica_aviva;

-- ============================================================
-- 1. TABLA: ESPECIALIDAD
-- ============================================================
CREATE TABLE especialidad (
    id_especialidad INT           NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100)  NOT NULL,
    descripcion     TEXT,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,

    CONSTRAINT pk_especialidad PRIMARY KEY (id_especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 2. TABLA: ADMINISTRADOR
-- ============================================================
CREATE TABLE administrador (
    id_administrador INT           NOT NULL AUTO_INCREMENT,
    nombres          VARCHAR(100)  NOT NULL,
    apellidos        VARCHAR(100)  NOT NULL,
    correo           VARCHAR(150)  NOT NULL,
    usuario          VARCHAR(50)   NOT NULL,
    contrasena       VARCHAR(255)  NOT NULL,
    activo           TINYINT(1)    NOT NULL DEFAULT 1,

    CONSTRAINT pk_administrador  PRIMARY KEY (id_administrador),
    CONSTRAINT uq_admin_correo   UNIQUE (correo),
    CONSTRAINT uq_admin_usuario  UNIQUE (usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 3. TABLA: PACIENTE
-- ============================================================
CREATE TABLE paciente (
    id_paciente    INT           NOT NULL AUTO_INCREMENT,
    dni            CHAR(8)       NOT NULL,
    nombres        VARCHAR(100)  NOT NULL,
    apellidos      VARCHAR(100)  NOT NULL,
    correo         VARCHAR(150)  NOT NULL,
    telefono       VARCHAR(15)   NULL,
    contrasena     VARCHAR(255)  NOT NULL,
    fecha_registro DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo         TINYINT(1)    NOT NULL DEFAULT 1,

    CONSTRAINT pk_paciente       PRIMARY KEY (id_paciente),
    CONSTRAINT uq_paciente_dni   UNIQUE (dni),
    CONSTRAINT uq_paciente_correo UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 4. TABLA: MEDICO
-- ============================================================
CREATE TABLE medico (
    id_medico       INT           NOT NULL AUTO_INCREMENT,
    id_especialidad INT           NOT NULL,
    nombres         VARCHAR(100)  NOT NULL,
    apellidos       VARCHAR(100)  NOT NULL,
    correo          VARCHAR(150)  NOT NULL,
    usuario         VARCHAR(50)   NOT NULL,
    contrasena      VARCHAR(255)  NOT NULL,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,

    CONSTRAINT pk_medico              PRIMARY KEY (id_medico),
    CONSTRAINT uq_medico_correo       UNIQUE (correo),
    CONSTRAINT uq_medico_usuario      UNIQUE (usuario),
    CONSTRAINT fk_medico_especialidad FOREIGN KEY (id_especialidad)
        REFERENCES especialidad(id_especialidad)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 5. TABLA: HORARIO_MEDICO
-- ============================================================
CREATE TABLE horario_medico (
    id_horario         INT        NOT NULL AUTO_INCREMENT,
    id_medico          INT        NOT NULL,
    dia_semana         TINYINT    NOT NULL COMMENT '1=Lun 2=Mar 3=Mie 4=Jue 5=Vie 6=Sab 7=Dom',
    hora_inicio        TIME       NOT NULL,
    hora_fin           TIME       NOT NULL,
    duracion_turno_min INT        NOT NULL DEFAULT 30,
    activo             TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT pk_horario       PRIMARY KEY (id_horario),
    CONSTRAINT fk_horario_medico FOREIGN KEY (id_medico)
        REFERENCES medico(id_medico)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_horario_horas CHECK (hora_fin > hora_inicio),
    CONSTRAINT chk_dia_semana    CHECK (dia_semana BETWEEN 1 AND 7)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 6. TABLA: TURNO
-- ============================================================
CREATE TABLE turno (
    id_turno    INT         NOT NULL AUTO_INCREMENT,
    id_medico   INT         NOT NULL,
    fecha       DATE        NOT NULL,
    hora_inicio TIME        NOT NULL,
    hora_fin    TIME        NOT NULL,
    estado      ENUM('Libre','Ocupado','Bloqueado') NOT NULL DEFAULT 'Libre',

    CONSTRAINT pk_turno       PRIMARY KEY (id_turno),
    CONSTRAINT uq_turno_slot  UNIQUE (id_medico, fecha, hora_inicio),
    CONSTRAINT fk_turno_medico FOREIGN KEY (id_medico)
        REFERENCES medico(id_medico)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 7. TABLA: CITA
-- ============================================================
CREATE TABLE cita (
    id_cita       INT         NOT NULL AUTO_INCREMENT,
    id_paciente   INT         NOT NULL,
    id_turno      INT         NOT NULL,
    fecha_reserva DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado        ENUM('Programada','Atendida','No Asistio','Cancelada')
                  NOT NULL DEFAULT 'Programada',

    CONSTRAINT pk_cita         PRIMARY KEY (id_cita),
    CONSTRAINT fk_cita_paciente FOREIGN KEY (id_paciente)
        REFERENCES paciente(id_paciente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_cita_turno   FOREIGN KEY (id_turno)
        REFERENCES turno(id_turno)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 8. TABLA: NOTIFICACION
-- ============================================================
CREATE TABLE notificacion (
    id_notificacion  INT      NOT NULL AUTO_INCREMENT,
    id_cita          INT      NOT NULL,
    tipo             ENUM('Confirmacion','Recordatorio','Cancelacion') NOT NULL,
    fecha_programada DATETIME NOT NULL,
    fecha_envio      DATETIME NULL,
    estado_envio     ENUM('Pendiente','Enviado','Fallido') NOT NULL DEFAULT 'Pendiente',
    leido            TINYINT(1) NOT NULL DEFAULT 0,

    CONSTRAINT pk_notificacion  PRIMARY KEY (id_notificacion),
    CONSTRAINT fk_notif_cita    FOREIGN KEY (id_cita)
        REFERENCES cita(id_cita)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 9. TABLA: CONFIGURACION_GENERAL (Info dinamica de la clinica)
-- ============================================================
CREATE TABLE configuracion_general (
    id                       INT          NOT NULL AUTO_INCREMENT,
    nombre_clinica           VARCHAR(150) NOT NULL,
    correo_contacto          VARCHAR(150) NOT NULL,
    telefono_contacto        VARCHAR(20)  NOT NULL,
    direccion                VARCHAR(255) NOT NULL,
    horario_apertura_general TIME         NOT NULL,
    horario_cierre_general   TIME         NOT NULL,
    estado_disponibilidad    TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '1=Activa, 0=Mantenimiento',
    fecha_actualizacion      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_configuracion PRIMARY KEY (id),
    CONSTRAINT chk_horario_general CHECK (horario_cierre_general > horario_apertura_general)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- FOREIGN KEYS ADICIONALES (Administrador -> Medico/Horario)
-- ============================================================
ALTER TABLE medico
    ADD COLUMN creado_por INT NOT NULL,
    ADD CONSTRAINT fk_medico_admin FOREIGN KEY (creado_por)
        REFERENCES administrador(id_administrador)
        ON UPDATE CASCADE
        ON DELETE RESTRICT;

ALTER TABLE horario_medico
    ADD COLUMN creado_por INT NOT NULL,
    ADD CONSTRAINT fk_horario_admin FOREIGN KEY (creado_por)
        REFERENCES administrador(id_administrador)
        ON UPDATE CASCADE
        ON DELETE RESTRICT;

-- ============================================================
-- INDICES OPTIMIZADOS PARA CONSULTAS FRECUENTES
-- ============================================================
CREATE INDEX idx_turno_fecha ON turno(fecha);
CREATE INDEX idx_turno_medico_fecha ON turno(id_medico, fecha);
CREATE INDEX idx_turno_estado ON turno(estado);
CREATE INDEX idx_cita_paciente ON cita(id_paciente);
CREATE INDEX idx_cita_estado ON cita(estado);
CREATE INDEX idx_notif_pendiente ON notificacion(estado_envio, fecha_programada);
CREATE INDEX idx_horario_medico_dia ON horario_medico(id_medico, dia_semana);

-- ============================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ============================================================
DELIMITER $$

-- ------------------------------------------------------------
-- SP 1: Reservar Cita (Transaccional con bloqueo pesimista)
-- Uso: CALL sp_reservar_cita(1, 5, @res); SELECT @res;
-- ------------------------------------------------------------
CREATE PROCEDURE sp_reservar_cita(
    IN  p_id_paciente INT,
    IN  p_id_turno    INT,
    OUT p_resultado   VARCHAR(50)
)
BEGIN
    DECLARE v_estado_turno VARCHAR(20);

    -- Manejador de excepciones para rollback automatico
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 'ERROR_INTERNO';
    END;

    START TRANSACTION;

    -- Bloqueo pesimista: impide que otro hilo tome el mismo turno concurrentemente
    SELECT estado INTO v_estado_turno
    FROM turno
    WHERE id_turno = p_id_turno
    FOR UPDATE;

    IF v_estado_turno = 'Libre' THEN
        -- Ocupar el turno
        UPDATE turno SET estado = 'Ocupado' WHERE id_turno = p_id_turno;

        -- Crear la cita
        INSERT INTO cita (id_paciente, id_turno)
        VALUES (p_id_paciente, p_id_turno);

        -- Programar notificacion de confirmacion (inmediata)
        INSERT INTO notificacion (id_cita, tipo, fecha_programada)
        VALUES (LAST_INSERT_ID(), 'Confirmacion', NOW());

        -- Programar recordatorio 24h antes de la cita
        INSERT INTO notificacion (id_cita, tipo, fecha_programada)
        SELECT id_cita,
               'Recordatorio',
               DATE_SUB(CONCAT(t.fecha, ' ', t.hora_inicio), INTERVAL 24 HOUR)
        FROM cita c
        JOIN turno t ON c.id_turno = t.id_turno
        WHERE c.id_cita = LAST_INSERT_ID();

        SET p_resultado = 'RESERVA_EXITOSA';
        COMMIT;
    ELSE
        SET p_resultado = 'TURNO_NO_DISPONIBLE';
        ROLLBACK;
    END IF;
END$$

-- ------------------------------------------------------------
-- SP 2: Cancelar Cita (Valida plazo minimo de 24 horas)
-- Uso: CALL sp_cancelar_cita(3, 1, @res); SELECT @res;
-- ------------------------------------------------------------
CREATE PROCEDURE sp_cancelar_cita(
    IN  p_id_cita     INT,
    IN  p_id_paciente INT,
    OUT p_resultado   VARCHAR(50)
)
BEGIN
    DECLARE v_fecha_hora_cita DATETIME;
    DECLARE v_id_turno        INT;

    SELECT CONCAT(t.fecha, ' ', t.hora_inicio), c.id_turno
    INTO   v_fecha_hora_cita, v_id_turno
    FROM   cita c
    JOIN   turno t ON c.id_turno = t.id_turno
    WHERE  c.id_cita     = p_id_cita
      AND  c.id_paciente = p_id_paciente
      AND  c.estado      = 'Programada';

    IF v_fecha_hora_cita IS NULL THEN
        SET p_resultado = 'CITA_NO_ENCONTRADA';
    ELSEIF TIMESTAMPDIFF(HOUR, NOW(), v_fecha_hora_cita) < 24 THEN
        SET p_resultado = 'FUERA_DE_PLAZO';
    ELSE
        -- Actualizar estado de la cita a Cancelada
        UPDATE cita  SET estado = 'Cancelada' WHERE id_cita   = p_id_cita;
        -- Liberar el turno
        UPDATE turno SET estado = 'Libre'     WHERE id_turno  = v_id_turno;
        -- Notificacion de cancelacion
        INSERT INTO notificacion (id_cita, tipo, fecha_programada)
        VALUES (p_id_cita, 'Cancelacion', NOW());

        SET p_resultado = 'CANCELACION_EXITOSA';
    END IF;
END$$

-- ------------------------------------------------------------
-- SP 3: Actualizar Estado de Cita (Atendida / No Asistio)
-- Uso: CALL sp_actualizar_estado_cita(3, 'Atendida', @res);
-- ------------------------------------------------------------
CREATE PROCEDURE sp_actualizar_estado_cita(
    IN  p_id_cita  INT,
    IN  p_estado   VARCHAR(20),
    OUT p_resultado VARCHAR(50)
)
BEGIN
    IF EXISTS (SELECT 1 FROM cita WHERE id_cita = p_id_cita AND estado = 'Programada') THEN
        UPDATE cita SET estado = p_estado WHERE id_cita = p_id_cita;

        -- Si no asistio, liberar el turno automaticamente
        IF p_estado = 'No Asistio' THEN
            UPDATE turno t
            JOIN   cita  c ON c.id_turno = t.id_turno
            SET    t.estado = 'Libre'
            WHERE  c.id_cita = p_id_cita;
        END IF;

        SET p_resultado = 'ACTUALIZADO';
    ELSE
        SET p_resultado = 'CITA_NO_VALIDA';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- FUNCIONES
-- ============================================================
DELIMITER $$

-- ------------------------------------------------------------
-- FN 1: Contar citas programadas de un medico en una fecha
-- Uso: SELECT fn_contar_citas_medico(2, '2026-05-10');
-- ------------------------------------------------------------
CREATE FUNCTION fn_contar_citas_medico(
    p_id_medico INT,
    p_fecha     DATE
)
RETURNS INT
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_total INT;
    SELECT COUNT(*) INTO v_total
    FROM   cita  c
    JOIN   turno t ON c.id_turno = t.id_turno
    WHERE  t.id_medico = p_id_medico
      AND  t.fecha     = p_fecha
      AND  c.estado    = 'Programada';
    RETURN v_total;
END$$

-- ------------------------------------------------------------
-- FN 2: Calcular tasa de ausentismo de un medico en un periodo
-- Uso: SELECT fn_tasa_ausentismo(2, '2026-04-01', '2026-04-30');
-- ------------------------------------------------------------
CREATE FUNCTION fn_tasa_ausentismo(
    p_id_medico   INT,
    p_fecha_desde DATE,
    p_fecha_hasta DATE
)
RETURNS DECIMAL(5,2)
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_total    INT;
    DECLARE v_ausentes INT;

    SELECT COUNT(*) INTO v_total
    FROM   cita c JOIN turno t ON c.id_turno = t.id_turno
    WHERE  t.id_medico = p_id_medico
      AND  t.fecha BETWEEN p_fecha_desde AND p_fecha_hasta
      AND  c.estado IN ('Atendida','No Asistio');

    SELECT COUNT(*) INTO v_ausentes
    FROM   cita c JOIN turno t ON c.id_turno = t.id_turno
    WHERE  t.id_medico = p_id_medico
      and  t.fecha BETWEEN p_fecha_desde AND p_fecha_hasta
      AND  c.estado = 'No Asistio';

    IF v_total = 0 THEN RETURN 0.00; END IF;
    RETURN ROUND((v_ausentes / v_total) * 100, 2);
END$$

DELIMITER ;

-- ============================================================
-- VISTAS
-- ============================================================

-- ------------------------------------------------------------
-- VISTA 1: Citas del dia (Panel medico y dashboard)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vista_citas_del_dia AS
SELECT
    c.id_cita,
    CONCAT(p.nombres, ' ', p.apellidos) AS nombre_paciente,
    p.dni,
    p.telefono,
    CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico,
    e.nombre                            AS especialidad,
    t.fecha,
    t.hora_inicio,
    t.hora_fin,
    c.estado
FROM cita c
    JOIN paciente   p ON c.id_paciente   = p.id_paciente
    JOIN turno      t ON c.id_turno      = t.id_turno
    JOIN medico     m ON t.id_medico     = m.id_medico
    JOIN especialidad e ON m.id_especialidad = e.id_especialidad
WHERE t.fecha = CURDATE()
ORDER BY t.hora_inicio;

-- ------------------------------------------------------------
-- VISTA 2: Turnos disponibles para reserva (Portal paciente)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vista_turnos_disponibles AS
SELECT
    t.id_turno,
    m.id_medico,
    CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico,
    e.id_especialidad,
    e.nombre   AS especialidad,
    t.fecha,
    t.hora_inicio,
    t.hora_fin
FROM turno      t
    JOIN medico     m ON t.id_medico         = m.id_medico
    JOIN especialidad e ON m.id_especialidad = e.id_especialidad
WHERE t.estado = 'Libre'
  AND t.fecha  >= CURDATE()
  AND m.activo  = 1
ORDER BY t.fecha, t.hora_inicio;

-- ------------------------------------------------------------
-- VISTA 3: Dashboard administrativo (KPIs por fecha)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vista_dashboard_admin AS
SELECT
    t.fecha,
    COUNT(c.id_cita)                                         AS total_citas,
    SUM(c.estado = 'Programada')                             AS programadas,
    SUM(c.estado = 'Atendida')                               AS atendidas,
    SUM(c.estado = 'No Asistio')                             AS inasistencias,
    SUM(c.estado = 'Cancelada')                              AS canceladas,
    ROUND(SUM(c.estado = 'No Asistio') / COUNT(*) * 100, 1) AS tasa_ausentismo_pct
FROM cita  c
    JOIN turno t ON c.id_turno = t.id_turno
GROUP BY t.fecha
ORDER BY t.fecha DESC;

-- ------------------------------------------------------------
-- VISTA 4: Notificaciones pendientes de envio (Cola de envio)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vista_notificaciones_pendientes AS
SELECT
    n.id_notificacion,
    n.tipo,
    n.fecha_programada,
    c.id_cita,
    p.correo            AS correo_paciente,
    CONCAT(p.nombres, ' ', p.apellidos) AS nombre_paciente,
    CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico,
    e.nombre            AS especialidad,
    t.fecha             AS fecha_cita,
    t.hora_inicio       AS hora_cita
FROM notificacion n
    JOIN cita       c ON n.id_cita       = c.id_cita
    JOIN paciente   p ON c.id_paciente   = p.id_paciente
    JOIN turno      t ON c.id_turno      = t.id_turno
    JOIN medico     m ON t.id_medico     = m.id_medico
    JOIN especialidad e ON m.id_especialidad = e.id_especialidad
WHERE n.estado_envio    = 'Pendiente'
  AND n.fecha_programada <= NOW()
  AND c.estado           = 'Programada';

-- ============================================================
-- DATOS INICIALES (Seed data para pruebas)
-- ============================================================

-- Especialidades medicas
INSERT INTO especialidad (nombre, descripcion) VALUES
('Medicina General', 'Atencion primaria y diagnostico general'),
('Pediatria', 'Atencion medica de ninos y adolescentes'),
('Cardiologia', 'Diagnostico y tratamiento de enfermedades del corazon'),
('Dermatologia', 'Tratamiento de enfermedades de la piel'),
('Ginecologia', 'Salud reproductive femenina'),
('Neurologia', 'Tratamiento de trastornos del sistema nervioso'),
('Oftalmologia', 'Salud visual y cirugia ocular'),
('Ortopedia', 'Tratamiento de huesos, musculos y articulaciones'),
('Psicologia', 'Salud mental y bienestar emocional'),
('Traumatologia', 'Lesiones y accidentes del sistema musculoesqueletico');

-- Administrador por defecto (contrasena: Admin123! - BCrypt)
-- La contrasena debe ser reemplazada por un hash BCrypt real en produccion
INSERT INTO administrador (nombres, apellidos, correo, usuario, contrasena) VALUES
('Admin', 'Principal', 'admin@clinicaaviva.pe', 'admin', '$2a$10$oUWPx9yH1FjIcB1XtAE73uY1NZvH32Zkzx/7HvsZqyQzvHzfjyFTe');

-- Configuracion general de la clinica (registro unico)
INSERT INTO configuracion_general (
    nombre_clinica, correo_contacto, telefono_contacto, direccion,
    horario_apertura_general, horario_cierre_general, estado_disponibilidad
) VALUES (
    'Clinica Aviva',
    'contacto@clinicaaviva.pe',
    '(01) 712-3456',
    'Av. Carlos Izaguirre 1200, Los Olivos, Lima, Peru',
    '08:00:00',
    '20:00:00',
    1
);

COMMIT;
