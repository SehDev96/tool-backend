CREATE TABLE IF NOT EXISTS app_user
(
    id uuid NOT NUll PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_postcode_lat_long
(
    id int NOT NULL PRIMARY KEY,
    postcode varchar(10) UNIQUE NOT NULL,
    latitude numeric(10,7) NULL,
    longitude numeric(10,7) NULL
);
