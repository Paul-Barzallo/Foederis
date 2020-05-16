package es.uned.foederis;

public class Constantes {
	public static final String PANTALLA = "pantalla";
	public static final String ROLES = "roles";
	public static final String PARAMS_BUSQUEDA = "params_busqueda";
	public static final String USUARIO = "usuario";
	public static final String USUARIOS = "usuarios";
	public static final String ACCIONES = "acciones";
	public static final String ALERTA = "alerta";
	public static final String ALERTA_TITULO = "alerta_titulo";
	public static final String PARAM_BUSQUEDA = "param_busqueda";
	public static final String VALOR_BUSQUEDA = "valor_busqueda";
	
	public static final class Pantalla {
		public static final String PERFIL = "perfil";
		public static final String USUARIOS = "usuario";
		public static final String SALAS = "salas";
		public static final String ADM_SALAS = "adm_salas";
		public static final String AGENDA = "agenda";
	}
	
	public static final class Vista {
		public static final String PERFIL = "/administracion/perfil";
		public static final String USUARIOS = "/administracion/usuarios/usuarios";
		public static final String SALAS = "/administracion/salas/salas";
		public static final String HOME = "/home";
	}
	
	public static final class Accion {
		public static final String MODIFICAR = "modificar";
		public static final String ACTIVAR = "activar";
	}
}
