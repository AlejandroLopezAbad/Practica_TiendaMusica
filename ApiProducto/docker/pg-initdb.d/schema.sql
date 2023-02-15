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


