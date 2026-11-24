package motor360.modelo;

/**
 * Tipos de incidente que atiende la empresa.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum TipoIncidente {

	/** Avería mecánica. */
	AVERIA_MECANICA("Avería mecánica"),

	/** Pinchazo de llanta. */
	PINCHAZO("Pinchazo"),

	/** Batería descargada que requiere recarga. */
	BATERIA("Recarga de batería"),

	/** Vehículo que debe ser remolcado con grúa. */
	REMOLQUE("Remolque con grúa"),

	/** Vehículo cerrado o llaves perdidas. */
	CERRAJERIA("Cerrajería vehicular"),

	/** Vehículo sin combustible. */
	COMBUSTIBLE("Entrega de combustible"),

	/** Revisión preventiva en el sitio. */
	DIAGNOSTICO("Diagnóstico preventivo");

	/** Nombre legible del tipo de incidente. */
	private final String nombre;

	/**
	 * Crea el tipo de incidente con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	TipoIncidente(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible del tipo de incidente.
	 *
	 * @return nombre del tipo de incidente
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
