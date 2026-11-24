package motor360.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.Duration;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import motor360.estructuras.Secuencia;
import motor360.modelo.Solicitud;

/**
 * Pestaña de monitoreo de rescates: muestra los servicios activos, permite
 * marcarlos como atendidos o solucionados y lista las incidencias
 * solucionadas, que pueden exportarse a CSV.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelServicios extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Botón para marcar como atendido el servicio seleccionado. */
	private final JButton btnAtendida;

	/** Botón para marcar como solucionado el servicio seleccionado. */
	private final JButton btnSolucionar;

	/** Botón para exportar el reporte del día. */
	private final JButton btnExportar;

	/** Modelo de la tabla de servicios activos. */
	private final ModeloTabla<Solicitud> modeloActivos;

	/** Tabla de servicios activos. */
	private final JTable tablaActivos;

	/** Modelo de la tabla de incidencias solucionadas. */
	private final ModeloTabla<Solicitud> modeloSolucionados;

	/**
	 * Construye la pestaña con sus dos tablas y acciones.
	 */
	public PanelServicios() {
		modeloActivos = new ModeloTabla<>(
				new String[] { "Código", "Cliente", "Incidente", "Zona", "Ubicación", "Puntaje", "Prioritaria", "Estado",
						"Unidad", "Técnico", "Maletín" },
				(s, i) -> new Object[] { s.getCodigo(), s.getCliente().getNombre(), s.getTipoIncidente(), s.getZona(),
						s.getUbicacion(), s.getPuntaje(), Formato.siNo(s.isPrioritaria()), s.getEstado(),
						s.getUnidad() == null ? "-" : s.getUnidad().getPlaca(),
						s.getTecnico() == null ? "-" : s.getTecnico().getNombre(),
						s.getMaletin() == null ? "Sin maletín" : s.getMaletin().getCodigo() });
		tablaActivos = crearTabla(modeloActivos);

		modeloSolucionados = new ModeloTabla<>(
				new String[] { "Código", "Cliente", "Incidente", "Puntaje", "Unidad", "Técnico", "Registro", "Solución",
						"Duración (min)" },
				(s, i) -> new Object[] { s.getCodigo(), s.getCliente().getNombre(), s.getTipoIncidente(),
						s.getPuntaje(), s.getUnidad().getPlaca(), s.getTecnico().getNombre(),
						Formato.fechaHora(s.getFechaRegistro()), Formato.fechaHora(s.getFechaSolucion()),
						s.getFechaSolucion() == null ? "-"
								: Duration.between(s.getFechaRegistro(), s.getFechaSolucion()).toMinutes() });
		JTable tablaSolucionados = crearTabla(modeloSolucionados);

		btnAtendida = new JButton("Marcar como atendida");
		btnSolucionar = new JButton("Marcar como solucionada");
		btnExportar = new JButton("Exportar reporte CSV del día...");

		JPanel superior = new JPanel(new BorderLayout());
		superior.add(envolver(tablaActivos, "Rescates activos"), BorderLayout.CENTER);
		superior.add(filaDe(btnAtendida, btnSolucionar), BorderLayout.SOUTH);

		JPanel inferior = new JPanel(new BorderLayout());
		inferior.add(envolver(tablaSolucionados, "Incidencias solucionadas"), BorderLayout.CENTER);
		inferior.add(filaDe(btnExportar), BorderLayout.SOUTH);

		JPanel centro = new JPanel(new GridLayout(2, 1, 0, 8));
		centro.add(superior);
		centro.add(inferior);
		add(centro, BorderLayout.CENTER);
	}

	/**
	 * Muestra los servicios activos y las incidencias solucionadas.
	 *
	 * @param activos      servicios en curso
	 * @param solucionados incidencias solucionadas
	 */
	public void mostrarServicios(Secuencia<Solicitud> activos, Secuencia<Solicitud> solucionados) {
		modeloActivos.setDatos(activos);
		modeloSolucionados.setDatos(solucionados);
	}

	/**
	 * Obtiene el servicio activo seleccionado.
	 *
	 * @return el servicio seleccionado, o {@code null} si no hay selección
	 */
	public Solicitud getServicioSeleccionado() {
		return modeloActivos.getElemento(tablaActivos.getSelectedRow());
	}

	/**
	 * Obtiene el botón para marcar atendida.
	 *
	 * @return botón atendida
	 */
	public JButton getBtnAtendida() {
		return btnAtendida;
	}

	/**
	 * Obtiene el botón para marcar solucionada.
	 *
	 * @return botón solucionar
	 */
	public JButton getBtnSolucionar() {
		return btnSolucionar;
	}

	/**
	 * Obtiene el botón de exportación.
	 *
	 * @return botón exportar
	 */
	public JButton getBtnExportar() {
		return btnExportar;
	}
}
