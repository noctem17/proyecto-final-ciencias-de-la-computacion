package motor360.modelo;

import java.util.UUID;

/**
 * Unidad móvil de la flota de rescate. Cada unidad se identifica con un
 * identificador universal único (UUID) generado automáticamente al
 * registrarla, y siempre tiene categoría, estatus, zona de operación y nivel
 * de disponibilidad.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Unidad {

	/** Identificador universal único de la unidad. */
	private final UUID id;

	/** Placa del vehículo. */
	private final String placa;

	/** Categoría de la unidad. */
	private final TipoUnidad tipo;

	/** Estatus actual de la unidad. */
	private EstadoUnidad estado;

	/** Zona donde opera la unidad. */
	private Zona zona;

	/** Nivel de disponibilidad operativa de la unidad. */
	private NivelDisponibilidad nivelDisponibilidad;

	/**
	 * Crea una unidad disponible con un UUID nuevo.
	 *
	 * @param placa               placa del vehículo
	 * @param tipo                categoría de la unidad
	 * @param zona                zona de operación
	 * @param nivelDisponibilidad nivel de disponibilidad operativa
	 */
	public Unidad(String placa, TipoUnidad tipo, Zona zona, NivelDisponibilidad nivelDisponibilidad) {
		this.id = UUID.randomUUID();
		this.placa = placa;
		this.tipo = tipo;
		this.zona = zona;
		this.nivelDisponibilidad = nivelDisponibilidad;
		this.estado = EstadoUnidad.DISPONIBLE;
	}

	/**
	 * Obtiene el identificador universal único.
	 *
	 * @return UUID de la unidad
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Obtiene la placa del vehículo.
	 *
	 * @return placa de la unidad
	 */
	public String getPlaca() {
		return placa;
	}

	/**
	 * Obtiene la categoría de la unidad.
	 *
	 * @return tipo de unidad
	 */
	public TipoUnidad getTipo() {
		return tipo;
	}

	/**
	 * Obtiene el estatus actual.
	 *
	 * @return estatus de la unidad
	 */
	public EstadoUnidad getEstado() {
		return estado;
	}

	/**
	 * Cambia el estatus de la unidad. Solo el modelo lo invoca, después de
	 * validar las reglas de negocio.
	 *
	 * @param estado nuevo estatus
	 */
	public void setEstado(EstadoUnidad estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene la zona de operación.
	 *
	 * @return zona de la unidad
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * Cambia la zona de operación.
	 *
	 * @param zona nueva zona
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * Obtiene el nivel de disponibilidad operativa.
	 *
	 * @return nivel de disponibilidad
	 */
	public NivelDisponibilidad getNivelDisponibilidad() {
		return nivelDisponibilidad;
	}

	/**
	 * Cambia el nivel de disponibilidad operativa.
	 *
	 * @param nivelDisponibilidad nuevo nivel
	 */
	public void setNivelDisponibilidad(NivelDisponibilidad nivelDisponibilidad) {
		this.nivelDisponibilidad = nivelDisponibilidad;
	}

	/**
	 * Indica si la unidad está lista para un nuevo despacho.
	 *
	 * @return {@code true} si el estatus es disponible
	 */
	public boolean estaDisponible() {
		return estado == EstadoUnidad.DISPONIBLE;
	}

	/**
	 * Devuelve un texto corto que identifica la unidad.
	 *
	 * @return placa, tipo y zona de la unidad
	 */
	@Override
	public String toString() {
		return placa + " - " + tipo + " (" + zona + ")";
	}
}
