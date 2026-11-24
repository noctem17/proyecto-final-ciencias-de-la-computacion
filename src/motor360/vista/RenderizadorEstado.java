package motor360.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Pinta las celdas de las tablas resaltando con colores los estados: verde
 * para lo que está listo, naranja para lo que está en operación y rojo para
 * lo que está bloqueado o es crítico. Así el centro de control identifica de
 * un vistazo el estatus de sus activos.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class RenderizadorEstado extends DefaultTableCellRenderer {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Color para estados listos o libres. */
	public static final Color VERDE = new Color(0, 128, 0);

	/** Color para estados en operación. */
	public static final Color NARANJA = new Color(200, 110, 0);

	/** Color para estados bloqueados o críticos. */
	public static final Color ROJO = new Color(180, 0, 0);

	/** Textos que se pintan de verde. */
	private static final String[] TEXTOS_VERDES = { "Disponible", "Libre" };

	/** Textos que se pintan de naranja. */
	private static final String[] TEXTOS_NARANJAS = { "Asignada", "En atención de siniestro", "En uso", "Atendida" };

	/** Textos que se pintan de rojo. */
	private static final String[] TEXTOS_ROJOS = { "En mantenimiento", "Fuera de turno", "En auditoría", "CRÍTICA" };

	/**
	 * Crea el renderizador con la configuración por defecto de las celdas.
	 */
	public RenderizadorEstado() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada, boolean foco,
			int fila, int columna) {
		Component celda = super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
		Color color = colorPara(valor == null ? "" : valor.toString());
		if (color == null) {
			celda.setFont(celda.getFont().deriveFont(Font.PLAIN));
			if (!seleccionada) {
				celda.setForeground(tabla.getForeground());
			}
		} else {
			celda.setFont(celda.getFont().deriveFont(Font.BOLD));
			if (!seleccionada) {
				celda.setForeground(color);
			}
		}
		return celda;
	}

	/**
	 * Decide el color de un texto.
	 *
	 * @param texto texto de la celda
	 * @return el color que le corresponde, o {@code null} si no se resalta
	 */
	private static Color colorPara(String texto) {
		if (esta(texto, TEXTOS_VERDES)) {
			return VERDE;
		}
		if (esta(texto, TEXTOS_NARANJAS)) {
			return NARANJA;
		}
		if (esta(texto, TEXTOS_ROJOS)) {
			return ROJO;
		}
		return null;
	}

	/**
	 * Indica si un texto está dentro de un arreglo de textos.
	 *
	 * @param texto  texto buscado
	 * @param textos arreglo donde se busca
	 * @return {@code true} si el texto está en el arreglo
	 */
	private static boolean esta(String texto, String[] textos) {
		for (String t : textos) {
			if (t.equals(texto)) {
				return true;
			}
		}
		return false;
	}
}
