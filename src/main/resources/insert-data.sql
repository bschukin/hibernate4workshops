INSERT INTO Gender (ID, GENDER, classic) VALUES (1, 'WOMAN', 1);
INSERT INTO Gender (ID, GENDER, classic) VALUES (2, 'MAN', 1);
INSERT INTO Gender (ID, GENDER, classic) VALUES (3, 'FOO', 0);
INSERT INTO Gender (ID, GENDER, classic) VALUES (4, 'BAR', 0);


--INSERT INTO JiraWorker (ID, name, email, genderId) VALUES (1, 'Madonna', 'madonna@google.com', 1);
--INSERT INTO JiraWorker (ID, name, email, genderId) VALUES (2, 'John Lennon', 'john@google.com', 2);
--INSERT INTO JiraWorker (ID, name, email, genderId) VALUES (3, 'Fillip Bedrosovich', 'filya@google.com', 3);
--INSERT INTO JiraWorker (ID, name, email, genderId) VALUES (4, 'Oleg Gazmanov', 'gazman@google.com', 4);


INSERT INTO Department (ID, name, parent_id) VALUES (1, 'Департамент', null);
INSERT INTO Department (ID, name, parent_id) VALUES (2, 'Отдел', 1);
INSERT INTO Department (ID, name, parent_id) VALUES (3, 'Сектор', 2);
INSERT INTO Department (ID, name, parent_id) VALUES (4, 'Пара', 3);
