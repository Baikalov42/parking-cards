CREATE TABLE IF NOT EXISTS roles (id bigint PRIMARY KEY, role text NOT NULL);

CREATE TABLE IF NOT EXISTS users
(id bigint PRIMARY KEY,
first_name text NOT NULL,
last_name text NOT NULL,
telephone text,
e_mail text NOT NULL,
role_id bigint,
FOREIGN KEY (role_id) REFERENCES roles (id));

CREATE TABLE IF NOT EXISTS car_brands (id bigint PRIMARY KEY, name text, deleted boolean);

CREATE TABLE IF NOT EXISTS car_models (id bigint PRIMARY KEY,
  name text NOT NULL,
  brand_id bigint,
  deleted boolean,
  FOREIGN KEY (brand_id) REFERENCES car_brands (id));

CREATE TABLE IF NOT EXISTS cars
(id bigint PRIMARY KEY,
model_id bigint,
user_id bigint,
license_plate text NOT NULL,
FOREIGN KEY (model_id) REFERENCES car_models (id),
FOREIGN KEY (user_id) REFERENCES users (id));

