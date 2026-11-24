package motor360.estructuras;

/**
 * Condición que puede cumplir o no un elemento. Se usa para buscar y filtrar
 * elementos dentro de una {@link ListaEnlazada} sin depender de ninguna
 * interfaz del framework de colecciones de Java.
 *
 * @param <T> tipo del elemento evaluado
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
@FunctionalInterface
public interface Criterio<T> {

	/**
	 * Evalúa si el elemento cumple la condición.
	 *
	 * @param elemento elemento a evaluar
	 * @return {@code true} si el elemento cumple la condición
	 */
	boolean cumple(T elemento);
}
