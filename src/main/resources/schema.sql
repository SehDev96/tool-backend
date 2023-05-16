CREATE TABLE IF NOT EXISTS app_user
(
    id uuid NOT NUll PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);
