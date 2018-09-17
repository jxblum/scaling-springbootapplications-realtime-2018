CREATE TABLE IF NOT EXISTS chat_service.chats (
  id VARCHAR(255) PRIMARY KEY,
  person_id INTEGER REFERENCES chat_service.people(id),
  message VARCHAR(1024),
  timestamp TIMESTAMP,
  received BOOLEAN DEFAULT false
);
