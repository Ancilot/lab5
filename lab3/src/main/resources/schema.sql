create table if not exists addresses(
    id bigint auto_increment primary key,
    postal_code varchar(255) not null,
    country varchar(255) not null,
    region varchar(255) not null,
    district varchar(255) not null,
    city varchar(255) not null,
    street varchar(255) not null,
    house varchar(255) not null,
    apartment varchar(255)
);

create table if not exists owners(
    id bigint auto_increment primary key,
    surname varchar(50) not null,
    name varchar(50) not null,
    patronymic varchar(50) not null,
    phone varchar(20) not null,
    address_id bigint not null,
    foreign key (address_id) references addresses(id)
);

create table if not exists cars(
    id bigint auto_increment primary key,
    brand varchar(50) not null,
    plate_number varchar(12) not null,
    passport_number varchar(30) not null,
    owner_id bigint not null,
    foreign key (owner_id) references owners(id)
);