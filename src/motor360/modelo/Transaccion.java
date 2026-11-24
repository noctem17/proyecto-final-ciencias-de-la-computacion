package motor360.modelo;

import java.time.LocalDateTime;

/**
 * Operación ejecutada en el sistema que puede deshacerse. Se guarda en la
 * pila de deshacer: como la última transacción apilada es la primera en
 * salir, las operaciones se revierten en el orden inverso en que se
 * realizaron.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Transaccion {

	/** Descripción de lo que hizo la operación. */
	private final String descripcion;

	/** Momento en que se ejecutó la operación. */
	private final LocalDateTime fecha;

	/** Instrucciones para revertir la operación. */
	private final Reversion reversion;

	/**
	 * Crea una transacción con la fecha actual.
	 *
	 * @param descripcion descripción de la operación
	 * @param reversion   instrucciones para revertirla
	 */
	public Transaccion(String descripcion, Reversion reversion) {
		this.descripcion = descripcion;
		this.reversion = reversion;
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Revierte la operación.
	 */
	public void deshacer() {
		reversion.revertir();
	}

	/**
	 * Obtiene la descripción de la operación.
	 *
	 * @return descripción
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Obtiene el momento de ejecución.
	 *
	 * @return fecha y hora
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}
}
