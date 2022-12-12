DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS publisher;
DROP TABLE IF EXISTS phone;
DROP TABLE IF EXISTS published;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS written_by;
DROP TABLE IF EXISTS order_receipt;
DROP TABLE IF EXISTS book_order;
DROP TABLE IF EXISTS owner;
DROP TABLE IF EXISTS access;
DROP TABLE IF EXISTS user_order;
DROP TABLE IF EXISTS user_info;

CREATE TABLE book(
    ISBN integer NOT NULL,              
    book_name text NOT NULL,                    
	sales_price float NOT NULL,
	purchase_price float NOT NULL,
	num_pages integer NOT NULL,
	quantity integer NOT NULL,
    primary key (ISBN)                  
);

CREATE TABLE genre(
	book_id integer NOT NULL,
	genre text NOT NULL,
	primary key(book_id, genre)
);
CREATE TABLE publisher(
	publisher_id integer NOT NULL,
	fname text NOT NULL,
	lname text NOT NULL,
	street_name text NOT NULL,
	building_num integer NOT NULL,
	postal_code text NOT NULL,
	city text NOT NULL,
	prov text NOT NULL,
	country text NOT NULL,
	bank_acc integer NOT NULL,
	primary key(publisher_id)
);
CREATE TABLE phone(
	p_id integer NOT NULL,
	pno text NOT NULL,
	primary key(p_id, pno)
);
CREATE TABLE published(
	p_id integer NOT NULL,
	book_id integer NOT NULL,
	percentage integer NOT NULL,
	primary key(p_id, book_id)
);

CREATE TABLE author(
	author_id integer NOT NULL,
	fname text NOT NULL,
	lname text NOT NULL,
	primary key(author_id)
);

CREATE TABLE written_by(
	author_id integer NOT NULL,
	book_id integer NOT NULL,
	primary key(author_id, book_id)
);

CREATE TABLE order_receipt(
	order_number integer NOT NULL,
	tracking_num integer NOT NULL,
	o_day integer NOT NULL,
	o_month text NOT NULL,
	o_year integer NOT NULL,
	b_street_name text NOT NULL,
	b_building_num integer NOT NULL,
	b_postal_code text NOT NULL,
	b_city text NOT NULL,
	b_prov text NOT NULL,
	b_country text NOT NULL,
	s_street_name text NOT NULL,
	s_building_num integer NOT NULL,
	s_postal_code text NOT NULL,
	s_city text NOT NULL,
	s_prov text NOT NULL,
	s_country text NOT NULL,
	primary key (order_number)
);

CREATE TABLE book_order(
	book_id integer NOT NULL,
	order_num integer NOT NULL,
	primary key (book_id, order_num)
);

CREATE TABLE owner(
	username text NOT NULL,
	password text NOT NULL,
	primary key(username)
);

CREATE TABLE access(
	username text NOT NULL,
	order_num integer NOT NULL,
	primary key (username, order_num)
);

CREATE TABLE user_info(
	username text NOT NULL,
	password text NOT NULL,
	street_name text NOT NULL,
	building_num integer NOT NULL,
	postal_code text NOT NULL,
	city text NOT NULL,
	prov text NOT NULL,
	country text NOT NULL,
	primary key (username)
);

CREATE TABLE user_order(
	username text NOT NULL,
	order_num integer NOT NULL,
	primary key (username, order_num)
);
