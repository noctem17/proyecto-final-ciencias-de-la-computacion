package motor360.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import motor360.estructuras.Secuencia;
import motor360.modelo.Maletin;
import motor360.modelo.Solicitud;
import motor360.modelo.Tecnico;
import motor360.modelo.Unidad;

/**
 * Ventana modal para despachar la siguiente solicitud: muestra sus datos y
 * permite elegir la unidad, el técnico y, opcionalmente, el maletín. Se
 * muestran todas las unidades y todos los técnicos con su estado, para que
 * el modelo sea quien valide las reglas de negocio y explique por qué un
 * recurso no puede asignarse.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class DialogoAsignacion extends JDialog {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Selector de la unidad. */
	private final JComboBox<Unidad> cmbUnidad;

	/** Selector del técnico. */
	private final JComboBox<Tecnico> cmbTecnico;

	/** Selector del maletín. */
	private final JComboBox<Maletin> cmbMaletin;

	/** Casilla para indicar si se usa maletín. */
	private final JCheckBox chkMaletin;

	/** Indica si el usuario confirmó el despacho. */
	private boolean confirmado;

	/**
	 * Construye el diálogo para una solicitud.
	 *
	 * @param propietario ventana principal
	 * @param solicitud   solicitud que se va a despachar
	 * @param unidades    todas las unidades registradas
	 * @param tecnicos    todos los técnicos registrados
	 * @param maletines   maletines disponibles
	 */
	public DialogoAsignacion(JFrame propietario, Solicitud solicitud, Secuencia<Unidad> unidades,
			Secuencia<Tecnico> tecnicos, Secuencia<Maletin> maletines) {
		super(propietario, "Despachar " + solicitud.getCodigo(), true);
		confirmado = false;

		JLabel resumen = new JLabel("<html><b>" + solicitud.getCodigo() + "</b> - " + solicitud.getTipoIncidente()
				+ "<br>Cliente: " + solicitud.getCliente() + "<br>Ubicación: " + solicitud.getUbicacion() + " ("
				+ solicitud.getZona() + ")<br>Descripción: " + solicitud.getDescripcion() + "<br>Puntaje: "
				+ solicitud.getPuntaje() + (solicitud.isPrioritaria() ? " - CRÍTICA" : " - ordinaria") + "</html>");
		resumen.setBorder(BorderFactory.createTitledBorder("Solicitud en turno"));

		cmbUnidad = new JComboBox<>();
		for (int i = 0; i < unidades.tamano(); i++) {
			cmbUnidad.addItem(unidades.obtener(i));
		}
		cmbUnidad.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
					boolean seleccionado, boolean foco) {
				Object texto = valor instanceof Unidad ? valor + "  [" + ((Unidad) valor).getEstado() + "]" : valor;
				return super.getListCellRendererComponent(lista, texto, indice, seleccionado, foco);
			}
		});

		cmbTecnico = new JComboBox<>();
		for (int i = 0; i < tecnicos.tamano(); i++) {
			cmbTecnico.addItem(tecnicos.obtener(i));
		}
		cmbTecnico.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
					boolean seleccionado, boolean foco) {
				Object texto = valor instanceof Tecnico ? valor + "  [" + ((Tecnico) valor).getEstado() + "]" : valor;
				return super.getListCellRendererComponent(lista, texto, indice, seleccionado, foco);
			}
		});

		cmbMaletin = new JComboBox<>();
		for (int i = 0; i < maletines.tamano(); i++) {
			cmbMaletin.addItem(maletines.obtener(i));
		}
		chkMaletin = new JCheckBox("Llevar maletín de respuesta rápida", maletines.tamano() > 0);
		chkMaletin.setEnabled(maletines.tamano() > 0);
		cmbMaletin.setEnabled(chkMaletin.isSelected());
		chkMaletin.addActionListener(e -> cmbMaletin.setEnabled(chkMaletin.isSelected()));

		JPanel formulario = PanelBase.crearFormulario("Recursos");
		PanelBase.agregarCampo(formulario, 0, "Unidad:", cmbUnidad);
		PanelBase.agregarCampo(formulario, 1, "Técnico:", cmbTecnico);
		PanelBase.agregarFilaCompleta(formulario, 2, chkMaletin);
		PanelBase.agregarCampo(formulario, 3, "Maletín:", cmbMaletin);

		JButton btnConfirmar = new JButton("Despachar");
		JButton btnCancelar = new JButton("Cancelar");
		btnConfirmar.addActionListener(e -> {
			confirmado = true;
			dispose();
		});
		btnCancelar.addActionListener(e -> dispose());
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		botones.add(btnCancelar);
		botones.add(btnConfirmar);

		JPanel contenido = new JPanel(new BorderLayout(8, 8));
		contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contenido.add(resumen, BorderLayout.NORTH);
		contenido.add(formulario, BorderLayout.CENTER);
		contenido.add(botones, BorderLayout.SOUTH);
		setContentPane(contenido);
		getRootPane().setDefaultButton(btnConfirmar);
		pack();
		setSize(Math.max(getWidth(), 620), getHeight());
		setLocationRelativeTo(propietario);
	}

	/**
	 * Indica si el usuario confirmó el despacho.
	 *
	 * @return {@code true} si pulsó Despachar
	 */
	public boolean isConfirmado() {
		return confirmado;
	}

	/**
	 * Obtiene la unidad elegida.
	 *
	 * @return unidad seleccionada, o {@code null}
	 */
	public Unidad getUnidad() {
		return (Unidad) cmbUnidad.getSelectedItem();
	}

	/**
	 * Obtiene el técnico elegido.
	 *
	 * @return técnico seleccionado, o {@code null}
	 */
	public Tecnico getTecnico() {
		return (Tecnico) cmbTecnico.getSelectedItem();
	}

	/**
	 * Obtiene el maletín elegido, si se marcó la casilla.
	 *
	 * @return maletín seleccionado, o {@code null} si no se usa
	 */
	public Maletin getMaletin() {
		return chkMaletin.isSelected() ? (Maletin) cmbMaletin.getSelectedItem() : null;
	}
}
