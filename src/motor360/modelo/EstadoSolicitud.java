package motor360.modelo;

/**
 * Etapas por las que pasa una solicitud de asistencia, en este orden:
 * pendiente, asignada, atendida y solucionada.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum EstadoSolicitud {

	/** En espera dentro de la cola regular o de la cola prioritaria. */
	PENDIENTE("Pendiente"),

	/** Con unidad y técnico asignados, en camino al sitio. */
	ASIGNADA("Asignada"),

	/** El técnico está atendiendo el incidente en el sitio. */
	ATENDIDA("Atendida"),

	/** El incidente fue solucionado y el servicio se cerró. */
	SOLUCIONADA("Solucionada");

	/** Nombre legible del estado. */
	private final String nombre;

	/**
	 * Crea el estado con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	EstadoSolicitud(String nombre) {
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
