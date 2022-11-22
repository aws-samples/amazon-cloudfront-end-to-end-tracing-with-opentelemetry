CREATE SCHEMA IF NOT EXISTS delivery;

CREATE SEQUENCE IF NOT EXISTS delivery.delivery_seq;
CREATE SEQUENCE IF NOT EXISTS delivery.delivery_item_seq;

CREATE TABLE IF NOT EXISTS delivery.orders
(
    order_no      varchar(255) not null primary key,
    order_created timestamp,
    orderer_id    varchar(255),
    orderer_name  varchar(255)
);

CREATE TABLE IF NOT EXISTS delivery.delivery
(
    delivery_id        bigint not null primary key,
    receiver_address1  varchar(255),
    receiver_address2  varchar(255),
    receiver_city      varchar(255),
    receiver_name      varchar(255),
    receiver_telephone varchar(255),
    sender_address1    varchar(255),
    sender_address2    varchar(255),
    sender_city        varchar(255),
    sender_name        varchar(255),
    sender_telephone   varchar(255),
    last_updated       timestamp,
    status             varchar(255),
    order_no           varchar(255) constraint fk2bhrmlgg13q7jkwlfljhsbe6b references delivery.orders
);

CREATE TABLE IF NOT EXISTS delivery.delivery_item
(
    item_id     bigint not null primary key,
    item_name   varchar(255),
    quantity    integer,
    delivery_id bigint constraint fk56cvume0tkg1ai7j33bsdmlx8 references delivery.delivery
);
