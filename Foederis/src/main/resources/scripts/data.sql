INSERT INTO rol
	(id_rol,    nombre)
VALUES
	(1,         'Usuario'),
	(2,         'Jefe de Proyecto'),
	(3,         'Administrador')
;

INSERT INTO usuario
	(id_usuario,    username,       nombre,             apellidos,          password,   id_rol_fk,  activo)
VALUES
	(1,             'admin',		'administrador', 	'', 				'admin', 	3,          TRUE),
	(2,             'admin2',		'adminsitrador', 	'segundo',			'admin', 	3,          FALSE),
	(3,             'pbarza',		'paul', 			'barzallo', 		'foederis', 3,          TRUE),	
	(4,             'lidia',		'lidia', 			'', 				'1234', 	2,          TRUE),	
	(5,             'juancarlos',	'juan carlos', 		'', 				'1234', 	3,          TRUE),
	(6,             'albeas',		'alex', 			'bernat asensi', 	'user', 	2,          TRUE),	
	(7,             'daoval',		'david', 			'oviedo almeida', 	'user', 	2,          TRUE),
	(8,             'ambain',		'amparo', 			'bas infante', 		'user', 	2,          TRUE),	
	(9,             'sasetr',		'sandra', 			'seco triviño', 	'user', 	1,          TRUE),
	(10,            'miduca',		'milagros', 		'dueñas casellas', 	'user', 	1,          TRUE),	
	(11,            'virovi',		'vicente', 			'roura villalba', 	'user', 	1,          TRUE),
	(12,            'gabaco',		'gabriel', 			'baron corominas', 	'user', 	1,          TRUE)
;

INSERT INTO sala
	(id_sala,   nombre,         aforo,  presentacion,   megafonia,  grabacion,  streaming,  wifi,   hora_inicio,    hora_fin,   activa)
VALUES 
	(1,         'Sucellus',     5,      TRUE,           FALSE,      TRUE,       FALSE,      TRUE,   '07:00:00',     '20:00:00', TRUE),
	(2,         'Tanaris',      5,      TRUE,           TRUE,       TRUE,       TRUE,       TRUE,   '08:00:00',     '18:00:00', TRUE),
	(3,         'Dea Dama',     5,      TRUE,           FALSE,      TRUE,       FALSE,      TRUE,   '07:00:00',     '14:00:00', FALSE),
	(4,         'Dagda',        8,      TRUE,           TRUE,       FALSE,      FALSE,      TRUE,   '13:00:00',     '20:00:00', TRUE),
	(5,         'Lugh',         8,      FALSE,          FALSE,      TRUE,       TRUE,       FALSE,  '19:00:00',     '06:00:00', TRUE),
	(6,         'Morrigan',     8,      FALSE,          FALSE,      TRUE,       TRUE,       FALSE,  '15:00:00',     '15:00:00', TRUE),
    (7,         'Epona',        10,     FALSE,          FALSE,      TRUE,       TRUE,       FALSE,  '15:00:00',     '15:00:00', TRUE),
    (8,         'Belenus',      10,     FALSE,          FALSE,      TRUE,       TRUE,       FALSE,  '15:00:00',     '15:00:00', TRUE),
    (9,         'Cernunnos',    12,     FALSE,          FALSE,      TRUE,       TRUE,       FALSE,  '15:00:00',     '15:00:00', TRUE),
    (10,        'Mesa redonda', 15,     TRUE,           FALSE,      TRUE,       FALSE,      FALSE,  '11:00:00',     '20:00:00', TRUE),
    (11,        'Excalibur',    15,     TRUE,           TRUE,       FALSE,      FALSE,      FALSE,  '10:00:00',     '21:00:00', TRUE)
;

insert into evento
	(id_evento, id_usuario_creador_fk,  id_sala_evento_fk,  estado,         nombre)
values
	(1,         3,                      4,                  'FINALIZADO',   'Planificación Fase 3'),
    (2,         4,                      5,                  'FINALIZADO',   'Campaña de Verano'),
    (3,         5,                      6,                  'INACTIVO',     'Contabilidad Primer trimestre 2020'),
    (4,         6,                      2,                  'INACTIVO',     'Planificar inversiones'),
    (5,         7,                      3,                  'INACTIVO',     'Negociaciones: Ampliación de contrato')
;

insert into horarios
	(id_horario,    id_evento_fk,   fecha_Inicio,           fecha_fin)
