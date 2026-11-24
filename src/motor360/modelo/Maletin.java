package motor360.modelo;

/**
 * Maletín de respuesta rápida con insumos para atender servicios. Al cerrar
 * un servicio no vuelve directamente al almacén: pasa a la estación de
 * auditoría, donde se revisa su contenido.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Maletin {

	/** Código único del maletín. */
	private final String codigo;

	/** Descripción del contenido del maletín. */
	private final String contenido;

	/** Situación actual del maletín. */
	private EstadoMaletin estado;

	/** Número de auditorías que ha superado el maletín. */
	private int auditorias;

	/** Número de piezas que se le han reemplazado en auditoría. */
	private int piezasReemplazadas;

	/**
	 * Crea un maletín disponible y sin auditorías.
	 *
	 * @param codigo    código único
	 * @param contenido descripción del contenido
	 */
	public Maletin(String codigo, String contenido) {
		this.codigo = codigo;
		this.contenido = contenido;
		this.estado = EstadoMaletin.DISPONIBLE;
		this.auditorias = 0;
		this.piezasReemplazadas = 0;
	}

	/**
	 * Obtiene el código del maletín.
	 *
	 * @return código del maletín
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Obtiene la descripción del contenido.
	 *
	 * @return contenido del maletín
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Obtiene la situación actual.
	 *
	 * @return estado del maletín
	 */
	public EstadoMaletin getEstado() {
		return estado;
	}

	/**
	 * Cambia la situación del maletín.
	 *
	 * @param estado nuevo estado
	 */
	public void setEstado(EstadoMaletin estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el número de auditorías superadas.
	 *
	 * @return cantidad de auditorías
	 */
	public int getAuditorias() {
		return auditorias;
	}

	/**
	 * Cambia el número de auditorías superadas.
	 *
	 * @param auditorias nueva cantidad de auditorías
	 */
	public void setAuditorias(int auditorias) {
		this.auditorias = auditorias;
	}

	/**
	 * Obtiene el número de piezas reemplazadas.
	 *
	 * @return cantidad de piezas reemplazadas
	 */
	public int getPiezasReemplazadas() {
		return piezasReemplazadas;
	}

	/**
	 * Cambia el número de piezas reemplazadas.
	 *
	 * @param piezasReemplazadas nueva cantidad de piezas reemplazadas
	 */
	public void setPiezasReemplazadas(int piezasReemplazadas) {
		this.piezasReemplazadas = piezasReemplazadas;
	}

	/**
	 * Devuelve un texto corto que identifica el maletín.
	 *
	 * @return código y contenido del maletín
	 */
	@Override
	public String toString() {
		return codigo + " - " + contenido;
	}
}
