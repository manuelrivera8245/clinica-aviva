**Desarrollo de Software** 

## **Desarrollo de un Sistema Web mediante Spring Tool Suite para la Reducción de Tiempos de Espera en la Gestión de Citas Médicas del Hospital Aviva** 

Chumbes Jara, Piero Alessandro Reyes Garcilazo, Jeremy Yober Rivera Laura,  Jose Manuel Rodriguez Cuadros, Jeremy David Saavedra Chavez, Angelo Jesus 

Estudiante de la Universidad de Ciencias y humanidades (UCH) 

Docente: SIRHAN WILLIAMS BENITES LAVADO 

Los Olivos, Lima, Perú 

Junio – 2026 

**Desarrollo de Software** 

## **Índice de Contenidos** 

Índice de Contenidos............................................................................................................... 2 1. Descripción de la empresa o negocio..................................................................................3 1.1. Misión y Visión............................................................................................................ 3 1.2. Objetivos estratégicos.................................................................................................3 1.3. Análisis estratégico..................................................................................................... 4 1.4. Aporte estratégico....................................................................................................... 4 2. Descripción del proceso(s) seleccionado(s)........................................................................ 5 2.1. Alcance........................................................................................................................5 2.2. Mapa de procesos.......................................................................................................5 2.3. Proceso Actual (AS-IS):.............................................................................................. 6 2.3.1 Procesos de formalización internos.................................................................... 6 2.3.2. Procesos de formalización externos.................................................................. 7 2.4. Diagnóstico y problemas identificados........................................................................7 2.4.1. Análisis de brecha (Gap Analysis)..................................................................... 7 2.5 Proceso Propuesto (TO-BE)........................................................................................ 9 2.5.1 Procesos de formalización (to-be):......................................................................9 3. Especificación De Requisitos Del Software....................................................................... 10 3.1. Requerimientos Funcionales:....................................................................................11 3.2. Historias de Usuario:.................................................................................................13 3.3. Requerimientos No Funcionales:.............................................................................. 16 3.4. Arquitectura y Stack Tecnológico:............................................................................. 17 4. Diseño De La Base De Datos Relacional.......................................................................... 18 4.1. Diagrama del modelo físico:......................................................................................18 4.2. Diccionario de Datos:................................................................................................ 18 4.3. Script SQL (Estructura y restricciones):.................................................................... 23 4.4. Script SQL (Lógica de base de datos):..................................................................... 29 5. Prototipos de la aplicación web..........................................................................................39 6. Conclusiones Y Recomendaciones................................................................................... 42 6.1. Conclusiones del proyecto:....................................................................................... 42 6.2. Recomendaciones del proyecto:...............................................................................42 

**Desarrollo de Software** 

## **1. Descripción de la empresa o negocio** 

**Razón Social:** Centros de Salud Peruanos S.A.C. 

**Nombre Comercial:** Clínica Aviva 

**Sector:** Salud / Servicios Médicos 

La Clínica Aviva es una institución de salud privada que forma parte del ecosistema de empresas del Grupo Intercorp. Fue concebida con el propósito de democratizar el acceso a una salud de alta calidad en el Perú, enfocándose inicialmente en el sector de la clase media en Lima Norte (Los Olivos). Con una inversión que supera los S/ 50 millones, la clínica ofrece una propuesta de valor basada en tecnología de vanguardia, trato humano y costos accesibles. Actualmente, cuenta con un equipo de más de 180 profesionales médicos y ofrece más de 30 especialidades, consolidándose como un referente en atención materno-infantil y medicina integral. 

## **1.1. Misión y Visión** 

- **Misión:** Transformar la salud de los peruanos brindando un servicio médico de excelente calidad, accesible y con trato amable, permitiendo que más personas vivan sanas para alcanzar sus metas. 

- **Visión:** Ser la red de salud líder e innovadora del país, contribuyendo al propósito de hacer del Perú el mejor lugar para formar una familia en América Latina. 

## **1.2. Objetivos estratégicos** 

- **Expansión Operativa:** Incrementar el volumen de atenciones en 250,000 nuevos pacientes para el cierre del año 2026 a través de la apertura de nuevos medicentros y sedes. 

- **Excelencia en el Servicio:** Mantener un índice de satisfacción del paciente superior al 90%, garantizando tiempos de espera reducidos y una atención personalizada. 

- **Transformación Digital:** Implementar soluciones tecnológicas que optimicen el flujo de los procesos críticos, desde la reserva de citas hasta la entrega de resultados médicos. 

- **Eficiencia de Recursos:** Maximizar el uso de la capacidad instalada y reducir las tasas de ausentismo en las citas programadas. 

