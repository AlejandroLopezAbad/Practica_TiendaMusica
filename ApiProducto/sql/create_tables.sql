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
    description VARCHAR(255)     NOT NULL,
    url         VARCHAR(255)     NOT NULL,
    category    VARCHAR(255)     NOT NULL,
    stock       INTEGER          NOT NULL,
    brand       VARCHAR(255)     NOT NULL,
    model       VARCHAR(255)     NOT NULL
);