package es.uned.foederis.constantes;

import org.springframework.stereotype.Component;

@Component
public class Vistas {
	public static final String HOME = 			"/home";
	public static final String PERFIL = 		"/administracion/usuario/perfil";
	public static final String USUARIOS = 		"/administracion/usuario/usuarios";
	public static final String ADM_SALAS = 		"/administracion/sala/salas";
	public static final String ADM_SALA = 		"/administracion/sala/form_sala";
	public static final String NUEVO_EVENTO = 	"/eventos/form_nuevo_evento";
}
