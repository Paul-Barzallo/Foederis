package es.uned.foederis.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class DateTimeServie {

	/**
	 * Realiza las validaciones de formato y rango de hora
	 * @param hora
	 * @return
	 */
	public boolean isHoraValida(String hora) {
		return isFormatoHoraValida(hora) && isRangoHoraValido(hora);
	}
	
	/**
	 * Valida que el formato de hora sea valido HH:mm
	 * @param hora
	 * @return
	 */
    public boolean isFormatoHoraValida(String hora) { 
        String regex = "^[0-2]?[0-9]:[0-9]{2}$"; 
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher((CharSequence)hora); 
        return matcher.matches(); 
    } 
    
    /**
     * Valida que el rango de hora este en rango [0-23];[0:59]
     * @param hora
     * @return
     */
    public boolean isRangoHoraValido(String hora) {
    	String[] partes = hora.split(":");
    	int hh = Integer.parseInt(partes[0]);
    	int mm = Integer.parseInt(partes[1]);
    	if (0>hh || hh>23) {
    		return false;
    	}
    	if (0>mm || mm>59) {
    		return false;
    	}
    	return true;
    }
}