**Desarrollo de Software** {i=4 UCH HUMANIDADESCIENCIAS Y 

## **1.3. Análisis estratégico** 

Para entender la posición de la clínica y la relevancia del proyecto, se presenta el siguiente análisis de factores internos y externos: 

## **1.4. Aporte estratégico** 

El desarrollo del **Sistema Web para la Optimización del Proceso de Reserva y Gestión de Citas Médicas** es una pieza fundamental para la estrategia de crecimiento de la organización. Su implementación no es solo una mejora técnica, sino un motor de valor que impacta en los siguientes niveles: 

- **Sostenibilidad del Crecimiento:** Permite que la clínica absorba el incremento proyectado de pacientes sin degradar la calidad del servicio. 

- **Optimización Financiera:** Reduce las pérdidas económicas derivadas del ausentismo y la subutilización de consultorios mediante un control eficiente de la agenda médica. 

- **Diferenciación:** Refuerza la imagen de Aviva como una institución moderna y centrada en el paciente, eliminando las fricciones administrativas y facilitando el acceso a la salud desde cualquier dispositivo. 

DZ UNIVERSIDAD DE **Desarrollo de Software** — U ( |] CIENCIAS Y 

## **2. Descripción del proceso(s) seleccionado(s)** 

El proceso elegido para este proyecto de investigación es la **Reserva y gestión de citas médicas** . Este es considerado el proceso más crítico de la clínica, ya que es el punto de entrada de los pacientes y afecta directamente tanto la rentabilidad del negocio como la calidad del servicio percibida. Aunque la clínica implementó chatbots de WhatsApp logrando ciertas mejoras, el núcleo del proceso manual presenta deficiencias significativas. 

## **2.1. Alcance** 

El proyecto comprende la digitalización integral del ciclo de vida de una cita médica. 

- **Límite Inicial:** Comienza con el acceso del usuario al sistema y la consulta de disponibilidad por especialidad o médico. 

- **Límite Final:** Concluye con el registro de la asistencia del paciente en el panel del médico o la liberación automática del cupo en caso de inasistencia. 

- **Funcionalidades Clave:** Gestión de usuarios (pacientes/admin), administración de horarios médicos, notificaciones vía sistema y reportes de gestión de citas. 

## **2.2. Mapa de procesos** 

Para entender dónde se ubica este proyecto, lo clasificamos dentro de la cadena de valor de la **Clínica Aviva** : 

- **Procesos Estratégicos:** Gestión de la expansión operativa y metas de crecimiento al 2026. 

- **Procesos Operativos (Misionales):** Aquí se ubica la **Gestión de Citas** , que actúa como el disparador de la atención médica, seguida por el diagnóstico y tratamiento. 

- **Procesos de Apoyo:** Desarrollo de software (soporte tecnológico), gestión de personal médico (+180 profesionales) y mantenimiento de infraestructura. 

Z= UNIVERSIDADCIENCIAS DEY **Desarrollo de Software** 

## **2.3. Proceso Actual (AS-IS):** 

## **2.3.1 Procesos de formalización internos** 

Internamente, la gestión carece de una plataforma centralizada: 

- **Sincronización:** La recepcionista debe verificar manualmente agendas en cuadernos o archivos Excel locales. 

- **Comunicación Interna:** No hay un sistema que avise al médico en tiempo real sobre cancelaciones, lo que genera tiempos muertos significativos. 

- **Resguardo de Datos:** La información es vulnerable a duplicidad o pérdida debido al registro descentralizado. 

**Desarrollo de Software** Z UCH fines 

## **2.3.2. Procesos de formalización externos** 

La interacción con el paciente es rígida: 

- **Canales:** Limitados a llamadas telefónicas (central 712-3456) o presencia física. 

- **Feedback:** El paciente sólo recibe confirmación verbal o telefónica, sin un comprobante digital que respalde la transacción. 

- **Seguimiento:** El paciente no cuenta con recordatorios, lo que eleva la tasa de ausentismo al 30% - 35%. 

## **2.4. Diagnóstico y problemas identificados** 

Se identifican cuellos de botella en la central telefónica y una subutilización de la capacidad instalada debido a la falta de herramientas tecnológicas. 

## **2.4.1. Análisis de brecha (Gap Analysis)** 

El análisis de brecha permite identificar la distancia operativa y tecnológica entre la situación actual (AS-IS) de la gestión de citas en la Clínica Aviva y el estado ideal (TO-BE) que se busca alcanzar con la implementación del nuevo sistema web. Este análisis justifica la necesidad de digitalización para cumplir con el objetivo estratégico de atender a 250,000 nuevos pacientes. 

## **Canales de Atención y Accesibilidad** 

- **Situación Actual (AS-IS):** Existe una alta dependencia y saturación de canales tradicionales. Las reservas se realizan exclusivamente mediante la central telefónica (712-3456) o con la presencia física del paciente en la clínica, lo que genera demoras y cuellos de botella. 

- **Estado Ideal (TO-BE):** Implementación de una plataforma web que permita la autogestión de citas, facilitando el acceso a la salud desde cualquier dispositivo de forma asincrónica. 

**Desarrollo de Software** 

- **Brecha:** Transición de un modelo de atención rígido y saturado hacia uno digital, escalable y centrado en la experiencia de usuario (paciente). 

## **Gestión de la Información y Resguardo de Datos** 

- **Situación Actual (AS-IS):** El registro de las citas se realiza de forma manual mediante cuadernos o archivos de Excel locales manejados por las recepcionistas. Esto descentraliza los datos, haciéndolos altamente vulnerables a duplicidad o pérdida. 

- **Estado Ideal (TO-BE):** Uso de una base de datos relacional (MySQL) que funcione como un repositorio centralizado, garantizando la integridad, consistencia y disponibilidad de la información clínica y de las agendas médicas. 

- **Brecha:** Carencia de un modelo de datos estructurado. Es imperativo migrar de herramientas ofimáticas aisladas a un Sistema de Gestión de Bases de Datos robusto. 

## **Comunicación y Seguimiento al Paciente** 

- **Situación Actual (AS-IS):** El feedback al paciente es deficiente; solo se entrega una confirmación verbal o telefónica sin respaldo digital. La falta de recordatorios automáticos contribuye a una tasa crítica de ausentismo que oscila entre el 30% y 35% el mismo día de la cita. 

- **Estado Ideal (TO-BE):** Un módulo de notificaciones automatizado que envíe confirmaciones digitales y recordatorios previos a la cita. 

- **Brecha:** Falta de automatización en los flujos de comunicación saliente, lo cual es vital para reducir las pérdidas económicas por inasistencias. 

## **Optimización del Tiempo Médico y Capacidad Instalada** 

- **Situación Actual (AS-IS):** La nula comunicación interna en tiempo real entre el área de recepción y los consultorios provoca que los médicos tengan tiempos muertos prolongados esperando pacientes que no asisten. 

- **Estado Ideal (TO-BE):** Sincronización en tiempo real de las agendas, permitiendo el registro de asistencia en el panel del médico o la liberación automática del cupo en caso de inasistencia. 

- **Brecha:** Inexistencia de interoperabilidad interna. Se requiere conectar los procesos de recepción directamente con la visualización del médico. 

## **Arquitectura Tecnológica** 

- **Situación Actual (AS-IS):** El núcleo del proceso misional (gestión de citas) opera sobre procesos completamente manuales sin una infraestructura de software que lo soporte. 

**Desarrollo de Software** 

- **Estado Ideal (TO-BE):** Un sistema web modularizado, desarrollado en el backend con el framework Spring Boot para una lógica de negocio eficiente, y un frontend responsivo basado en HTML5, Bootstrap y Angular. 

- **Brecha:** Inexistencia de una arquitectura de software. Es necesario construir e implementar una solución tecnológica integral desde cero para eliminar la fricción operativa y escalar los servicios de la clínica. 

## **2.5 Proceso Propuesto (TO-BE)** 

El proceso propuesto plantea un cambio de paradigma en la Clínica Aviva: pasar de un modelo reactivo y manual a un ecosistema de autogestión digital. A través de la implementación del nuevo Sistema Web, se centraliza y automatiza la reserva de citas, operando de manera ininterrumpida (24/7) y eliminando las fricciones del canal tradicional. 

Este nuevo modelo no sólo empodera al paciente, sino que transforma el rol del personal de admisión, quienes dejarán de ser digitadores de datos para enfocarse en la atención presencial de calidad y la resolución de casos complejos. 

## **2.5.1 Procesos de formalización (to-be):** 

El nuevo flujo operativo integrado se define a través de los siguientes pasos automatizados: 

1. **Ingreso y Autenticación (Autogestión):** El paciente ingresa al portal web desde cualquier dispositivo. Si es paciente nuevo, se registra mediante un formulario 

## G2Iz UCH iis...UNIVERSIDAD DE **Desarrollo de Software** 

validado; si es continuador, inicia sesión con sus credenciales, garantizando la integridad de la base de datos. 

2. **Búsqueda Dinámica en Tiempo Real:** El paciente selecciona la especialidad o el médico de su preferencia. El sistema consulta instantáneamente la base de datos y muestra únicamente los bloques de horarios disponibles, eliminando el "ensayo y error" verbal. 

3. **Reserva y Bloqueo Automático:** Al elegir un horario, el sistema bloquea inmediatamente el cupo en la agenda médica general para evitar cruces o duplicidad de citas por concurrencia. 

4. **Confirmación Inmediata:** El sistema procesa la reserva y muestra una pantalla de éxito, generando en paralelo un comprobante digital que el paciente puede descargar o visualizar en su perfil. 

5. **Notificaciones y Prevención de Ausentismo:** El módulo de alertas entra en acción enviando un correo electrónico de confirmación al instante. Posteriormente, 24 horas antes de la cita, el sistema dispara automáticamente un recordatorio, dándole al paciente la opción de confirmar su asistencia o cancelar/reprogramar la cita, liberando el turno para otro usuario de forma automática. 

## Visualizar mas grande 

## **3. Especificación De Requisitos Del Software** 

Se detalla las capacidades que el Sistema Web de Gestión de Citas debe proveer para satisfacer las necesidades de la Clínica Aviva, estructurando desde los requerimientos funcionales hasta la arquitectura tecnológica que los soportará. 

> ]D UC Fl UNIVERSIDADCIENCIAS DEY **Desarrollo de Software** 

