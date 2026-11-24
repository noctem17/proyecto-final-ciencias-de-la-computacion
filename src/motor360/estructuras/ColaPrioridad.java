package motor360.estructuras;

/**
 * Cola de prioridad genérica implementada como una lista enlazada que se
 * mantiene siempre ordenada. Cada elemento nuevo se inserta justo antes del
 * primer elemento que tenga menos prioridad que él, de modo que el frente
 * siempre contiene el elemento más urgente. Entre elementos de igual
 * prioridad se respeta el orden de llegada, porque el nuevo queda detrás de
 * los que ya estaban.
 * <p>
 * El orden lo define el método {@code compareTo} de los elementos: un valor
 * negativo significa que el elemento debe ser atendido antes que el otro.
 * Se usa para las solicitudes críticas o prioritarias.
 *
 * @param <T> tipo de los elementos almacenados, que deben ser comparables
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class ColaPrioridad<T extends Comparable<T>> implements Secuencia<T> {

	/** Nodo del elemento más prioritario. */
	private Nodo<T> frente;

	/** Cantidad de elementos almacenados. */
	private int tamano;

	/**
	 * Crea una cola de prioridad vacía.
	 */
	public ColaPrioridad() {
		frente = null;
		tamano = 0;
	}

	/**
	 * Inserta un elemento en la posición que le corresponde según su
	 * prioridad.
	 *
	 * @param elemento elemento a insertar
	 */
	public void insertar(T elemento) {
		Nodo<T> nuevo = new Nodo<>(elemento);
		if (frente == null || elemento.compareTo(frente.getDato()) < 0) {
			nuevo.setSiguiente(frente);
			frente = nuevo;
		} else {
			Nodo<T> actual = frente;
			while (actual.getSiguiente() != null
					&& actual.getSiguiente().getDato().compareTo(elemento) <= 0) {
				actual = actual.getSiguiente();
			}
			nuevo.setSiguiente(actual.getSiguiente());
			actual.setSiguiente(nuevo);
		}
		tamano++;
	}

	/**
	 * Retira y devuelve el elemento más prioritario.
	 *
	 * @return el elemento del frente
	 * @throws IllegalStateException si la cola está vacía
	 */
	public T extraer() {
		if (frente == null) {
			throw new IllegalStateException("La cola de prioridad está vacía");
		}
		T dato = frente.getDato();
		frente = frente.getSiguiente();
		tamano--;
		return dato;
	}

	/**
	 * Consulta el elemento más prioritario sin retirarlo.
	 *
	 * @return el elemento del frente, o {@code null} si la cola está vacía
	 */
	public T verFrente() {
		return frente == null ? null : frente.getDato();
	}

	/**
	 * Retira un elemento específico, comparando por identidad de objeto.
	 * Se usa al deshacer el registro o el escalamiento de una solicitud.
	 *
	 * @param elemento elemento a retirar
	 * @return {@code true} si el elemento estaba en la cola y fue retirado
	 */
	public boolean eliminar(T elemento) {
		Nodo<T> anterior = null;
		Nodo<T> actual = frente;
		while (actual != null && actual.getDato() != elemento) {
			anterior = actual;
			actual = actual.getSiguiente();
		}
		if (actual == null) {
			return false;
		}
		if (anterior == null) {
			frente = actual.getSiguiente();
		} else {
			anterior.setSiguiente(actual.getSiguiente());
		}
		tamano--;
		return true;
	}

	/**
	 * Indica si un elemento está en la cola de prioridad.
	 *
	 * @param elemento elemento buscado
	 * @return {@code true} si el elemento está en la cola
	 */
	public boolean contiene(T elemento) {
		Nodo<T> actual = frente;
		while (actual != null) {
			if (actual.getDato() == elemento) {
				return true;
			}
			actual = actual.getSiguiente();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T obtener(int posicion) {
		if (posicion < 0 || posicion >= tamano) {
			throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
		}
		Nodo<T> actual = frente;
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
