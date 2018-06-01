DROP TABLE IF EXISTS channel;

CREATE TABLE IF NOT EXISTS channel (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name CHAR(50) NOT NULL UNIQUE CHECK(length(trim(name)) != 0)
);

INSERT INTO channel (name) VALUES
	('general');
