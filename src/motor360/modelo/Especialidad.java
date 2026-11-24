package motor360.modelo;

/**
 * Áreas de especialidad del personal técnico.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum Especialidad {

	/** Reparación de averías mecánicas. */
	MECANICA("Mecánica general"),

	/** Sistema eléctrico y recarga de batería. */
	ELECTRICIDAD("Electricidad y baterías"),

	/** Cambio y reparación de llantas. */
	LLANTAS("Llantas y pinchazos"),

	/** Operación de grúa y remolque. */
	REMOLQUE("Remolque con grúa"),

	/** Apertura de vehículos y cerrajería. */
	CERRAJERIA("Cerrajería vehicular"),

	/** Entrega de combustible. */
	COMBUSTIBLE("Entrega de combustible"),

	/** Diagnóstico preventivo en sitio. */
	DIAGNOSTICO("Diagnóstico preventivo");

	/** Nombre legible de la especialidad. */
	private final String nombre;

	/**
	 * Crea la especialidad con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	Especialidad(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible de la especialidad.
	 *
	 * @return nombre de la especialidad
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
