package motor360.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import motor360.estructuras.Secuencia;
import motor360.modelo.Cliente;
import motor360.modelo.TipoCliente;

/**
 * Pestaña de gestión de clientes: registro y listado.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelClientes extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Campo del documento. */
	private final JTextField txtDocumento;

	/** Campo del nombre. */
	private final JTextField txtNombre;

	/** Campo del teléfono. */
	private final JTextField txtTelefono;

	/** Selector del perfil del cliente. */
	private final JComboBox<TipoCliente> cmbTipo;

	/** Botón para registrar el cliente. */
	private final JButton btnRegistrar;

	/** Modelo de la tabla de clientes. */
	private final ModeloTabla<Cliente> modelo;

	/**
	 * Construye la pestaña con su formulario y tabla.
	 */
	public PanelClientes() {
		txtDocumento = new JTextField(12);
		txtNombre = new JTextField(12);
		txtTelefono = new JTextField(12);
		cmbTipo = new JComboBox<>(TipoCliente.values());
		btnRegistrar = new JButton("Registrar cliente");

		JPanel formulario = crearFormulario("Nuevo cliente");
		agregarCampo(formulario, 0, "Documento o NIT:", txtDocumento);
		agregarCampo(formulario, 1, "Nombre o razón social:", txtNombre);
		agregarCampo(formulario, 2, "Teléfono:", txtTelefono);
		agregarCampo(formulario, 3, "Tipo de cliente:", cmbTipo);
		agregarFilaCompleta(formulario, 4, btnRegistrar);
		agregarRelleno(formulario, 5);
		formulario.setPreferredSize(new Dimension(300, 0));

		modelo = new ModeloTabla<>(
				new String[] { "Documento", "Nombre", "Teléfono", "Tipo de cliente", "Peso en prioridad" },
				(c, i) -> new Object[] { c.getDocumento(), c.getNombre(), c.getTelefono(), c.getTipo(),
						c.getTipo().getPeso() });
		JTable tabla = crearTabla(modelo);

		add(formulario, BorderLayout.WEST);
		add(envolver(tabla, "Clientes registrados"), BorderLayout.CENTER);
	}

	/**
	 * Muestra los clientes.
	 *
	 * @param clientes clientes registrados
	 */
	public void mostrarClientes(Secuencia<Cliente> clientes) {
		modelo.setDatos(clientes);
	}

	/**
	 * Limpia el formulario de registro.
	 */
	public void limpiarFormulario() {
		txtDocumento.setText("");
		txtNombre.setText("");
		txtTelefono.setText("");
		cmbTipo.setSelectedIndex(0);
		txtDocumento.requestFocusInWindow();
	}

	/**
	 * Obtiene el documento escrito.
	 *
	 * @return texto del campo documento
	 */
	public String getDocumento() {
		return txtDocumento.getText();
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
	 * Obtiene el teléfono escrito.
	 *
	 * @return texto del campo teléfono
	 */
	public String getTelefono() {
		return txtTelefono.getText();
	}

	/**
	 * Obtiene el perfil elegido.
	 *
	 * @return tipo de cliente seleccionado
	 */
	public TipoCliente getTipo() {
		return (TipoCliente) cmbTipo.getSelectedItem();
	}

	/**
	 * Obtiene el botón de registro.
	 *
	 * @return botón registrar
	 */
	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}
}
