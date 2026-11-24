package motor360;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import motor360.controlador.ControladorPrincipal;
import motor360.modelo.CentroControl;
import motor360.modelo.DatosIniciales;
import motor360.vista.VentanaPrincipal;

/**
 * Punto de entrada del sistema Asistencia Motor 360. Crea el modelo, la vista
 * y el controlador del patrón MVC y los conecta.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public final class Aplicacion {

	/**
	 * Constructor privado: la clase solo contiene el método principal.
	 */
	private Aplicacion() {
	}

	/**
	 * Inicia la aplicación en el hilo de eventos de Swing.
	 *
	 * @param args argumentos de la línea de comandos; no se usan
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("No se pudo aplicar la apariencia del sistema: " + e.getMessage());
			}
			CentroControl modelo = new CentroControl();
			DatosIniciales.cargar(modelo);
			VentanaPrincipal vista = new VentanaPrincipal();
			ControladorPrincipal controlador = new ControladorPrincipal(modelo, vista);
			controlador.iniciar();
		});
	}
}
