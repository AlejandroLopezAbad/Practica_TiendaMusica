CREATE TABLE IF NOT EXISTS services
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid        VARCHAR(255) UNIQUE,
    price       DOUBLE PRECISION NOT NULL,
    available   BOOL             NOT NULL,
    description VARCHAR(255)     NOT NULL,
    url         VARCHAR(255)     NOT NULL,
    category    VARCHAR(255)     NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid        VARCHAR(255) UNIQUE,
    name        VARCHAR(255)     NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    available   BOOL             NOT NULL,
    description TEXT             NOT NULL,
    url         VARCHAR(255)     NOT NULL,
    category    VARCHAR(255)     NOT NULL,
    stock       INTEGER          NOT NULL,
    brand       VARCHAR(255)     NOT NULL,
    model       VARCHAR(255)     NOT NULL
);

INSERT INTO services(uuid, price, available, description, url, category)
VALUES ('a11ea11e-cfa4-4fb6-9fd6-ff1eb893e296',25,true,'Servicio de reparación de amplificadores.','','AMPLIFIER_REPAIR');

INSERT INTO services(uuid, price, available, description, url, category)
VALUES ('3e4750a5-eb4d-4f7d-99e1-c32894b6f836',45,true,'Servicio de reparación de guitarras.','','GUITAR_REPAIR');

INSERT INTO services(uuid, price, available, description, url, category)
VALUES ('b7bab6ac-9084-4af2-820b-86edb4a2b53f',20,true,'Servicio de sustitución de cuerdas.','','CHANGE_OF_STRINGS');



INSERT INTO products(uuid, name, price, available, description, url, category, stock, brand, model)
VALUES ('092c4363-eb9e-40cd-8db4-4dcf8b4db7ca','CORDOBA C4-CE',359.00,true,'La C4-CE también presenta un corte suave y un cuello más delgado de 50 mm de ancho, ideal para las estructuras de acordes, a menudo complejas con las intrincadas ejecuciones de la guitarra de estilo clásico. El sistema  activo Fishman Sonitone proporciona un hermoso tono amplificado cuando se conecta. ','','GUITAR',10,'Cordoba','C4-CE');

INSERT INTO products(uuid, name, price, available, description, url, category, stock, brand, model)
VALUES ('f2288662-f01b-4d99-8e49-9462e170a272','Fender Player Mustang Bass PJ Firemist Gold ',695.00,true,'Esta versión actualizada a pequeña escala agrega el poder de las venerables pastillas P Bass y J Bass al diseño tradicional Mustang para un tono de graves flexible y atronador con una sensación de juego suave y un estilo visual elegante.','','BASS_GUITAR',8,'Fender','Mustang Bass PJ Firemist Gold');

INSERT INTO products(uuid, name, price, available, description, url, category, stock, brand, model)
VALUES ('cf3c4585-b23d-4083-af3d-3705877b1250','Marshall Origin 20C',539.00,true,'Nuevo diseño clásico y contemporáneo con nueva tecnología Powerstem, que permite tener modos de alta y baja potencia sin afectar a la calidad de sonido. Los amplificadores de canal único de estilo clásico generan tonos ricos y armónicos que son ideales para que los guitarristas exploren nuevas posibilidades y creen su propio sonido.','','BOOSTER',20,'Marshall','Origin 20C');
