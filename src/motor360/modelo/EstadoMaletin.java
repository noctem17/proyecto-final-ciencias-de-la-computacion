package motor360.modelo;

/**
 * Situación de un maletín de respuesta rápida.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum EstadoMaletin {

	/** En el almacén, listo para salir en un servicio. */
	DISPONIBLE("Disponible"),

	/** Asignado a un servicio en curso. */
	EN_USO("En uso"),

	/** Apilado en la estación de auditoría esperando revisión. */
	EN_AUDITORIA("En auditoría");

	/** Nombre legible del estado. */
	private final String nombre;

	/**
	 * Crea el estado con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	EstadoMaletin(String nombre) {
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
