create table book (
    book_id int generated by default as identity primary key,
    title varchar(50) not null,
    author varchar(50) not null,
    published_year varchar(4) not null,
    genre varchar(20) not null,
    price numeric not null
);

create table customer (
    customer_id int generated by default as identity primary key,
    name varchar(250) not null
);

create table purchase_order (
    order_id int generated by default as identity primary key,
    customer_id int not null,
    created_date date not null,

    constraint customer_fk foreign key (customer_id) references customer(customer_id)
);

create table purchase_order_line (
    order_line_id int generated by default as identity primary key,
    order_id int not null,
    book_id int not null,
    item_count int not null,

    constraint order_fk foreign key (order_id) references purchase_order(order_id),
    constraint book_fk foreign key (book_id) references book(book_id)
);