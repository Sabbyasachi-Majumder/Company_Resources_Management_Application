-- File: src/test/resources/data.sql
SELECT 'Executing data.sql to insert users' AS debug_message;

INSERT INTO UserProfileTable (UserId, UserName, Password, Role, Enabled) VALUES
(1, 'admin1', '$2a$10$IlN72vcY8enDUJB8vG6mUuVMONntc3axKrjW4mnPaQhvFmcnJbpRq', 'user', true),
(2, 'disabledUser', '$2a$10$bAG5hubsWC.Fe64bjjmUTO7qCZUMxfrzszXV79FMI17cHDLHhdtGu', 'user', false),
(3, 'user1', '$2a$10$5Wjg/6gnk1Gjs1wsYyra7eCuDgxQtV1Vzvcnx/b8asDwIzfTtnZ2q', 'user', true),
(4, 'admin2', '$2a$10$8ISIRdutR2w9YlAO5NnWWenAZ1bP0yZA//V8lbaiwhU/EFqjsk.xK', 'admin', true);