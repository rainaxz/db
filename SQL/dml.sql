-- ISBN, book name, sales price, purchase price, num pages, quantity
INSERT INTO book
VALUES (1, 'Let it go', 7.50, 0.20, 40, 15),
		(2, 'Let it snow', 10.25, 0.50, 150, 20),
		(3, 'Our missing hearts', 21.60, 12.00, 320, 18),
		(4, 'Babel', 26.24, 10.40, 560, 15),
		(5, 'Book Lovers', 20.00, 8.00, 400, 16);

-- book id, genre
INSERT INTO genre
VALUES (1, 'Comedy'),
	(1, 'Drama'),
	(2, 'Sci-Fi'),
	(2, 'Romance'),
	(3, 'Drama'),
	(3, 'Dystopian'),
	(4, 'Fantasy'),
	(4, 'Historial Fiction'),
	(5, 'Romance');

-- author id, fname, lname
INSERT INTO author
VALUES (1, 'Raina', 'Zhang'),
	(2, 'Maya', 'Wong'),
	(3, 'Celeste', 'Ng'),
	(4, 'R. F', 'Kuang'),
	(5, 'Emily', 'Henry');

-- author id, book id
INSERT INTO written_by
VALUES (1,1), (2,2), (2,3), (3,3), (4,4), (5,5);

-- publisher id, fname, lname, street name, building num, postal code, city, prov, country, bank_acc
INSERT INTO publisher
VALUES (1, 'John', 'Green', 'Street St.', 1, 'A0A 0A0', 'Ottawa', 'ON', 'Canada', 123456),
(2, 'Sam', 'Darth', 'Gingerbread Street', 11, 'R6Y 8L0', 'Victoria', 'PE', 'Canada', 654321),
(3, 'Harper', 'Collins', 'Apple Bottom Jeans Street', 3434, 'H8V4N6', 'Toronto', 'ON', 'Canada', 809754),
(4, 'Penguin', 'Inc', 'Time Square Street', 101, 'J6G8H9', 'Winnepeg', 'MA', 'Canada', 456876);

-- p id, book id, percentage
INSERT INTO published
VALUES (1, 1, 20), (1, 2, 30), (2, 3, 25), (4, 4, 40);

-- p_id, pno
INSERT INTO phone
VALUES (1, '6136921234'), (2, '6139119111'), (3, '3433439999'), (4, '6138185555'), (4, '6138889999');

-- order num, tracking num, o day, o month, o year, b street name, b building num, b postal, b city, b prov, b country
-- s street name, s building num, s postal, s city, s prov, s country
INSERT INTO order_receipt
VALUES (1, 1, 7, 'March', 2022, 'blackshire', 16, 'K2J5L8', 'Nepean', 'Ontario', 'Canada', 'blackshire', 16, 'K2J5L8', 'Nepean', 'Ontario', 'Canada'),
(2, 2, 13, 'March', 2022, 'Rivermist', 26, 'J8K9L8', 'Merivale', 'Ontario', 'Canada', 'Rivermist', 26, 'J8K9L8', 'Merivale', 'Ontario', 'Canada'),
(3, 3, 22, 'March', 2022, 'Bentgrass', 2, 'J7F5L0', 'Cole', 'Edmonton', 'Canada', 'Moana', 8, 'J7F5L0', 'Alexa', 'Edmonton', 'Canada');

-- book id, order num
INSERT INTO book_order
VALUES (1,1), (2,1), (3, 2), (5, 3), (4, 3);

-- username, password
INSERT INTO owner
VALUES ('mayawong', '1234'),
('rainazhang', '5678'),
('annabel', '123');

-- username, order num
INSERT INTO access
VALUES ('mayawong', 1),
('rainazhang', 2),
('annabel', 3);

-- username, password, street name, building num, postal code, city, prov, country
INSERT INTO user_info
VALUES ('reader1', 'password321', 'Micheal street', 3, 'I8L9P0', 'kalona', 'Alberta', 'Canada'),
('reader2', 'comp3005', 'St. Nicholas Street', 88, 'T3D7H6', 'sandwich', 'Saskatoon', 'Canada'),
('reader3', 'haha', 'Libby Street', 7, 'G7J5F4', 'Doorknob', 'Prince Edward Island', 'Canada');

-- username, order num
INSERT INTO user_order
VALUES ('reader1', 1), ('reader2', 2), ('reader3', 3);
