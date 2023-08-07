CREATE TABLE IF NOT EXISTS users
(
    id       UUID,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(500) NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username
    on authorities (username, authority);