# --- !Ups
CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL);

# --- !Downs
drop table if exists "user";