## **3.1. Requerimientos Funcionales:** 

|**ID**|**Módulo / Actor**|**Descripción del Requisito Funcional**|**Prioridad**|
|---|---|---|---|
|**RF-01**|Paciente|El sistema deberá permitir el registro de<br>nuevos pacientes, solicitando la<br>siguiente información: Documento<br>Nacional de Identidad (DNI), nombres,<br>apellidos, correo electrónico, número de<br>teléfono y contraseña.|**Alta**|
|**RF-02**|Paciente|El sistema deberá posibilitar que el<br>paciente inicie sesión mediante la<br>validación de sus credenciales (correo<br>electrónico o DNI y contraseña<br>encriptada).|**Alta**|
|**RF-03**|Paciente|El sistema deberá ofrecer la<br>funcionalidad de filtrar los horarios de<br>atención por especialidad, nombre del<br>médico y fecha específica.|**Alta**|
|**RF-04**|Paciente|El sistema deberá permitir la selección<br>de un bloque de horario disponible y<br>confirmar la reserva, bloqueando<br>automáticamente dicho turno en el<br>calendario de atención.|**Alta**|
|**RF-05**|Paciente|El sistema deberá presentar un panel<br>que muestre las citas vigentes y las citas<br>históricas, permitiendo la cancelación de<br>una reserva con una antelación mínima<br>de 24 horas.|**Media**|
|**RF-06**|Paciente|El sistema deberá generar un<br>comprobante digital descargable en<br>formato PDF que contenga los detalles<br>de la cita reservada (Especialidad,<br>Médico, Fecha y Hora).|**Media**|
|**RF-07**|Médico|El sistema deberá permitir el acceso al<br>personal médico a través de un proceso<br>de autenticación que utilice un usuario y|**Alta**|



## ]D UC Fl UNIVERSIDADCIENCIAS DEY **Desarrollo de Software** 

