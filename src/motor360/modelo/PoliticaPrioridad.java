package motor360.modelo;

/**
 * Reúne las reglas con las que la empresa decide si una solicitud es
 * ordinaria o crítica. Cada solicitud obtiene un puntaje a partir de cuatro
 * factores: nivel de riesgo, afectación vial, perfil del cliente y severidad
 * de la avería.
 * <p>
 * Puntaje = 3 x riesgo + 2 x afectación vial + 2 x severidad + 2 x peso del
 * cliente. Como cada factor va de 1 a 5, el puntaje va de 9 a 45.
 * <p>
 * Una solicitud es crítica si su puntaje alcanza el umbral, o si alguno de
 * los factores está en su valor máximo (5).
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public final class PoliticaPrioridad {

	/** Valor mínimo de cada factor. */
	public static final int MINIMO = 1;

	/** Valor máximo de cada factor. */
	public static final int MAXIMO = 5;

	/** Puntaje a partir del cual una solicitud se considera crítica. */
	public static final int UMBRAL_CRITICO = 25;

	/** Peso del nivel de riesgo en el puntaje. */
	public static final int PESO_RIESGO = 3;

	/** Peso de la afectación vial en el puntaje. */
	public static final int PESO_AFECTACION = 2;

	/** Peso de la severidad de la avería en el puntaje. */
	public static final int PESO_SEVERIDAD = 2;

	/** Peso del perfil del cliente en el puntaje. */
	public static final int PESO_CLIENTE = 2;

	/**
	 * Constructor privado: la clase solo ofrece métodos estáticos.
	 */
	private PoliticaPrioridad() {
	}

	/**
	 * Calcula el puntaje de prioridad de una solicitud.
	 *
	 * @param riesgo      nivel de peligro, de 1 a 5
	 * @param afectacion  afectación vial o congestión generada, de 1 a 5
	 * @param severidad   gravedad de la avería, de 1 a 5
	 * @param tipoCliente perfil del cliente
	 * @return puntaje entre 9 y 45; a mayor puntaje, mayor urgencia
	 */
	public static int calcularPuntaje(int riesgo, int afectacion, int severidad, TipoCliente tipoCliente) {
		return PESO_RIESGO * riesgo + PESO_AFECTACION * afectacion + PESO_SEVERIDAD * severidad
				+ PESO_CLIENTE * tipoCliente.getPeso();
	}

	/**
	 * Decide si una solicitud debe escalarse automáticamente como crítica.
	 *
	 * @param riesgo      nivel de peligro, de 1 a 5
	 * @param afectacion  afectación vial o congestión generada, de 1 a 5
	 * @param severidad   gravedad de la avería, de 1 a 5
	 * @param tipoCliente perfil del cliente
	 * @return {@code true} si la solicitud debe ir a la cola prioritaria
	 */
	public static boolean esCritica(int riesgo, int afectacion, int severidad, TipoCliente tipoCliente) {
		if (riesgo == MAXIMO || afectacion == MAXIMO || severidad == MAXIMO || tipoCliente.getPeso() == MAXIMO) {
			return true;
		}
		return calcularPuntaje(riesgo, afectacion, severidad, tipoCliente) >= UMBRAL_CRITICO;
	}

	/**
	 * Verifica que un factor esté dentro del rango permitido.
	 *
	 * @param valor valor del factor
	 * @return {@code true} si el valor está entre 1 y 5
	 */
	public static boolean enRango(int valor) {
		return valor >= MINIMO && valor <= MAXIMO;
	}
}
