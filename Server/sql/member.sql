DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name CHAR(50) NOT NULL CHECK(length(trim(name)) != 0),
	surname CHAR(50) NOT NULL CHECK(length(trim(surname)) != 0),
	secondname CHAR(50) NOT NULL CHECK(length(trim(surname)) != 0),
	email CHAR(255) NOT NULL CHECK(length(trim(email)) != 0),
	login CHAR(50) NOT NULL UNIQUE,
	password CHAR(50) NOT NULL CHECK(length(trim(password)) != 0)
);

INSERT INTO member (name, surname, secondname, email, login, password) VALUES
	('Misha', 'Eliseyev', 'Viktorovich', 'm.e.v@gmail.com', 'misha', '123'),
	('Alex', 'Salun', 'Denisovich', 'a.s.d@gmail.com', 'alex', '123');
