-- File: src/test/resources/data.sql
SELECT 'Executing data.sql to insert users' AS debug_message;

INSERT INTO UserProfileTable (UserId, UserName, Password, Role, Enabled) VALUES
(1, 'admin0001', '$2a$10$r1IwNRFpRFTSKRofhbm0ke2g8/5qIfZ0wRDA1Pl.64wVgwCZCefLK', 'ROLE_ADMIN', true),
(2, 'disabledUser', '$2a$10$9kcwn5zDIr4givCXGDjSKuKZx/FgEsyMOeTn37UHFJt7OFX.tkJsW', 'ROLE_USER', false),
(3, 'user0001', '$2a$10$QFb80isO6JNeK6wDHPfrvuqzEvFwc1PR49rs4G.yKTVI7nwFQ7muK', 'ROLE_USER', true),
(4, 'admin0002', '$2a$10$oDMMjD8yt.4pEM/6nawKK.0ahKoMDZq3f2nhcj867NjRrCUpi1DfO', 'ROLE_ADMIN', false);