package motor360.modelo;

/**
 * Situación actual de un técnico.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum EstadoTecnico {

	/** Libre y listo para recibir un servicio. */
	DISPONIBLE("Libre"),

	/** Atendiendo un siniestro. */
	EN_SERVICIO("En atención de siniestro"),

	/** Fuera de su turno de trabajo. */
	FUERA_DE_TURNO("Fuera de turno");

	/** Nombre legible del estado. */
	private final String nombre;

	/**
	 * Crea el estado con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	EstadoTecnico(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible del estado.
	 *
	 * @return nombre del estado
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
