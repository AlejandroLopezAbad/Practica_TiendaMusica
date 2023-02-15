CREATE TABLE IF NOT EXISTS services
(
    id          BIGINT PRIMARY KEY,
    uuid        UUID UNIQUE,
    name        VARCHAR(255)     NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    available   BOOL             NOT NULL,
    description VARCHAR(255)     NOT NULL,
    url         VARCHAR(255)     NOT NULL,
    category    VARCHAR(255)     NOT NULL
)

create table if not exists products
(
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE,
    name TEXT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    available BOOL NOT NULL,
    description TEXT NOT NULL,
    url TEXT NOT NULL,
    category TEXT NOT NULL,
    stock INTEGER NOT NULL,
    brand TEXT NOT NULL,
    model TEXT NOT NULL
);