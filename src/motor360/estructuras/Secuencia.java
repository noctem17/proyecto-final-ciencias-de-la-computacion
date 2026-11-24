package motor360.estructuras;

/**
 * Contrato común que cumplen todas las estructuras de datos propias del
 * sistema. Permite consultar cuántos elementos hay y obtener cualquiera de
 * ellos por su posición, sin modificar la estructura. Gracias a este contrato
 * la vista puede mostrar en una tabla una lista, una cola o una pila de la
 * misma manera.
 *
 * @param <T> tipo de los elementos almacenados
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public interface Secuencia<T> {

	/**
	 * Indica la cantidad de elementos almacenados.
	 *
	 * @return número de elementos de la estructura
	 */
	int tamano();

	/**
	 * Indica si la estructura no contiene elementos.
	 *
	 * @return {@code true} si no hay elementos, {@code false} en caso contrario
	 */
	boolean estaVacia();

	/**
	 * Obtiene el elemento ubicado en una posición, contando desde cero a partir
	 * del elemento que sería atendido o extraído primero.
	 *
	 * @param posicion índice del elemento, entre 0 y tamano() - 1
	 * @return el elemento ubicado en la posición indicada
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	T obtener(int posicion);
}
