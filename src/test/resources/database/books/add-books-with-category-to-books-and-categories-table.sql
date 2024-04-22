INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Kobzar', 'Shevchenko T. G.', '0123456789', 100.55),
       (2, 'Lisova pisnia', 'Lesia Ykrainka', '9874563210', 150.75);

INSERT INTO categories (id, name)
VALUES (1, 'Poetry'),
       (2, 'Fantasy');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 2);