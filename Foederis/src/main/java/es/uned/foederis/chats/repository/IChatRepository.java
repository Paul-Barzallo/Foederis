package es.uned.foederis.chats.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.eventos.model.Evento;

public interface IChatRepository extends CrudRepository<Chat, Integer> {
	List<Chat> findByEvento(Evento evento);
}
