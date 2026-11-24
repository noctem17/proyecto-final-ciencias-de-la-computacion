package motor360.modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

import motor360.estructuras.Secuencia;

/**
 * Genera el reporte de cierre de jornada en un archivo plano CSV, con una
 * fila por cada incidencia solucionada. El archivo se escribe en UTF-8 con
 * marca de orden de bytes para que las tildes se vean bien en Excel.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public final class ExportadorCSV {

	/** Separador de columnas del archivo. */
	public static final char SEPARADOR = ',';

	/** Formato de fecha y hora usado en el reporte. */
	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/** Nombres de las columnas del reporte. */
	private static final String[] ENCABEZADOS = { "Codigo", "Turno", "Fecha registro", "Fecha solucion",
			"Duracion (min)", "Documento cliente", "Cliente", "Tipo cliente", "Telefono", "Tipo incidente", "Zona",
			"Ubicacion", "Descripcion", "Riesgo", "Afectacion vial", "Severidad", "Puntaje", "Prioritaria",
			"Escalada manualmente", "UUID unidad", "Placa unidad", "Tipo unidad", "Identificacion tecnico", "Tecnico",
			"Especialidad", "Maletin" };

	/**
	 * Constructor privado: la clase solo ofrece métodos estáticos.
	 */
	private ExportadorCSV() {
	}

	/**
	 * Escribe el reporte con las incidencias indicadas.
	 *
	 * @param solicitudes incidencias solucionadas que se van a exportar
	 * @param archivo     archivo de destino; se sobrescribe si ya existe
	 * @throws IOException si no se puede escribir el archivo
	 */
	public static void exportar(Secuencia<Solicitud> solicitudes, File archivo) throws IOException {
		try (BufferedWriter escritor = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
			escritor.write('﻿');
			escritor.write(unirFila(ENCABEZADOS));
			escritor.newLine();
			for (int i = 0; i < solicitudes.tamano(); i++) {
				escritor.write(unirFila(convertirFila(solicitudes.obtener(i))));
				escritor.newLine();
			}
		}
	}

	/**
	 * Convierte una incidencia en los valores de una fila del reporte.
	 *
	 * @param s incidencia solucionada
	 * @return valores de la fila, en el mismo orden de los encabezados
	 */
	private static String[] convertirFila(Solicitud s) {
		Cliente c = s.getCliente();
		Unidad u = s.getUnidad();
		Tecnico t = s.getTecnico();
		Maletin m = s.getMaletin();
		long minutos = s.getFechaSolucion() == null ? 0
				: Duration.between(s.getFechaRegistro(), s.getFechaSolucion()).toMinutes();
		return new String[] { s.getCodigo(), String.valueOf(s.getNumeroLlegada()),
				s.getFechaRegistro().format(FORMATO),
				s.getFechaSolucion() == null ? "" : s.getFechaSolucion().format(FORMATO), String.valueOf(minutos),
				c.getDocumento(), c.getNombre(), c.getTipo().toString(), c.getTelefono(),
				s.getTipoIncidente().toString(), s.getZona().toString(), s.getUbicacion(), s.getDescripcion(),
				String.valueOf(s.getNivelRiesgo()), String.valueOf(s.getAfectacionVial()),
				String.valueOf(s.getSeveridad()), String.valueOf(s.getPuntaje()), s.isPrioritaria() ? "Si" : "No",
				s.isEscaladaManualmente() ? "Si" : "No", u == null ? "" : u.getId().toString(),
				u == null ? "" : u.getPlaca(), u == null ? "" : u.getTipo().toString(),
				t == null ? "" : t.getIdentificacion(), t == null ? "" : t.getNombre(),
				t == null ? "" : t.getEspecialidad().toString(), m == null ? "Sin maletín" : m.getCodigo() };
	}

	/**
	 * Une los valores de una fila con el separador, escapando cada uno.
	 *
	 * @param valores valores de la fila
	 * @return la línea de texto lista para escribir
	 */
	private static String unirFila(String[] valores) {
		StringBuilder linea = new StringBuilder();
		for (int i = 0; i < valores.length; i++) {
			if (i > 0) {
				linea.append(SEPARADOR);
			}
			linea.append(escapar(valores[i]));
		}
		return linea.toString();
	}

	/**
	 * Escapa un valor según el formato CSV: si contiene el separador, comillas
	 * o saltos de línea, se encierra entre comillas y las comillas internas se
	 * duplican.
	 *
	 * @param valor valor original
	 * @return valor listo para el archivo CSV
	 */
	private static String escapar(String valor) {
		if (valor == null) {
			return "";
		}
		boolean requiereComillas = valor.indexOf(SEPARADOR) >= 0 || valor.indexOf('"') >= 0
				|| valor.indexOf('\n') >= 0 || valor.indexOf('\r') >= 0;
		if (!requiereComillas) {
			return valor;
		}
		return "\"" + valor.replace("\"", "\"\"") + "\"";
	}
}
