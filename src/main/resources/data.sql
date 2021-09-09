INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@email.com', 'user'),
       ('admin', 'admin@email.com', 'admin');

INSERT INTO USER_ROLE(ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);