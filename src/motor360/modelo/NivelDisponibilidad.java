package motor360.modelo;

/**
 * Nivel de disponibilidad de una unidad: indica qué tanta capacidad operativa
 * le queda (combustible, equipo y autonomía) para responder a un servicio.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum NivelDisponibilidad {

	/** Capacidad completa para cualquier servicio. */
	ALTO("Alto"),

	/** Capacidad suficiente para servicios cercanos. */
	MEDIO("Medio"),

	/** Capacidad reducida; conviene reabastecer pronto. */
	BAJO("Bajo");

	/** Nombre legible del nivel. */
	private final String nombre;

	/**
	 * Crea el nivel con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	NivelDisponibilidad(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible del nivel.
	 *
	 * @return nombre del nivel
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