|<br>]D UC<br>D UCUC Fl<br>wv|<br>UNIVERSIDADCIENCIAS DEY<br>Fl CIENCIAS Y<br>HUMANIDADES|**Desarrollo de Software**||
|---|---|---|---|
|||una contraseña proporcionados por la<br>institución clínica.||
|**RF-08**|Médico|El sistema deberá mostrar al profesional<br>médico su calendario de turnos<br>asignados para el día en curso,<br>detallando el nombre del paciente y la<br>hora exacta de la cita programada.|**Alta**|
|**RF-09**|Médico|El sistema deberá facilitar al médico la<br>capacidad de modificar el estado de la<br>cita a "Atendido" o "No Asistió" una vez<br>que el turno programado haya concluido.|**Media**|
|**RF-10**|Administrador|El sistema deberá proporcionar al<br>administrador las herramientas para<br>registrar, modificar y dar de baja a los<br>doctores, asignándoles las<br>especialidades médicas<br>correspondientes.|**Alta**|
|**RF-11**|Administrador|El sistema deberá permitir al<br>**Alta**<br>administrador la configuración de los<br>días y los rangos de horas de atención<br>de cada médico, con el fin de generar la<br>disponibilidad de turnos.|**Alta**|
|**RF-12**|Administrador|El sistema deberá presentar al<br>administrador un monitor o_dashboard_<br>en tiempo real que muestre la totalidad<br>de las citas programadas en la clínica.|**Baja**|
|**RF-13**|Sistema|El sistema deberá enviar<br>automáticamente una notificación por<br>correo electrónico al paciente con 24<br>horas de antelación a su cita, con el<br>objetivo de reducir el ausentismo.||



**Desarrollo de Software** 

**3.2. Historias de Usuario:** 

62Iz UCH sits...UNIVERSIDAD DE **Desarrollo de Software** 

62Zz UCH sits...UNIVERSIDAD DE **Desarrollo de Software** 

## ]D UC Fl UNIVERSIDADCIENCIAS DEY **Desarrollo de Software** 

## **3.3. Requerimientos No Funcionales:** 

|**ID**|**Categoría**|**Descripción del Requisito No**<br>**Funcional**|**Prioridad**|
|---|---|---|---|
|**RNF-01**|**Rendimiento**|El sistema debe exhibir un tiempo de<br>respuesta óptimo. El tiempo de carga<br>de las interfaces de usuario y las<br>consultas a la base de datos no debe<br>exceder los 3 segundos en el entorno<br>de servidor local.|**Media**|
|**RNF-02**|**Seguridad**|Las credenciales de acceso de los<br>usuarios deben ser protegidas mediante<br>técnicas de cifrado unidireccional<br>(hashing), asegurando que no sean<br>visibles en texto plano dentro de la base<br>de datos (Bcrypt en Spring Boot).|**Alta**|
|**RNF-03**|**Seguridad**|Implementación de un control de<br>acceso robusto que impida el acceso a<br>recursos o pantallas restringidas<br>mediante la manipulación directa de la<br>URL si la sesión del usuario no ha sido<br>previamente autenticada.|**Alta**|
|**RNF-04**|**Privacidad**|A nivel de arquitectura de código y<br>diseño de base de datos, se garantizará<br>que un paciente solo pueda acceder a<br>su propio historial clínico o de citas,<br>respetando la confidencialidad de la<br>información de otros usuarios.|**Alta**|
|**RNF-05**|**Usabilidad**|La interfaz de usuario web deberá<br>adoptar un diseño adaptable<br>(Responsive Design) que garantice una<br>visualización correcta y funcional en<br>diversos dispositivos, incluyendo<br>computadoras de escritorio y<br>dispositivos móviles (Bootstrap o CSS<br>puro).|**Alta**|



## Z UNIVERSIDAD DE **Desarrollo de Software** HUMANIDADES {4 uC Fl CIENCIAS Y 

|**RNF-06**|**Compatibilidad**|El sistema deberá operar sin presentar<br>anomalías visuales o funcionales en los<br>navegadores web predominantes en el<br>mercado actual.|**Baja**|
|---|---|---|---|
|**RNF-07**|**Disponibilidad**|El sistema mantendrá su operatividad y<br>disponibilidad siempre que los servicios<br>esenciales del entorno local (Apache y<br>MySQL) se encuentren activos.|**Alta**|



## **3.4. Arquitectura y Stack Tecnológico:** 

Para el desarrollo del sistema web de la Clínica Aviva, se ha definido una arquitectura **Cliente-Servidor** . Esta estructura separa claramente la interfaz visual de las reglas del negocio y los datos, permitiendo un desarrollo ordenado. Para cumplir con los objetivos del proyecto, el sistema se implementará de manera local utilizando el siguiente stack tecnológico: 

## **1. Entorno de Desarrollo y Base de Datos** 

- **XAMPP (MySQL):** Se utilizará este entorno local gratuito para alojar el motor de base de datos relacional. Aquí se implementará el modelo físico mediante scripts SQL, incluyendo la creación de todas las tablas, vistas, procedimientos almacenados y funciones requeridas para la gestión de las citas y los pacientes. 

## **2. Backend** 

- **Spring Boot (Framework Spring):** Se utilizará como el núcleo del servidor (Back-End). Este framework en Java gestionará la seguridad (Login), el panel principal (Home), y expondrá los servicios necesarios para realizar los CRUDs (Crear, Leer, Actualizar, Eliminar) de pacientes, médicos y citas, así como la generación de reportes. 

- **Spring Tool Suite 4 (STS 4):** Todo el código del Back-End será estructurado, desarrollado y compilado obligatoriamente utilizando este entorno de desarrollo integrado (IDE), garantizando el estándar exigido para el proyecto. 

## **3. Frontend (Capa de Presentación)** 

- **Angular y Tecnologías Web:** La interfaz de usuario (Front-End) se construirá utilizando el framework **Angular** para crear una aplicación dinámica (SPA). Para la estructura, estilos y dinamismo adicional, se integrarán tecnologías web estándar como **HTML5, CSS (Bootstrap), JavaScript y JQuery** . 

Z UNIVERSIDAD DE **Desarrollo de Software** HUMANIDADES }— CIENCIAS Y 

## **4. Diseño De La Base De Datos Relacional** 

## **4.1. Diagrama del modelo físico:** 

## **4.2. Diccionario de Datos:** 

**Tabla: especialidad** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_especialida<br>d|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|



## G2i) UCH UNIVERSIDADEiiik’...DE **Desarrollo de Software** 

|nombre|VARCHAR|100|NOT NULL|Nombre de la<br>especialidad (ej.<br>Pediatría)|
|---|---|---|---|---|
|descripcion|TEXT|—|NULL|Descripción general|
|activo|TINYINT(1)|—|DEFAULT 1|Borrado lógico|



