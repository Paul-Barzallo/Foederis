INSERT INTO sala
	(id, nombre, aforo)
VALUES 
	(1, 'Sala 1', 10),
	(2, 'Sala 2', 15),
	(3, 'Sala 3', 11),
	(4, 'Sala 4', 14),
	(5, 'Sala 5', 12)
;
insert into Evento
	( id_evento, nombre, id_usuario_creador, estado, id_sala, id_chat, id_repositorio_compartido, fecha_Inicio, fecha_fin)
values
	(1,'Evento 1', 1, 1, 1, 1, 1, '2020-05-08 09:00:00','2020-05-08 09:30:00'),
	(2,'Evento 2', 1, 2, 2, 2, 2, '2020-05-15 13:30:00','2020-05-15 14:30:00'),
	(3,'Evento 3', 1, 2, 2, 2, 2, '2020-05-09 21:30:00','2020-05-09 23:00:00') 
;
	