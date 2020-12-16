create TABLE IF NOT EXISTS roles
(
    role_id   BIGSERIAL PRIMARY KEY,
    role_name text UNIQUE NOT NULL
);

create TABLE IF NOT EXISTS users
(
    user_id    bigserial PRIMARY KEY,
    first_name text NOT NULL,
    last_name  text NOT NULL,
    phone      text UNIQUE NOT NULL ,
    email      text NOT NULL UNIQUE ,
    password   text NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGINT references users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id BIGINT references roles (role_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

create TABLE IF NOT EXISTS brands
(
    brand_id   BIGSERIAL PRIMARY KEY,
    brand_name text UNIQUE NOT NULL ,
    deleted    boolean DEFAULT false
);

create TABLE IF NOT EXISTS models
(
    model_id   BIGSERIAL PRIMARY KEY,
    model_name text UNIQUE NOT NULL,
    brand_id   BIGINT references brands (brand_id),
    deleted    boolean DEFAULT false
);

create TABLE IF NOT EXISTS cars
(
    car_id            BIGSERIAL PRIMARY KEY,
    model_id      BIGINT  NOT NULL ,
    user_id       BIGINT  NOT NULL ,
    license_plate text NOT NULL,
    FOREIGN KEY (model_id) REFERENCES models (model_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

