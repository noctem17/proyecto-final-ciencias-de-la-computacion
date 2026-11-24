package motor360.modelo;

/**
 * Carga un conjunto de datos de demostración para que el sistema pueda
 * probarse desde el primer momento: unidades, técnicos, clientes, maletines,
 * repuestos y algunas solicitudes en espera. Todos los nombres son ficticios.
 *
 * @author Reynel Felipe Amezquita Puentes
 * @author Nancy Gabriela Jaimes Luna
 * @author Cristian Camilo Velosa Ortiz
 * @version 1.0
 */
public final class DatosIniciales {

	/**
	 * Constructor privado: la clase solo ofrece métodos estáticos.
	 */
	private DatosIniciales() {
	}

	/**
	 * Registra los datos de demostración en el centro de control y luego
	 * consolida el historial, para que estos registros no puedan deshacerse.
	 *
	 * @param centro centro de control donde se cargan los datos
	 */
	public static void cargar(CentroControl centro) {
		try {
			centro.registrarUnidad("GRU-101", TipoUnidad.GRUA, Zona.NORTE, NivelDisponibilidad.ALTO);
			Unidad gruaTaller = centro.registrarUnidad("GRU-102", TipoUnidad.GRUA, Zona.PERIFERIA,
					NivelDisponibilidad.MEDIO);
			centro.registrarUnidad("MOT-201", TipoUnidad.MOTOCICLETA, Zona.CENTRO, NivelDisponibilidad.ALTO);
			centro.registrarUnidad("MOT-202", TipoUnidad.MOTOCICLETA, Zona.SUR, NivelDisponibilidad.ALTO);
			centro.registrarUnidad("UTL-301", TipoUnidad.UTILITARIO, Zona.ORIENTE, NivelDisponibilidad.ALTO);
			centro.registrarUnidad("UTL-302", TipoUnidad.UTILITARIO, Zona.OCCIDENTE, NivelDisponibilidad.MEDIO);
			centro.registrarUnidad("LIG-401", TipoUnidad.COCHE_LIGERO, Zona.NORTE, NivelDisponibilidad.BAJO);
			centro.registrarUnidad("LIG-402", TipoUnidad.COCHE_LIGERO, Zona.SUR, NivelDisponibilidad.ALTO);
			centro.cambiarEstadoUnidad(gruaTaller, EstadoUnidad.MANTENIMIENTO);

			centro.registrarTecnico("1010101010", "Andrés Felipe Rojas", Especialidad.REMOLQUE, Zona.NORTE);
			centro.registrarTecnico("1020202020", "Paola Andrea Gómez", Especialidad.MECANICA, Zona.CENTRO);
			centro.registrarTecnico("1030303030", "Julián David Torres", Especialidad.ELECTRICIDAD, Zona.SUR);
			centro.registrarTecnico("1040404040", "Diana Carolina Ruiz", Especialidad.LLANTAS, Zona.ORIENTE);
			centro.registrarTecnico("1050505050", "Sergio Alejandro Peña", Especialidad.CERRAJERIA, Zona.OCCIDENTE);
			Tecnico descanso = centro.registrarTecnico("1060606060", "Marcela Herrera", Especialidad.COMBUSTIBLE,
					Zona.PERIFERIA);
			centro.registrarTecnico("1070707070", "Camilo Andrés Vargas", Especialidad.DIAGNOSTICO, Zona.NORTE);
			centro.cambiarEstadoTecnico(descanso, EstadoTecnico.FUERA_DE_TURNO);

			Cliente particular = centro.registrarCliente("52123456", "Laura Martínez", "3001234567",
					TipoCliente.PARTICULAR);
			Cliente flota = centro.registrarCliente("900123456-1", "Transportes Cordillera S.A.S.", "6014567890",
					TipoCliente.FLOTA);
			Cliente aseguradora = centro.registrarCliente("860987654-3", "Seguros Horizonte", "6017654321",
					TipoCliente.ASEGURADORA);
			Cliente buses = centro.registrarCliente("901555777-2", "Buses Urbanos del Sur", "6013332211",
					TipoCliente.TRANSPORTE_PUBLICO);
			Cliente ambulancias = centro.registrarCliente("900888999-0", "Ambulancias Pronto Auxilio", "6011112233",
					TipoCliente.EMERGENCIA);
			Cliente conductor = centro.registrarCliente("79876543", "Carlos Pérez", "3109876543",
					TipoCliente.PARTICULAR);

			centro.registrarMaletin("MAL-01", "Kit de arranque y cables de batería");
			centro.registrarMaletin("MAL-02", "Kit de llantas: gato, cruceta y parches");
			centro.registrarMaletin("MAL-03", "Kit de cerrajería vehicular");
			centro.registrarMaletin("MAL-04", "Kit de diagnóstico con escáner OBD");
			centro.registrarMaletin("MAL-05", "Kit de mecánica general");
			centro.registrarMaletin("MAL-06", "Kit de combustible y bidones");

			centro.apilarRepuesto("Fusible de 15 amperios");
			centro.apilarRepuesto("Correa de alternador");
			centro.apilarRepuesto("Bombillo H4");
			centro.apilarRepuesto("Válvula de llanta");
			centro.apilarRepuesto("Borne de batería");

			centro.registrarSolicitud(particular, TipoIncidente.BATERIA, Zona.NORTE,
					"Conjunto residencial, parqueadero sótano 2", "El carro no enciende, batería agotada", 1, 1, 2);
			centro.registrarSolicitud(buses, TipoIncidente.AVERIA_MECANICA, Zona.PERIFERIA,
					"Autopista Sur, kilómetro 12, carril derecho", "Bus con 40 pasajeros varado por falla de motor", 4,
					4, 3);
			centro.registrarSolicitud(aseguradora, TipoIncidente.PINCHAZO, Zona.OCCIDENTE,
					"Avenida Boyacá con calle 13", "Asegurado con llanta pinchada en berma", 2, 2, 1);
			centro.registrarSolicitud(ambulancias, TipoIncidente.REMOLQUE, Zona.CENTRO, "Carrera 7 con calle 19",
					"Ambulancia inmovilizada, requiere grúa urgente", 4, 3, 4);
			centro.registrarSolicitud(conductor, TipoIncidente.AVERIA_MECANICA, Zona.CENTRO,
					"Avenida Caracas con calle 26, hora pico", "Automóvil detenido obstruyendo un carril", 3, 5, 3);
			centro.registrarSolicitud(flota, TipoIncidente.COMBUSTIBLE, Zona.SUR, "Bodega de la flota, calle 40 sur",
					"Camión sin combustible dentro de la bodega", 1, 1, 1);
		} catch (ReglaNegocioException e) {
			throw new IllegalStateException("No se pudieron cargar los datos de demostración: " + e.getMessage(), e);
		}
		centro.consolidarHistorial("Datos de demostración cargados al iniciar el sistema");
	}
}
