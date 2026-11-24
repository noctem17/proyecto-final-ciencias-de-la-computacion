package motor360.modelo;

import java.time.LocalDateTime;

/**
 * Solicitud de asistencia reportada por un cliente. Guarda el incidente, los
 * factores con los que se calcula su prioridad, los recursos asignados y las
 * fechas de registro y de solución.
 * <p>
 * Implementa {@link Comparable} para que la cola de prioridad sepa cuál
 * solicitud atender primero: gana la de mayor puntaje y, si empatan, la que
 * llegó primero.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Solicitud implements Comparable<Solicitud> {

	/** Código legible de la solicitud, por ejemplo SOL-0001. */
	private final String codigo;

	/** Número de turno de llegada; define el orden de atención. */
	private final long numeroLlegada;

	/** Cliente que reporta el incidente. */
	private final Cliente cliente;

	/** Tipo de incidente reportado. */
	private final TipoIncidente tipoIncidente;

	/** Zona donde ocurrió el incidente. */
	private final Zona zona;

	/** Dirección o punto de referencia del incidente. */
	private final String ubicacion;

	/** Descripción del incidente dada por el cliente. */
	private final String descripcion;

	/** Nivel de peligro, de 1 a 5. */
	private final int nivelRiesgo;

	/** Afectación vial o congestión generada, de 1 a 5. */
	private final int afectacionVial;

	/** Gravedad de la avería, de 1 a 5. */
	private final int severidad;

	/** Puntaje de prioridad calculado con la política de la empresa. */
	private final int puntaje;

	/** Momento en que se registró la solicitud. */
	private final LocalDateTime fechaRegistro;

	/** Indica si la solicitud está en la cola prioritaria. */
	private boolean prioritaria;

	/** Indica si el supervisor la escaló manualmente. */
	private boolean escaladaManualmente;

	/** Etapa actual de la solicitud. */
	private EstadoSolicitud estado;

	/** Unidad asignada, o {@code null} si aún no tiene. */
	private Unidad unidad;

	/** Técnico asignado, o {@code null} si aún no tiene. */
	private Tecnico tecnico;

	/** Maletín asignado, o {@code null} si no se usó. */
	private Maletin maletin;

	/** Momento en que se solucionó, o {@code null} si sigue abierta. */
	private LocalDateTime fechaSolucion;

	/**
	 * Crea una solicitud pendiente y calcula su puntaje de prioridad.
	 *
	 * @param codigo         código legible
	 * @param numeroLlegada  turno de llegada
	 * @param cliente        cliente que reporta
	 * @param tipoIncidente  tipo de incidente
	 * @param zona           zona del incidente
	 * @param ubicacion      dirección o referencia
	 * @param descripcion    descripción del incidente
	 * @param nivelRiesgo    nivel de peligro, de 1 a 5
	 * @param afectacionVial afectación vial, de 1 a 5
	 * @param severidad      gravedad de la avería, de 1 a 5
	 */
	public Solicitud(String codigo, long numeroLlegada, Cliente cliente, TipoIncidente tipoIncidente, Zona zona,
			String ubicacion, String descripcion, int nivelRiesgo, int afectacionVial, int severidad) {
		this.codigo = codigo;
		this.numeroLlegada = numeroLlegada;
		this.cliente = cliente;
		this.tipoIncidente = tipoIncidente;
		this.zona = zona;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
		this.nivelRiesgo = nivelRiesgo;
		this.afectacionVial = afectacionVial;
		this.severidad = severidad;
		this.puntaje = PoliticaPrioridad.calcularPuntaje(nivelRiesgo, afectacionVial, severidad, cliente.getTipo());
		this.fechaRegistro = LocalDateTime.now();
		this.estado = EstadoSolicitud.PENDIENTE;
		this.prioritaria = false;
		this.escaladaManualmente = false;
	}

	/**
	 * Compara dos solicitudes para la cola de prioridad. Un resultado negativo
	 * indica que esta solicitud debe atenderse antes que la otra.
	 *
	 * @param otra solicitud con la que se compara
	 * @return negativo si esta va primero, positivo si va después, cero si
	 *         son equivalentes
	 */
	@Override
	public int compareTo(Solicitud otra) {
		if (this.puntaje != otra.puntaje) {
			return otra.puntaje - this.puntaje;
		}
		return Long.compare(this.numeroLlegada, otra.numeroLlegada);
	}

	/**
	 * Indica si la solicitud ya tiene los recursos mínimos asignados: una
	 * unidad y un técnico.
	 *
	 * @return {@code true} si tiene unidad y técnico
	 */
	public boolean tieneRecursosAsignados() {
		return unidad != null && tecnico != null;
	}

	/**
	 * Obtiene el código legible.
	 *
	 * @return código de la solicitud
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Obtiene el turno de llegada.
	 *
	 * @return número de llegada
	 */
	public long getNumeroLlegada() {
		return numeroLlegada;
	}

	/**
	 * Obtiene el cliente que reportó el incidente.
	 *
	 * @return cliente de la solicitud
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Obtiene el tipo de incidente.
	 *
	 * @return tipo de incidente
	 */
	public TipoIncidente getTipoIncidente() {
		return tipoIncidente;
	}

	/**
	 * Obtiene la zona del incidente.
	 *
	 * @return zona del incidente
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * Obtiene la dirección o referencia del incidente.
	 *
	 * @return ubicación del incidente
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * Obtiene la descripción del incidente.
	 *
	 * @return descripción del incidente
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Obtiene el nivel de riesgo.
	 *
	 * @return nivel de riesgo, de 1 a 5
	 */
	public int getNivelRiesgo() {
		return nivelRiesgo;
	}

	/**
	 * Obtiene la afectación vial.
	 *
	 * @return afectación vial, de 1 a 5
	 */
	public int getAfectacionVial() {
		return afectacionVial;
	}

	/**
	 * Obtiene la severidad de la avería.
	 *
	 * @return severidad, de 1 a 5
	 */
	public int getSeveridad() {
		return severidad;
	}

	/**
	 * Obtiene el puntaje de prioridad.
	 *
	 * @return puntaje entre 9 y 45
	 */
	public int getPuntaje() {
		return puntaje;
	}

	/**
	 * Obtiene el momento de registro.
	 *
	 * @return fecha y hora de registro
	 */
	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * Indica si la solicitud es prioritaria.
	 *
	 * @return {@code true} si está o estuvo en la cola prioritaria
	 */
	public boolean isPrioritaria() {
		return prioritaria;
	}

	/**
	 * Marca la solicitud como prioritaria u ordinaria.
	 *
	 * @param prioritaria {@code true} para marcarla como prioritaria
	 */
	public void setPrioritaria(boolean prioritaria) {
		this.prioritaria = prioritaria;
	}

	/**
	 * Indica si el supervisor la escaló manualmente.
	 *
	 * @return {@code true} si fue escalada a mano
	 */
	public boolean isEscaladaManualmente() {
		return escaladaManualmente;
	}

	/**
	 * Marca si la solicitud fue escalada manualmente.
	 *
	 * @param escaladaManualmente {@code true} si fue escalada a mano
	 */
	public void setEscaladaManualmente(boolean escaladaManualmente) {
		this.escaladaManualmente = escaladaManualmente;
	}

	/**
	 * Obtiene la etapa actual.
	 *
	 * @return estado de la solicitud
	 */
	public EstadoSolicitud getEstado() {
		return estado;
	}

	/**
	 * Cambia la etapa de la solicitud.
	 *
	 * @param estado nuevo estado
	 */
	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene la unidad asignada.
	 *
	 * @return unidad asignada, o {@code null}
	 */
	public Unidad getUnidad() {
		return unidad;
	}

	/**
	 * Asigna o retira la unidad.
	 *
	 * @param unidad unidad asignada, o {@code null}
	 */
	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	/**
	 * Obtiene el técnico asignado.
	 *
	 * @return técnico asignado, o {@code null}
	 */
	public Tecnico getTecnico() {
		return tecnico;
	}

	/**
	 * Asigna o retira el técnico.
	 *
	 * @param tecnico técnico asignado, o {@code null}
	 */
	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	/**
	 * Obtiene el maletín asignado.
	 *
	 * @return maletín asignado, o {@code null}
	 */
	public Maletin getMaletin() {
		return maletin;
	}

	/**
	 * Asigna o retira el maletín.
	 *
	 * @param maletin maletín asignado, o {@code null}
	 */
	public void setMaletin(Maletin maletin) {
		this.maletin = maletin;
	}

	/**
	 * Obtiene el momento de solución.
	 *
	 * @return fecha y hora de solución, o {@code null} si sigue abierta
	 */
	public LocalDateTime getFechaSolucion() {
		return fechaSolucion;
	}

	/**
	 * Registra o borra el momento de solución.
	 *
	 * @param fechaSolucion fecha y hora de solución, o {@code null}
	 */
	public void setFechaSolucion(LocalDateTime fechaSolucion) {
		this.fechaSolucion = fechaSolucion;
	}

	/**
	 * Devuelve un texto corto que identifica la solicitud.
	 *
	 * @return código, incidente, cliente y puntaje
	 */
	@Override
	public String toString() {
		return codigo + " - " + tipoIncidente + " - " + cliente.getNombre() + " (puntaje " + puntaje + ")";
	}
}
