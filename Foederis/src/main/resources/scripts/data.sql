INSERT INTO sala
	(id, nombre, aforo, presentacion, megafonia, grabacion, streaming, wifi)
VALUES 
	(1, 'Sala 1', 10, TRUE, FALSE, TRUE, FALSE, TRUE),
	(2, 'Sala 2', 15, TRUE, TRUE, TRUE, TRUE, TRUE),
	(3, 'Sala 3', 11, TRUE, FALSE, TRUE, FALSE, TRUE),
	(4, 'Sala 4', 14, TRUE, TRUE, FALSE, FALSE, TRUE),
	(5, 'Sala 5', 12, FALSE, FALSE, TRUE, TRUE, FALSE)
;

INSERT INTO rol
	(id, nombre)
VALUES
	(1, 'Usuario'),
	(2, 'Jefe de Proyecto'),
	(3, 'Administrador')
;

insert into Evento
	( id_evento, nombre, id_usuario_creador, estado, id_sala, id_chat, id_repositorio_compartido, fecha_Inicio, fecha_fin)
values
	(1,'Evento 1', 1, 1, 1, 1, 1, '2020-05-08 09:00:00','2020-05-08 09:30:00'),
	(2,'Evento 2', 1, 2, 2, 2, 2, '2020-05-15 13:30:00','2020-05-15 14:30:00'),
	(3,'Evento 3', 1, 2, 2, 2, 2, '2020-05-09 21:30:00','2020-05-09 23:00:00') 
;
