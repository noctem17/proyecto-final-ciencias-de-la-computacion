package motor360.vista;

/**
 * Indica cómo convertir un elemento en los valores de una fila de tabla.
 * Cada tabla de la interfaz define su propio extractor con las columnas que
 * quiere mostrar.
 *
 * @param <T> tipo del elemento mostrado en la tabla
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
@FunctionalInterface
public interface ExtractorFila<T> {

	/**
	 * Obtiene los valores de la fila correspondiente a un elemento.
	 *
	 * @param elemento elemento a mostrar
	 * @param posicion posición del elemento dentro de su estructura
	 * @return valores de cada columna, en orden
	 */
	Object[] extraer(T elemento, int posicion);
}
