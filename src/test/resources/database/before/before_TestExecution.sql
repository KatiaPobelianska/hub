INSERT into person(username, email, password, role, enable)
VALUES ('Bob', 'bob@mail.com', '$2a$10$11dWe870ik1hm7di.ooCSO8DZL7kPR89FhgzoG553kdmxdwSUT6k2', 'ROLE_USER', true);

INSERT into person(username, email, password, role, enable)
VALUES ('Joe', 'joe@mail.com', '$2a$10$VC51e1QCrXOGiMiGmDdU5uOD7UUssZG6Y3jzA2hooKQXokQefXnTK', 'ROLE_ADMIN', true);

INSERT into person(username, email, password, role, enable)
VALUES ('Billy', 'billy@mail.com', '$2a$10$ZvmNbEF3OR/CVRABM92B8.J2GnIPf7uyr3ztjLNdc3zP539Yboofa', 'ROLE_USER', true);

INSERT INTO activation(key, username)
VALUES ('SaNloXnB1K', 'Bob');

INSERT INTO activation(key, username)
VALUES ('Th917uA72jkv', 'Joe');

INSERT INTO activation(key, username)
VALUES ('bfuKLDuEoxGRu', 'Billy');

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Bob', 'Java Dev - My new desktop setup', 'This is my new setup..', 'someUrl', 'TECHNIC', 127);

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Bob', 'Latest Java world news', 'This is the list of latest java 33 updates', 'someUrl', 'TECHNIC', 1200);

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Joe', 'Adventure in the Enchanted Forest', 'Embark on an enchanting journey through the mystical forest!', 'forestUrl', 'CARTOON', 600);

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Joe', 'Underwater Odyssey: Exploring the Deep Sea', 'Dive into the depths of the ocean and discover its hidden wonders!', 'underwaterUrl', 'CARTOON', 430);

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Billy', 'Exploring mountains', 'The view from the top was breathtaking..', 'mountainsUrl', 'NATURE', 484);

INSERT INTO post(owner_username, title, description, photo_url, category, views)
VALUES ('Billy', 'Beach getaway', 'Spent the weekend relaxing by the ocean..', 'beachUrl', 'NATURE', 781);

INSERT INTO comment(post_id, person_username, description)
VALUES (1, 'Joe', 'Very useful post!Thanks!');
INSERT INTO comment(post_id, person_username, description)
VALUES (1, 'Billy', 'Thanks for such a good time spending!');

INSERT INTO photo_like(post_id, person_username) VALUES (1, 'Joe');
INSERT INTO photo_like(post_id, person_username) VALUES (1, 'Billy');
INSERT INTO photo_like(post_id, person_username) VALUES (3, 'Bob');

INSERT INTO statistic(total_views, total_likes, total_comments) VALUES (1500, 504, 42);
INSERT INTO statistic(total_views, total_likes, total_comments) VALUES (2394, 600, 140);
