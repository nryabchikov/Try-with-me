CREATE TABLE users
(
    id                 INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    login              VARCHAR(30) CHECK (LENGTH(login) BETWEEN 5 AND 30) UNIQUE,
    password           VARCHAR(100) CHECK (LENGTH(password) BETWEEN 8 AND 100),
    email              VARCHAR(40) UNIQUE,
    name               VARCHAR(30),
    surname            VARCHAR(30),
    patronymic         VARCHAR(30),
    address            VARCHAR(100),
    date_of_birthday   DATE,
    enable             BOOL NOT NULL DEFAULT FALSE,
    verification_code VARCHAR(50),
    photo_filename     VARCHAR(255),
    reset_token        VARCHAR(50)
);

CREATE TABLE friends
(
    id        INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    user_id   INT,
    friend_id INT
);

CREATE TABLE friend_request
(
    id          INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    sender_id   INT NOT NULL,
    receiver_id INT NOT NULL
);