package motor360.estructuras;

/**
 * Cola genérica FIFO (primero en entrar, primero en salir) implementada con
 * nodos enlazados. Los elementos entran por el final y salen por el frente.
 * Se usa para las solicitudes ordinarias, que deben atenderse estrictamente
 * en el orden en que llegaron.
 *
 * @param <T> tipo de los elementos almacenados
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Cola<T> implements Secuencia<T> {

	/** Nodo del elemento que será atendido primero. */
	private Nodo<T> frente;

	/** Nodo del último elemento que llegó. */
	private Nodo<T> fin;

	/** Cantidad de elementos en la cola. */
	private int tamano;

	/**
	 * Crea una cola vacía.
	 */
	public Cola() {
		frente = null;
		fin = null;
		tamano = 0;
	}

	/**
	 * Agrega un elemento al final de la cola.
	 *
	 * @param elemento elemento que llega a la cola
	 */
	public void encolar(T elemento) {
		Nodo<T> nuevo = new Nodo<>(elemento);
		if (fin == null) {
			frente = nuevo;
		} else {
			fin.setSiguiente(nuevo);
		}
		fin = nuevo;
		tamano++;
	}

	/**
	 * Retira y devuelve el elemento del frente de la cola.
	 *
	 * @return el elemento que llevaba más tiempo esperando
	 * @throws IllegalStateException si la cola está vacía
	 */
	public T desencolar() {
		if (frente == null) {
			throw new IllegalStateException("La cola está vacía");
		}
		T dato = frente.getDato();
		frente = frente.getSiguiente();
		if (frente == null) {
			fin = null;
		}
		tamano--;
		return dato;
	}

	/**
	 * Consulta el elemento del frente sin retirarlo.
	 *
	 * @return el elemento del frente, o {@code null} si la cola está vacía
	 */
	public T verFrente() {
		return frente == null ? null : frente.getDato();
	}

	/**
	 * Retira un elemento específico de cualquier posición de la cola,
	 * comparando por identidad de objeto. Se usa cuando una solicitud
	 * ordinaria es escalada a prioritaria o cuando se deshace su registro.
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
		if (actual == fin) {
			fin = anterior;
		}
		tamano--;
		return true;
	}

	/**
	 * Busca la posición de un elemento dentro de la cola.
	 *
	 * @param elemento elemento buscado
	 * @return la posición contando desde el frente, o -1 si no está
	 */
	public int indiceDe(T elemento) {
		Nodo<T> actual = frente;
		int posicion = 0;
		while (actual != null) {
			if (actual.getDato() == elemento) {
				return posicion;
			}
			actual = actual.getSiguiente();
			posicion++;
		}
		return -1;
	}

	/**
	 * Devuelve un elemento a la posición exacta que ocupaba antes. Es de uso
	 * exclusivo de la operación deshacer, para que una solicitud recupere su
	 * turno original y no pierda su lugar en la fila.
	 *
	 * @param posicion posición que ocupaba el elemento, entre 0 y tamano()
	 * @param elemento elemento a devolver
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	public void reinsertarEn(int posicion, T elemento) {
		if (posicion < 0 || posicion > tamano) {
			throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
		}
		if (posicion == tamano) {
			encolar(elemento);
			return;
		}
		Nodo<T> nuevo = new Nodo<>(elemento);
		if (posicion == 0) {
			nuevo.setSiguiente(frente);
			frente = nuevo;
		} else {
			Nodo<T> anterior = frente;
			for (int i = 0; i < posicion - 1; i++) {
				anterior = anterior.getSiguiente();
			}
			nuevo.setSiguiente(anterior.getSiguiente());
			anterior.setSiguiente(nuevo);
		}
		tamano++;
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
