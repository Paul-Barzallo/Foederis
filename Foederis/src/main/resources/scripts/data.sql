INSERT INTO sala
	(id_sala, nombre, aforo, presentacion, megafonia, grabacion, streaming, wifi, hora_inicio, hora_fin, activa)
VALUES 
	(1, 'Sala 1', 5, TRUE, FALSE, TRUE, FALSE, TRUE, '07:00:00', '20:00:00', TRUE),
	(2, 'Sala 2', 20, TRUE, TRUE, TRUE, TRUE, TRUE, '08:00:00', '18:00:00', TRUE),
	(3, 'Sala 3', 10, TRUE, FALSE, TRUE, FALSE, TRUE, '07:00:00', '14:00:00', FALSE),
	(4, 'Sala 4', 10, TRUE, TRUE, FALSE, FALSE, TRUE, '13:00:00', '20:00:00', TRUE),
	(5, 'Sala 5', 10, FALSE, FALSE, TRUE, TRUE, FALSE, '19:30:00', '06:00:00', TRUE),
	(6, 'Sala 6', 15, FALSE, FALSE, TRUE, TRUE, FALSE, '15:00:00', '15:00:00', TRUE)
;

INSERT INTO rol
	(id_rol, nombre)
VALUES
	(1, 'Usuario'),
	(2, 'Jefe de Proyecto'),
	(3, 'Administrador')
;

INSERT INTO usuario
	(id_usuario, username, nombre, apellidos, password, id_rol_fk, activo)
VALUES
	(1,  'admin',		'administrador', 	null, 				'admin', 	3, TRUE),
	(2,  'admin2',		'adminsitrador', 	'segundo',			'admin', 	3, FALSE),
	(3,  'pbarza',		'paul', 			'barzallo', 		'foederis', 3, TRUE),	
	(4,  'lidia',		'lidia', 			null, 				'1234', 	3, TRUE),	
	(5,  'juancarlos',	'juan carlos', 		null, 				'1234', 	3, TRUE),
	(6,  'albeas',		'alex', 			'bernat asensi', 	'user', 	2, TRUE),	
	(7,  'daoval',		'david', 			'oviedo almeida', 	'user', 	2, TRUE),
	(8,  'ambain',		'amparo', 			'bas infante', 		'user', 	2, TRUE),	
	(9,  'sasetr',		'sandra', 			'seco triviño', 	'user', 	1, TRUE),
	(10, 'miduca',		'milagros', 		'dueñas casellas', 	'user', 	1, fALSE),	
	(11, 'virovi',		'vicente', 			'roura villalba', 	'user', 	1, TRUE),
	(12, 'gabaco',		'gabriel', 			'baron corominas', 	'user', 	1, TRUE)
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
