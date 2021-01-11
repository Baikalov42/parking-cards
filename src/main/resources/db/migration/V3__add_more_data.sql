INSERT INTO brands (brand_name)
VAlUES ('Kia'),
       ('Tesla'),
       ('Jeep'),
       ('Lexus'),
       ('Opel'),
       ('Skoda'),
       ('Mazda'),
       ('Ford'),
       ('GAZ');

INSERT INTO models (model_name, brand_id)
VALUES ('Soul', (select brand_id from brands where brand_name = 'Kia')),
       ('Sportage', (select brand_id from brands where brand_name = 'Kia')),
       ('Rio', (select brand_id from brands where brand_name = 'Kia')),
       ('Model S', (select brand_id from brands where brand_name = 'Tesla')),
       ('Model X', (select brand_id from brands where brand_name = 'Tesla')),
       ('Corsa', (select brand_id from brands where brand_name = 'Opel')),
       ('Astra', (select brand_id from brands where brand_name = 'Opel')),
       ('Transit', (select brand_id from brands where brand_name = 'Ford')),
       ('Focus', (select brand_id from brands where brand_name = 'Ford')),
       ('Mondeo', (select brand_id from brands where brand_name = 'Ford')),
       ('Explorer', (select brand_id from brands where brand_name = 'Ford'));

INSERT INTO users (first_name, last_name, email, phone, password)
VALUES  ('Sersey', 'Lannister', 'sersey@got.com', '+71086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Jamie', 'Lannister', 'jamie@got.com', '+72086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Tirion', 'Lannister', 'tirion@got.com', '+73086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Sansa', 'Stark', 'sansa@got.com', '+74086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Rob', 'Stark', 'rob@got.com', '+75086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Ed', 'Stark', 'ed@got.com', '+76086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Cathelyn', 'Stark', 'cathelyn@got.com', '+77086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Bran', 'Stark', 'bran@got.com', '+77186396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Rikon', 'Stark', 'rikon@got.com', '+77286396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe'),
        ('Arya', 'Stark', 'arya@got.com', '+78086396278',
        '$2a$10$CH8Q21IP6NL6eGCDgC3fdualB51C6vIRfOT9yYdyrjMysoYfLpZLe');


INSERT INTO users_roles (user_id, role_id)
VALUES  ((select user_id from users where email = 'sersey@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'jamie@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'tirion@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'sansa@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'rob@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'ed@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'cathelyn@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'bran@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'rikon@got.com'),
        (select role_id from roles where role_name = 'ROLE_user')),

        ((select user_id from users where email = 'arya@got.com'),
        (select role_id from roles where role_name = 'ROLE_user'));

INSERT INTO cars (model_id, user_id, license_plate)
VALUES ((select model_id from models where model_name = 'Model S'),
        (select user_id from users where email = 'sansa@got.com'), 'O326TP178'),

       ((select model_id from models where model_name = 'Focus'),
        (select user_id from users where email = 'bran@got.com'), 'X037BH178'),

       ((select model_id from models where model_name = 'Rio'),
        (select user_id from users where email = 'ed@got.com'), 'T111TO178'),

       ((select model_id from models where model_name = 'Astra'),
        (select user_id from users where email = 'tirion@got.com'), 'O789OO178');
