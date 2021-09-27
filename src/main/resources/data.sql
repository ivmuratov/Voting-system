INSERT INTO RESTAURANT (NAME)
VALUES ('Restaurant 1'),
       ('Restaurant 2'),
       ('Restaurant 3');

INSERT INTO MENU_ITEM (NAME, PRICE, RESTAURANT_ID)
VALUES ('Menu Item 1 for Restaurant 1', 10000, 1),
       ('Menu Item 2 for Restaurant 1', 10000, 1),
       ('Menu Item 1 for Restaurant 2', 20000, 2),
       ('Menu Item 2 for Restaurant 2', 20000, 2),
       ('Menu Item 1 for Restaurant 3', 30000, 3),
       ('Menu Item 2 for Restaurant 3', 30000, 3);

INSERT INTO MENU_ITEM (NAME, CREATED, PRICE, RESTAURANT_ID)
VALUES ('Menu Item 2021-09-20 for Restaurant 1', '2021-09-20', 10000, 1),
       ('Menu Item 2021-09-20 for Restaurant 2', '2021-09-20', 20000, 2),
       ('Menu Item 2021-09-20 for Restaurant 3', '2021-09-20', 30000, 3);

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@email.com', '{noop}user'),
       ('admin', 'admin@email.com', '{noop}admin'),
       ('voted_user', 'voted.user@email.com', '{noop}voted_user');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO VOTE (RESTAURANT_ID, USER_ID)
VALUES (1, 3);

INSERT INTO VOTE (VOTING_DATE, RESTAURANT_ID, USER_ID)
VALUES ('2021-09-20', 1, 1),
       ('2021-09-20', 1, 2);