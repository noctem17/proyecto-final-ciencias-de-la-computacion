package motor360.modelo;

/**
 * Integrante del personal técnico. Guarda su identificación, especialidad,
 * estado vigente, sector de patrullaje y el servicio que tiene a cargo. Un
 * técnico solo puede tener un servicio activo a la vez.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Tecnico {

	/** Número de identificación del técnico. */
	private final String identificacion;

	/** Nombre completo del técnico. */
	private final String nombre;

	/** Área de especialidad asignada. */
	private final Especialidad especialidad;

	/** Estado vigente del técnico. */
	private EstadoTecnico estado;

	/** Sector de patrullaje del técnico. */
	private Zona zona;

	/** Servicio activo a cargo del técnico, o {@code null} si no tiene. */
	private Solicitud servicioActivo;

	/**
	 * Crea un técnico libre y sin servicio a cargo.
	 *
	 * @param identificacion número de identificación
	 * @param nombre         nombre completo
	 * @param especialidad   área de especialidad
	 * @param zona           sector de patrullaje
	 */
	public Tecnico(String identificacion, String nombre, Especialidad especialidad, Zona zona) {
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.zona = zona;
		this.estado = EstadoTecnico.DISPONIBLE;
		this.servicioActivo = null;
	}

	/**
	 * Obtiene el número de identificación.
	 *
	 * @return identificación del técnico
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * Obtiene el nombre completo.
	 *
	 * @return nombre del técnico
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el área de especialidad.
	 *
	 * @return especialidad del técnico
	 */
	public Especialidad getEspecialidad() {
		return especialidad;
	}

	/**
	 * Obtiene el estado vigente.
	 *
	 * @return estado del técnico
	 */
	public EstadoTecnico getEstado() {
		return estado;
	}

	/**
	 * Cambia el estado del técnico. Solo el modelo lo invoca, después de
	 * validar las reglas de negocio.
	 *
	 * @param estado nuevo estado
	 */
	public void setEstado(EstadoTecnico estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el sector de patrullaje.
	 *
	 * @return zona del técnico
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * Cambia el sector de patrullaje.
	 *
	 * @param zona nueva zona
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * Obtiene el servicio activo a cargo.
	 *
	 * @return la solicitud en curso, o {@code null} si no tiene
	 */
	public Solicitud getServicioActivo() {
		return servicioActivo;
	}

	/**
	 * Asigna o libera el servicio activo del técnico.
	 *
	 * @param servicioActivo solicitud en curso, o {@code null} para liberarlo
	 */
	public void setServicioActivo(Solicitud servicioActivo) {
		this.servicioActivo = servicioActivo;
	}

	/**
	 * Indica si el técnico ya tiene un servicio activo.
	 *
	 * @return {@code true} si tiene un servicio a cargo
	 */
	public boolean tieneServicioActivo() {
		return servicioActivo != null;
	}

	/**
	 * Devuelve un texto corto que identifica al técnico.
	 *
	 * @return nombre, especialidad y zona del técnico
	 */
	@Override
	public String toString() {
		return nombre + " - " + especialidad + " (" + zona + ")";
	}
}
