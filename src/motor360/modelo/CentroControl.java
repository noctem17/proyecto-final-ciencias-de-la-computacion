package motor360.modelo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import motor360.estructuras.Cola;
import motor360.estructuras.ColaPrioridad;
import motor360.estructuras.ListaEnlazada;
import motor360.estructuras.Pila;
import motor360.estructuras.Secuencia;

/**
 * Fachada del modelo: representa el centro de control de Asistencia Motor
 * 360. Contiene todas las estructuras de datos del sistema y es el único
 * lugar donde se modifican los datos, por lo que aquí se validan todas las
 * reglas de negocio.
 * <p>
 * Cada operación que modifica datos se registra como una {@link Transaccion}
 * en la pila de deshacer y como una entrada en la bitácora, para que el
 * supervisor pueda revertir errores de digitación en orden inverso.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class CentroControl {

	/** Tipo de registro de bitácora para operaciones normales. */
	public static final String TIPO_OPERACION = "OPERACIÓN";

	/** Tipo de registro de bitácora para operaciones deshechas. */
	public static final String TIPO_REVERSION = "REVERSIÓN";

	/** Tipo de registro de bitácora para eventos del sistema. */
	public static final String TIPO_SISTEMA = "SISTEMA";

	/** Inventario de unidades móviles. */
	private final ListaEnlazada<Unidad> unidades;

	/** Personal técnico registrado. */
	private final ListaEnlazada<Tecnico> tecnicos;

	/** Clientes registrados. */
	private final ListaEnlazada<Cliente> clientes;

	/** Inventario de maletines de respuesta rápida. */
	private final ListaEnlazada<Maletin> maletines;

	/** Solicitudes ordinarias en espera, en orden de llegada. */
	private final Cola<Solicitud> colaRegular;

	/** Solicitudes críticas en espera, ordenadas por puntaje. */
	private final ColaPrioridad<Solicitud> colaPrioritaria;

	/** Rescates en curso: solicitudes asignadas o atendidas. */
	private final ListaEnlazada<Solicitud> serviciosActivos;

	/** Incidencias solucionadas. */
	private final ListaEnlazada<Solicitud> serviciosSolucionados;

	/** Maletines usados que esperan auditoría; se revisa primero el último. */
	private final Pila<Maletin> pilaAuditoria;

	/** Repuestos de alta rotación; se despacha primero el último acomodado. */
	private final Pila<Repuesto> pilaRepuestos;

	/** Transacciones que pueden deshacerse; la cima es la más reciente. */
	private final Pila<Transaccion> pilaDeshacer;

	/** Historial de acciones; el primer elemento es el más reciente. */
	private final ListaEnlazada<RegistroBitacora> bitacora;

	/** Último consecutivo usado para los códigos de solicitud. */
	private int consecutivoSolicitud;

	/** Último consecutivo usado para los códigos de repuesto. */
	private int consecutivoRepuesto;

	/** Último turno de llegada asignado. */
	private long turnoLlegada;

	/**
	 * Crea un centro de control sin datos.
	 */
	public CentroControl() {
		unidades = new ListaEnlazada<>();
		tecnicos = new ListaEnlazada<>();
		clientes = new ListaEnlazada<>();
		maletines = new ListaEnlazada<>();
		colaRegular = new Cola<>();
		colaPrioritaria = new ColaPrioridad<>();
		serviciosActivos = new ListaEnlazada<>();
		serviciosSolucionados = new ListaEnlazada<>();
		pilaAuditoria = new Pila<>();
		pilaRepuestos = new Pila<>();
		pilaDeshacer = new Pila<>();
		bitacora = new ListaEnlazada<>();
		consecutivoSolicitud = 0;
		consecutivoRepuesto = 0;
		turnoLlegada = 0;
	}

	/**
	 * Registra una nueva unidad móvil con un UUID generado automáticamente.
	 *
	 * @param placa placa del vehículo, única en el sistema
	 * @param tipo  categoría de la unidad
	 * @param zona  zona de operación
	 * @param nivel nivel de disponibilidad
	 * @return la unidad registrada
	 * @throws ReglaNegocioException si falta algún dato o la placa ya existe
	 */
	public Unidad registrarUnidad(String placa, TipoUnidad tipo, Zona zona, NivelDisponibilidad nivel)
			throws ReglaNegocioException {
		String placaLimpia = textoObligatorio(placa, "placa").toUpperCase();
		requerir(tipo, "Debe indicar la categoría de la unidad (regla de negocio 2).");
		requerir(zona, "Debe indicar la zona de operación de la unidad (regla de negocio 2).");
		requerir(nivel, "Debe indicar el nivel de disponibilidad de la unidad (regla de negocio 2).");
		if (buscarUnidadPorPlaca(placaLimpia) != null) {
			throw new ReglaNegocioException("Ya existe una unidad con la placa " + placaLimpia + ".");
		}
		Unidad unidad = new Unidad(placaLimpia, tipo, zona, nivel);
		unidades.agregar(unidad);
		registrarTransaccion("Registro de unidad " + placaLimpia + " con UUID " + unidad.getId(),
				() -> unidades.eliminar(unidad));
		return unidad;
	}

	/**
	 * Cambia manualmente el estatus de una unidad entre disponible y
	 * mantenimiento. El estatus asignada solo se obtiene al despachar un
	 * servicio, y una unidad asignada solo se libera al solucionarlo.
	 *
	 * @param unidad unidad a modificar
	 * @param nuevo  nuevo estatus
	 * @throws ReglaNegocioException si el cambio incumple las reglas
	 */
	public void cambiarEstadoUnidad(Unidad unidad, EstadoUnidad nuevo) throws ReglaNegocioException {
		requerir(unidad, "Debe seleccionar una unidad.");
		requerir(nuevo, "Debe seleccionar el nuevo estatus.");
		if (nuevo == EstadoUnidad.ASIGNADA) {
			throw new ReglaNegocioException(
					"Una unidad solo queda asignada al despachar un servicio desde la cola de solicitudes.");
		}
		if (unidad.getEstado() == EstadoUnidad.ASIGNADA) {
			throw new ReglaNegocioException("La unidad " + unidad.getPlaca()
					+ " está asignada a un servicio activo; se libera al solucionar ese servicio (regla de negocio 4).");
		}
		if (unidad.getEstado() == nuevo) {
			throw new ReglaNegocioException("La unidad " + unidad.getPlaca() + " ya está en estado " + nuevo + ".");
		}
		EstadoUnidad anterior = unidad.getEstado();
		unidad.setEstado(nuevo);
		registrarTransaccion("Cambio de estatus de la unidad " + unidad.getPlaca() + ": " + anterior + " -> " + nuevo,
				() -> unidad.setEstado(anterior));
	}

	/**
	 * Cambia el nivel de disponibilidad de una unidad.
	 *
	 * @param unidad unidad a modificar
	 * @param nuevo  nuevo nivel de disponibilidad
	 * @throws ReglaNegocioException si falta algún dato o el nivel no cambia
	 */
	public void cambiarNivelUnidad(Unidad unidad, NivelDisponibilidad nuevo) throws ReglaNegocioException {
		requerir(unidad, "Debe seleccionar una unidad.");
		requerir(nuevo, "Debe seleccionar el nuevo nivel de disponibilidad.");
		if (unidad.getNivelDisponibilidad() == nuevo) {
			throw new ReglaNegocioException("La unidad " + unidad.getPlaca() + " ya tiene nivel " + nuevo + ".");
		}
		NivelDisponibilidad anterior = unidad.getNivelDisponibilidad();
		unidad.setNivelDisponibilidad(nuevo);
		registrarTransaccion("Cambio de nivel de disponibilidad de la unidad " + unidad.getPlaca() + ": " + anterior
				+ " -> " + nuevo, () -> unidad.setNivelDisponibilidad(anterior));
	}

	/**
	 * Registra un nuevo técnico.
	 *
	 * @param identificacion número de identificación, único en el sistema
	 * @param nombre         nombre completo
	 * @param especialidad   área de especialidad
	 * @param zona           sector de patrullaje
	 * @return el técnico registrado
	 * @throws ReglaNegocioException si falta algún dato o la identificación ya
	 *                               existe
	 */
	public Tecnico registrarTecnico(String identificacion, String nombre, Especialidad especialidad, Zona zona)
			throws ReglaNegocioException {
		String id = textoObligatorio(identificacion, "identificación");
		String nombreLimpio = textoObligatorio(nombre, "nombre");
		requerir(especialidad, "Debe indicar el área de especialidad del técnico (regla de negocio 3).");
		requerir(zona, "Debe indicar el sector de patrullaje del técnico.");
		if (buscarTecnico(id) != null) {
			throw new ReglaNegocioException("Ya existe un técnico con la identificación " + id + ".");
		}
		Tecnico tecnico = new Tecnico(id, nombreLimpio, especialidad, zona);
		tecnicos.agregar(tecnico);
		registrarTransaccion("Registro del técnico " + nombreLimpio + " (" + id + ")", () -> tecnicos.eliminar(tecnico));
		return tecnico;
	}

	/**
	 * Cambia manualmente el estado de un técnico entre libre y fuera de turno.
	 * El estado en servicio solo se obtiene al despachar un servicio.
	 *
	 * @param tecnico técnico a modificar
	 * @param nuevo   nuevo estado
	 * @throws ReglaNegocioException si el cambio incumple las reglas
	 */
	public void cambiarEstadoTecnico(Tecnico tecnico, EstadoTecnico nuevo) throws ReglaNegocioException {
		requerir(tecnico, "Debe seleccionar un técnico.");
		requerir(nuevo, "Debe seleccionar el nuevo estado.");
		if (nuevo == EstadoTecnico.EN_SERVICIO) {
			throw new ReglaNegocioException(
					"Un técnico solo queda en servicio al despachar una solicitud desde la cola.");
		}
		if (tecnico.getEstado() == EstadoTecnico.EN_SERVICIO) {
			throw new ReglaNegocioException("El técnico " + tecnico.getNombre()
					+ " está atendiendo un servicio; queda libre al solucionarlo.");
		}
		if (tecnico.getEstado() == nuevo) {
			throw new ReglaNegocioException("El técnico " + tecnico.getNombre() + " ya está en estado " + nuevo + ".");
		}
		EstadoTecnico anterior = tecnico.getEstado();
		tecnico.setEstado(nuevo);
		registrarTransaccion("Cambio de estado del técnico " + tecnico.getNombre() + ": " + anterior + " -> " + nuevo,
				() -> tecnico.setEstado(anterior));
	}

	/**
	 * Registra un nuevo cliente.
	 *
	 * @param documento documento o NIT, único en el sistema
	 * @param nombre    nombre o razón social
	 * @param telefono  teléfono de contacto
	 * @param tipo      perfil del cliente
	 * @return el cliente registrado
	 * @throws ReglaNegocioException si falta algún dato o el documento ya
	 *                               existe
	 */
	public Cliente registrarCliente(String documento, String nombre, String telefono, TipoCliente tipo)
			throws ReglaNegocioException {
		String doc = textoObligatorio(documento, "documento");
		String nombreLimpio = textoObligatorio(nombre, "nombre");
		String tel = textoObligatorio(telefono, "teléfono");
		requerir(tipo, "Debe indicar el tipo de cliente.");
		if (buscarCliente(doc) != null) {
			throw new ReglaNegocioException("Ya existe un cliente con el documento " + doc + ".");
		}
		Cliente cliente = new Cliente(doc, nombreLimpio, tel, tipo);
		clientes.agregar(cliente);
		registrarTransaccion("Registro del cliente " + nombreLimpio + " (" + doc + ")", () -> clientes.eliminar(cliente));
		return cliente;
	}

	/**
	 * Registra un nuevo maletín de respuesta rápida en el almacén.
	 *
	 * @param codigo    código del maletín, único en el sistema
	 * @param contenido descripción del contenido
	 * @return el maletín registrado
	 * @throws ReglaNegocioException si falta algún dato o el código ya existe
	 */
	public Maletin registrarMaletin(String codigo, String contenido) throws ReglaNegocioException {
		String cod = textoObligatorio(codigo, "código").toUpperCase();
		String cont = textoObligatorio(contenido, "contenido");
		if (buscarMaletin(cod) != null) {
			throw new ReglaNegocioException("Ya existe un maletín con el código " + cod + ".");
		}
		Maletin maletin = new Maletin(cod, cont);
		maletines.agregar(maletin);
		registrarTransaccion("Registro del maletín " + cod, () -> maletines.eliminar(maletin));
		return maletin;
	}

	/**
	 * Registra una solicitud de asistencia. Según la política de prioridad, la
	 * solicitud entra a la cola regular, que respeta el orden de llegada, o a
	 * la cola prioritaria, que atiende primero los casos más graves.
	 *
	 * @param cliente       cliente que reporta; es obligatorio
	 * @param tipoIncidente tipo de incidente
	 * @param zona          zona del incidente
	 * @param ubicacion     dirección o referencia
	 * @param descripcion   descripción del incidente
	 * @param riesgo        nivel de peligro, de 1 a 5
	 * @param afectacion    afectación vial, de 1 a 5
	 * @param severidad     gravedad de la avería, de 1 a 5
	 * @return la solicitud registrada
	 * @throws ReglaNegocioException si no hay cliente o algún dato es inválido
	 */
	public Solicitud registrarSolicitud(Cliente cliente, TipoIncidente tipoIncidente, Zona zona, String ubicacion,
			String descripcion, int riesgo, int afectacion, int severidad) throws ReglaNegocioException {
		requerir(cliente, "Toda solicitud debe estar vinculada a un cliente registrado (regla de negocio 7).");
		if (!clientes.contiene(cliente)) {
			throw new ReglaNegocioException("El cliente no está registrado en el sistema (regla de negocio 7).");
		}
		requerir(tipoIncidente, "Debe indicar el tipo de incidente.");
		requerir(zona, "Debe indicar la zona del incidente.");
		String ubic = textoObligatorio(ubicacion, "ubicación");
		String desc = descripcion == null ? "" : descripcion.trim();
		if (!PoliticaPrioridad.enRango(riesgo) || !PoliticaPrioridad.enRango(afectacion)
				|| !PoliticaPrioridad.enRango(severidad)) {
			throw new ReglaNegocioException("El riesgo, la afectación vial y la severidad deben estar entre "
					+ PoliticaPrioridad.MINIMO + " y " + PoliticaPrioridad.MAXIMO + ".");
		}
		consecutivoSolicitud++;
		turnoLlegada++;
		String codigo = String.format("SOL-%04d", consecutivoSolicitud);
		Solicitud solicitud = new Solicitud(codigo, turnoLlegada, cliente, tipoIncidente, zona, ubic, desc, riesgo,
				afectacion, severidad);
		boolean critica = PoliticaPrioridad.esCritica(riesgo, afectacion, severidad, cliente.getTipo());
		solicitud.setPrioritaria(critica);
		if (critica) {
			colaPrioritaria.insertar(solicitud);
		} else {
			colaRegular.encolar(solicitud);
		}
		registrarTransaccion("Registro de la solicitud " + codigo + " (" + tipoIncidente + ", puntaje "
				+ solicitud.getPuntaje() + ") en la cola " + (critica ? "prioritaria" : "regular"), () -> {
					if (solicitud.isPrioritaria()) {
						colaPrioritaria.eliminar(solicitud);
					} else {
						colaRegular.eliminar(solicitud);
					}
				});
		return solicitud;
	}

	/**
	 * Escala manualmente una solicitud ordinaria pendiente a la cola
	 * prioritaria (regla de negocio 11).
	 *
	 * @param solicitud solicitud ordinaria a escalar
	 * @throws ReglaNegocioException si la solicitud no está en la cola regular
	 */
	public void escalarSolicitud(Solicitud solicitud) throws ReglaNegocioException {
		requerir(solicitud, "Debe seleccionar una solicitud de la cola regular.");
		if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
			throw new ReglaNegocioException("Solo se pueden escalar solicitudes pendientes.");
		}
		if (solicitud.isPrioritaria()) {
			throw new ReglaNegocioException("La solicitud " + solicitud.getCodigo() + " ya es prioritaria.");
		}
		int posicion = colaRegular.indiceDe(solicitud);
		if (posicion < 0) {
			throw new ReglaNegocioException("La solicitud no se encuentra en la cola regular.");
		}
		colaRegular.eliminar(solicitud);
		solicitud.setPrioritaria(true);
		solicitud.setEscaladaManualmente(true);
		colaPrioritaria.insertar(solicitud);
		registrarTransaccion("Escalamiento manual de la solicitud " + solicitud.getCodigo() + " a la cola prioritaria",
				() -> {
					colaPrioritaria.eliminar(solicitud);
					solicitud.setPrioritaria(false);
					solicitud.setEscaladaManualmente(false);
					colaRegular.reinsertarEn(posicion, solicitud);
				});
	}

	/**
	 * Consulta cuál es la próxima solicitud que debe atenderse: primero las
	 * prioritarias y, si no hay, la ordinaria más antigua.
	 *
	 * @return la siguiente solicitud, o {@code null} si no hay pendientes
	 */
	public Solicitud siguienteSolicitud() {
		if (!colaPrioritaria.estaVacia()) {
			return colaPrioritaria.verFrente();
		}
		return colaRegular.verFrente();
	}

	/**
	 * Despacha la siguiente solicitud asignándole unidad, técnico y,
	 * opcionalmente, un maletín. La solicitud sale de su cola y pasa a los
	 * servicios activos.
	 *
	 * @param solicitud solicitud a despachar; debe ser la siguiente en turno
	 * @param unidad    unidad que atenderá el servicio
	 * @param tecnico   técnico que atenderá el servicio
	 * @param maletin   maletín de respuesta rápida, o {@code null} si no se usa
	 * @throws ReglaNegocioException si algún recurso no puede asignarse
	 */
	public void asignarRecursos(Solicitud solicitud, Unidad unidad, Tecnico tecnico, Maletin maletin)
			throws ReglaNegocioException {
		requerir(solicitud, "No hay solicitudes pendientes por despachar.");
		if (solicitud != siguienteSolicitud()) {
			throw new ReglaNegocioException("La solicitud " + solicitud.getCodigo()
					+ " no es la siguiente en turno; las solicitudes se atienden en orden (regla de negocio 10).");
		}
		requerir(unidad, "Debe seleccionar una unidad para el servicio.");
		requerir(tecnico, "Debe seleccionar un técnico para el servicio.");
		if (unidad.getEstado() == EstadoUnidad.MANTENIMIENTO) {
			throw new ReglaNegocioException("La unidad " + unidad.getPlaca()
					+ " está en mantenimiento y no puede asignarse a ningún caso (regla de negocio 6).");
		}
		if (unidad.getEstado() != EstadoUnidad.DISPONIBLE) {
			throw new ReglaNegocioException("La unidad " + unidad.getPlaca()
					+ " ya está asignada a otro servicio (regla de negocio 4).");
		}
		if (tecnico.tieneServicioActivo() || tecnico.getEstado() == EstadoTecnico.EN_SERVICIO) {
			throw new ReglaNegocioException("El técnico " + tecnico.getNombre()
					+ " ya tiene un servicio activo y no puede tener otro (regla de negocio 5).");
		}
		if (tecnico.getEstado() != EstadoTecnico.DISPONIBLE) {
			throw new ReglaNegocioException("El técnico " + tecnico.getNombre() + " está " + tecnico.getEstado() + ".");
		}
		if (maletin != null && maletin.getEstado() != EstadoMaletin.DISPONIBLE) {
			throw new ReglaNegocioException("El maletín " + maletin.getCodigo() + " no está disponible.");
		}
		boolean eraPrioritaria = solicitud.isPrioritaria();
		if (eraPrioritaria) {
			colaPrioritaria.extraer();
		} else {
			colaRegular.desencolar();
		}
		solicitud.setUnidad(unidad);
		solicitud.setTecnico(tecnico);
		solicitud.setMaletin(maletin);
		solicitud.setEstado(EstadoSolicitud.ASIGNADA);
		unidad.setEstado(EstadoUnidad.ASIGNADA);
		tecnico.setEstado(EstadoTecnico.EN_SERVICIO);
		tecnico.setServicioActivo(solicitud);
		if (maletin != null) {
			maletin.setEstado(EstadoMaletin.EN_USO);
		}
		serviciosActivos.agregar(solicitud);
		registrarTransaccion("Despacho de la solicitud " + solicitud.getCodigo() + " con la unidad " + unidad.getPlaca()
				+ " y el técnico " + tecnico.getNombre() + (maletin != null ? " con el maletín " + maletin.getCodigo() : ""),
				() -> {
					serviciosActivos.eliminar(solicitud);
					if (maletin != null) {
						maletin.setEstado(EstadoMaletin.DISPONIBLE);
					}
					tecnico.setServicioActivo(null);
					tecnico.setEstado(EstadoTecnico.DISPONIBLE);
					unidad.setEstado(EstadoUnidad.DISPONIBLE);
					solicitud.setEstado(EstadoSolicitud.PENDIENTE);
					solicitud.setMaletin(null);
					solicitud.setTecnico(null);
					solicitud.setUnidad(null);
					if (eraPrioritaria) {
						colaPrioritaria.insertar(solicitud);
					} else {
						colaRegular.reinsertarEn(0, solicitud);
					}
				});
	}

	/**
	 * Marca un servicio activo como atendido, es decir, el técnico ya está
	 * atendiendo el incidente en el sitio.
	 *
	 * @param solicitud servicio activo
	 * @throws ReglaNegocioException si la solicitud no tiene recursos o no
	 *                               está asignada
	 */
	public void marcarAtendida(Solicitud solicitud) throws ReglaNegocioException {
		requerir(solicitud, "Debe seleccionar un servicio activo.");
		if (!solicitud.tieneRecursosAsignados()) {
			throw new ReglaNegocioException("La solicitud " + solicitud.getCodigo()
					+ " no puede pasar a atendida porque no tiene recursos asignados (regla de negocio 8).");
		}
		if (solicitud.getEstado() != EstadoSolicitud.ASIGNADA) {
			throw new ReglaNegocioException("Solo un servicio en estado asignada puede pasar a atendida.");
		}
		solicitud.setEstado(EstadoSolicitud.ATENDIDA);
		registrarTransaccion("La solicitud " + solicitud.getCodigo() + " pasa a atendida",
				() -> solicitud.setEstado(EstadoSolicitud.ASIGNADA));
	}

	/**
	 * Cierra un servicio atendido: la solicitud sale del flujo de servicios
	 * activos (regla de negocio 9), la unidad y el técnico quedan libres y el
	 * maletín usado se apila en la estación de auditoría.
	 *
	 * @param solicitud servicio atendido
	 * @throws ReglaNegocioException si el servicio no ha sido atendido
	 */
	public void solucionarServicio(Solicitud solicitud) throws ReglaNegocioException {
		requerir(solicitud, "Debe seleccionar un servicio activo.");
		if (solicitud.getEstado() != EstadoSolicitud.ATENDIDA) {
			throw new ReglaNegocioException("El servicio " + solicitud.getCodigo()
					+ " debe estar en estado atendida antes de marcarse como solucionado.");
		}
		int posicion = serviciosActivos.indiceDe(solicitud);
		if (posicion < 0) {
			throw new ReglaNegocioException("El servicio no se encuentra entre los servicios activos.");
		}
		Unidad unidad = solicitud.getUnidad();
		Tecnico tecnico = solicitud.getTecnico();
		Maletin maletin = solicitud.getMaletin();
		serviciosActivos.eliminarEn(posicion);
		solicitud.setEstado(EstadoSolicitud.SOLUCIONADA);
		solicitud.setFechaSolucion(LocalDateTime.now());
		serviciosSolucionados.agregar(solicitud);
		unidad.setEstado(EstadoUnidad.DISPONIBLE);
		tecnico.setServicioActivo(null);
		tecnico.setEstado(EstadoTecnico.DISPONIBLE);
		if (maletin != null) {
			maletin.setEstado(EstadoMaletin.EN_AUDITORIA);
			pilaAuditoria.apilar(maletin);
		}
		registrarTransaccion("Solución del servicio " + solicitud.getCodigo()
				+ (maletin != null ? "; el maletín " + maletin.getCodigo() + " pasa a auditoría" : ""), () -> {
					if (maletin != null) {
						if (pilaAuditoria.verCima() == maletin) {
							pilaAuditoria.desapilar();
						}
						maletin.setEstado(EstadoMaletin.EN_USO);
					}
					tecnico.setEstado(EstadoTecnico.EN_SERVICIO);
					tecnico.setServicioActivo(solicitud);
					unidad.setEstado(EstadoUnidad.ASIGNADA);
					serviciosSolucionados.eliminar(solicitud);
					solicitud.setFechaSolucion(null);
					solicitud.setEstado(EstadoSolicitud.ATENDIDA);
					serviciosActivos.insertarEn(posicion, solicitud);
				});
	}

	/**
	 * Audita el maletín que está en la cima de la pila de auditoría, es decir,
	 * el último que llegó. Si se indica, se reemplaza una pieza tomando el
	 * repuesto de la cima de la pila de repuestos. El maletín vuelve al
	 * almacén como disponible.
	 *
	 * @param reemplazarPieza {@code true} si hay que reemplazar una pieza
	 * @return el maletín auditado
	 * @throws ReglaNegocioException si no hay maletines por auditar o no hay
	 *                               repuestos para el reemplazo
	 */
	public Maletin auditarSiguienteMaletin(boolean reemplazarPieza) throws ReglaNegocioException {
		if (pilaAuditoria.estaVacia()) {
			throw new ReglaNegocioException("No hay maletines esperando auditoría.");
		}
		if (reemplazarPieza && pilaRepuestos.estaVacia()) {
			throw new ReglaNegocioException("No hay repuestos en la pila para reemplazar piezas.");
		}
		Maletin maletin = pilaAuditoria.desapilar();
		Repuesto repuesto = reemplazarPieza ? pilaRepuestos.desapilar() : null;
		maletin.setAuditorias(maletin.getAuditorias() + 1);
		if (repuesto != null) {
			maletin.setPiezasReemplazadas(maletin.getPiezasReemplazadas() + 1);
		}
		maletin.setEstado(EstadoMaletin.DISPONIBLE);
		registrarTransaccion("Auditoría del maletín " + maletin.getCodigo()
				+ (repuesto != null ? " reemplazando una pieza con " + repuesto : " con contenido completo"), () -> {
					maletin.setEstado(EstadoMaletin.EN_AUDITORIA);
					if (repuesto != null) {
						maletin.setPiezasReemplazadas(maletin.getPiezasReemplazadas() - 1);
						pilaRepuestos.apilar(repuesto);
					}
					maletin.setAuditorias(maletin.getAuditorias() - 1);
					pilaAuditoria.apilar(maletin);
				});
		return maletin;
	}

	/**
	 * Acomoda un repuesto de alta rotación en la cima de la pila de repuestos.
	 *
	 * @param nombre nombre del repuesto
	 * @return el repuesto acomodado
	 * @throws ReglaNegocioException si el nombre está vacío
	 */
	public Repuesto apilarRepuesto(String nombre) throws ReglaNegocioException {
		String nombreLimpio = textoObligatorio(nombre, "nombre del repuesto");
		consecutivoRepuesto++;
		Repuesto repuesto = new Repuesto(String.format("REP-%04d", consecutivoRepuesto), nombreLimpio);
		pilaRepuestos.apilar(repuesto);
		registrarTransaccion("Ingreso del repuesto " + repuesto + " a la pila", () -> pilaRepuestos.desapilar());
		return repuesto;
	}

	/**
	 * Despacha el repuesto de la cima de la pila, es decir, el último que fue
	 * acomodado.
	 *
	 * @return el repuesto despachado
	 * @throws ReglaNegocioException si la pila de repuestos está vacía
	 */
	public Repuesto despacharRepuesto() throws ReglaNegocioException {
		if (pilaRepuestos.estaVacia()) {
			throw new ReglaNegocioException("No hay repuestos para despachar.");
		}
		Repuesto repuesto = pilaRepuestos.desapilar();
		registrarTransaccion("Despacho del repuesto " + repuesto, () -> pilaRepuestos.apilar(repuesto));
		return repuesto;
	}

	/**
	 * Deshace la última transacción ejecutada. Llamadas sucesivas retroceden
	 * paso a paso en el orden inverso en que se realizaron las operaciones.
	 *
	 * @return la transacción deshecha
	 * @throws ReglaNegocioException si no hay transacciones por deshacer
	 */
	public Transaccion deshacer() throws ReglaNegocioException {
		if (pilaDeshacer.estaVacia()) {
			throw new ReglaNegocioException("No hay transacciones para deshacer.");
		}
		Transaccion transaccion = pilaDeshacer.desapilar();
		transaccion.deshacer();
		bitacora.agregarAlInicio(new RegistroBitacora(TIPO_REVERSION, "Se deshizo: " + transaccion.getDescripcion()));
		return transaccion;
	}

	/**
	 * Vacía la pila de deshacer, para que las operaciones previas no puedan
	 * revertirse. Se usa después de cargar los datos de demostración.
	 *
	 * @param motivo descripción que queda en la bitácora
	 */
	public void consolidarHistorial(String motivo) {
		pilaDeshacer.vaciar();
		bitacora.agregarAlInicio(new RegistroBitacora(TIPO_SISTEMA, motivo));
	}

	/**
	 * Exporta a un archivo CSV todas las incidencias solucionadas en el día de
	 * hoy.
	 *
	 * @param archivo archivo de destino
	 * @return número de incidencias exportadas
	 * @throws ReglaNegocioException si no hay incidencias solucionadas hoy
	 * @throws IOException           si no se puede escribir el archivo
	 */
	public int exportarReporteDiario(File archivo) throws ReglaNegocioException, IOException {
		requerir(archivo, "Debe indicar el archivo de destino.");
		ListaEnlazada<Solicitud> deHoy = solucionadasDelDia(LocalDate.now());
		if (deHoy.estaVacia()) {
			throw new ReglaNegocioException("No hay incidencias solucionadas hoy para exportar.");
		}
		ExportadorCSV.exportar(deHoy, archivo);
		bitacora.agregarAlInicio(new RegistroBitacora(TIPO_SISTEMA,
				"Reporte diario exportado con " + deHoy.tamano() + " incidencias en " + archivo.getName()));
		return deHoy.tamano();
	}

	/**
	 * Obtiene las incidencias solucionadas en una fecha.
	 *
	 * @param fecha día consultado
	 * @return lista con las incidencias solucionadas ese día
	 */
	public ListaEnlazada<Solicitud> solucionadasDelDia(LocalDate fecha) {
		return serviciosSolucionados
				.filtrar(s -> s.getFechaSolucion() != null && s.getFechaSolucion().toLocalDate().equals(fecha));
	}

	/**
	 * Obtiene las unidades listas para un nuevo despacho.
	 *
	 * @return lista de unidades disponibles
	 */
	public ListaEnlazada<Unidad> unidadesDisponibles() {
		return unidades.filtrar(u -> u.getEstado() == EstadoUnidad.DISPONIBLE);
	}

	/**
	 * Obtiene los técnicos libres y sin servicio activo.
	 *
	 * @return lista de técnicos disponibles
	 */
	public ListaEnlazada<Tecnico> tecnicosDisponibles() {
		return tecnicos.filtrar(t -> t.getEstado() == EstadoTecnico.DISPONIBLE && !t.tieneServicioActivo());
	}

	/**
	 * Obtiene los maletines que están en el almacén.
	 *
	 * @return lista de maletines disponibles
	 */
	public ListaEnlazada<Maletin> maletinesDisponibles() {
		return maletines.filtrar(m -> m.getEstado() == EstadoMaletin.DISPONIBLE);
	}

	/**
	 * Cuenta las unidades que tienen un estatus.
	 *
	 * @param estado estatus buscado
	 * @return cantidad de unidades con ese estatus
	 */
	public int contarUnidades(EstadoUnidad estado) {
		return unidades.contar(u -> u.getEstado() == estado);
	}

	/**
	 * Cuenta los técnicos que tienen un estado.
	 *
	 * @param estado estado buscado
	 * @return cantidad de técnicos con ese estado
	 */
	public int contarTecnicos(EstadoTecnico estado) {
		return tecnicos.contar(t -> t.getEstado() == estado);
	}

	/**
	 * Busca una unidad por su placa.
	 *
	 * @param placa placa buscada
	 * @return la unidad, o {@code null} si no existe
	 */
	public Unidad buscarUnidadPorPlaca(String placa) {
		return unidades.buscar(u -> u.getPlaca().equalsIgnoreCase(placa));
	}

	/**
	 * Busca un técnico por su identificación.
	 *
	 * @param identificacion identificación buscada
	 * @return el técnico, o {@code null} si no existe
	 */
	public Tecnico buscarTecnico(String identificacion) {
		return tecnicos.buscar(t -> t.getIdentificacion().equals(identificacion));
	}

	/**
	 * Busca un cliente por su documento.
	 *
	 * @param documento documento buscado
	 * @return el cliente, o {@code null} si no existe
	 */
	public Cliente buscarCliente(String documento) {
		return clientes.buscar(c -> c.getDocumento().equals(documento));
	}

	/**
	 * Busca un maletín por su código.
	 *
	 * @param codigo código buscado
	 * @return el maletín, o {@code null} si no existe
	 */
	public Maletin buscarMaletin(String codigo) {
		return maletines.buscar(m -> m.getCodigo().equalsIgnoreCase(codigo));
	}

	/**
	 * Obtiene el inventario de unidades en modo de solo lectura.
	 *
	 * @return secuencia de unidades
	 */
	public Secuencia<Unidad> getUnidades() {
		return unidades;
	}

	/**
	 * Obtiene el personal técnico en modo de solo lectura.
	 *
	 * @return secuencia de técnicos
	 */
	public Secuencia<Tecnico> getTecnicos() {
		return tecnicos;
	}

	/**
	 * Obtiene los clientes en modo de solo lectura.
	 *
	 * @return secuencia de clientes
	 */
	public Secuencia<Cliente> getClientes() {
		return clientes;
	}

	/**
	 * Obtiene el inventario de maletines en modo de solo lectura.
	 *
	 * @return secuencia de maletines
	 */
	public Secuencia<Maletin> getMaletines() {
		return maletines;
	}

	/**
	 * Obtiene la cola de solicitudes ordinarias en modo de solo lectura.
	 *
	 * @return secuencia en orden de llegada
	 */
	public Secuencia<Solicitud> getColaRegular() {
		return colaRegular;
	}

	/**
	 * Obtiene la cola de solicitudes prioritarias en modo de solo lectura.
	 *
	 * @return secuencia ordenada por prioridad
	 */
	public Secuencia<Solicitud> getColaPrioritaria() {
		return colaPrioritaria;
	}

	/**
	 * Obtiene los servicios activos en modo de solo lectura.
	 *
	 * @return secuencia de servicios activos
	 */
	public Secuencia<Solicitud> getServiciosActivos() {
		return serviciosActivos;
	}

	/**
	 * Obtiene las incidencias solucionadas en modo de solo lectura.
	 *
	 * @return secuencia de incidencias solucionadas
	 */
	public Secuencia<Solicitud> getServiciosSolucionados() {
		return serviciosSolucionados;
	}

	/**
	 * Obtiene la pila de maletines en auditoría en modo de solo lectura.
	 *
	 * @return secuencia desde la cima
	 */
	public Secuencia<Maletin> getPilaAuditoria() {
		return pilaAuditoria;
	}

	/**
	 * Obtiene la pila de repuestos en modo de solo lectura.
	 *
	 * @return secuencia desde la cima
	 */
	public Secuencia<Repuesto> getPilaRepuestos() {
		return pilaRepuestos;
	}

	/**
	 * Obtiene la pila de transacciones que se pueden deshacer en modo de solo
	 * lectura.
	 *
	 * @return secuencia desde la transacción más reciente
	 */
	public Secuencia<Transaccion> getPilaDeshacer() {
		return pilaDeshacer;
	}

	/**
	 * Obtiene el historial de acciones en modo de solo lectura.
	 *
	 * @return secuencia desde la acción más reciente
	 */
	public Secuencia<RegistroBitacora> getBitacora() {
		return bitacora;
	}

	/**
	 * Guarda una operación en la pila de deshacer y en la bitácora.
	 *
	 * @param descripcion descripción de la operación
	 * @param reversion   instrucciones para revertirla
	 */
	private void registrarTransaccion(String descripcion, Reversion reversion) {
		pilaDeshacer.apilar(new Transaccion(descripcion, reversion));
		bitacora.agregarAlInicio(new RegistroBitacora(TIPO_OPERACION, descripcion));
	}

	/**
	 * Verifica que un texto obligatorio tenga contenido.
	 *
	 * @param valor texto ingresado
	 * @param campo nombre del campo, para el mensaje de error
	 * @return el texto sin espacios al inicio ni al final
	 * @throws ReglaNegocioException si el texto está vacío
	 */
	private String textoObligatorio(String valor, String campo) throws ReglaNegocioException {
		if (valor == null || valor.trim().isEmpty()) {
			throw new ReglaNegocioException("El campo " + campo + " es obligatorio.");
		}
		return valor.trim();
	}

	/**
	 * Verifica que un dato obligatorio no sea nulo.
	 *
	 * @param objeto  dato a verificar
	 * @param mensaje mensaje de error si el dato es nulo
	 * @throws ReglaNegocioException si el dato es nulo
	 */
	private void requerir(Object objeto, String mensaje) throws ReglaNegocioException {
		if (objeto == null) {
			throw new ReglaNegocioException(mensaje);
		}
	}
}
