-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(10) NOT NULL
);

-- Create books table if it doesn't exist
CREATE TABLE IF NOT EXISTS BOOK (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    pages INTEGER NOT NULL,
    summary VARCHAR(2000),
    review VARCHAR(1000),
    position INTEGER,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);

-- Create table seats if it doesn't exist
CREATE TABLE IF NOT EXISTS TABLE_SEATS (
    seat_number INTEGER PRIMARY KEY,
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
); 