create table if not exists products
(
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    available BOOL NOT NULL,
    description VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL
);


