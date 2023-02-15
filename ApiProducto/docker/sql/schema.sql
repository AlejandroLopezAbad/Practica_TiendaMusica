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