package es.uned.foederis.chats.repository;

import org.springframework.data.repository.CrudRepository;
import es.uned.foederis.chats.model.Chat;

public interface IChatRepository extends CrudRepository<Chat, Integer> {

}
