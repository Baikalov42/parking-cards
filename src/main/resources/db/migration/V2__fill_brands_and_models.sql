insert into car_brands (name, deleted)
values
('Audi', false),
('Huyndai', false),
('Honda', false),
('BMW', false);
insert into car_models (name, brand_id, deleted)
values
('A4', (select id from car_brands where name = 'Audi' limit 1), false),
('A5', (select id from car_brands where name = 'Audi' limit 1), false),
('Q3', (select id from car_brands where name = 'Audi' limit 1), false),
('Solaris', (select id from car_brands where name = 'Huyndai' limit 1), false),
('Creta', (select id from car_brands where name = 'Huyndai' limit 1), false),
('Civic', (select id from car_brands where name = 'Honda' limit 1), false),
('Accord', (select id from car_brands where name = 'Honda' limit 1), false),
('X3', (select id from car_brands where name = 'BMW' limit 1), false),
('X5', (select id from car_brands where name = 'BMW' limit 1), false),
('5', (select id from car_brands where name = 'BMW' limit 1), false),
('1', (select id from car_brands where name = 'BMW' limit 1), false);