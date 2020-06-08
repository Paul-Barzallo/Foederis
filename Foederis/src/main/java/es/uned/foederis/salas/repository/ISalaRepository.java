package es.uned.foederis.salas.repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.sesion.model.Usuario;

@Repository
public interface ISalaRepository extends CrudRepository<Sala, Long> {

	Optional<Sala> findByNombre(String nombre);
	List<Sala> findByNombreContaining(String nombre);
	List<Sala> findByNombreContainingAndActivaTrue(String nombre);
	List<Sala> findByActivaTrue();

	@Query("select s from Sala s where s.activa = true and s.idSala not in ("+
				"select e.salaEvento from Evento e, Horarios h where "+
				"h.evento = e.idEvento and ((h.Horario_Fecha_Inicio < ?1 and h.Horario_Fecha_Fin > ?2) or h.Horario_Fecha_Inicio between ?1 and ?2 or h.Horario_Fecha_Fin between ?1 and ?2)"+
			")")
	List<Sala> findByDisponibles(Timestamp fechaInicio, Timestamp fechaFin);
	
	@Query("select s from Sala s where s.idSala = ?1 and s.idSala in ("+
				"select e.salaEvento from Evento e, Horarios h where "+
				"h.evento = e.idEvento and ((h.Horario_Fecha_Inicio < ?2 and h.Horario_Fecha_Fin > ?3) or h.Horario_Fecha_Inicio between ?2 and ?3 or h.Horario_Fecha_Fin between ?2 and ?3)"+
			")")
	Optional<Sala> findBySalaOcupada(long idSala, Timestamp fechaInicio, Timestamp fechaFin);
}
