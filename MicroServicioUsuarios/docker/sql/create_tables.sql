DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid varchar(255) UNIQUE,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    telephone INT NOT NULL,
    rol VARCHAR(255) NOT NULL,
    avaliable BOOL NOT NULL,
    url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOL
);


INSERT INTO users ( uuid, email, name, password, telephone, rol, avaliable, url)
# -- Contraseña: pepe1234
VALUES ( '2e278c8c-2309-49a1-b329-18fd85abcffc','pepe@pepe.es','JUAN','$2a$12$249dkPGBT6dH46f4Dbu7ouEuO8eZ7joonzWGefPJbHH8eDpJy0oCq',
  '666666666','ADMIN',true,'https://www.google.com');
 INSERT INTO users ( uuid, email, name, password, telephone, rol, avaliable, url)
# -- Contraseña: ana1234
 VALUES ( '569967d5-9b3e-461e-a795-6620f32ee06e','ana@ana.es','Ana','$2a$12$ZymlZf4Ja48WpBliFEU0qOUwb6HEJnhzlKYUoywhCxutkf1BzMbW2',
     '666444444','ADMIN',true,'https://www.google.com');
# -- Contraseña
INSERT INTO users ( uuid, email, name, password, telephone, rol, avaliable, url)
# -- Contraseña: ana1234
 VALUES ('72a513ee-68aa-42ec-a928-aedb5bc2d98d','juan@juan.es','Juan','$2a$12$ZymlZf4Ja48WpBliFEU0qOUwb6HEJnhzlKYUoywhCxutkf1BzMbW2',
      '666333333','USER',true,'https://www.google.com');