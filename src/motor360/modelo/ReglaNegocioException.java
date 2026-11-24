package motor360.modelo;

/**
 * Excepción que lanza el modelo cuando una operación incumple alguna regla de
 * negocio de la empresa. El controlador la captura y la vista muestra su
 * mensaje al usuario.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class ReglaNegocioException extends Exception {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea la excepción con el mensaje que verá el usuario.
	 *
	 * @param mensaje explicación de la regla incumplida
	 */
	public ReglaNegocioException(String mensaje) {
		super(mensaje);
	}
}
