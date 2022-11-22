CREATE SCHEMA IF NOT EXISTS ORDERS;

DROP TABLE IF EXISTS orders.products;
CREATE TABLE orders.products
(
    product_id varchar(255) not null primary key,
    name       varchar(255),
    price      integer
);

CREATE TABLE IF NOT EXISTS orders.orders
(
    order_number     varchar(255) not null primary key,
    member_id        varchar(255),
    member_name      varchar(255),
    delivery_id      varchar(255),
    order_date_time  timestamp,
    order_state      varchar(255),
    address1         varchar(255),
    address2         varchar(255),
    city             varchar(255),
    receiver         varchar(255),
    telephone_number varchar(255)
);

CREATE TABLE IF NOT EXISTS orders.order_lines
(
    order_line_id bigint not null primary key,
    order_number  varchar(255) constraint fkqq96vo31reljufxvp92ikeg7g references orders.orders,
    price         integer,
    product_id    varchar(255),
    product_name  varchar(255),
    quantity      integer
);

CREATE SEQUENCE IF NOT EXISTS orders.order_seq;