## **Tabla: paciente** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_paciente|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|
|dni|CHAR|8|NOT NULL, UNIQUE|Documento de<br>identidad|
|nombres|VARCHAR|100|NOT NULL|Nombres del paciente|
|apellidos|VARCHAR|100|NOT NULL|Apellidos del paciente|
|correo|VARCHAR|150|NOT NULL, UNIQUE|Correo electrónico<br>(login)|
|telefono|VARCHAR|15|NULL|Número de contacto|
|contrasena|VARCHAR|255|NOT NULL|Hash BCrypt|



## G2i) UCH UNIVERSIDADEiiik’...DE **Desarrollo de Software** 

|fecha_registr<br>o|DATETIME|—|DEFAULT NOW()|Fecha de alta en el<br>sistema|
|---|---|---|---|---|
|activo|TINYINT(1)|—|DEFAULT 1|Borrado lógico|



## **Tabla: medico** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_medico|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|
|id_especialidad|INT|—|FK → especialidad|Especialidad<br>asignada|
|nombres /<br>apellidos|VARCHAR|100|NOT NULL|Nombre completo|
|correo|VARCHAR|150|UNIQUE|Correo<br>institucional|
|usuario|VARCHAR|50|UNIQUE|Usuario de acceso|
|contrasena|VARCHAR|255|NOT NULL|Hash BCrypt|
|activo|TINYINT(1)|—|DEFAULT 1|Borrado lógico|



**Tabla: horario_medico** 

## G2i) UCH UNIVERSIDADEiiik’...DE **Desarrollo de Software** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_horario|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|
|id_medico|INT|—|FK → medico|Médico al que<br>aplica|
|dia_semana|TINYINT|—|NOT NULL|1=Lunes …<br>7=Domingo|
|hora_inicio /<br>hora_fin|TIME|—|NOT NULL|Rango de atención<br>diario|
|duracion_turno_mi<br>n|INT|—|DEFAULT 30|Duración de cada<br>slot en minutos|
|activo|TINYINT(1)|—|DEFAULT 1|Borrado lógico|



## **Tabla: turno** 

## G2i) UCH UNIVERSIDADEiiik’...DE **Desarrollo de Software** 

|fecha|DATE|—|NOT NULL|Fecha del turno|
|---|---|---|---|---|
|hora_inicio /<br>hora_fin|TIME|—|NOT NULL|Horario exacto|
|estado|ENUM —|ENUM —|DEFAULT 'Libre'|Libre / Ocupado /<br>Bloqueado|
|—|—|—|UNIQUE(id_medico, fecha,<br>hora_inicio)|Evita duplicados|



## **Tabla: cita** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_cita|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|
|id_paciente|INT|—|FK → paciente|Paciente que reserva|
|id_turno|INT|—|FK → turno, UNIQUE|Un turno = una cita<br>máximo|
|fecha_reser<br>va|DATETIME —|DATETIME —|DEFAULT NOW()|Momento de la reserva|
|estado|ENUM|—|DEFAULT<br>'Programada'|Programada / Atendida /<br>No Asistió / Cancelada|



## ¢Zz UCH UNIVERSIDADsiencis’...DE **Desarrollo de Software** 

**Tabla: notificacion** 

|**Campo**|**Tipo**|**Longitu**<br>**d**|**Restricción**|**Descripción**|
|---|---|---|---|---|
|id_notificacion|INT|—|PK,<br>AUTO_INCREMENT|Identificador único|
|id_cita|INT|—|FK → cita|Cita asociada|
|tipo|ENUM|—|NOT NULL|Confirmacion /<br>Recordatorio /<br>Cancelacion|
|fecha_programa<br>da|DATETIME —|DATETIME —|NOT NULL|Cuándo debe<br>enviarse|
|fecha_envio|DATETIME —|DATETIME —|NULL|Cuándo se envió<br>efectivamente|
|estado_envio|ENUM|—|DEFAULT 'Pendiente'|Pendiente / Enviado /<br>Fallido|



## **4.3. Script SQL (Estructura y restricciones):** 

-- ============================================================ 

-- CLINICA AVIVA 

-- ============================================================ 

CREATE DATABASE clinica_aviva 

USE clinica_aviva; 

**Desarrollo de Software** 

-- ------------------------------------------------------------ 

-- 1. ESPECIALIDAD 

-- ------------------------------------------------------------ 

CREATE TABLE especialidad ( 

id_especialidad  INT           NOT NULL AUTO_INCREMENT, 

nombre           VARCHAR(100)  NOT NULL, 

descripcion      TEXT, 

activo           TINYINT(1)    NOT NULL DEFAULT 1, 

CONSTRAINT pk_especialidad PRIMARY KEY (id_especialidad) 

); 

-- ------------------------------------------------------------ 

-- 2. ADMINISTRADOR 

-- ------------------------------------------------------------ 

CREATE TABLE administrador ( 

id_administrador INT           NOT NULL AUTO_INCREMENT, 

nombres          VARCHAR(100)  NOT NULL, 

apellidos        VARCHAR(100)  NOT NULL, correo           VARCHAR(150)  NOT NULL, usuario          VARCHAR(50)   NOT NULL, contrasena       VARCHAR(255)  NOT NULL, 

activo           TINYINT(1)    NOT NULL DEFAULT 1, 

CONSTRAINT pk_administrador  PRIMARY KEY (id_administrador), 

CONSTRAINT uq_admin_correo   UNIQUE (correo), 

CONSTRAINT uq_admin_usuario  UNIQUE (usuario) 

); 

-- ------------------------------------------------------------ 

-- 3. PACIENTE 

-- ------------------------------------------------------------ 

## **Desarrollo de Software** 

