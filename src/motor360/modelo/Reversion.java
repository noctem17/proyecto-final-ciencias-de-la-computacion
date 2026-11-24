package motor360.modelo;

/**
 * Instrucciones para revertir una transacción. Cada operación que modifica
 * datos guarda, junto con su descripción, la forma exacta de devolver todo a
 * como estaba antes.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
@FunctionalInterface
public interface Reversion {

	/**
	 * Devuelve los datos al estado que tenían antes de la transacción.
	 */
	void revertir();
}
