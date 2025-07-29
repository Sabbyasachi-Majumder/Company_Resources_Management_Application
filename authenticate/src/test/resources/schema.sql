-- src/test/resources/schema.sql
DROP TABLE IF EXISTS UserProfileTable;
CREATE TABLE UserProfileTable (
    UserId BIGINT PRIMARY KEY,
    UserName VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(50) NOT NULL,
    Enabled BOOLEAN NOT NULL
);