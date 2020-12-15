INSERT INTO car_brands (name)
VAlUES
('Audi'),
('Huyndai'),
('Honda'),
('BMW');

INSERT INTO car_models (name, brand_id)
VALUES
('A4', (select id from car_brands where name = 'Audi' limit 1)),
('A5', (select id from car_brands where name = 'Audi' limit 1)),
('Q3', (select id from car_brands where name = 'Audi' limit 1)),
('Solaris', (select id from car_brands where name = 'Huyndai' limit 1)),
('Creta', (select id from car_brands where name = 'Huyndai' limit 1)),
('Civic', (select id from car_brands where name = 'Honda' limit 1)),
('Accord', (select id from car_brands where name = 'Honda' limit 1)),
('X3', (select id from car_brands where name = 'BMW' limit 1)),
('X5', (select id from car_brands where name = 'BMW' limit 1)),
('5', (select id from car_brands where name = 'BMW' limit 1)),
('1', (select id from car_brands where name = 'BMW' limit 1));

INSERT INTO roles (role)
VALUES
('user'),
('admin');

INSERT INTO users (first_name, last_name, email, phone, role_id)
VALUES
('Ivan', 'Ivanov', 'ivan@mail.ru', '+7921234567', (select id from roles where role = 'admin' limit 1)),
('Petr', 'Petrov', 'petr@mail.ru', '+78124356709', (select id from roles where role = 'user' limit 1)),
('Irina', 'Soboleva', 'ira@mail.ru', '+74352784903', (select id from roles where role = 'user' limit 1));

INSERT INTO cars (model_id, user_id, license_plate)
VALUES
((select id from car_models where name = 'A4' limit 1), (select id from users where email = 'ivan@mail.ru' limit 1), 'A123BC178'),
((select id from car_models where name = 'Solaris' limit 1), (select id from users where email = 'petr@mail.ru' limit 1), 'X342BH178'),
((select id from car_models where name = 'X3' limit 1), (select id from users where email = 'ira@mail.ru' limit 1), 'T573TO178'),
((select id from car_models where name = 'Q3' limit 1), (select id from users where email = 'ira@mail.ru' limit 1), 'O666OO178');