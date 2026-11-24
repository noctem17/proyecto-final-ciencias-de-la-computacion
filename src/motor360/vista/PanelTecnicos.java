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
import motor360.modelo.Especialidad;
import motor360.modelo.EstadoTecnico;
import motor360.modelo.Tecnico;
import motor360.modelo.Zona;

/**
 * Pestaña de gestión del personal técnico: registro, listado y cambio de
 * estado.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelTecnicos extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Campo de la identificación. */
	private final JTextField txtIdentificacion;

	/** Campo del nombre. */
	private final JTextField txtNombre;

	/** Selector de la especialidad. */
	private final JComboBox<Especialidad> cmbEspecialidad;

	/** Selector del sector de patrullaje. */
	private final JComboBox<Zona> cmbZona;

	/** Botón para registrar el técnico. */
	private final JButton btnRegistrar;

	/** Selector del nuevo estado. */
	private final JComboBox<EstadoTecnico> cmbNuevoEstado;

	/** Botón para cambiar el estado. */
	private final JButton btnCambiarEstado;

	/** Modelo de la tabla de técnicos. */
	private final ModeloTabla<Tecnico> modelo;

	/** Tabla de técnicos. */
	private final JTable tabla;

	/**
	 * Construye la pestaña con su formulario, tabla y acciones.
	 */
	public PanelTecnicos() {
		txtIdentificacion = new JTextField(12);
		txtNombre = new JTextField(12);
		cmbEspecialidad = new JComboBox<>(Especialidad.values());
		cmbZona = new JComboBox<>(Zona.values());
		btnRegistrar = new JButton("Registrar técnico");

		JPanel formulario = crearFormulario("Nuevo técnico");
		agregarCampo(formulario, 0, "Identificación:", txtIdentificacion);
		agregarCampo(formulario, 1, "Nombre completo:", txtNombre);
		agregarCampo(formulario, 2, "Especialidad:", cmbEspecialidad);
		agregarCampo(formulario, 3, "Sector de patrullaje:", cmbZona);
		agregarFilaCompleta(formulario, 4, btnRegistrar);
		agregarRelleno(formulario, 5);
		formulario.setPreferredSize(new Dimension(300, 0));

		modelo = new ModeloTabla<>(
				new String[] { "Identificación", "Nombre", "Especialidad", "Estado", "Sector", "Servicio activo" },
				(t, i) -> new Object[] { t.getIdentificacion(), t.getNombre(), t.getEspecialidad(), t.getEstado(),
						t.getZona(), t.tieneServicioActivo() ? t.getServicioActivo().getCodigo() : "-" });
		tabla = crearTabla(modelo);

		cmbNuevoEstado = new JComboBox<>(new EstadoTecnico[] { EstadoTecnico.DISPONIBLE, EstadoTecnico.FUERA_DE_TURNO });
		btnCambiarEstado = new JButton("Cambiar estado");

		add(formulario, BorderLayout.WEST);
		add(envolver(tabla, "Personal técnico"), BorderLayout.CENTER);
		add(filaDe(new JLabel("Técnico seleccionado -> nuevo estado:"), cmbNuevoEstado, btnCambiarEstado),
				BorderLayout.SOUTH);
	}

	/**
	 * Muestra el personal técnico.
	 *
	 * @param tecnicos técnicos registrados
	 */
	public void mostrarTecnicos(Secuencia<Tecnico> tecnicos) {
		modelo.setDatos(tecnicos);
	}

	/**
	 * Obtiene el técnico seleccionado en la tabla.
	 *
	 * @return el técnico seleccionado, o {@code null} si no hay selección
	 */
	public Tecnico getTecnicoSeleccionado() {
		return modelo.getElemento(tabla.getSelectedRow());
	}

	/**
	 * Limpia el formulario de registro.
	 */
	public void limpiarFormulario() {
		txtIdentificacion.setText("");
		txtNombre.setText("");
		cmbEspecialidad.setSelectedIndex(0);
		cmbZona.setSelectedIndex(0);
		txtIdentificacion.requestFocusInWindow();
	}

	/**
	 * Obtiene la identificación escrita.
	 *
	 * @return texto del campo identificación
	 */
	public String getIdentificacion() {
		return txtIdentificacion.getText();
	}

	/**
	 * Obtiene el nombre escrito.
	 *
	 * @return texto del campo nombre
	 */
	public String getNombre() {
		return txtNombre.getText();
	}

	/**
	 * Obtiene la especialidad elegida.
	 *
	 * @return especialidad seleccionada
	 */
	public Especialidad getEspecialidad() {
		return (Especialidad) cmbEspecialidad.getSelectedItem();
	}

	/**
	 * Obtiene el sector elegido.
	 *
	 * @return zona seleccionada
	 */
	public Zona getZona() {
		return (Zona) cmbZona.getSelectedItem();
	}

	/**
	 * Obtiene el nuevo estado elegido.
	 *
	 * @return estado seleccionado
	 */
	public EstadoTecnico getNuevoEstado() {
		return (EstadoTecnico) cmbNuevoEstado.getSelectedItem();
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
	 * Obtiene el botón de cambio de estado.
	 *
	 * @return botón cambiar estado
	 */
	public JButton getBtnCambiarEstado() {
		return btnCambiarEstado;
	}
}
