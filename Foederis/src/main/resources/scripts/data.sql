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

INSERT INTO usuario
	(id, nombre, apellidos, username, password, rol_id, activo)
VALUES
	(1, 'ADMINISTRADOR', null, 'admin', 'admin', 3, TRUE), 
	(2, 'ADMINISTRADOR', 'SEGUNDO', 'admin2', 'admin', 3, FALSE), 
	(3, 'PAUL', 'BARZALLO', 'pbarza', 'foederis', 3, TRUE), 
	(4, 'LIDIA', null, 'lidia', '1234', 3, TRUE), 
	(5, 'JUAN CARLOS', null, 'juancarlos', '1234', 3, TRUE), 
	(6, 'ALEX', 'BERNAT ASENSI', 'albeas', 'user', 2, TRUE), 
	(7, 'DAVID', 'OVIEDO ALMEIDA', 'daoval', 'user', 2, TRUE), 
	(8, 'AMPARO', 'BAS INFANTE', 'ambain', 'user', 2, TRUE), 
	(9, 'SANDRA', 'SECO TRIVIÑO', 'sasetr', 'user', 1, TRUE), 
	(10, 'MILAGROS', 'DUEÑAS CASELLAS', 'miduca', 'user', 1, TRUE),
	(11, 'VICENTE', 'ROURA VILLALBA', 'virovi', 'user', 1, TRUE),
	(12, 'GABRIEL', 'BARON COROMINAS', 'gabaco', 'user', 1, TRUE)
;



insert into Evento
	( id_evento, nombre, usuario_creador_id, estado, id_sala, id_chat, id_repositorio_compartido, fecha_Inicio, fecha_fin)
values
	(1,'Evento 1', 1, 1, 1, 1, 1, '2020-05-09 09:00:00','2020-05-08 09:30:00'),
	(2,'Evento 2', 3, 2, 2, 2, 2, '2020-05-25 13:30:00','2020-05-25 14:30:00'),
	(3,'Evento 3', 4, 2, 2, 2, 2, null,null),
	(4,'Evento 2', 5, 2, 2, 2, 2, '2020-05-14 13:30:00','2020-05-15 14:30:00')
		
;

insert into Usuario_Evento
	(ID_USUARIO_EVENTO, confirmado, asistente, presencial,evento, id)
	values
	(1,1, 1,1,1, 1),
	(2,1, 1,1,2, 1),
	(3,0, 0,1,3, 1),
	(4,1, 0,1,2, 11),
	(5,1, 0,1,2, 12),
	(6,1, 0,1,4, 12)
	;
	
insert into Horarios
	(id_horario, id_evento, Horario_Fecha_Inicio, Horario_Fecha_fin)
	values
	(1, 2, '2020-05-19 09:00:00','2020-05-19 10:30:00'),
	(2, 1, '2020-05-19 10:00:00','2020-05-19 11:30:00'),
	(3, 3, '2020-05-10 09:00:00','2020-05-10 10:30:00'),
	(4, 3, '2020-05-29 10:00:00','2020-05-29 11:30:00'),
	(5, 3, '2020-05-29 10:00:00','2020-05-29 11:30:00')
	;

