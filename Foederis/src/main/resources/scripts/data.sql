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
	(id_rol, nombre)
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