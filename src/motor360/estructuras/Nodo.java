package motor360.estructuras;

/**
 * Nodo de una estructura enlazada simple. Guarda un dato y la referencia al
 * nodo que le sigue. Es la pieza básica con la que se construyen la lista, la
 * cola, la cola de prioridad y la pila del sistema.
 *
 * @param <T> tipo del dato almacenado
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Nodo<T> {

	/** Dato almacenado en el nodo. */
	private T dato;

	/** Referencia al nodo siguiente, o {@code null} si es el último. */
	private Nodo<T> siguiente;

	/**
	 * Crea un nodo sin nodo siguiente.
	 *
	 * @param dato dato que se almacenará en el nodo
	 */
	public Nodo(T dato) {
		this.dato = dato;
		this.siguiente = null;
	}

	/**
	 * Obtiene el dato almacenado.
	 *
	 * @return el dato del nodo
	 */
	public T getDato() {
		return dato;
	}

	/**
	 * Reemplaza el dato almacenado.
	 *
	 * @param dato nuevo dato del nodo
	 */
	public void setDato(T dato) {
		this.dato = dato;
	}

	/**
	 * Obtiene el nodo siguiente.
	 *
	 * @return el nodo siguiente, o {@code null} si no existe
	 */
	public Nodo<T> getSiguiente() {
		return siguiente;
	}

	/**
	 * Enlaza este nodo con otro nodo siguiente.
	 *
	 * @param siguiente nodo que quedará después de este
	 */
	public void setSiguiente(Nodo<T> siguiente) {
		this.siguiente = siguiente;
	}
}
