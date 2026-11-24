package motor360.vista;

import javax.swing.table.AbstractTableModel;

import motor360.estructuras.Secuencia;

/**
 * Modelo de tabla genérico que muestra en un {@code JTable} cualquier
 * estructura propia que cumpla {@link Secuencia}: una lista, una cola, una
 * cola de prioridad o una pila. Extiende {@link AbstractTableModel}, de modo
 * que la tabla lee los datos directamente de las estructuras propias del
 * sistema.
 *
 * @param <T> tipo de los elementos mostrados
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class ModeloTabla<T> extends AbstractTableModel {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Nombres de las columnas. */
	private final String[] columnas;

	/** Forma de convertir cada elemento en una fila. */
	private final ExtractorFila<T> extractor;

	/** Estructura de datos que se muestra. */
	private Secuencia<T> datos;

	/**
	 * Crea un modelo de tabla sin datos.
	 *
	 * @param columnas  nombres de las columnas
	 * @param extractor forma de convertir cada elemento en una fila
	 */
	public ModeloTabla(String[] columnas, ExtractorFila<T> extractor) {
		this.columnas = columnas;
		this.extractor = extractor;
		this.datos = null;
	}

	/**
	 * Cambia la estructura mostrada y redibuja la tabla.
	 *
	 * @param datos estructura de datos a mostrar
	 */
	public void setDatos(Secuencia<T> datos) {
		this.datos = datos;
		fireTableDataChanged();
	}

	/**
	 * Obtiene el elemento que se muestra en una fila.
	 *
	 * @param fila índice de la fila
	 * @return el elemento de la fila, o {@code null} si la fila no existe
	 */
	public T getElemento(int fila) {
		if (datos == null || fila < 0 || fila >= datos.tamano()) {
			return null;
		}
		return datos.obtener(fila);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowCount() {
		return datos == null ? 0 : datos.tamano();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(int columna) {
		return columnas[columna];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int fila, int columna) {
		Object[] valores = extractor.extraer(datos.obtener(fila), fila);
		return valores[columna];
	}
}
