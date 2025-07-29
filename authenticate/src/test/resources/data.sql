-- src/test/resources/data.sql
INSERT INTO UserProfileTable (UserId, UserName, Password, Role, Enabled) VALUES
(1, 'admin1', '$2a$10$zVkjDjzJ9U4x8GoIimlSi.9WU2f5.1C8R6kgFlt3nyBwhrSFZiUTi', 'USER', true),
(2, 'disabledUser', '$2a$10$XURPShQ5u7x6e6uV1N2b4u5Pq2k6V0L8E8G6F7H9I0J1K2L3M4N5', 'USER', false);