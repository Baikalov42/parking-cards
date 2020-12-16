INSERT INTO brands (brand_name)
VAlUES ('Audi'),
       ('Huyndai'),
       ('Honda'),
       ('BMW');

INSERT INTO models (model_name, brand_id)
VALUES ('A4', (select brand_id from brands where brand_name = 'Audi')),
       ('A5', (select brand_id from brands where brand_name = 'Audi')),
       ('Q3', (select brand_id from brands where brand_name = 'Audi')),
       ('Solaris', (select brand_id from brands where brand_name = 'Huyndai')),
       ('Creta', (select brand_id from brands where brand_name = 'Huyndai')),
       ('Civic', (select brand_id from brands where brand_name = 'Honda')),
       ('Accord', (select brand_id from brands where brand_name = 'Honda')),
       ('X3', (select brand_id from brands where brand_name = 'BMW')),
       ('X5', (select brand_id from brands where brand_name = 'BMW')),
       ('5', (select brand_id from brands where brand_name = 'BMW')),
       ('1', (select brand_id from brands where brand_name = 'BMW'));

INSERT INTO roles (role_name)
VALUES ('ROLE_user'),
       ('ROLE_admin');

INSERT INTO users (first_name, last_name, email, phone, password)
VALUES ('Ivan', 'Ivanov', 'ivan@mail.ru', '+7921234567',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),

       ('Petr', 'Petrov', 'petr@mail.ru', '+78124356709',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),

       ('Irina', 'Soboleva', 'ira@mail.ru', '+74352784903',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),

       ('Admin', 'Adminov', 'adm@mail.ru', '+74352784913',
        '$2a$10$lxUb6YreA12I4RnISsClT.UwLD1i23nOrSEEgD4g3lFo9n9k7EpCC');

INSERT INTO users_roles (user_id, role_id)
VALUES ((select user_id from users where email = 'ivan@mail.ru'),
        (select role_id from roles where role_name = 'ROLE_user')),

       ((select user_id from users where email = 'petr@mail.ru'),
        (select role_id from roles where role_name = 'ROLE_user')),

       ((select user_id from users where email = 'ira@mail.ru'),
        (select role_id from roles where role_name = 'ROLE_user')),

       ((select user_id from users where email = 'adm@mail.ru'),
        (select role_id from roles where role_name = 'ROLE_user')),

       ((select user_id from users where email = 'adm@mail.ru'),
        (select role_id from roles where role_name = 'ROLE_admin'));

INSERT INTO cars (model_id, user_id, license_plate)
VALUES ((select model_id from models where model_name = 'A4'),
        (select user_id from users where email = 'ivan@mail.ru'), 'A123BC178'),

       ((select model_id from models where model_name = 'Solaris'),
        (select user_id from users where email = 'petr@mail.ru'), 'X342BH178'),

       ((select model_id from models where model_name = 'X3'),
        (select user_id from users where email = 'ira@mail.ru'), 'T573TO178'),

       ((select model_id from models where model_name = 'Q3'),
        (select user_id from users where email = 'ira@mail.ru'), 'O666OO178');