package motor360.modelo;

/**
 * Categorías de unidades móviles que conforman la flota de rescate.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum TipoUnidad {

	/** Grúa para remolque de vehículos. */
	GRUA("Grúa"),

	/** Motocicleta de respuesta rápida. */
	MOTOCICLETA("Motocicleta de respuesta rápida"),

	/** Vehículo utilitario con herramienta y equipo. */
	UTILITARIO("Vehículo utilitario"),

	/** Coche ligero para servicios menores. */
	COCHE_LIGERO("Coche ligero");

	/** Nombre legible de la categoría. */
	private final String nombre;

	/**
	 * Crea la categoría con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	TipoUnidad(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible de la categoría.
	 *
	 * @return nombre de la categoría
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
