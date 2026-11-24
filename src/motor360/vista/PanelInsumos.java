package motor360.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import motor360.estructuras.Secuencia;
import motor360.modelo.Maletin;
import motor360.modelo.Repuesto;

/**
 * Pestaña del departamento de insumos: inventario de maletines, pila de
 * maletines que esperan auditoría y pila de repuestos de alta rotación.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelInsumos extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Campo del código del maletín. */
	private final JTextField txtCodigoMaletin;

	/** Campo del contenido del maletín. */
	private final JTextField txtContenidoMaletin;

	/** Botón para registrar un maletín. */
	private final JButton btnRegistrarMaletin;

	/** Casilla para indicar que la auditoría reemplaza una pieza. */
	private final JCheckBox chkReemplazarPieza;

	/** Botón para auditar el maletín de la cima. */
	private final JButton btnAuditar;

	/** Campo del nombre del repuesto. */
	private final JTextField txtRepuesto;

	/** Botón para apilar un repuesto. */
	private final JButton btnApilarRepuesto;

	/** Botón para despachar el repuesto de la cima. */
	private final JButton btnDespacharRepuesto;

	/** Modelo de la tabla de maletines. */
	private final ModeloTabla<Maletin> modeloMaletines;

	/** Modelo de la tabla de la pila de auditoría. */
	private final ModeloTabla<Maletin> modeloAuditoria;

	/** Modelo de la tabla de la pila de repuestos. */
	private final ModeloTabla<Repuesto> modeloRepuestos;

	/**
	 * Construye la pestaña con sus tres secciones.
	 */
	public PanelInsumos() {
		txtCodigoMaletin = new JTextField(8);
		txtContenidoMaletin = new JTextField(18);
		btnRegistrarMaletin = new JButton("Registrar maletín");
		modeloMaletines = new ModeloTabla<>(
				new String[] { "Código", "Contenido", "Estado", "Auditorías", "Piezas reemplazadas" },
				(m, i) -> new Object[] { m.getCodigo(), m.getContenido(), m.getEstado(), m.getAuditorias(),
						m.getPiezasReemplazadas() });
		JPanel inventario = new JPanel(new BorderLayout());
		inventario.add(filaDe(new JLabel("Código:"), txtCodigoMaletin, new JLabel("Contenido:"), txtContenidoMaletin,
				btnRegistrarMaletin), BorderLayout.NORTH);
		inventario.add(envolver(crearTabla(modeloMaletines), "Inventario de maletines de respuesta rápida"),
				BorderLayout.CENTER);

		chkReemplazarPieza = new JCheckBox("Reemplazar una pieza con el repuesto de la cima");
		btnAuditar = new JButton("Auditar maletín de la cima");
		modeloAuditoria = new ModeloTabla<>(new String[] { "Posición", "Código", "Contenido", "Estado" },
				(m, i) -> new Object[] { i == 0 ? "CIMA" : String.valueOf(i + 1), m.getCodigo(), m.getContenido(),
						m.getEstado() });
		JPanel auditoria = new JPanel(new BorderLayout());
		auditoria.add(envolver(crearTabla(modeloAuditoria), "Pila de auditoría (se revisa primero el último apilado)"),
				BorderLayout.CENTER);
		auditoria.add(filaDe(btnAuditar, chkReemplazarPieza), BorderLayout.SOUTH);

		txtRepuesto = new JTextField(16);
		btnApilarRepuesto = new JButton("Apilar repuesto");
		btnDespacharRepuesto = new JButton("Despachar repuesto de la cima");
		modeloRepuestos = new ModeloTabla<>(new String[] { "Posición", "Código", "Repuesto", "Ingreso" },
				(r, i) -> new Object[] { i == 0 ? "CIMA" : String.valueOf(i + 1), r.getCodigo(), r.getNombre(),
						Formato.hora(r.getFechaIngreso()) });
		JPanel repuestos = new JPanel(new BorderLayout());
		repuestos.add(envolver(crearTabla(modeloRepuestos), "Pila de repuestos de alta rotación (último en entrar, primero en salir)"),
				BorderLayout.CENTER);
		repuestos.add(filaDe(new JLabel("Repuesto:"), txtRepuesto, btnApilarRepuesto, btnDespacharRepuesto),
				BorderLayout.SOUTH);

		JPanel pilas = new JPanel(new GridLayout(2, 1, 0, 8));
		pilas.add(auditoria);
		pilas.add(repuestos);

		JPanel centro = new JPanel(new GridLayout(1, 2, 8, 0));
		centro.add(inventario);
		centro.add(pilas);
		add(centro, BorderLayout.CENTER);
	}

	/**
	 * Muestra el inventario de maletines y las dos pilas.
	 *
	 * @param maletines inventario de maletines
	 * @param auditoria pila de maletines en auditoría
	 * @param repuestos pila de repuestos
	 */
	public void mostrarInsumos(Secuencia<Maletin> maletines, Secuencia<Maletin> auditoria,
			Secuencia<Repuesto> repuestos) {
		modeloMaletines.setDatos(maletines);
		modeloAuditoria.setDatos(auditoria);
		modeloRepuestos.setDatos(repuestos);
	}

	/**
	 * Limpia el formulario de maletines.
	 */
	public void limpiarMaletin() {
		txtCodigoMaletin.setText("");
		txtContenidoMaletin.setText("");
	}

	/**
	 * Limpia el campo de repuesto.
	 */
	public void limpiarRepuesto() {
		txtRepuesto.setText("");
	}

	/**
	 * Obtiene el código del maletín escrito.
	 *
	 * @return texto del campo código
	 */
	public String getCodigoMaletin() {
		return txtCodigoMaletin.getText();
	}

	/**
	 * Obtiene el contenido del maletín escrito.
	 *
	 * @return texto del campo contenido
	 */
	public String getContenidoMaletin() {
		return txtContenidoMaletin.getText();
	}

	/**
	 * Indica si la auditoría debe reemplazar una pieza.
	 *
	 * @return {@code true} si la casilla está marcada
	 */
	public boolean isReemplazarPieza() {
		return chkReemplazarPieza.isSelected();
	}

	/**
	 * Obtiene el nombre del repuesto escrito.
	 *
	 * @return texto del campo repuesto
	 */
	public String getNombreRepuesto() {
		return txtRepuesto.getText();
	}

	/**
	 * Obtiene el botón de registro de maletín.
	 *
	 * @return botón registrar maletín
	 */
	public JButton getBtnRegistrarMaletin() {
		return btnRegistrarMaletin;
	}

	/**
	 * Obtiene el botón de auditoría.
	 *
	 * @return botón auditar
	 */
	public JButton getBtnAuditar() {
		return btnAuditar;
	}

	/**
	 * Obtiene el botón para apilar repuestos.
	 *
	 * @return botón apilar
	 */
	public JButton getBtnApilarRepuesto() {
		return btnApilarRepuesto;
	}

	/**
	 * Obtiene el botón para despachar repuestos.
	 *
	 * @return botón despachar
	 */
	public JButton getBtnDespacharRepuesto() {
		return btnDespacharRepuesto;
	}
}
