DROP TABLE IF EXISTS message;

CREATE TABLE IF NOT EXISTS message (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	body TEXT NOT NULL CHECK(length(trim(body)) != 0),
	member_id INTEGER NOT NULL REFERENCES member(id) ON DELETE SET NULL ON UPDATE CASCADE,
	channel_id INTEGER NOT NULL REFERENCES channel(id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TRIGGER message_member_exists
BEFORE INSERT ON message
WHEN NOT EXISTS (SELECT * FROM member WHERE id = NEW.member_id)
BEGIN
    SELECT RAISE(FAIL, "Message insertion failed: No member with such id");
END;

CREATE TRIGGER message_channel_exists
BEFORE INSERT ON message
WHEN NOT EXISTS (SELECT * FROM channel WHERE id = NEW.channel_id)
BEGIN
    SELECT RAISE(FAIL, "Message insertion failed: No channel with such id");
END;

INSERT INTO message (member_id, channel_id, body) VALUES
	(1, 1, "Hi!"),
	(2, 1, "Sup!");
