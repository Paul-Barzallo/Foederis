package es.uned.foederis.archivos.repository;

import org.springframework.data.repository.CrudRepository;

import es.uned.foederis.archivos.model.Archivo;

public interface IArchivoRepository extends CrudRepository<Archivo, Integer> {

}
