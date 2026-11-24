package motor360.vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidades para mostrar fechas y valores de forma legible en la interfaz.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public final class Formato {

	/** Formato de fecha y hora completa. */
	private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/** Formato de solo hora. */
	private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

	/**
	 * Constructor privado: la clase solo ofrece métodos estáticos.
	 */
	private Formato() {
	}

	/**
	 * Convierte una fecha y hora en texto.
	 *
	 * @param fecha fecha a mostrar
	 * @return texto con fecha y hora, o vacío si la fecha es nula
	 */
	public static String fechaHora(LocalDateTime fecha) {
		return fecha == null ? "" : fecha.format(FECHA_HORA);
	}

	/**
	 * Convierte una fecha y hora en texto con solo la hora.
	 *
	 * @param fecha fecha a mostrar
	 * @return texto con la hora, o vacío si la fecha es nula
	 */
	public static String hora(LocalDateTime fecha) {
		return fecha == null ? "" : fecha.format(HORA);
	}

	/**
	 * Convierte un valor lógico en Sí o No.
	 *
	 * @param valor valor lógico
	 * @return "Sí" si es verdadero, "No" si es falso
	 */
	public static String siNo(boolean valor) {
		return valor ? "Sí" : "No";
	}

	/**
	 * Muestra un objeto como texto, o un guion si es nulo.
	 *
	 * @param objeto objeto a mostrar
	 * @return el texto del objeto, o "-" si es nulo
	 */
	public static String texto(Object objeto) {
		return objeto == null ? "-" : objeto.toString();
	}
}
