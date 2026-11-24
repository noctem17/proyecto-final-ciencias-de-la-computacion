package motor360.estructuras;

/**
 * Pila genérica LIFO (último en entrar, primero en salir) implementada con
 * nodos enlazados. Todos los elementos entran y salen por la cima. Se usa
 * para los maletines que esperan auditoría, para los repuestos de alta
 * rotación y para el historial de transacciones que se pueden deshacer.
 *
 * @param <T> tipo de los elementos almacenados
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Pila<T> implements Secuencia<T> {

	/** Nodo del elemento que está en la cima. */
	private Nodo<T> cima;

	/** Cantidad de elementos apilados. */
	private int tamano;

	/**
	 * Crea una pila vacía.
	 */
	public Pila() {
		cima = null;
		tamano = 0;
	}

	/**
	 * Coloca un elemento en la cima de la pila.
	 *
	 * @param elemento elemento a apilar
	 */
	public void apilar(T elemento) {
		Nodo<T> nuevo = new Nodo<>(elemento);
		nuevo.setSiguiente(cima);
		cima = nuevo;
		tamano++;
	}

	/**
	 * Retira y devuelve el elemento de la cima, es decir, el último que fue
	 * apilado.
	 *
	 * @return el elemento de la cima
	 * @throws IllegalStateException si la pila está vacía
	 */
	public T desapilar() {
		if (cima == null) {
			throw new IllegalStateException("La pila está vacía");
		}
		T dato = cima.getDato();
		cima = cima.getSiguiente();
		tamano--;
		return dato;
	}

	/**
	 * Consulta el elemento de la cima sin retirarlo.
	 *
	 * @return el elemento de la cima, o {@code null} si la pila está vacía
	 */
	public T verCima() {
		return cima == null ? null : cima.getDato();
	}

	/**
	 * Elimina todos los elementos de la pila.
	 */
	public void vaciar() {
		cima = null;
		tamano = 0;
	}

	/**
	 * Obtiene el elemento de una posición contando desde la cima: la
	 * posición 0 es la cima.
	 *
	 * @param posicion posición contando desde la cima
	 * @return el elemento ubicado en la posición
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	@Override
	public T obtener(int posicion) {
		if (posicion < 0 || posicion >= tamano) {
			throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
		}
		Nodo<T> actual = cima;
		for (int i = 0; i < posicion; i++) {
			actual = actual.getSiguiente();
		}
		return actual.getDato();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int tamano() {
		return tamano;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estaVacia() {
		return tamano == 0;
	}
}
