package es.uned.foederis.sesion.constantes;

import org.springframework.stereotype.Component;

@Component
public class UsuarioConstantes {
	public final static long ROL_USUARIO = 1;
	public final static long ROL_JEFE_PROY = 2;
	public final static long ROL_ADMIN = 3;
	
	public final static long ID_USUARIO_ANONIMO = 1;
	
	// Nombres de las columnas de la tabla de usuarios
	// y paramtros de busqueda de usuarios
	public static final String USERNAME = "Usuario";
	public static final String ROL = "Rol";
	public static final String NOMBRE = "Nombre";
	public static final String APELLIDOS = "Apellidos";
	public static final String PASSWORD = "Password";
	public static final String ESTADO = "Estado";
	
}
