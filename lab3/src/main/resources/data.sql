insert into addresses(postal_code, country, region, district, city, street, house, apartment)
values ('123456', 'Россия', 'Московская', 'Центральный', 'Москва', 'Тверская', '1', '10');

insert into owners(surname, name, patronymic, phone, address_id)
values ('Иванов', 'Иван', 'Иванович', '+79991234567', 1);

insert into cars(brand, plate_number, passport_number, owner_id)
values ('Toyota', 'A123BC', 'P123456', 1);