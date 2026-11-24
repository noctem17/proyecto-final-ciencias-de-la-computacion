package motor360.vista;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ventana principal de la aplicación. Reúne todas las pestañas, el menú y la
 * barra de estado, y ofrece los cuadros de diálogo de mensajes. No contiene
 * lógica de negocio: el controlador decide qué hacer con cada acción.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Título de la aplicación. */
	public static final String TITULO = "Asistencia Motor 360 - Centro de Control";

	/** Pestaña del tablero. */
	private final PanelTablero panelTablero;

	/** Pestaña de solicitudes. */
	private final PanelSolicitudes panelSolicitudes;

	/** Pestaña de servicios. */
	private final PanelServicios panelServicios;

	/** Pestaña de unidades. */
	private final PanelUnidades panelUnidades;

	/** Pestaña de técnicos. */
	private final PanelTecnicos panelTecnicos;

	/** Pestaña de clientes. */
	private final PanelClientes panelClientes;

	/** Pestaña de insumos. */
	private final PanelInsumos panelInsumos;

	/** Pestaña de historial. */
	private final PanelHistorial panelHistorial;

	/** Opción de menú para exportar el reporte. */
	private final JMenuItem itemExportar;

	/** Opción de menú para salir. */
	private final JMenuItem itemSalir;

	/** Opción de menú para deshacer. */
	private final JMenuItem itemDeshacer;

	/** Opción de menú acerca de. */
	private final JMenuItem itemAcercaDe;

	/** Barra de estado inferior. */
	private final JLabel barraEstado;

	/**
	 * Construye la ventana con todas sus pestañas y su menú.
	 */
	public VentanaPrincipal() {
		super(TITULO);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		panelTablero = new PanelTablero();
		panelSolicitudes = new PanelSolicitudes();
		panelServicios = new PanelServicios();
		panelUnidades = new PanelUnidades();
		panelTecnicos = new PanelTecnicos();
		panelClientes = new PanelClientes();
		panelInsumos = new PanelInsumos();
		panelHistorial = new PanelHistorial();

		JTabbedPane pestanas = new JTabbedPane();
		pestanas.addTab("Tablero", panelTablero);
		pestanas.addTab("Solicitudes", panelSolicitudes);
		pestanas.addTab("Rescates", panelServicios);
		pestanas.addTab("Unidades", panelUnidades);
		pestanas.addTab("Técnicos", panelTecnicos);
		pestanas.addTab("Clientes", panelClientes);
		pestanas.addTab("Insumos", panelInsumos);
		pestanas.addTab("Historial", panelHistorial);

		itemExportar = new JMenuItem("Exportar reporte CSV del día...");
		itemSalir = new JMenuItem("Salir");
		itemDeshacer = new JMenuItem("Deshacer última transacción");
		itemDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		itemAcercaDe = new JMenuItem("Acerca de");

		JMenu menuArchivo = new JMenu("Archivo");
		menuArchivo.add(itemExportar);
		menuArchivo.addSeparator();
		menuArchivo.add(itemSalir);
		JMenu menuEdicion = new JMenu("Edición");
		menuEdicion.add(itemDeshacer);
		JMenu menuAyuda = new JMenu("Ayuda");
		menuAyuda.add(itemAcercaDe);
		JMenuBar barraMenu = new JMenuBar();
		barraMenu.add(menuArchivo);
		barraMenu.add(menuEdicion);
		barraMenu.add(menuAyuda);
		setJMenuBar(barraMenu);

		barraEstado = new JLabel("Listo.");
		barraEstado.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pestanas, BorderLayout.CENTER);
		getContentPane().add(barraEstado, BorderLayout.SOUTH);

		setSize(1280, 780);
		setLocationRelativeTo(null);
	}

	/**
	 * Muestra un mensaje informativo.
	 *
	 * @param mensaje texto a mostrar
	 */
	public void mostrarInformacion(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Muestra un mensaje de error.
	 *
	 * @param mensaje texto a mostrar
	 */
	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Operación no permitida", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Pide confirmación al usuario.
	 *
	 * @param mensaje pregunta a mostrar
	 * @return {@code true} si el usuario responde que sí
	 */
	public boolean confirmar(String mensaje) {
		return JOptionPane.showConfirmDialog(this, mensaje, TITULO,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	/**
	 * Pide al usuario el archivo donde guardar el reporte CSV.
	 *
	 * @param nombreSugerido nombre de archivo propuesto
	 * @return el archivo elegido, o {@code null} si se canceló
	 */
	public File elegirArchivoCSV(String nombreSugerido) {
		JFileChooser selector = new JFileChooser();
		selector.setDialogTitle("Guardar reporte de cierre de jornada");
		selector.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
		selector.setSelectedFile(new File(nombreSugerido));
		if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File archivo = selector.getSelectedFile();
		if (!archivo.getName().toLowerCase().endsWith(".csv")) {
			archivo = new File(archivo.getParentFile(), archivo.getName() + ".csv");
		}
		return archivo;
	}

	/**
	 * Muestra un texto en la barra de estado.
	 *
	 * @param texto texto a mostrar
	 */
	public void setEstado(String texto) {
		barraEstado.setText(texto);
	}

	/**
	 * Obtiene la pestaña del tablero.
	 *
	 * @return panel del tablero
	 */
	public PanelTablero getPanelTablero() {
		return panelTablero;
	}

	/**
	 * Obtiene la pestaña de solicitudes.
	 *
	 * @return panel de solicitudes
	 */
	public PanelSolicitudes getPanelSolicitudes() {
		return panelSolicitudes;
	}

	/**
	 * Obtiene la pestaña de rescates.
	 *
	 * @return panel de servicios
	 */
	public PanelServicios getPanelServicios() {
		return panelServicios;
	}

	/**
	 * Obtiene la pestaña de unidades.
	 *
	 * @return panel de unidades
	 */
	public PanelUnidades getPanelUnidades() {
		return panelUnidades;
	}

	/**
	 * Obtiene la pestaña de técnicos.
	 *
	 * @return panel de técnicos
	 */
	public PanelTecnicos getPanelTecnicos() {
		return panelTecnicos;
	}

	/**
	 * Obtiene la pestaña de clientes.
	 *
	 * @return panel de clientes
	 */
	public PanelClientes getPanelClientes() {
		return panelClientes;
	}

	/**
	 * Obtiene la pestaña de insumos.
	 *
	 * @return panel de insumos
	 */
	public PanelInsumos getPanelInsumos() {
		return panelInsumos;
	}

	/**
	 * Obtiene la pestaña de historial.
	 *
	 * @return panel de historial
	 */
	public PanelHistorial getPanelHistorial() {
		return panelHistorial;
	}

	/**
	 * Obtiene la opción de menú para exportar.
	 *
	 * @return opción exportar
	 */
	public JMenuItem getItemExportar() {
		return itemExportar;
	}

	/**
	 * Obtiene la opción de menú para salir.
	 *
	 * @return opción salir
	 */
	public JMenuItem getItemSalir() {
		return itemSalir;
	}

	/**
	 * Obtiene la opción de menú para deshacer.
	 *
	 * @return opción deshacer
	 */
	public JMenuItem getItemDeshacer() {
		return itemDeshacer;
	}

	/**
	 * Obtiene la opción de menú acerca de.
	 *
	 * @return opción acerca de
	 */
	public JMenuItem getItemAcercaDe() {
		return itemAcercaDe;
	}
}
