package motor360.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import motor360.estructuras.Secuencia;
import motor360.modelo.Tecnico;
import motor360.modelo.Unidad;

/**
 * Pestaña de tablero del centro de control. Muestra en tiempo real el
 * estatus de los activos: unidades operando, en taller o listas para
 * despacho, técnicos libres u ocupados, colas de solicitudes, rescates
 * activos e insumos en revisión.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class PanelTablero extends PanelBase {

	/** Versión de serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Color neutro para indicadores generales. */
	private static final Color AZUL = new Color(30, 70, 150);

	/** Etiqueta con la fecha y hora actual. */
	private final JLabel lblReloj;

	/** Etiqueta con la siguiente solicitud en turno. */
	private final JLabel lblSiguiente;

	/** Indicador de unidades disponibles. */
	private final JLabel lblUnidadesDisponibles;

	/** Indicador de unidades asignadas. */
	private final JLabel lblUnidadesAsignadas;

	/** Indicador de unidades en mantenimiento. */
	private final JLabel lblUnidadesTaller;

	/** Indicador de técnicos libres. */
	private final JLabel lblTecnicosLibres;

	/** Indicador de técnicos en servicio. */
	private final JLabel lblTecnicosOcupados;

	/** Indicador de técnicos fuera de turno. */
	private final JLabel lblTecnicosFuera;

	/** Indicador de solicitudes prioritarias en espera. */
	private final JLabel lblPrioritarias;

	/** Indicador de solicitudes regulares en espera. */
	private final JLabel lblRegulares;

	/** Indicador de rescates activos. */
	private final JLabel lblActivos;

	/** Indicador de incidencias solucionadas hoy. */
	private final JLabel lblSolucionadas;

	/** Indicador de maletines en auditoría. */
	private final JLabel lblAuditoria;

	/** Indicador de repuestos disponibles. */
	private final JLabel lblRepuestos;

	/** Modelo de la tabla de unidades. */
	private final ModeloTabla<Unidad> modeloUnidades;

	/** Modelo de la tabla de técnicos. */
	private final ModeloTabla<Tecnico> modeloTecnicos;

	/**
	 * Construye el tablero con sus indicadores y tablas.
	 */
	public PanelTablero() {
		lblReloj = new JLabel(" ");
		lblReloj.setFont(lblReloj.getFont().deriveFont(Font.BOLD, 16f));
		lblSiguiente = new JLabel(" ");
		lblSiguiente.setFont(lblSiguiente.getFont().deriveFont(Font.BOLD, 13f));

		lblUnidadesDisponibles = crearValor(RenderizadorEstado.VERDE);
		lblUnidadesAsignadas = crearValor(RenderizadorEstado.NARANJA);
		lblUnidadesTaller = crearValor(RenderizadorEstado.ROJO);
		lblTecnicosLibres = crearValor(RenderizadorEstado.VERDE);
		lblTecnicosOcupados = crearValor(RenderizadorEstado.NARANJA);
		lblTecnicosFuera = crearValor(RenderizadorEstado.ROJO);
		lblPrioritarias = crearValor(RenderizadorEstado.ROJO);
		lblRegulares = crearValor(AZUL);
		lblActivos = crearValor(RenderizadorEstado.NARANJA);
		lblSolucionadas = crearValor(RenderizadorEstado.VERDE);
		lblAuditoria = crearValor(AZUL);
		lblRepuestos = crearValor(AZUL);

		JPanel indicadores = new JPanel(new GridLayout(2, 6, 8, 8));
		indicadores.add(tarjeta("Unidades listas", lblUnidadesDisponibles));
		indicadores.add(tarjeta("Unidades operando", lblUnidadesAsignadas));
		indicadores.add(tarjeta("Unidades en taller", lblUnidadesTaller));
		indicadores.add(tarjeta("Técnicos libres", lblTecnicosLibres));
		indicadores.add(tarjeta("Técnicos en siniestro", lblTecnicosOcupados));
		indicadores.add(tarjeta("Técnicos fuera de turno", lblTecnicosFuera));
		indicadores.add(tarjeta("Solicitudes críticas", lblPrioritarias));
		indicadores.add(tarjeta("Solicitudes regulares", lblRegulares));
		indicadores.add(tarjeta("Rescates activos", lblActivos));
		indicadores.add(tarjeta("Solucionadas hoy", lblSolucionadas));
		indicadores.add(tarjeta("Maletines en auditoría", lblAuditoria));
		indicadores.add(tarjeta("Repuestos en pila", lblRepuestos));

		JPanel encabezado = new JPanel(new BorderLayout(0, 8));
		encabezado.add(filaDe(lblReloj), BorderLayout.NORTH);
		encabezado.add(indicadores, BorderLayout.CENTER);
		encabezado.add(filaDe(lblSiguiente), BorderLayout.SOUTH);

		modeloUnidades = new ModeloTabla<>(new String[] { "Placa", "Categoría", "Estatus", "Zona", "Nivel" },
				(u, i) -> new Object[] { u.getPlaca(), u.getTipo(), u.getEstado(), u.getZona(),
						u.getNivelDisponibilidad() });
		modeloTecnicos = new ModeloTabla<>(new String[] { "Técnico", "Especialidad", "Estado", "Sector", "Servicio" },
				(t, i) -> new Object[] { t.getNombre(), t.getEspecialidad(), t.getEstado(), t.getZona(),
						t.tieneServicioActivo() ? t.getServicioActivo().getCodigo() : "-" });

		JPanel tablas = new JPanel(new GridLayout(1, 2, 8, 0));
		tablas.add(envolver(crearTabla(modeloUnidades), "Estatus de la flota en tiempo real"));
		tablas.add(envolver(crearTabla(modeloTecnicos), "Estatus del personal técnico en tiempo real"));

		add(encabezado, BorderLayout.NORTH);
		add(tablas, BorderLayout.CENTER);
	}

	/**
	 * Crea la etiqueta grande de un indicador.
	 *
	 * @param color color del número
	 * @return etiqueta del indicador
	 */
	private static JLabel crearValor(Color color) {
		JLabel valor = new JLabel("0", SwingConstants.CENTER);
		valor.setFont(valor.getFont().deriveFont(Font.BOLD, 28f));
		valor.setForeground(color);
		return valor;
	}

	/**
	 * Crea una tarjeta con título para un indicador.
	 *
	 * @param titulo título de la tarjeta
	 * @param valor  etiqueta con el número
	 * @return panel de la tarjeta
	 */
	private static JPanel tarjeta(String titulo, JLabel valor) {
		JPanel tarjeta = new JPanel(new BorderLayout());
		tarjeta.setBorder(BorderFactory.createTitledBorder(titulo));
		tarjeta.add(valor, BorderLayout.CENTER);
		return tarjeta;
	}

	/**
	 * Actualiza los indicadores de la flota.
	 *
	 * @param disponibles   unidades disponibles
	 * @param asignadas     unidades asignadas
	 * @param mantenimiento unidades en mantenimiento
	 */
	public void setIndicadoresFlota(int disponibles, int asignadas, int mantenimiento) {
		lblUnidadesDisponibles.setText(String.valueOf(disponibles));
		lblUnidadesAsignadas.setText(String.valueOf(asignadas));
		lblUnidadesTaller.setText(String.valueOf(mantenimiento));
	}

	/**
	 * Actualiza los indicadores del personal técnico.
	 *
	 * @param libres     técnicos libres
	 * @param enServicio técnicos en servicio
	 * @param fuera      técnicos fuera de turno
	 */
	public void setIndicadoresTecnicos(int libres, int enServicio, int fuera) {
		lblTecnicosLibres.setText(String.valueOf(libres));
		lblTecnicosOcupados.setText(String.valueOf(enServicio));
		lblTecnicosFuera.setText(String.valueOf(fuera));
	}

	/**
	 * Actualiza los indicadores de la operación.
	 *
	 * @param prioritarias    solicitudes críticas en espera
	 * @param regulares       solicitudes regulares en espera
	 * @param activos         rescates activos
	 * @param solucionadasHoy incidencias solucionadas hoy
	 * @param auditoria       maletines en auditoría
	 * @param repuestos       repuestos en la pila
	 */
	public void setIndicadoresOperacion(int prioritarias, int regulares, int activos, int solucionadasHoy,
			int auditoria, int repuestos) {
		lblPrioritarias.setText(String.valueOf(prioritarias));
		lblRegulares.setText(String.valueOf(regulares));
		lblActivos.setText(String.valueOf(activos));
		lblSolucionadas.setText(String.valueOf(solucionadasHoy));
		lblAuditoria.setText(String.valueOf(auditoria));
		lblRepuestos.setText(String.valueOf(repuestos));
	}

	/**
	 * Muestra las tablas de estatus de unidades y técnicos.
	 *
	 * @param unidades unidades registradas
	 * @param tecnicos técnicos registrados
	 */
	public void mostrarActivos(Secuencia<Unidad> unidades, Secuencia<Tecnico> tecnicos) {
		modeloUnidades.setDatos(unidades);
		modeloTecnicos.setDatos(tecnicos);
	}

	/**
	 * Muestra la fecha y hora actual.
	 *
	 * @param texto fecha y hora
	 */
	public void setReloj(String texto) {
		lblReloj.setText(texto);
	}

	/**
	 * Muestra la siguiente solicitud en turno.
	 *
	 * @param texto descripción de la siguiente solicitud
	 */
	public void setSiguiente(String texto) {
		lblSiguiente.setText(texto);
	}
}
