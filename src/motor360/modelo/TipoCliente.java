package motor360.modelo;

/**
 * Perfil del cliente que solicita la asistencia. Cada perfil tiene un peso
 * entre 1 y 5 que participa en el cálculo de la prioridad de la solicitud.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum TipoCliente {

	/** Conductor particular. */
	PARTICULAR("Conductor particular", 1),

	/** Compañía de seguros. */
	ASEGURADORA("Compañía de seguros", 2),

	/** Flota de transporte de carga o empresarial. */
	FLOTA("Flota de transporte", 3),

	/** Transporte público de pasajeros. */
	TRANSPORTE_PUBLICO("Transporte público de pasajeros", 4),

	/** Vehículo de emergencia, como una ambulancia. */
	EMERGENCIA("Vehículo de emergencia", 5);

	/** Nombre legible del perfil. */
	private final String nombre;

	/** Peso del perfil en el cálculo de prioridad, de 1 a 5. */
	private final int peso;

	/**
	 * Crea el perfil con su nombre y su peso.
	 *
	 * @param nombre nombre que se muestra al usuario
	 * @param peso   peso del perfil en el cálculo de prioridad
	 */
	TipoCliente(String nombre, int peso) {
		this.nombre = nombre;
		this.peso = peso;
	}

	/**
	 * Obtiene el peso del perfil en el cálculo de prioridad.
	 *
	 * @return peso entre 1 y 5
	 */
	public int getPeso() {
		return peso;
	}

	/**
	 * Devuelve el nombre legible del perfil.
	 *
	 * @return nombre del perfil
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
