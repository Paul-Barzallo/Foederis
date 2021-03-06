package es.uned.foederis.constantes;

import org.springframework.stereotype.Component;

/**
 * Se pone a etiqueta Component para poder usarlo como Beans en thymeleaf
 * y acceder a las constantes
 * @author barza
 *
 */
@Component
public class Rutas {
	public static final String MODIFICAR = 	"/modificar";
	public static final String NUEVO = 		"/nuevo";
	public static final String ACTIVAR = 	"/activar";
	public static final String DESACTIVAR = "/desactivar";
	public static final String GUARDAR = 	"/guardar";
	public static final String AJAX = 		"/ajax";
	public static final String BUSCAR = 	"/buscar";
	
	// Constantes administacion
	public static final String ADMINISTRACION =	"/administracion";
	public static final String PERFIL = 		"/perfil";
	public static final String USUARIO = 		"/usuario";
	public static final String USUARIOS = 		"/usuarios";
	public static final String SALA = 			"/sala";
	public static final String SALAS = 			"/salas";
	public static final String BUSQ_USERS = 	USUARIOS+AJAX+BUSCAR;
	// Rutas base controladores administacion
	public static final String ADM_PERFIL = 	ADMINISTRACION+PERFIL;
	public static final String ADM_USER = 		ADMINISTRACION+USUARIO;
	public static final String ADM_SALA = 		ADMINISTRACION+SALA;
	// Rutas controladores administacion/usuario
	public static final String ADM_USER_USERS = 		ADM_USER+USUARIOS;
	public static final String ADM_USER_USERS_BUSQ = 	ADM_USER+BUSQ_USERS;
	public static final String ADM_USER_MODIF = 		ADM_USER+MODIFICAR;
	public static final String ADM_USER_NUEVO = 		ADM_USER+NUEVO;
	public static final String ADM_USER_ACTIVAR = 		ADM_USER+ACTIVAR;
	public static final String ADM_USER_DESACTI = 		ADM_USER+DESACTIVAR;
	public static final String ADM_USER_GUARDAR = 		ADM_USER+GUARDAR;
	// Rutas controladores administracion/salas
	public static final String ADM_SALA_SALAS = 		ADM_SALA+SALAS;
}
