INSERT INTO genre (genre_name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO rating_MPA (rating_mpa_name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO film (film_name, film_description, film_releaseDate, film_duration, film_rating_MPA_id)
VALUES ('Имя_Фильма_1', 'Описание_1', '2010-10-10', 100, 1),
('Имя_Фильма_2', 'Описание_2', '2015-10-10', 50, 2),
('Имя_Фильма_3', 'Описание_3', '2017-10-10', 200, 3),
('Имя_Фильма_4', 'Описание_4', '2000-10-10', 123, 4),
('Имя_Фильма_4', 'Описание_5', '1999-10-10', 180, 5);

INSERT INTO users (user_name, user_email, user_login, user_birthday)
VALUES ('Имя_1', 'mail1@mail.ru', 'login_1', '1988-10-10'),
('Имя_2', 'mail2@mail.ru', 'login_2', '2015-10-10'),
('Имя_3', 'mail3@mail.ru', 'login_3', '2000-10-10'),
('Имя_4', 'mail4@mail.ru', 'login_4', '2007-10-10'),
('Имя_5', 'mail5@mail.ru', 'login_5', '2003-10-10');

INSERT INTO friendship (user_id, friend_id)
VALUES (1,2),
       (3,5),
       (1,3);

INSERT INTO film_like (film_id, user_id)
VALUES (1,3),
       (2,5),
       (3,4),
       (3,1),
       (3,2);

INSERT INTO film_genres (film_id, genre_id)
VALUES (1,1),
       (1,2),
       (2,1),
       (3,5);