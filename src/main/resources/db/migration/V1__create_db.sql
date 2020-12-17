DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS brands CASCADE;
DROP TABLE IF EXISTS models CASCADE;
DROP TABLE IF EXISTS cars CASCADE;

CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(30) UNIQUE NOT NULL
);
CREATE TABLE users (
    user_id bigserial PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    phone VARCHAR(12) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE users_roles (
    user_id BIGINT references users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id BIGINT references roles (role_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
CREATE TABLE brands (
    brand_id BIGSERIAL PRIMARY KEY,
    brand_name VARCHAR(30) UNIQUE NOT NULL,
    deleted BOOLEAN DEFAULT false
);
CREATE TABLE models (
    model_id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(30) UNIQUE NOT NULL,
    brand_id BIGINT REFERENCES brands (brand_id),
    deleted BOOLEAN DEFAULT false
);
CREATE TABLE cars (
    car_id BIGSERIAL PRIMARY KEY,
    model_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    license_plate VARCHAR(9) NOT NULL,
    FOREIGN KEY (model_id) REFERENCES models (model_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);