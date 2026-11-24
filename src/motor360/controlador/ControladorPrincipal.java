package motor360.controlador;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Timer;

import motor360.modelo.CentroControl;
import motor360.modelo.Cliente;
import motor360.modelo.EstadoTecnico;
import motor360.modelo.EstadoUnidad;
import motor360.modelo.Maletin;
import motor360.modelo.PoliticaPrioridad;
import motor360.modelo.ReglaNegocioException;
import motor360.modelo.Repuesto;
import motor360.modelo.Solicitud;
import motor360.modelo.Transaccion;
import motor360.modelo.Unidad;
import motor360.vista.DialogoAsignacion;
import motor360.vista.PanelHistorial;
import motor360.vista.PanelInsumos;
import motor360.vista.PanelServicios;
import motor360.vista.PanelSolicitudes;
import motor360.vista.PanelTablero;
import motor360.vista.PanelTecnicos;
import motor360.vista.PanelUnidades;
import motor360.vista.PanelClientes;
import motor360.vista.VentanaPrincipal;

/**
 * Controlador del patrón MVC. Escucha los eventos de la vista, toma los datos
 * que el usuario ingresó, invoca las operaciones del modelo y, según el
 * resultado, actualiza la vista o muestra el mensaje de error. Ni la vista
 * conoce las reglas de negocio ni el modelo conoce la interfaz gráfica: el
 * controlador es el puente entre los dos.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class ControladorPrincipal {

	/** Formato del reloj del tablero. */
	private static final DateTimeFormatter FORMATO_RELOJ = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

	/** Modelo del sistema. */
	private final CentroControl modelo;

	/** Ventana principal de la vista. */
	private final VentanaPrincipal vista;

	/** Temporizador que actualiza el tablero cada segundo. */
	private final Timer reloj;

	/**
	 * Conecta el modelo con la vista, registra los eventos y muestra los datos
	 * iniciales.
	 *
	 * @param modelo modelo del sistema
	 * @param vista  ventana principal
	 */
	public ControladorPrincipal(CentroControl modelo, VentanaPrincipal vista) {
		this.modelo = modelo;
		this.vista = vista;
		registrarEventos();
		actualizarVistaPrevia();
		refrescarVista();
		reloj = new Timer(1000, e -> actualizarTablero());
	}

	/**
	 * Muestra la ventana y arranca el reloj del tablero.
	 */
	public void iniciar() {
		reloj.start();
		vista.setVisible(true);
	}

	/**
	 * Asocia cada botón y opción de menú con la acción que le corresponde.
	 */
	private void registrarEventos() {
		PanelUnidades unidades = vista.getPanelUnidades();
		unidades.getBtnRegistrar().addActionListener(e -> registrarUnidad());
		unidades.getBtnCambiarEstado().addActionListener(e -> cambiarEstadoUnidad());
		unidades.getBtnCambiarNivel().addActionListener(e -> cambiarNivelUnidad());

		PanelTecnicos tecnicos = vista.getPanelTecnicos();
		tecnicos.getBtnRegistrar().addActionListener(e -> registrarTecnico());
		tecnicos.getBtnCambiarEstado().addActionListener(e -> cambiarEstadoTecnico());

		PanelClientes clientes = vista.getPanelClientes();
		clientes.getBtnRegistrar().addActionListener(e -> registrarCliente());

		PanelSolicitudes solicitudes = vista.getPanelSolicitudes();
		solicitudes.getBtnRegistrar().addActionListener(e -> registrarSolicitud());
		solicitudes.getBtnEscalar().addActionListener(e -> escalarSolicitud());
		solicitudes.getBtnDespachar().addActionListener(e -> despacharSiguiente());
		solicitudes.getCmbCliente().addActionListener(e -> actualizarVistaPrevia());
		solicitudes.getSpnRiesgo().addChangeListener(e -> actualizarVistaPrevia());
		solicitudes.getSpnAfectacion().addChangeListener(e -> actualizarVistaPrevia());
		solicitudes.getSpnSeveridad().addChangeListener(e -> actualizarVistaPrevia());

		PanelServicios servicios = vista.getPanelServicios();
		servicios.getBtnAtendida().addActionListener(e -> marcarAtendida());
		servicios.getBtnSolucionar().addActionListener(e -> solucionarServicio());
		servicios.getBtnExportar().addActionListener(e -> exportarReporte());

		PanelInsumos insumos = vista.getPanelInsumos();
		insumos.getBtnRegistrarMaletin().addActionListener(e -> registrarMaletin());
		insumos.getBtnAuditar().addActionListener(e -> auditarMaletin());
		insumos.getBtnApilarRepuesto().addActionListener(e -> apilarRepuesto());
		insumos.getBtnDespacharRepuesto().addActionListener(e -> despacharRepuesto());

		PanelHistorial historial = vista.getPanelHistorial();
		historial.getBtnDeshacer().addActionListener(e -> deshacer());

		vista.getItemDeshacer().addActionListener(e -> deshacer());
		vista.getItemExportar().addActionListener(e -> exportarReporte());
		vista.getItemSalir().addActionListener(e -> salir());
		vista.getItemAcercaDe().addActionListener(e -> mostrarAcercaDe());
		vista.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evento) {
				salir();
			}
		});
	}

	/**
	 * Ejecuta una acción sobre el modelo. Si tiene éxito, refresca la vista y
	 * muestra el mensaje en la barra de estado; si incumple una regla de
	 * negocio, muestra el error al usuario.
	 *
	 * @param operacion acción a ejecutar
	 * @return {@code true} si la acción tuvo éxito
	 */
	private boolean ejecutar(Operacion operacion) {
		try {
			String mensaje = operacion.ejecutar();
			refrescarVista();
			vista.setEstado(mensaje);
			return true;
		} catch (ReglaNegocioException e) {
			vista.mostrarError(e.getMessage());
			vista.setEstado("Operación rechazada: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Registra la unidad escrita en el formulario.
	 */
	private void registrarUnidad() {
		PanelUnidades panel = vista.getPanelUnidades();
		boolean exito = ejecutar(() -> {
			Unidad unidad = modelo.registrarUnidad(panel.getPlaca(), panel.getTipo(), panel.getZona(),
					panel.getNivel());
			return "Unidad " + unidad.getPlaca() + " registrada con UUID " + unidad.getId();
		});
		if (exito) {
			panel.limpiarFormulario();
		}
	}

	/**
	 * Cambia el estatus de la unidad seleccionada.
	 */
	private void cambiarEstadoUnidad() {
		PanelUnidades panel = vista.getPanelUnidades();
		ejecutar(() -> {
			Unidad unidad = panel.getUnidadSeleccionada();
			modelo.cambiarEstadoUnidad(unidad, panel.getNuevoEstado());
			return "Unidad " + unidad.getPlaca() + " ahora está " + unidad.getEstado();
		});
	}

	/**
	 * Cambia el nivel de disponibilidad de la unidad seleccionada.
	 */
	private void cambiarNivelUnidad() {
		PanelUnidades panel = vista.getPanelUnidades();
		ejecutar(() -> {
			Unidad unidad = panel.getUnidadSeleccionada();
			modelo.cambiarNivelUnidad(unidad, panel.getNuevoNivel());
			return "Unidad " + unidad.getPlaca() + " con nivel " + unidad.getNivelDisponibilidad();
		});
	}

	/**
	 * Registra el técnico escrito en el formulario.
	 */
	private void registrarTecnico() {
		PanelTecnicos panel = vista.getPanelTecnicos();
		boolean exito = ejecutar(() -> "Técnico registrado: " + modelo.registrarTecnico(panel.getIdentificacion(),
				panel.getNombre(), panel.getEspecialidad(), panel.getZona()).getNombre());
		if (exito) {
			panel.limpiarFormulario();
		}
	}

	/**
	 * Cambia el estado del técnico seleccionado.
	 */
	private void cambiarEstadoTecnico() {
		PanelTecnicos panel = vista.getPanelTecnicos();
		ejecutar(() -> {
			modelo.cambiarEstadoTecnico(panel.getTecnicoSeleccionado(), panel.getNuevoEstado());
			return "Estado del técnico actualizado";
		});
	}

	/**
	 * Registra el cliente escrito en el formulario.
	 */
	private void registrarCliente() {
		PanelClientes panel = vista.getPanelClientes();
		boolean exito = ejecutar(() -> "Cliente registrado: " + modelo.registrarCliente(panel.getDocumento(),
				panel.getNombre(), panel.getTelefono(), panel.getTipo()).getNombre());
		if (exito) {
			panel.limpiarFormulario();
		}
	}

	/**
	 * Registra la solicitud escrita en el formulario.
	 */
	private void registrarSolicitud() {
		PanelSolicitudes panel = vista.getPanelSolicitudes();
		boolean exito = ejecutar(() -> {
			Solicitud solicitud = modelo.registrarSolicitud(panel.getCliente(), panel.getIncidente(),
					panel.getZona(), panel.getUbicacion(), panel.getDescripcion(), panel.getRiesgo(),
					panel.getAfectacion(), panel.getSeveridad());
			return "Solicitud " + solicitud.getCodigo() + " registrada en la cola "
					+ (solicitud.isPrioritaria() ? "prioritaria" : "regular") + " con puntaje "
					+ solicitud.getPuntaje();
		});
		if (exito) {
			panel.limpiarFormulario();
		}
	}

	/**
	 * Escala a prioritaria la solicitud regular seleccionada.
	 */
	private void escalarSolicitud() {
		PanelSolicitudes panel = vista.getPanelSolicitudes();
		ejecutar(() -> {
			Solicitud solicitud = panel.getSolicitudRegularSeleccionada();
			modelo.escalarSolicitud(solicitud);
			return "Solicitud " + solicitud.getCodigo() + " escalada a la cola prioritaria";
		});
	}

	/**
	 * Abre el diálogo de asignación para la siguiente solicitud en turno y, si
	 * el usuario confirma, la despacha.
	 */
	private void despacharSiguiente() {
		Solicitud siguiente = modelo.siguienteSolicitud();
		if (siguiente == null) {
			vista.mostrarError("No hay solicitudes pendientes por despachar.");
			return;
		}
		DialogoAsignacion dialogo = new DialogoAsignacion(vista, siguiente, modelo.getUnidades(),
				modelo.getTecnicos(), modelo.maletinesDisponibles());
		dialogo.setVisible(true);
		if (!dialogo.isConfirmado()) {
			return;
		}
		ejecutar(() -> {
			modelo.asignarRecursos(siguiente, dialogo.getUnidad(), dialogo.getTecnico(), dialogo.getMaletin());
			return "Solicitud " + siguiente.getCodigo() + " despachada con la unidad "
					+ siguiente.getUnidad().getPlaca() + " y el técnico " + siguiente.getTecnico().getNombre();
		});
	}

	/**
	 * Marca como atendido el servicio activo seleccionado.
	 */
	private void marcarAtendida() {
		PanelServicios panel = vista.getPanelServicios();
		ejecutar(() -> {
			Solicitud solicitud = panel.getServicioSeleccionado();
			modelo.marcarAtendida(solicitud);
			return "Servicio " + solicitud.getCodigo() + " en atención";
		});
	}

	/**
	 * Marca como solucionado el servicio activo seleccionado, previa
	 * confirmación.
	 */
	private void solucionarServicio() {
		PanelServicios panel = vista.getPanelServicios();
		Solicitud solicitud = panel.getServicioSeleccionado();
		if (solicitud != null && !vista.confirmar("¿Confirma que el servicio " + solicitud.getCodigo()
				+ " quedó solucionado?\nLa unidad y el técnico quedarán libres.")) {
			return;
		}
		ejecutar(() -> {
			modelo.solucionarServicio(solicitud);
			return "Servicio " + solicitud.getCodigo() + " solucionado";
		});
	}

	/**
	 * Registra el maletín escrito en el formulario.
	 */
	private void registrarMaletin() {
		PanelInsumos panel = vista.getPanelInsumos();
		boolean exito = ejecutar(() -> "Maletín registrado: "
				+ modelo.registrarMaletin(panel.getCodigoMaletin(), panel.getContenidoMaletin()).getCodigo());
		if (exito) {
			panel.limpiarMaletin();
		}
	}

	/**
	 * Audita el maletín de la cima de la pila de auditoría.
	 */
	private void auditarMaletin() {
		PanelInsumos panel = vista.getPanelInsumos();
		ejecutar(() -> {
			Maletin maletin = modelo.auditarSiguienteMaletin(panel.isReemplazarPieza());
			return "Maletín " + maletin.getCodigo() + " auditado y devuelto al almacén";
		});
	}

	/**
	 * Apila el repuesto escrito en el formulario.
	 */
	private void apilarRepuesto() {
		PanelInsumos panel = vista.getPanelInsumos();
		boolean exito = ejecutar(() -> "Repuesto apilado: " + modelo.apilarRepuesto(panel.getNombreRepuesto()));
		if (exito) {
			panel.limpiarRepuesto();
		}
	}

	/**
	 * Despacha el repuesto de la cima de la pila.
	 */
	private void despacharRepuesto() {
		ejecutar(() -> {
			Repuesto repuesto = modelo.despacharRepuesto();
			return "Repuesto despachado: " + repuesto;
		});
	}

	/**
	 * Deshace la última transacción, previa confirmación del supervisor.
	 */
	private void deshacer() {
		if (modelo.getPilaDeshacer().estaVacia()) {
			vista.mostrarError("No hay transacciones para deshacer.");
			return;
		}
		Transaccion ultima = modelo.getPilaDeshacer().obtener(0);
		if (!vista.confirmar("¿Deshacer la última transacción?\n\n" + ultima.getDescripcion())) {
			return;
		}
		ejecutar(() -> "Se deshizo: " + modelo.deshacer().getDescripcion());
	}

	/**
	 * Exporta el reporte CSV con las incidencias solucionadas hoy.
	 */
	private void exportarReporte() {
		File archivo = vista.elegirArchivoCSV("reporte_incidencias_" + LocalDate.now() + ".csv");
		if (archivo == null) {
			return;
		}
		try {
			int cantidad = modelo.exportarReporteDiario(archivo);
			refrescarVista();
			vista.setEstado("Reporte exportado: " + archivo.getAbsolutePath());
			vista.mostrarInformacion(
					"Se exportaron " + cantidad + " incidencias solucionadas a:\n" + archivo.getAbsolutePath());
		} catch (ReglaNegocioException e) {
			vista.mostrarError(e.getMessage());
		} catch (IOException e) {
			vista.mostrarError("No se pudo escribir el archivo: " + e.getMessage());
		}
	}

	/**
	 * Calcula y muestra el puntaje estimado de la solicitud que se está
	 * digitando, antes de registrarla.
	 */
	private void actualizarVistaPrevia() {
		PanelSolicitudes panel = vista.getPanelSolicitudes();
		Cliente cliente = panel.getCliente();
		if (cliente == null) {
			panel.setVistaPrevia("Seleccione un cliente para calcular la prioridad.");
			return;
		}
		int puntaje = PoliticaPrioridad.calcularPuntaje(panel.getRiesgo(), panel.getAfectacion(),
				panel.getSeveridad(), cliente.getTipo());
		boolean critica = PoliticaPrioridad.esCritica(panel.getRiesgo(), panel.getAfectacion(), panel.getSeveridad(),
				cliente.getTipo());
		panel.setVistaPrevia("Puntaje estimado: " + puntaje + " -> " + (critica ? "CRÍTICA (cola prioritaria)"
				: "ordinaria (cola regular)"));
	}

	/**
	 * Vuelve a mostrar todos los datos del modelo en todas las pestañas.
	 */
	private void refrescarVista() {
		vista.getPanelUnidades().mostrarUnidades(modelo.getUnidades());
		vista.getPanelTecnicos().mostrarTecnicos(modelo.getTecnicos());
		vista.getPanelClientes().mostrarClientes(modelo.getClientes());
		vista.getPanelSolicitudes().mostrarClientes(modelo.getClientes());
		vista.getPanelSolicitudes().mostrarColas(modelo.getColaPrioritaria(), modelo.getColaRegular());
		vista.getPanelServicios().mostrarServicios(modelo.getServiciosActivos(), modelo.getServiciosSolucionados());
		vista.getPanelInsumos().mostrarInsumos(modelo.getMaletines(), modelo.getPilaAuditoria(),
				modelo.getPilaRepuestos());
		vista.getPanelHistorial().mostrarHistorial(modelo.getPilaDeshacer(), modelo.getBitacora());
		vista.getPanelTablero().mostrarActivos(modelo.getUnidades(), modelo.getTecnicos());
		vista.getItemDeshacer().setEnabled(!modelo.getPilaDeshacer().estaVacia());
		vista.getPanelHistorial().getBtnDeshacer().setEnabled(!modelo.getPilaDeshacer().estaVacia());
		String siguiente = textoSiguiente();
		vista.getPanelSolicitudes().setSiguiente(siguiente);
		vista.getPanelTablero().setSiguiente(siguiente);
		actualizarTablero();
	}

	/**
	 * Actualiza el reloj y los indicadores del tablero.
	 */
	private void actualizarTablero() {
		PanelTablero tablero = vista.getPanelTablero();
		tablero.setReloj("Centro de control  |  " + LocalDateTime.now().format(FORMATO_RELOJ));
		tablero.setIndicadoresFlota(modelo.contarUnidades(EstadoUnidad.DISPONIBLE),
				modelo.contarUnidades(EstadoUnidad.ASIGNADA), modelo.contarUnidades(EstadoUnidad.MANTENIMIENTO));
		tablero.setIndicadoresTecnicos(modelo.contarTecnicos(EstadoTecnico.DISPONIBLE),
				modelo.contarTecnicos(EstadoTecnico.EN_SERVICIO), modelo.contarTecnicos(EstadoTecnico.FUERA_DE_TURNO));
		tablero.setIndicadoresOperacion(modelo.getColaPrioritaria().tamano(), modelo.getColaRegular().tamano(),
				modelo.getServiciosActivos().tamano(), modelo.solucionadasDelDia(LocalDate.now()).tamano(),
				modelo.getPilaAuditoria().tamano(), modelo.getPilaRepuestos().tamano());
	}

	/**
	 * Construye el texto que describe la siguiente solicitud en turno.
	 *
	 * @return descripción de la siguiente solicitud
	 */
	private String textoSiguiente() {
		Solicitud siguiente = modelo.siguienteSolicitud();
		if (siguiente == null) {
			return "Siguiente en turno: no hay solicitudes pendientes.";
		}
		return "Siguiente en turno: " + siguiente.getCodigo() + " - " + siguiente.getTipoIncidente() + " - "
				+ siguiente.getCliente().getNombre() + " - puntaje " + siguiente.getPuntaje()
				+ (siguiente.isPrioritaria() ? " (CRÍTICA)" : " (ordinaria)");
	}

	/**
	 * Muestra la información de la aplicación.
	 */
	private void mostrarAcercaDe() {
		vista.mostrarInformacion(VentanaPrincipal.TITULO + "\n\nProyecto final de Ciencias de la Computación I\n"
				+ "Universidad Distrital Francisco José de Caldas\n\n"
				+ "Reynel Felipe Amezquita Puentes - 20252020056\n" + "Nancy Gabriela Jaimes Luna - 20252020055\n"
				+ "Cristian Camilo Velosa Ortiz - 20252020066");
	}

	/**
	 * Cierra la aplicación, previa confirmación.
	 */
	private void salir() {
		if (vista.confirmar("¿Desea cerrar el centro de control?\nLos datos no se guardan al salir.")) {
			reloj.stop();
			vista.dispose();
			System.exit(0);
		}
	}
}