CREATE TABLE paciente ( 

id_paciente    INT           NOT NULL AUTO_INCREMENT, 

dni            CHAR(8)       NOT NULL, 

nombres        VARCHAR(100)  NOT NULL, apellidos      VARCHAR(100)  NOT NULL, correo         VARCHAR(150)  NOT NULL, 

telefono       VARCHAR(15), 

contrasena     VARCHAR(255)  NOT NULL, 

fecha_registro DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP, 

activo         TINYINT(1)    NOT NULL DEFAULT 1, 

CONSTRAINT pk_paciente       PRIMARY KEY (id_paciente), 

CONSTRAINT uq_paciente_dni   UNIQUE (dni), 

CONSTRAINT uq_paciente_correo UNIQUE (correo) 

); 

-- ------------------------------------------------------------ 

-- 4. MEDICO 

-- ------------------------------------------------------------ 

CREATE TABLE medico ( 

id_medico       INT           NOT NULL AUTO_INCREMENT, 

id_especialidad INT           NOT NULL, 

nombres         VARCHAR(100)  NOT NULL, apellidos       VARCHAR(100)  NOT NULL, correo          VARCHAR(150)  NOT NULL, usuario         VARCHAR(50)   NOT NULL, contrasena      VARCHAR(255)  NOT NULL, 

activo          TINYINT(1)    NOT NULL DEFAULT 1, 

CONSTRAINT pk_medico              PRIMARY KEY (id_medico), CONSTRAINT uq_medico_correo       UNIQUE (correo), 

**Desarrollo de Software** 

CONSTRAINT uq_medico_usuario      UNIQUE (usuario), 

CONSTRAINT fk_medico_especialidad FOREIGN KEY (id_especialidad) 

REFERENCES especialidad(id_especialidad) 

ON UPDATE CASCADE 

ON DELETE RESTRICT 

); 

-- ------------------------------------------------------------ 

-- 5. HORARIO_MEDICO 

-- ------------------------------------------------------------ 

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

); 

-- ------------------------------------------------------------ 

**Desarrollo de Software** 

-- 6. TURNO 

-- ------------------------------------------------------------ 

CREATE TABLE turno ( 

id_turno    INT         NOT NULL AUTO_INCREMENT, 

id_medico   INT         NOT NULL, 

fecha       DATE        NOT NULL, hora_inicio TIME        NOT NULL, hora_fin    TIME        NOT NULL, 

estado      ENUM('Libre','Ocupado','Bloqueado') NOT NULL DEFAULT 'Libre', CONSTRAINT pk_turno       PRIMARY KEY (id_turno), 

CONSTRAINT uq_turno_slot  UNIQUE (id_medico, fecha, hora_inicio), 

CONSTRAINT fk_turno_medico FOREIGN KEY (id_medico) 

REFERENCES medico(id_medico) 

ON UPDATE CASCADE 

ON DELETE CASCADE 

); 

-- ------------------------------------------------------------ 

-- 7. CITA 

-- ------------------------------------------------------------ 

CREATE TABLE cita ( 

id_cita       INT         NOT NULL AUTO_INCREMENT, 

id_paciente   INT         NOT NULL, 

id_turno      INT         NOT NULL, 

fecha_reserva DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP, 

estado        ENUM('Programada','Atendida','No Asistió','Cancelada') 

NOT NULL DEFAULT 'Programada', 

CONSTRAINT pk_cita         PRIMARY KEY (id_cita), 

## **Desarrollo de Software** 

CONSTRAINT uq_cita_turno   UNIQUE (id_turno), 

CONSTRAINT fk_cita_paciente FOREIGN KEY (id_paciente) 

REFERENCES paciente(id_paciente) 

ON UPDATE CASCADE 

ON DELETE RESTRICT, 

CONSTRAINT fk_cita_turno   FOREIGN KEY (id_turno) 

REFERENCES turno(id_turno) 

ON UPDATE CASCADE 

ON DELETE RESTRICT 

); 

-- ------------------------------------------------------------ 

-- 8. NOTIFICACION 

-- ------------------------------------------------------------ 

CREATE TABLE notificacion ( 

id_notificacion  INT      NOT NULL AUTO_INCREMENT, 

id_cita          INT      NOT NULL, 

tipo             ENUM('Confirmacion','Recordatorio','Cancelacion') NOT NULL, 

fecha_programada DATETIME NOT NULL, 

fecha_envio      DATETIME, 

estado_envio     ENUM('Pendiente','Enviado','Fallido') NOT NULL DEFAULT 'Pendiente', 

CONSTRAINT pk_notificacion  PRIMARY KEY (id_notificacion), 

CONSTRAINT fk_notif_cita    FOREIGN KEY (id_cita) 

REFERENCES cita(id_cita) 

ON UPDATE CASCADE 

ON DELETE CASCADE 

); 

- -- Conectar administrador con medico 

## **Desarrollo de Software** 

ALTER TABLE medico 

ADD COLUMN creado_por INT NOT NULL, 

ADD CONSTRAINT fk_medico_admin FOREIGN KEY (creado_por) 

REFERENCES administrador(id_administrador) 

ON UPDATE CASCADE 

ON DELETE RESTRICT; 

-- Conectar administrador con horario_medico 

ALTER TABLE horario_medico 

ADD COLUMN creado_por INT NOT NULL, 

ADD CONSTRAINT fk_horario_admin FOREIGN KEY (creado_por) 

REFERENCES administrador(id_administrador) 

ON UPDATE CASCADE 

ON DELETE RESTRICT; 

## **4.4. Script SQL (Lógica de base de datos):** 

USE clinica_aviva; 

-- ============================================================ 

-- PROCEDIMIENTOS ALMACENADOS 

-- ============================================================ 

-- ------------------------------------------------------------ 

-- SP 1: Reservar cita 

-- Uso: CALL sp_reservar_cita(1, 5, @res); SELECT @res; 

-- ------------------------------------------------------------ 

DELIMITER $$ 

CREATE PROCEDURE sp_reservar_cita( 

IN  p_id_paciente INT, 

**Desarrollo de Software** 

IN  p_id_turno    INT, 

OUT p_resultado   VARCHAR(50) 

) 

