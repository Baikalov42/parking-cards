create TABLE IF NOT EXISTS roles
(
    id   bigserial PRIMARY KEY,
    role text
);

create TABLE IF NOT EXISTS users
(
    id         bigserial PRIMARY KEY,
    first_name text NOT NULL,
    last_name  text NOT NULL,
    phone      text,
    email      text NOT NULL,
    role_id    bigint,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

create TABLE IF NOT EXISTS car_brands
(
    id      bigserial PRIMARY KEY,
    name    text,
    deleted boolean DEFAULT false
);

create TABLE IF NOT EXISTS car_models
(
    id       bigserial PRIMARY KEY,
    name     text NOT NULL,
    brand_id bigint,
    deleted  boolean DEFAULT false,
    FOREIGN KEY (brand_id) REFERENCES car_brands (id)
);

create TABLE IF NOT EXISTS cars
(
    id            bigserial PRIMARY KEY,
    model_id      bigint,
    user_id       bigint,
    license_plate text NOT NULL,
    FOREIGN KEY (model_id) REFERENCES car_models (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

