CREATE TABLE IF NOT EXISTS carts(
    id BINARY(16) DEFAULT (uuid_to_bin(uuid())) NOT NULL PRIMARY KEY,
    date_created DATE DEFAULT (curdate()) NOT NULL
);