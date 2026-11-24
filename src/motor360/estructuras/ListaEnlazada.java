package motor360.estructuras;

/**
 * Lista enlazada simple genérica implementada desde cero. Mantiene una
 * referencia al primer nodo (cabeza) y al último nodo (ultimo) para poder
 * agregar al final en tiempo constante. Se usa para los inventarios de
 * unidades, técnicos, clientes y maletines, para los servicios activos, los
 * servicios solucionados y la bitácora de acciones.
 *
 * @param <T> tipo de los elementos almacenados
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class ListaEnlazada<T> implements Secuencia<T> {

	/** Primer nodo de la lista. */
	private Nodo<T> cabeza;

	/** Último nodo de la lista. */
	private Nodo<T> ultimo;

	/** Cantidad de elementos almacenados. */
	private int tamano;

	/**
	 * Crea una lista vacía.
	 */
	public ListaEnlazada() {
		cabeza = null;
		ultimo = null;
		tamano = 0;
	}

	/**
	 * Agrega un elemento al final de la lista.
	 *
	 * @param elemento elemento a agregar
	 */
	public void agregar(T elemento) {
		Nodo<T> nuevo = new Nodo<>(elemento);
		if (cabeza == null) {
			cabeza = nuevo;
		} else {
			ultimo.setSiguiente(nuevo);
		}
		ultimo = nuevo;
		tamano++;
	}

	/**
	 * Agrega un elemento al inicio de la lista.
	 *
	 * @param elemento elemento a agregar
	 */
	public void agregarAlInicio(T elemento) {
		Nodo<T> nuevo = new Nodo<>(elemento);
		nuevo.setSiguiente(cabeza);
		cabeza = nuevo;
		if (ultimo == null) {
			ultimo = nuevo;
		}
		tamano++;
	}

	/**
	 * Inserta un elemento en una posición determinada, desplazando hacia
	 * adelante los elementos que estaban desde esa posición.
	 *
	 * @param posicion posición donde quedará el elemento, entre 0 y tamano()
	 * @param elemento elemento a insertar
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	public void insertarEn(int posicion, T elemento) {
		if (posicion < 0 || posicion > tamano) {
			throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
		}
		if (posicion == 0) {
			agregarAlInicio(elemento);
			return;
		}
		if (posicion == tamano) {
			agregar(elemento);
			return;
		}
		Nodo<T> anterior = nodoEn(posicion - 1);
		Nodo<T> nuevo = new Nodo<>(elemento);
		nuevo.setSiguiente(anterior.getSiguiente());
		anterior.setSiguiente(nuevo);
		tamano++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T obtener(int posicion) {
		validarPosicion(posicion);
		return nodoEn(posicion).getDato();
	}

	/**
	 * Elimina el elemento ubicado en una posición.
	 *
	 * @param posicion posición del elemento a eliminar
	 * @return el elemento eliminado
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	public T eliminarEn(int posicion) {
		validarPosicion(posicion);
		Nodo<T> eliminado;
		if (posicion == 0) {
			eliminado = cabeza;
			cabeza = cabeza.getSiguiente();
			if (cabeza == null) {
				ultimo = null;
			}
		} else {
			Nodo<T> anterior = nodoEn(posicion - 1);
			eliminado = anterior.getSiguiente();
			anterior.setSiguiente(eliminado.getSiguiente());
			if (eliminado == ultimo) {
				ultimo = anterior;
			}
		}
		tamano--;
		return eliminado.getDato();
	}

	/**
	 * Elimina la primera aparición de un elemento, comparando por identidad
	 * de objeto.
	 *
	 * @param elemento elemento a eliminar
	 * @return {@code true} si el elemento estaba en la lista y fue eliminado
	 */
	public boolean eliminar(T elemento) {
		int posicion = indiceDe(elemento);
		if (posicion < 0) {
			return false;
		}
		eliminarEn(posicion);
		return true;
	}

	/**
	 * Busca la posición de un elemento comparando por identidad de objeto.
	 *
	 * @param elemento elemento buscado
	 * @return la posición del elemento, o -1 si no está en la lista
	 */
	public int indiceDe(T elemento) {
		Nodo<T> actual = cabeza;
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
	 * Indica si un elemento está en la lista.
	 *
	 * @param elemento elemento buscado
	 * @return {@code true} si el elemento está en la lista
	 */
	public boolean contiene(T elemento) {
		return indiceDe(elemento) >= 0;
	}

	/**
	 * Busca el primer elemento que cumple un criterio.
	 *
	 * @param criterio condición que debe cumplir el elemento
	 * @return el primer elemento que cumple el criterio, o {@code null} si
	 *         ninguno lo cumple
	 */
	public T buscar(Criterio<T> criterio) {
		Nodo<T> actual = cabeza;
		while (actual != null) {
			if (criterio.cumple(actual.getDato())) {
				return actual.getDato();
			}
			actual = actual.getSiguiente();
		}
		return null;
	}

	/**
	 * Construye una nueva lista con los elementos que cumplen un criterio,
	 * conservando su orden original.
	 *
	 * @param criterio condición que deben cumplir los elementos
	 * @return una nueva lista con los elementos que cumplen el criterio
	 */
	public ListaEnlazada<T> filtrar(Criterio<T> criterio) {
		ListaEnlazada<T> resultado = new ListaEnlazada<>();
		Nodo<T> actual = cabeza;
		while (actual != null) {
			if (criterio.cumple(actual.getDato())) {
				resultado.agregar(actual.getDato());
			}
			actual = actual.getSiguiente();
		}
		return resultado;
	}

	/**
	 * Cuenta cuántos elementos cumplen un criterio.
	 *
	 * @param criterio condición que deben cumplir los elementos
	 * @return número de elementos que cumplen el criterio
	 */
	public int contar(Criterio<T> criterio) {
		int cantidad = 0;
		Nodo<T> actual = cabeza;
		while (actual != null) {
			if (criterio.cumple(actual.getDato())) {
				cantidad++;
			}
			actual = actual.getSiguiente();
		}
		return cantidad;
	}

	/**
	 * Elimina todos los elementos de la lista.
	 */
	public void vaciar() {
		cabeza = null;
		ultimo = null;
		tamano = 0;
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

	/**
	 * Recorre la lista hasta el nodo de una posición.
	 *
	 * @param posicion posición del nodo buscado
	 * @return el nodo ubicado en la posición
	 */
	private Nodo<T> nodoEn(int posicion) {
		Nodo<T> actual = cabeza;
		for (int i = 0; i < posicion; i++) {
			actual = actual.getSiguiente();
		}
		return actual;
	}

	/**
	 * Verifica que una posición corresponda a un elemento existente.
	 *
	 * @param posicion posición a verificar
	 * @throws IndexOutOfBoundsException si la posición no es válida
	 */
	private void validarPosicion(int posicion) {
		if (posicion < 0 || posicion >= tamano) {
			throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
		}
	}
}
