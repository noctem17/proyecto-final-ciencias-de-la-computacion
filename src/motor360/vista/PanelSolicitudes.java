package motor360.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import motor360.estructuras.Secuencia;
import motor360.modelo.Cliente;
import motor360.modelo.Solicitud;
import motor360.modelo.TipoIncidente;
import motor360.modelo.Zona;

/**
 * Pestaña de la mesa de ayuda: registro de solicitudes de asistencia y
 * visualización de la cola prioritaria y de la cola regular. Desde aquí se
 * escalan solicitudes ordinarias y se despacha la siguiente en turno.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelSolicitudes extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Columnas de las tablas de solicitudes en espera. */
	private static final String[] COLUMNAS = { "Pos.", "Código", "Turno", "Cliente", "Tipo de cliente", "Incidente",
			"Zona", "Riesgo", "Afectación", "Severidad", "Puntaje", "Registro", "Origen" };

	/** Selector del cliente. */
	private final JComboBox<Cliente> cmbCliente;

	/** Selector del tipo de incidente. */
	private final JComboBox<TipoIncidente> cmbIncidente;

	/** Selector de la zona del incidente. */
	private final JComboBox<Zona> cmbZona;

	/** Campo de la ubicación. */
	private final JTextField txtUbicacion;

	/** Campo de la descripción. */
	private final JTextField txtDescripcion;

	/** Selector del nivel de riesgo. */
	private final JSpinner spnRiesgo;

	/** Selector de la afectación vial. */
	private final JSpinner spnAfectacion;

	/** Selector de la severidad. */
	private final JSpinner spnSeveridad;

	/** Etiqueta con el puntaje estimado. */
	private final JLabel lblVistaPrevia;

	/** Botón para registrar la solicitud. */
	private final JButton btnRegistrar;

	/** Etiqueta con la siguiente solicitud en turno. */
	private final JLabel lblSiguiente;

	/** Botón para escalar la solicitud regular seleccionada. */
	private final JButton btnEscalar;

	/** Botón para despachar la siguiente solicitud. */
	private final JButton btnDespachar;

	/** Modelo de la tabla de la cola prioritaria. */
	private final ModeloTabla<Solicitud> modeloPrioritaria;

	/** Modelo de la tabla de la cola regular. */
	private final ModeloTabla<Solicitud> modeloRegular;

	/** Tabla de la cola regular. */
	private final JTable tablaRegular;

	/**
	 * Construye la pestaña con su formulario, las dos colas y las acciones.
	 */
	public PanelSolicitudes() {
		cmbCliente = new JComboBox<>();
		cmbIncidente = new JComboBox<>(TipoIncidente.values());
		cmbZona = new JComboBox<>(Zona.values());
		txtUbicacion = new JTextField(14);
		txtDescripcion = new JTextField(14);
		spnRiesgo = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
		spnAfectacion = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
		spnSeveridad = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
		lblVistaPrevia = new JLabel(" ");
		lblVistaPrevia.setFont(lblVistaPrevia.getFont().deriveFont(Font.BOLD));
		btnRegistrar = new JButton("Registrar solicitud");

		JPanel formulario = crearFormulario("Nueva solicitud de asistencia");
		agregarCampo(formulario, 0, "Cliente:", cmbCliente);
		agregarCampo(formulario, 1, "Incidente:", cmbIncidente);
		agregarCampo(formulario, 2, "Zona:", cmbZona);
		agregarCampo(formulario, 3, "Ubicación:", txtUbicacion);
		agregarCampo(formulario, 4, "Descripción:", txtDescripcion);
		agregarCampo(formulario, 5, "Nivel de riesgo (1-5):", spnRiesgo);
		agregarCampo(formulario, 6, "Afectación vial (1-5):", spnAfectacion);
		agregarCampo(formulario, 7, "Severidad (1-5):", spnSeveridad);
		agregarFilaCompleta(formulario, 8, lblVistaPrevia);
		agregarFilaCompleta(formulario, 9, btnRegistrar);
		agregarFilaCompleta(formulario, 10, new JLabel("<html>Puntaje = 3·riesgo + 2·afectación + 2·severidad"
				+ " + 2·peso del cliente. Es crítica si el puntaje es 25 o más, o si algún factor vale 5.</html>"));
		agregarRelleno(formulario, 11);
		formulario.setPreferredSize(new Dimension(320, 0));

		modeloPrioritaria = new ModeloTabla<>(COLUMNAS, PanelSolicitudes::filaSolicitud);
		modeloRegular = new ModeloTabla<>(COLUMNAS, PanelSolicitudes::filaSolicitud);
		JTable tablaPrioritaria = crearTabla(modeloPrioritaria);
		tablaRegular = crearTabla(modeloRegular);

		JPanel colas = new JPanel(new GridLayout(2, 1, 0, 8));
		colas.add(envolver(tablaPrioritaria, "Cola prioritaria (casos críticos, mayor puntaje primero)"));
		colas.add(envolver(tablaRegular, "Cola regular (estricto orden de llegada)"));

		lblSiguiente = new JLabel(" ");
		lblSiguiente.setFont(lblSiguiente.getFont().deriveFont(Font.BOLD));
		btnEscalar = new JButton("Escalar seleccionada a prioritaria");
		btnDespachar = new JButton("Despachar siguiente...");

		JPanel acciones = new JPanel(new BorderLayout());
		acciones.add(filaDe(lblSiguiente), BorderLayout.NORTH);
		acciones.add(filaDe(btnDespachar, btnEscalar), BorderLayout.SOUTH);

		add(formulario, BorderLayout.WEST);
		add(colas, BorderLayout.CENTER);
		add(acciones, BorderLayout.SOUTH);
	}

	/**
	 * Convierte una solicitud en espera en una fila de tabla.
	 *
	 * @param s        solicitud en espera
	 * @param posicion posición en la cola
	 * @return valores de la fila
	 */
	private static Object[] filaSolicitud(Solicitud s, int posicion) {
		return new Object[] { posicion + 1, s.getCodigo(), s.getNumeroLlegada(), s.getCliente().getNombre(),
				s.getCliente().getTipo(), s.getTipoIncidente(), s.getZona(), s.getNivelRiesgo(),
				s.getAfectacionVial(), s.getSeveridad(), s.getPuntaje(), Formato.hora(s.getFechaRegistro()),
				s.isEscaladaManualmente() ? "Escalada manual" : (s.isPrioritaria() ? "CRÍTICA" : "Ordinaria") };
	}

	/**
	 * Muestra las dos colas de solicitudes en espera.
	 *
	 * @param prioritaria cola de solicitudes críticas
	 * @param regular     cola de solicitudes ordinarias
	 */
	public void mostrarColas(Secuencia<Solicitud> prioritaria, Secuencia<Solicitud> regular) {
		modeloPrioritaria.setDatos(prioritaria);
		modeloRegular.setDatos(regular);
	}

	/**
	 * Carga los clientes en el selector, conservando el que estaba elegido.
	 *
	 * @param clientes clientes registrados
	 */
	public void mostrarClientes(Secuencia<Cliente> clientes) {
		Object anterior = cmbCliente.getSelectedItem();
		cmbCliente.removeAllItems();
		for (int i = 0; i < clientes.tamano(); i++) {
			cmbCliente.addItem(clientes.obtener(i));
		}
		if (anterior != null) {
			cmbCliente.setSelectedItem(anterior);
		}
	}

	/**
	 * Muestra el texto de la siguiente solicitud en turno.
	 *
	 * @param texto descripción de la siguiente solicitud
	 */
	public void setSiguiente(String texto) {
		lblSiguiente.setText(texto);
	}

	/**
	 * Muestra el puntaje estimado de la solicitud que se está digitando.
	 *
	 * @param texto texto de la vista previa
	 */
	public void setVistaPrevia(String texto) {
		lblVistaPrevia.setText(texto);
	}

	/**
	 * Obtiene la solicitud seleccionada en la cola regular.
	 *
	 * @return la solicitud seleccionada, o {@code null} si no hay selección
	 */
	public Solicitud getSolicitudRegularSeleccionada() {
		return modeloRegular.getElemento(tablaRegular.getSelectedRow());
	}

	/**
	 * Limpia el formulario de registro.
	 */
	public void limpiarFormulario() {
		txtUbicacion.setText("");
		txtDescripcion.setText("");
		spnRiesgo.setValue(1);
		spnAfectacion.setValue(1);
		spnSeveridad.setValue(1);
		cmbIncidente.setSelectedIndex(0);
		cmbZona.setSelectedIndex(0);
	}

	/**
	 * Obtiene el cliente elegido.
	 *
	 * @return cliente seleccionado, o {@code null}
	 */
	public Cliente getCliente() {
		return (Cliente) cmbCliente.getSelectedItem();
	}

	/**
	 * Obtiene el tipo de incidente elegido.
	 *
	 * @return tipo de incidente seleccionado
	 */
	public TipoIncidente getIncidente() {
		return (TipoIncidente) cmbIncidente.getSelectedItem();
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
	 * Obtiene la ubicación escrita.
	 *
	 * @return texto del campo ubicación
	 */
	public String getUbicacion() {
		return txtUbicacion.getText();
	}

	/**
	 * Obtiene la descripción escrita.
	 *
	 * @return texto del campo descripción
	 */
	public String getDescripcion() {
		return txtDescripcion.getText();
	}

	/**
	 * Obtiene el nivel de riesgo elegido.
	 *
	 * @return riesgo de 1 a 5
	 */
	public int getRiesgo() {
		return (Integer) spnRiesgo.getValue();
	}

	/**
	 * Obtiene la afectación vial elegida.
	 *
	 * @return afectación de 1 a 5
	 */
	public int getAfectacion() {
		return (Integer) spnAfectacion.getValue();
	}

	/**
	 * Obtiene la severidad elegida.
	 *
	 * @return severidad de 1 a 5
	 */
	public int getSeveridad() {
		return (Integer) spnSeveridad.getValue();
	}

	/**
	 * Obtiene el selector de cliente, para escuchar sus cambios.
	 *
	 * @return selector de cliente
	 */
	public JComboBox<Cliente> getCmbCliente() {
		return cmbCliente;
	}

	/**
	 * Obtiene el selector de riesgo, para escuchar sus cambios.
	 *
	 * @return selector de riesgo
	 */
	public JSpinner getSpnRiesgo() {
		return spnRiesgo;
	}

	/**
	 * Obtiene el selector de afectación, para escuchar sus cambios.
	 *
	 * @return selector de afectación
	 */
	public JSpinner getSpnAfectacion() {
		return spnAfectacion;
	}

	/**
	 * Obtiene el selector de severidad, para escuchar sus cambios.
	 *
	 * @return selector de severidad
	 */
	public JSpinner getSpnSeveridad() {
		return spnSeveridad;
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
	 * Obtiene el botón de escalamiento.
	 *
	 * @return botón escalar
	 */
	public JButton getBtnEscalar() {
		return btnEscalar;
	}

	/**
	 * Obtiene el botón de despacho.
	 *
	 * @return botón despachar
	 */
	public JButton getBtnDespachar() {
		return btnDespachar;
	}
}
