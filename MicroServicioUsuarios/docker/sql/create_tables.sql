CREATE TABLE IF NOT EXISTS users
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid      VARCHAR(255) UNIQUE,
    email     VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    telephone VARCHAR(255) NOT NULL,
    role      VARCHAR(255) NOT NULL,
    available BOOL         NOT NULL,
    url       VARCHAR(255) NOT NULL
);