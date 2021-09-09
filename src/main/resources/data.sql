INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@email.com', '{noop}user'),
       ('admin', 'admin@email.com', '{noop}admin');

INSERT INTO USER_ROLE(ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);