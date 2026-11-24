package motor360.modelo;

import java.time.LocalDateTime;

/**
 * Repuesto de alta rotación preparado con anticipación en los picos de
 * trabajo. Los repuestos se acomodan en una pila: el último acomodado es el
 * primero que se despacha.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Repuesto {

	/** Código consecutivo del repuesto. */
	private final String codigo;

	/** Nombre del repuesto. */
	private final String nombre;

	/** Momento en que el repuesto fue acomodado en la pila. */
	private final LocalDateTime fechaIngreso;

	/**
	 * Crea un repuesto registrando el momento actual como su ingreso.
	 *
	 * @param codigo código consecutivo
	 * @param nombre nombre del repuesto
	 */
	public Repuesto(String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.fechaIngreso = LocalDateTime.now();
	}

	/**
	 * Obtiene el código del repuesto.
	 *
	 * @return código del repuesto
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Obtiene el nombre del repuesto.
	 *
	 * @return nombre del repuesto
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el momento en que el repuesto fue acomodado.
	 *
	 * @return fecha y hora de ingreso
	 */
	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * Devuelve un texto corto que identifica el repuesto.
	 *
	 * @return código y nombre del repuesto
	 */
	@Override
	public String toString() {
		return codigo + " - " + nombre;
	}
}
