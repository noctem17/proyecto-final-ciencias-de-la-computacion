package motor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Clase base de las pestañas de la ventana principal. Ofrece métodos para
 * construir formularios, tablas y filas de botones con un estilo uniforme.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public abstract class PanelBase extends JPanel {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea el panel con distribución de bordes y un margen interno.
	 */
	protected PanelBase() {
		super(new BorderLayout(8, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	}

	/**
	 * Crea un panel de formulario con título.
	 *
	 * @param titulo título del formulario
	 * @return el panel del formulario
	 */
	protected static JPanel crearFormulario(String titulo) {
		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setBorder(BorderFactory.createTitledBorder(titulo));
		return formulario;
	}

	/**
	 * Agrega una etiqueta y su campo en una fila del formulario.
	 *
	 * @param formulario panel del formulario
	 * @param fila       número de fila
	 * @param etiqueta   texto de la etiqueta
	 * @param campo      componente de entrada
	 */
	protected static void agregarCampo(JPanel formulario, int fila, String etiqueta, JComponent campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = fila;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(4, 4, 4, 4);
		formulario.add(new JLabel(etiqueta), c);
		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		formulario.add(campo, c);
	}

	/**
	 * Agrega un componente que ocupa las dos columnas del formulario.
	 *
	 * @param formulario panel del formulario
	 * @param fila       número de fila
	 * @param componente componente a agregar
	 */
	protected static void agregarFilaCompleta(JPanel formulario, int fila, JComponent componente) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = fila;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(8, 4, 4, 4);
		formulario.add(componente, c);
	}

	/**
	 * Agrega un relleno al final del formulario para que los campos queden
	 * arriba.
	 *
	 * @param formulario panel del formulario
	 * @param fila       número de fila donde va el relleno
	 */
	protected static void agregarRelleno(JPanel formulario, int fila) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = fila;
		c.weighty = 1;
		formulario.add(new JLabel(), c);
	}

	/**
	 * Crea una tabla de solo lectura y selección simple.
	 *
	 * @param modelo modelo de datos de la tabla
	 * @return la tabla creada
	 */
	protected static JTable crearTabla(ModeloTabla<?> modelo) {
		JTable tabla = new JTable(modelo);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setFillsViewportHeight(true);
		tabla.setRowHeight(22);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setDefaultRenderer(Object.class, new RenderizadorEstado());
		return tabla;
	}

	/**
	 * Envuelve una tabla en un panel con barras de desplazamiento y título.
	 *
	 * @param tabla  tabla a envolver
	 * @param titulo título del panel
	 * @return el panel con la tabla
	 */
	protected static JScrollPane envolver(JTable tabla, String titulo) {
		JScrollPane panel = new JScrollPane(tabla);
		panel.setBorder(BorderFactory.createTitledBorder(titulo));
		return panel;
	}

	/**
	 * Crea una fila horizontal de componentes alineados a la izquierda.
	 *
	 * @param componentes componentes de la fila
	 * @return el panel con los componentes
	 */
	protected static JPanel filaDe(JComponent... componentes) {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
		for (JComponent componente : componentes) {
			fila.add(componente);
		}
		return fila;
	}
}
