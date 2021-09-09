INSERT INTO RESTAURANT (NAME)
VALUES ('Restaurant 1'),
       ('Restaurant 2'),
       ('Restaurant 3');

INSERT INTO MENU_ITEM (NAME, PRICE, RESTAURANT_ID)
VALUES ('Menu Item for Restaurant 1', 50000, 1),
       ('Menu Item for Restaurant 2', 60000, 2),
       ('Menu Item for Restaurant 3', 70000, 3);

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@email.com', '{noop}user'),
       ('admin', 'admin@email.com', '{noop}admin'),
       ('voted_user', 'voted.user@email.com', '{noop}voted_user');

INSERT INTO USER_ROLE(ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);