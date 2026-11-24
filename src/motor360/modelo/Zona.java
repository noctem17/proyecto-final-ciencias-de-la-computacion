package motor360.modelo;

/**
 * Sectores de la ciudad y su periferia cubiertos por la empresa. Se usan como
 * zona de operación de las unidades, sector de patrullaje de los técnicos y
 * lugar de los incidentes.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum Zona {

	/** Sector norte de la ciudad. */
	NORTE("Norte"),

	/** Sector sur de la ciudad. */
	SUR("Sur"),

	/** Sector oriental de la ciudad. */
	ORIENTE("Oriente"),

	/** Sector occidental de la ciudad. */
	OCCIDENTE("Occidente"),

	/** Centro de la ciudad. */
	CENTRO("Centro"),

	/** Periferia y vías de acceso a la ciudad. */
	PERIFERIA("Periferia");

	/** Nombre legible de la zona. */
	private final String nombre;

	/**
	 * Crea la zona con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	Zona(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible de la zona.
	 *
	 * @return nombre de la zona
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