BEGIN 

DECLARE v_estado_turno VARCHAR(20); DECLARE EXIT HANDLER FOR SQLEXCEPTION 

BEGIN ROLLBACK; SET p_resultado = 'ERROR_INTERNO'; 

END; 

START TRANSACTION; 

-- Bloqueo pesimista: impide que otro hilo tome el mismo turno SELECT estado INTO v_estado_turno FROM turno WHERE id_turno = p_id_turno FOR UPDATE; 

IF v_estado_turno = 'Libre' THEN 

UPDATE turno SET estado = 'Ocupado' WHERE id_turno = p_id_turno; 

INSERT INTO cita (id_paciente, id_turno) 

VALUES (p_id_paciente, p_id_turno); 

-- Programar notificación de confirmación (inmediata) 

INSERT INTO notificacion (id_cita, tipo, fecha_programada) VALUES (LAST_INSERT_ID(), 'Confirmacion', NOW()); 

**Desarrollo de Software** 

-- Programar recordatorio 24h antes 

INSERT INTO notificacion (id_cita, tipo, fecha_programada) 

SELECT id_cita, 

'Recordatorio', DATE_SUB(CONCAT(t.fecha, ' ', t.hora_inicio), INTERVAL 24 HOUR) FROM cita c 

JOIN turno t ON c.id_turno = t.id_turno 

WHERE c.id_cita = LAST_INSERT_ID(); 

SET p_resultado = 'RESERVA_EXITOSA'; COMMIT; ELSE 

SET p_resultado = 'TURNO_NO_DISPONIBLE'; 

ROLLBACK; END IF; END$$ DELIMITER ; 

-- ------------------------------------------------------------ 

-- SP 2: Cancelar cita 

-- Uso: CALL sp_cancelar_cita(3, 1, @res); SELECT @res; 

-- ------------------------------------------------------------ 

DELIMITER $$ 

CREATE PROCEDURE sp_cancelar_cita( 

IN  p_id_cita     INT, IN  p_id_paciente INT, OUT p_resultado   VARCHAR(50) 

**Desarrollo de Software** 

) 

## BEGIN 

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

UPDATE cita  SET estado = 'Cancelada' WHERE id_cita   = p_id_cita; UPDATE turno SET estado = 'Libre'     WHERE id_turno  = v_id_turno; 

-- Notificación de cancelación 

INSERT INTO notificacion (id_cita, tipo, fecha_programada) VALUES (p_id_cita, 'Cancelacion', NOW()); 

SET p_resultado = 'CANCELACION_EXITOSA'; 

END IF; 

END$$ 

**Desarrollo de Software** 

DELIMITER ; 

-- ------------------------------------------------------------ 

-- SP 3: Actualizar estado de cita 

-- Uso: CALL sp_actualizar_estado_cita(3, 'Atendida', @res); 

-- ------------------------------------------------------------ 

DELIMITER $$ 

CREATE PROCEDURE sp_actualizar_estado_cita( 

IN  p_id_cita  INT, 

IN  p_estado   ENUM('Atendida','No Asistió'), 

OUT p_resultado VARCHAR(50) 

) 

## BEGIN 

IF EXISTS (SELECT 1 FROM cita WHERE id_cita = p_id_cita AND estado = 'Programada') THEN 

UPDATE cita SET estado = p_estado WHERE id_cita = p_id_cita; 

-- Si no asistió, liberar el turno 

IF p_estado = 'No Asistió' THEN 

UPDATE turno t 

JOIN   cita  c ON c.id_turno = t.id_turno 

SET    t.estado = 'Libre' 

WHERE  c.id_cita = p_id_cita; 

END IF; 

SET p_resultado = 'ACTUALIZADO'; 

ELSE 

SET p_resultado = 'CITA_NO_VALIDA'; 

**Desarrollo de Software** 

END IF; 

## END$$ 

DELIMITER ; 

-- ============================================================ 

-- FUNCIONES 

-- ============================================================ 

-- ------------------------------------------------------------ 

-- FN 1: Contar citas programadas de un médico en una fecha 

-- Uso: SELECT fn_contar_citas_medico(2, '2026-05-10'); 

-- ------------------------------------------------------------ 

## DELIMITER $$ 

CREATE FUNCTION fn_contar_citas_medico( 

p_id_medico INT, 

p_fecha     DATE 

) 

RETURNS INT 

DETERMINISTIC READS SQL DATA 

BEGIN 

DECLARE v_total INT; 

SELECT COUNT(*) INTO v_total FROM   cita  c 

JOIN   turno t ON c.id_turno = t.id_turno 

WHERE  t.id_medico = p_id_medico 

AND  t.fecha     = p_fecha 

AND  c.estado    = 'Programada'; 

RETURN v_total; 

**Desarrollo de Software** 

END$$ 

## DELIMITER ; 

-- ------------------------------------------------------------ 

-- FN 2: Calcular tasa de ausentismo de un médico 

-- Uso: SELECT fn_tasa_ausentismo(2, '2026-04-01', '2026-04-30'); 

-- ------------------------------------------------------------ 

DELIMITER $$ 

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

AND  c.estado IN ('Atendida','No Asistió'); 

SELECT COUNT(*) INTO v_ausentes 

FROM   cita c JOIN turno t ON c.id_turno = t.id_turno 

WHERE  t.id_medico = p_id_medico 

**Desarrollo de Software** 

AND  t.fecha BETWEEN p_fecha_desde AND p_fecha_hasta 

AND  c.estado = 'No Asistió'; 

IF v_total = 0 THEN RETURN 0.00; END IF; 

RETURN ROUND((v_ausentes / v_total) * 100, 2); 

## END$$ 

DELIMITER ; 

- -- ============================================================ -- VISTAS 

- -- ============================================================ -- ------------------------------------------------------------- VISTA 1: Citas del día -- ------------------------------------------------------------ 

