CREATE TABLE users(
    id serial primary key,
    username varchar(255),
    email varchar(255),
    password varchar(255),
    role varchar(255),
    picture varchar(255)
);

CREATE TABLE place_type(
    id serial primary key,
    name varchar(255)
);

CREATE TABLE plane_model(
    id serial primary key,
    name varchar(255)
);

CREATE TABLE plane(
    id serial primary key,
    idModel int references plane_model(id) ON DELETE CASCADE,
    fabrication_date date
);

CREATE TABLE plane_place(
    id serial primary key,
    idPlace int references place_type(id) ON DELETE CASCADE,
    idPlane int references plane(id) ON DELETE CASCADE,
    number int
);

CREATE TABLE city(
    id serial primary key,
    name varchar(255)
);

CREATE TABLE flight(
    id serial primary key,
    idPlane int references plane(id) ON DELETE CASCADE,
    idStartCity int references city(id) ON DELETE CASCADE,
    idDestinationCity int references city(id) ON DELETE CASCADE,
    start_date timestamp
);

CREATE TABLE flight_price(
    id serial primary key,
    idFlight int references flight(id) ON DELETE CASCADE,
    idPlace int references place_type(id) ON DELETE CASCADE,
    price decimal(10,2)
);

CREATE TABLE flight_promotion(
    id serial primary key,
    idFlight int references flight(id) ON DELETE CASCADE,
    idPlace int references place_type(id) ON DELETE CASCADE,
    place_number int,
    promotion decimal(10,2)
);

CREATE TABLE age(
    id serial primary key,
    name varchar(255),
    min_age int,
    max_age int,
    promotion decimal(10,2)
);

CREATE TABLE booking(
    id serial primary key,
    idUser int references users(id) ON DELETE CASCADE,
    idFlight int references flight(id) ON DELETE CASCADE,
    idPlace int references place_type(id) ON DELETE CASCADE,
    place_number int,
    idAge int references age(id) ON DELETE CASCADE,
    price decimal(10,2),
    picture varchar(255)
);
