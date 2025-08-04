SELECT 'Executing data.sql to insert users' AS debug_message;

INSERT INTO UserProfileTable (UserId, UserName, Password, Role, Enabled) VALUES
(1, 'admin1', '$2a$10$mY9J0PHtDPG8K3BP2vByde0HaHlkl42hQOHTrHBxGQEkC4grm5T06', 'USER', true),
(2, 'disabledUser', '$2a$10$PUwjw/Iq3rWW22d9AmaBMeiroMw9DjpQKMf3kestu0HjkZTJcbMUO', 'USER', false);