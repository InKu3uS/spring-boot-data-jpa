INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (1, 'Neftalí', 'Rodríguez Rodríguez', 'neftarguez@gmail.com', '2020-09-18', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (2, 'Nuria', 'Gutierrez Rodríguez', 'nurgrguez@gmail.com', '2023-09-18', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (3, 'Vicente', 'Rodríguez Padilla', 'virodpa@gmail.com', '2019-02-02', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (4, 'Javier', 'Gonzalez Garcia', 'javigoga@gmail.com', '2018-01-30', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (5, 'María', 'López Martínez', 'marialopez@gmail.com', '2019-05-15', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (6, 'Carlos', 'Sánchez Rodríguez', 'carlossanchez@gmail.com', '2019-07-20', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (7, 'Laura', 'Martínez Gómez', 'lauramartinez@gmail.com', '2019-08-10', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (8, 'Alejandro', 'Fernández Pérez', 'alejandrofernandez@gmail.com', '2020-01-05', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (9, 'Elena', 'García Serrano', 'elenagarcia@gmail.com', '2020-03-18', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (10, 'Pedro', 'Martín López', 'pedromartin@gmail.com', '2020-04-22', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (11, 'Sara', 'González Sánchez', 'saragonzalez@gmail.com', '2020-05-07', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (12, 'Antonio', 'Pérez Martínez', 'antonioperez@gmail.com', '2020-06-19', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (13, 'Lucía', 'López García', 'lucialopez@gmail.com', '2020-07-25', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (14, 'Diego', 'Fernández Serrano', 'diegofernandez@gmail.com', '2020-08-12', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (15, 'Carmen', 'Gómez López', 'carmengomez@gmail.com', '2020-09-01', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (16, 'Hugo', 'Serrano Martín', 'hugoserrano@gmail.com', '2020-10-17', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (17, 'Isabella', 'Martínez Sánchez', 'isabellamartinez@gmail.com', '2020-11-23', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (18, 'David', 'Pérez García', 'davidperez@gmail.com', '2021-01-09', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (19, 'Natalia', 'López Serrano', 'natalialopez@gmail.com', '2021-02-14', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (20, 'Roberto', 'García Martínez', 'robertogarcia@gmail.com', '2021-03-28', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (21, 'Patricia', 'Sánchez López', 'patriciasanchez@gmail.com', '2021-04-30', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (22, 'Álvaro', 'Martín García', 'alvaromartin@gmail.com', '2021-06-05', '');

INSERT INTO clientes (id, nombre, apellido, email, create_at, foto)
VALUES (23, 'Eva', 'Fernández Sánchez', 'evafdez@gmail.com', '2021-07-12', '');


INSERT INTO productos (nombre, precio, create_at) VALUES ('Steam Deck 512 GB', 599, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('PlayStation 5 1 TB', 599, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Xbox Series X 1 TB', 599, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Xbox Series S 1 TB', 399, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Starfield Collectors Edition', 110, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Baldurs Gate 3', 69, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Diablo 4 Ultimate Edition', 99, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Xbox Elite Series 2 Controller', 150, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Xbox Series X Controller', 150, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('PlayStation 5 Controller', 80, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Nintendo Switch', 225, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Nintendo Switch Oled', 299, NOW());

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Consolas', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2,1,1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1,1,3);

INSERT INTO users (username, password, enabled) VALUES ('Nefta', '$2a$12$kifxMvB9LJffyVX6YVVUoOzaN2Yodfh52i.JyX/g6sEAXJmlpR4sy', 1);
INSERT INTO users (username, password, enabled) VALUES ('Nuria', '$2y$12$fZtY/Lfh3Gn7gWLep1KfkOvs0h/6kK2n7AWMHG8Y5C8JCKDbJAkGK', 1);

INSERT INTO authorities (user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2, 'ROLE_USER');