values
	(1,             1,              '2020-05-25 09:00:00',  '2020-05-25 12:00:00'),
	(2,             1,              '2020-05-25 15:00:00',  '2020-05-25 18:00:00'),
	(3,             1,              '2020-05-26 11:00:00',  '2020-05-26 14:00:00'),
    
	(4,             2,              '2020-06-01 10:00:00',  '2020-06-01 13:00:00'),
	(5,             2,              '2020-06-02 12:00:00',  '2020-06-02 14:00:00'),
    
	(6,             3,              '2020-06-01 10:00:00',  '2020-06-01 12:00:00'),
    (7,             3,              '2020-06-03 11:00:00',  '2020-06-03 13:00:00'),
    (8,             3,              '2020-06-04 08:00:00',  '2020-06-04 10:00:00'),
    
    (9,             4,              '2020-06-02 10:00:00',  '2020-06-02 11:00:00'),
    (10,            4,              '2020-06-02 15:00:00',  '2020-06-02 16:00:00'),
    
    (11,            5,              '2020-06-02 17:00:00',  '2020-06-02 18:00:00'),
    (12,            5,              '2020-06-05 08:00:00',  '2020-06-05 09:00:00')
;

insert into usuario_evento
	(id_usuario_evento, id_evento_fk,   id_usuario_fk,  id_horario_fk,  confirmado, asistente,  presencial, conectado)
values
	(1,                 1,              3,              1,              1,          TRUE,       FALSE,       FALSE),
    (2,                 1,              4,              1,              1,          TRUE,       TRUE,        FALSE),
    (3,                 1,              5,              2,              1,          TRUE,       FALSE,       FALSE),
    (4,                 1,              6,              1,              1,          TRUE,       FALSE,       FALSE),
    (5,                 1,              7,              1,              1,          TRUE,       FALSE,       FALSE),
    (6,                 1,              8,              2,              1,          TRUE,       TRUE,        FALSE),
    (7,                 1,              9,              1,              1,          TRUE,       FALSE,       FALSE),
    (8,                 1,              10,             3,              1,          TRUE,       FALSE,       FALSE),
    (9,                 1,              11,             1,              1,          TRUE,       FALSE,       FALSE),
    (10,                1,              12,             1,              1,          TRUE,       FALSE,       FALSE),
    
    (11,                2,              3,              4,              1,          TRUE,       FALSE,       FALSE),
    (12,                2,              4,              4,              1,          TRUE,       TRUE,        FALSE),
    (13,                2,              5,              5,              1,          TRUE,       FALSE,       FALSE),
    (14,                2,              6,              5,              1,          TRUE,       FALSE,       FALSE),
    (15,                2,              7,              4,              1,          TRUE,       FALSE,       FALSE),
    (16,                2,              8,              4,              1,          TRUE,       TRUE,        FALSE),
    (17,                2,              9,              5,              0,          FALSE,      FALSE,       FALSE),
    (18,                2,              10,             5,              1,          TRUE,       FALSE,       FALSE),
    (19,                2,              11,             5,              1,          TRUE,       FALSE,       FALSE),
    (20,                2,              12,             5,              1,          TRUE,       FALSE,       FALSE),
    
    (21,                3,              3,              6,              1,          TRUE,       FALSE,       FALSE),
    (22,                3,              4,              6,              1,          TRUE,       TRUE ,       FALSE),
    (23,                3,              5,              7,              1,          TRUE,       FALSE,       FALSE),
    (24,                3,              6,              6,              1,          TRUE,       FALSE,       FALSE),
    (25,                3,              7,              7,              1,          TRUE,       FALSE,       FALSE),
    (26,                3,              8,              8,              1,          TRUE,       TRUE ,       FALSE),
    (27,                3,              9,              8,              1,          TRUE,       FALSE,       FALSE),
    (28,                3,              10,             7,             -1,          FALSE,      FALSE,       FALSE),
    (29,                3,              11,             7,              1,          TRUE,       FALSE,       FALSE),
    
    (30,                4,              3,              null,          -1,          null,       null,        FALSE),
    (31,                4,              6,              null,           1,          TRUE,       null,        FALSE),
    (32,                4,              9,              null,          -1,          null,       null,        FALSE),
    (33,                4,              11,             null,          -1,          null,       null,        FALSE),
    (34,                4,              12,             null,          -1,          null,       null,        FALSE),
    
    (35,                5,              4,              null,          -1,          null,       null,        FALSE),
    (36,                5,              7,              null,          -1,          null,       null,        FALSE),
    (37,                5,              8,              null,          -1,          null,       null,        FALSE),
    (38,                5,              9,              null,          -1,          null,       null,        FALSE),
    (39,                5,              10,             null,          -1,          null,       null,        FALSE) 
;
	
update  Evento
    set id_horario_fk = 1
    where id_evento = 1
;

update  Evento
    set id_horario_fk = 5
    where id_evento = 2
;

update  Evento
    set id_horario_fk = 7
    where id_evento = 3
;
