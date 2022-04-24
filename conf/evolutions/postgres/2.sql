# --- !Ups
CREATE TABLE "student" (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR UNIQUE NOT NULL,
                           intervieCounter NUMERIC NOT NULL,
                           chanceToComplete NUMERIC NOT NULL,
                           javaRushLevel NUMERIC NOT NULL);

# --- !Downs
drop table if exists "student";