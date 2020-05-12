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