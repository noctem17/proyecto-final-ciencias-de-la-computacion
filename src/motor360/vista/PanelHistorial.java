package motor360.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import motor360.estructuras.Secuencia;
import motor360.modelo.RegistroBitacora;
import motor360.modelo.Transaccion;

/**
 * Pestaña del supervisor: muestra la pila de transacciones que pueden
 * deshacerse y la bitácora completa de acciones del sistema.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelHistorial extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Botón para deshacer la última transacción. */
	private final JButton btnDeshacer;

	/** Modelo de la tabla de la pila de deshacer. */
	private final ModeloTabla<Transaccion> modeloDeshacer;

	/** Modelo de la tabla de la bitácora. */
	private final ModeloTabla<RegistroBitacora> modeloBitacora;

	/**
	 * Construye la pestaña con sus dos tablas.
	 */
	public PanelHistorial() {
		modeloDeshacer = new ModeloTabla<>(new String[] { "Posición", "Hora", "Transacción" },
				(t, i) -> new Object[] { i == 0 ? "CIMA" : String.valueOf(i + 1), Formato.hora(t.getFecha()),
						t.getDescripcion() });
		JTable tablaDeshacer = crearTabla(modeloDeshacer);
		tablaDeshacer.getColumnModel().getColumn(2).setPreferredWidth(600);

		modeloBitacora = new ModeloTabla<>(new String[] { "Fecha y hora", "Tipo", "Acción" },
				(r, i) -> new Object[] { Formato.fechaHora(r.getFecha()), r.getTipo(), r.getDescripcion() });
		JTable tablaBitacora = crearTabla(modeloBitacora);
		tablaBitacora.getColumnModel().getColumn(2).setPreferredWidth(600);

		btnDeshacer = new JButton("Deshacer última transacción (Ctrl+Z)");

		JPanel superior = new JPanel(new BorderLayout());
		superior.add(envolver(tablaDeshacer, "Pila de transacciones que se pueden deshacer (la cima se deshace primero)"),
				BorderLayout.CENTER);
		superior.add(filaDe(btnDeshacer), BorderLayout.SOUTH);

		JPanel centro = new JPanel(new GridLayout(2, 1, 0, 8));
		centro.add(superior);
		centro.add(envolver(tablaBitacora, "Historial reciente de acciones (la más reciente arriba)"));
		add(centro, BorderLayout.CENTER);
	}

	/**
	 * Muestra la pila de deshacer y la bitácora.
	 *
	 * @param pilaDeshacer transacciones que pueden deshacerse
	 * @param bitacora     historial de acciones
	 */
	public void mostrarHistorial(Secuencia<Transaccion> pilaDeshacer, Secuencia<RegistroBitacora> bitacora) {
		modeloDeshacer.setDatos(pilaDeshacer);
		modeloBitacora.setDatos(bitacora);
	}

	/**
	 * Obtiene el botón para deshacer.
	 *
	 * @return botón deshacer
	 */
	public JButton getBtnDeshacer() {
		return btnDeshacer;
	}
}
