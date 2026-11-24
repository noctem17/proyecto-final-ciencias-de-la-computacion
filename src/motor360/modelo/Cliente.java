package motor360.modelo;

/**
 * Cliente que solicita asistencia: un conductor particular, una flota, una
 * aseguradora, una empresa de transporte público o un servicio de
 * emergencia. Toda solicitud debe estar vinculada a un cliente.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public class Cliente {

	/** Documento o NIT del cliente. */
	private final String documento;

	/** Nombre o razón social del cliente. */
	private final String nombre;

	/** Teléfono de contacto. */
	private final String telefono;

	/** Perfil del cliente. */
	private final TipoCliente tipo;

	/**
	 * Crea un cliente.
	 *
	 * @param documento documento o NIT
	 * @param nombre    nombre o razón social
	 * @param telefono  teléfono de contacto
	 * @param tipo      perfil del cliente
	 */
	public Cliente(String documento, String nombre, String telefono, TipoCliente tipo) {
		this.documento = documento;
		this.nombre = nombre;
		this.telefono = telefono;
		this.tipo = tipo;
	}

	/**
	 * Obtiene el documento o NIT.
	 *
	 * @return documento del cliente
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Obtiene el nombre o razón social.
	 *
	 * @return nombre del cliente
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el teléfono de contacto.
	 *
	 * @return teléfono del cliente
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Obtiene el perfil del cliente.
	 *
	 * @return tipo de cliente
	 */
	public TipoCliente getTipo() {
		return tipo;
	}

	/**
	 * Devuelve un texto corto que identifica al cliente.
	 *
	 * @return nombre, documento y perfil del cliente
	 */
	@Override
	public String toString() {
		return nombre + " [" + documento + "] - " + tipo;
	}
}
