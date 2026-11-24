package motor360.controlador;

import motor360.modelo.ReglaNegocioException;

/**
 * Acción del usuario que el controlador ejecuta sobre el modelo y que puede
 * fallar por incumplir una regla de negocio.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
@FunctionalInterface
public interface Operacion {

	/**
	 * Ejecuta la acción.
	 *
	 * @return mensaje para la barra de estado cuando la acción tiene éxito
	 * @throws ReglaNegocioException si la acción incumple una regla de negocio
	 */
	String ejecutar() throws ReglaNegocioException;
}
