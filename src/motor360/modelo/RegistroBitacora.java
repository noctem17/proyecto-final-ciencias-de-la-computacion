package motor360.modelo;

import java.time.LocalDateTime;

/**
 * Entrada del historial de acciones del sistema. A diferencia de la pila de
 * deshacer, la bitácora nunca se borra: registra tanto las operaciones como
 * las reversiones, para que el supervisor pueda auditar todo lo ocurrido.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class RegistroBitacora {

	/** Momento en que ocurrió la acción. */
	private final LocalDateTime fecha;

	/** Clase de acción: operación, reversión o sistema. */
	private final String tipo;

	/** Descripción de la acción. */
	private final String descripcion;

	/**
	 * Crea un registro con la fecha actual.
	 *
	 * @param tipo        clase de acción
	 * @param descripcion descripción de la acción
	 */
	public RegistroBitacora(String tipo, String descripcion) {
		this.fecha = LocalDateTime.now();
		this.tipo = tipo;
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene el momento de la acción.
	 *
	 * @return fecha y hora
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}

	/**
	 * Obtiene la clase de acción.
	 *
	 * @return tipo de la acción
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Obtiene la descripción de la acción.
	 *
	 * @return descripción
	 */
	public String getDescripcion() {
		return descripcion;
	}
}
