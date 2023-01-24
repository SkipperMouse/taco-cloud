DROP TABLE IF EXISTS Ingredient_Ref;
DROP TABLE IF EXISTS INGREDIENT;
DROP TABLE IF EXISTS Taco;
DROP TABLE IF EXISTS Taco_Order;
DROP TABLE IF EXISTS Users;

create table if not exists Taco_Order
(
    id              identity,
    delivery_Name   varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City   varchar(50) not null,
    delivery_State  varchar(2)  not null,
    delivery_Zip    varchar(10) not null,
    cc_number       varchar(16) not null,
    cc_expiration   varchar(5)  not null,
    cc_cvv          varchar(3)  not null,
    placed_at       timestamp   not null
);
create table if not exists Taco
(
    id             identity,
    name           varchar(50) not null,
    taco_order     bigint      not null,
    taco_order_key bigint      not null,
    created_at     timestamp   not null
);
create table if not exists Ingredient_Ref
(
    ingredient varchar(4) not null,
    taco       bigint     not null,
    taco_key   bigint     not null
);
create table if not exists Ingredient
(
    id   varchar(4) PRIMARY KEY not null,
    name varchar(25)            not null,
    type varchar(10)            not null
);

create table if not exists Users
(
    id identity,
    username varchar(30) NOT NULL ,
    password varchar NOT NULL ,
    full_name varchar(120) NOT NULL,
    city varchar,
    street varchar(50),
    state varchar (4),
    zip varchar(30),
    phone_number varchar(30)
);

-- ADD MANY TO MANY LINK
alter table Taco
    add foreign key (taco_order) references Taco_Order (id);
alter table Ingredient_Ref
    add foreign key (ingredient) references Ingredient (id);