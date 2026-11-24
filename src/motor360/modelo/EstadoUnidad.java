package motor360.modelo;

/**
 * Estatus operativo de una unidad móvil. Como cada unidad tiene un único
 * estatus a la vez, es imposible que figure como disponible y asignada al
 * mismo tiempo.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public enum EstadoUnidad {

	/** Lista para un nuevo despacho. */
	DISPONIBLE("Disponible"),

	/** Operando en un servicio. */
	ASIGNADA("Asignada"),

	/** En el taller; no puede asignarse a ningún caso. */
	MANTENIMIENTO("En mantenimiento");

	/** Nombre legible del estatus. */
	private final String nombre;

	/**
	 * Crea el estatus con su nombre legible.
	 *
	 * @param nombre nombre que se muestra al usuario
	 */
	EstadoUnidad(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el nombre legible del estatus.
	 *
	 * @return nombre del estatus
	 */
	@Override
	public String toString() {
		return nombre;
	}
}