CREATE OR REPLACE VIEW vista_citas_del_dia AS 

## SELECT 

c.id_cita, 

CONCAT(p.nombres, ' ', p.apellidos) AS nombre_paciente, p.dni, 

p.telefono, CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico, e.nombre                            AS especialidad, t.fecha, 

t.hora_inicio, t.hora_fin, c.estado FROM cita c 

JOIN paciente   p ON c.id_paciente   = p.id_paciente 

**Desarrollo de Software** 

JOIN turno      t ON c.id_turno      = t.id_turno 

JOIN medico     m ON t.id_medico     = m.id_medico 

JOIN especialidad e ON m.id_especialidad = e.id_especialidad 

WHERE t.fecha = CURDATE() 

ORDER BY t.hora_inicio; 

-- ------------------------------------------------------------ 

-- VISTA 2: Turnos disponibles para reserva 

-- ------------------------------------------------------------ 

CREATE OR REPLACE VIEW vista_turnos_disponibles AS 

SELECT 

t.id_turno, m.id_medico, CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico, e.id_especialidad, e.nombre   AS especialidad, t.fecha, t.hora_inicio, t.hora_fin FROM turno      t JOIN medico     m ON t.id_medico         = m.id_medico JOIN especialidad e ON m.id_especialidad = e.id_especialidad 

WHERE t.estado = 'Libre' AND t.fecha  >= CURDATE() AND m.activo  = 1 ORDER BY t.fecha, t.hora_inicio; 

-- ------------------------------------------------------------ 

**Desarrollo de Software** 

-- VISTA 3: Dashboard administrativo 

-- ------------------------------------------------------------ 

CREATE OR REPLACE VIEW vista_dashboard_admin AS 

SELECT 

t.fecha, COUNT(c.id_cita)                                         AS total_citas, SUM(c.estado = 'Programada')                             AS programadas, SUM(c.estado = 'Atendida')                               AS atendidas, SUM(c.estado = 'No Asistió')                             AS inasistencias, SUM(c.estado = 'Cancelada')                              AS canceladas, 

ROUND(SUM(c.estado = 'No Asistió') / COUNT(*) * 100, 1) AS tasa_ausentismo_pct FROM cita  c 

JOIN turno t ON c.id_turno = t.id_turno 

GROUP BY t.fecha 

ORDER BY t.fecha DESC; 

-- ------------------------------------------------------------ 

-- VISTA 4: Notificaciones pendientes de envío 

-- ------------------------------------------------------------ 

CREATE OR REPLACE VIEW vista_notificaciones_pendientes AS SELECT 

n.id_notificacion, n.tipo, n.fecha_programada, c.id_cita, p.correo            AS correo_paciente, CONCAT(p.nombres, ' ', p.apellidos) AS nombre_paciente, CONCAT(m.nombres, ' ', m.apellidos) AS nombre_medico, 

**Desarrollo de Software** 

e.nombre            AS especialidad, 

t.fecha             AS fecha_cita, t.hora_inicio       AS hora_cita 

FROM notificacion n JOIN cita       c ON n.id_cita       = c.id_cita JOIN paciente   p ON c.id_paciente   = p.id_paciente JOIN turno      t ON c.id_turno      = t.id_turno JOIN medico     m ON t.id_medico     = m.id_medico JOIN especialidad e ON m.id_especialidad = e.id_especialidad WHERE n.estado_envio    = 'Pendiente' AND n.fecha_programada <= NOW() AND c.estado           = 'Programada'; 

## **5. Prototipos de la aplicación web** 

DZ— UNIVERSIDADCIENCIAS DEY **Desarrollo de Software** 

**==> picture [379 x 28] intentionally omitted <==**

**----- Start of picture text -----**<br>
Desarrollo de Software<br><q = U ( il CIENCIAS Y<br>**----- End of picture text -----**<br>


https://drive.google.com/drive/folders/1v7veNJkZrgib8Pg5ykdlDGnVnObi9DKN?usp=drive_li nk 

**Desarrollo de Software** 

## **6. Conclusiones Y Recomendaciones** 

## **6.1. Conclusiones del proyecto:** 

El proyecto concluyó con éxito la implementación de un **Sistema Web de Autogestión de Citas** que transforma el proceso misional de la Clínica Aviva: 

- **Eliminación de Fricciones Operativas:** Se logró eliminar la saturación del canal telefónico mediante un portal de autoservicio 24/7. 

- **Fundamento Tecnológico:** La migración de registros manuales a una infraestructura robusta (Spring Boot y MySQL) garantiza la centralización, integridad y seguridad de los datos. 

- **Reducción de Ausentismo y Optimización:** La automatización de notificaciones y recordatorios aborda directamente la problemática del ausentismo del 30-35%, optimizando la utilización de consultorios y recursos médicos. 

- **Alineación Estratégica:** La herramienta moderniza la gestión de citas y alinea la capacidad operativa de la clínica con su objetivo estratégico de crecimiento y mejora en la calidad del servicio. 

## **6.2. Recomendaciones del proyecto:** 

Para asegurar el crecimiento adecuado y maximizar la efectividad de la solución, se sugieren las siguientes acciones estratégicas: 

- **Pagos en Línea:** Incorporar un sistema de pagos en línea para el pago anticipado, lo que reducirá aún más las tasas de cancelación. 

- **Integración con Aseguradoras:** Planificar la capacidad del sistema para trabajar en conjunto con las plataformas de las aseguradoras. 

- **Telemedicina:** Incluir un módulo de telemedicina para incrementar el número de consultas disponibles. 

- **Aplicación Móvil Nativa:** Crear una aplicación móvil nativa para mejorar la experiencia de usuario. 

- **Análisis Avanzado de Datos:** Añadir herramientas avanzadas de análisis de datos para prever aumentos en la demanda según la especialidad. 

- **Formación Continua:** Implementar un programa de formación continua para el personal médico y administrativo. 

