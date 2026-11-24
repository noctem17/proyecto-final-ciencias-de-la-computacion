# Asistencia Motor 360 — Centro de Control

Proyecto final de Ciencias de la Computación I — Universidad Distrital Francisco José de Caldas.

Sistema de escritorio (Java + Swing, patrón MVC) para administrar la flota de rescate, el personal técnico, las solicitudes de asistencia vial (cola regular y cola prioritaria), los rescates activos, los insumos (pilas de auditoría y de repuestos), el historial con deshacer y el reporte diario en CSV.

Todas las estructuras de datos (lista enlazada, cola, cola de prioridad y pila) están implementadas desde cero en el paquete `motor360.estructuras`; no se usa el framework de colecciones de Java.

## Integrantes

- Reynel Felipe Amezquita Puentes — 20252020056
- Nancy Gabriela Jaimes Luna — 20252020055
- Cristian Camilo Velosa Ortiz — 20252020066

## Cómo abrirlo en Eclipse

1. Clonar el repositorio o descargarlo como ZIP y descomprimirlo.
2. En Eclipse: **File → Import → General → Existing Projects into Workspace**.
3. En *Select root directory* elegir la carpeta del repositorio. Aparece el proyecto **AsistenciaMotor360**. Pulsar **Finish**.
4. Abrir `src/motor360/Aplicacion.java` → clic derecho → **Run As → Java Application**.

Requiere Java 17 o superior.

## Estructura

```
src/motor360/
├── Aplicacion.java      punto de entrada (main)
├── estructuras/         lista enlazada, cola, cola de prioridad, pila
├── modelo/              entidades, reglas de negocio, deshacer, CSV
├── vista/               ventana Swing, pestañas y tablas
└── controlador/         conecta la vista con el modelo
doc/                     JavaDoc generado (abrir doc/index.html)
entrega/                 requisitos funcionales y diagrama de clases (PDF)
```
