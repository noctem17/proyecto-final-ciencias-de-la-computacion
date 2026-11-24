package motor360.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import motor360.estructuras.Secuencia;
import motor360.modelo.EstadoUnidad;
import motor360.modelo.NivelDisponibilidad;
import motor360.modelo.TipoUnidad;
import motor360.modelo.Unidad;
import motor360.modelo.Zona;

/**
 * Pestaña de gestión de la flota: registro de unidades móviles, inventario
 * completo y cambio de estatus o nivel de disponibilidad.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelUnidades extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Campo de la placa. */
	private final JTextField txtPlaca;

	/** Selector de la categoría. */
	private final JComboBox<TipoUnidad> cmbTipo;

	/** Selector de la zona de operación. */
	private final JComboBox<Zona> cmbZona;

	/** Selector del nivel de disponibilidad. */
	private final JComboBox<NivelDisponibilidad> cmbNivel;

	/** Botón para registrar la unidad. */
	private final JButton btnRegistrar;

	/** Selector del nuevo estatus para la unidad seleccionada. */
	private final JComboBox<EstadoUnidad> cmbNuevoEstado;

	/** Botón para cambiar el estatus. */
	private final JButton btnCambiarEstado;

	/** Selector del nuevo nivel para la unidad seleccionada. */
	private final JComboBox<NivelDisponibilidad> cmbNuevoNivel;

	/** Botón para cambiar el nivel. */
	private final JButton btnCambiarNivel;

	/** Modelo de la tabla de unidades. */
	private final ModeloTabla<Unidad> modelo;

	/** Tabla de unidades. */
	private final JTable tabla;

	/**
	 * Construye la pestaña con su formulario, tabla y acciones.
	 */
	public PanelUnidades() {
		txtPlaca = new JTextField(12);
		cmbTipo = new JComboBox<>(TipoUnidad.values());
		cmbZona = new JComboBox<>(Zona.values());
		cmbNivel = new JComboBox<>(NivelDisponibilidad.values());
		btnRegistrar = new JButton("Registrar unidad");

		JPanel formulario = crearFormulario("Nueva unidad móvil");
		agregarCampo(formulario, 0, "Placa:", txtPlaca);
		agregarCampo(formulario, 1, "Categoría:", cmbTipo);
		agregarCampo(formulario, 2, "Zona de operación:", cmbZona);
		agregarCampo(formulario, 3, "Nivel de disponibilidad:", cmbNivel);
		agregarFilaCompleta(formulario, 4, btnRegistrar);
		agregarFilaCompleta(formulario, 5, new JLabel("<html>El UUID se genera automáticamente.</html>"));
		agregarRelleno(formulario, 6);
		formulario.setPreferredSize(new Dimension(300, 0));

		modelo = new ModeloTabla<>(
				new String[] { "UUID", "Placa", "Categoría", "Estatus", "Zona", "Nivel de disponibilidad" },
				(u, i) -> new Object[] { u.getId(), u.getPlaca(), u.getTipo(), u.getEstado(), u.getZona(),
						u.getNivelDisponibilidad() });
		tabla = crearTabla(modelo);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(260);

		cmbNuevoEstado = new JComboBox<>(new EstadoUnidad[] { EstadoUnidad.DISPONIBLE, EstadoUnidad.MANTENIMIENTO });
		btnCambiarEstado = new JButton("Cambiar estatus");
		cmbNuevoNivel = new JComboBox<>(NivelDisponibilidad.values());
		btnCambiarNivel = new JButton("Cambiar nivel");

		JPanel acciones = filaDe(new JLabel("Unidad seleccionada -> nuevo estatus:"), cmbNuevoEstado,
				btnCambiarEstado, new JLabel("   nuevo nivel:"), cmbNuevoNivel, btnCambiarNivel);

		add(formulario, BorderLayout.WEST);
		add(envolver(tabla, "Inventario de unidades móviles"), BorderLayout.CENTER);
		add(acciones, BorderLayout.SOUTH);
	}

	/**
	 * Muestra el inventario de unidades.
	 *
	 * @param unidades unidades registradas
	 */
	public void mostrarUnidades(Secuencia<Unidad> unidades) {
		modelo.setDatos(unidades);
	}

	/**
	 * Obtiene la unidad seleccionada en la tabla.
	 *
	 * @return la unidad seleccionada, o {@code null} si no hay selección
	 */
	public Unidad getUnidadSeleccionada() {
		return modelo.getElemento(tabla.getSelectedRow());
	}

	/**
	 * Limpia el formulario de registro.
	 */
	public void limpiarFormulario() {
		txtPlaca.setText("");
		cmbTipo.setSelectedIndex(0);
		cmbZona.setSelectedIndex(0);
		cmbNivel.setSelectedIndex(0);
		txtPlaca.requestFocusInWindow();
	}

	/**
	 * Obtiene la placa escrita.
	 *
	 * @return texto del campo placa
	 */
	public String getPlaca() {
		return txtPlaca.getText();
	}

	/**
	 * Obtiene la categoría elegida.
	 *
	 * @return categoría seleccionada
	 */
	public TipoUnidad getTipo() {
		return (TipoUnidad) cmbTipo.getSelectedItem();
	}

	/**
	 * Obtiene la zona elegida.
	 *
	 * @return zona seleccionada
	 */
	public Zona getZona() {
		return (Zona) cmbZona.getSelectedItem();
	}

	/**
	 * Obtiene el nivel elegido en el formulario.
	 *
	 * @return nivel seleccionado
	 */
	public NivelDisponibilidad getNivel() {
		return (NivelDisponibilidad) cmbNivel.getSelectedItem();
	}

	/**
	 * Obtiene el nuevo estatus elegido.
	 *
	 * @return estatus seleccionado
	 */
	public EstadoUnidad getNuevoEstado() {
		return (EstadoUnidad) cmbNuevoEstado.getSelectedItem();
	}

	/**
	 * Obtiene el nuevo nivel elegido.
	 *
	 * @return nivel seleccionado
	 */
	public NivelDisponibilidad getNuevoNivel() {
		return (NivelDisponibilidad) cmbNuevoNivel.getSelectedItem();
	}

	/**
	 * Obtiene el botón de registro.
	 *
	 * @return botón registrar
	 */
	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}

	/**
	 * Obtiene el botón de cambio de estatus.
	 *
	 * @return botón cambiar estatus
	 */
	public JButton getBtnCambiarEstado() {
		return btnCambiarEstado;
	}

	/**
	 * Obtiene el botón de cambio de nivel.
	 *
	 * @return botón cambiar nivel
	 */
	public JButton getBtnCambiarNivel() {
		return btnCambiarNivel;
	}
}
