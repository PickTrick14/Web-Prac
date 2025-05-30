INSERT INTO City (name) VALUES
('Moscow'),
('St. Peterburg'),
('Smolensk'),
('Kazan'),
('Yaroslavl'),
('Novosibirsk'),
('Tula');

-- Аккаунты компаний
INSERT INTO Account (email, password, isEmployer) VALUES
('company1@gmail.com', 123456, FALSE),
('company2@gmail.com', 234567, FALSE),
('company3@gmail.com', 345678, FALSE),
('company4@gmail.com', 456789, FALSE),
('company5@gmail.com', 567890, FALSE);

-- Аккаунты соискателей
INSERT INTO Account (email, password, isEmployer) VALUES
('alice.johnson@gmail.com', 1113223, TRUE),
('bob.smith@gmail.com', 2242212, TRUE),
('charlie.davis@gmail.com', 3323433, TRUE),
('diana.brown@gmail.com', 4476544, TRUE),
('edward.wilson@gmail.com', 5555234, TRUE),
('fiona.clark@gmail.com', 6603221, TRUE),
('george.miller@gmail.com', 7564377, TRUE);

INSERT INTO Company (accountID, name) VALUES
(1, 'Tech Solutions Inc.'),
(2, 'Sberbank'),
(3, 'Yandex'),
(4, 'Positive Technologies'),
(5, 'OZON'),
(6, 'Gazprom'),
(7, 'X5 Group');

INSERT INTO Vacancy (companyID, position, salary, requirements) VALUES
(1, 'Software Engineer', 90000, 'Experience with Java, Python'),
(2, 'Graphic Designer', 50000, 'Adobe Creative Suite experience'),
(3, 'Project Manager', 80000, 'PMP certification required'),
(4, 'Data Analyst', 70000, 'SQL and data visualization skills'),
(5, 'Sales Manager', 60000, 'Proven sales record'),
(6, 'HR Specialist', 55000, 'HR degree and experience');

INSERT INTO Person (city, accountID, name, age, contactInfo, isSearching) VALUES
(1, 2, 'Alice Johnson', 28, 'alice.johnson@gmail.com', TRUE),
(2, 3, 'Bob Smith', 32, 'bob.smith@gmail.com', TRUE),
(3, 5, 'Charlie Davis', 26, 'charlie.davis@gmail.com', TRUE),
(4, 4, 'Diana Brown', 30, 'diana.brown@gmail.com', FALSE),
(5, 1, 'Edward Wilson', 35, 'edward.wilson@gmail.com', TRUE),
(6, 7, 'Fiona Clark', 29, 'fiona.clark@gmail.com', TRUE),
(7, 6, 'George Miller', 40, 'george.miller@gmail.com', FALSE);

INSERT INTO Education (personID, institution, specialization, endYear) VALUES
(1, 'MSU', 'Computer Science', 2015),
(2, 'Stanford University', 'Design', 2012),
(3, 'MIT', 'Engineering', 2018),
(4, 'Ranhigs', 'Business Administration', 2010),
(5, 'HSE', 'Economics', 2008),
(6, 'HSE', 'Marketing', 2016),
(7, 'MSU', 'Psychology', 2005);

INSERT INTO Experience (companyID, position, salary, startDate, endDate) VALUES
(1, 'Junior Developer', 60000, '2016-01-01', '2018-01-01'),
(2, 'Assistant Designer', 45000, '2014-06-01', '2016-06-01'),
(3, 'Coordinator', 50000, '2017-03-01', '2019-03-01'),
(4, 'Analyst', 65000, '2015-05-01', '2018-05-01'),
(5, 'Sales Representative', 40000, '2013-09-01', '2015-09-01'),
(6, 'HR Assistant', 42000, '2018-11-01', '2020-11-01'),
(7, 'Marketing Intern', 35000, '2019-07-01', '2020-07-01');

INSERT INTO Resume (personID, wanted_position, wanted_salary, skills) VALUES
(1, 'Software Engineer', 95000, 'Java, Python, SQL'),
(2, 'Senior Graphic Designer', 55000, 'Adobe Photoshop'),
(3, 'Project Manager', 85000, 'Agile, Scrum'),
(4, 'Data Scientist', 90000, 'Machine Learning, R, Python'),
(5, 'Sales Director', 75000, 'Negotiation, CRM'),
(6, 'Marketing Manager', 70000, 'SEO, Content Marketing'),
(7, 'HR Manager', 68000, 'Recruitment, Employee Relations');

INSERT INTO Response (personID, vacancyID, responseDate, status) VALUES
(1, 1, '2024-01-10 10:00:00', 'Pending'),
(1, 2, '2024-01-11 11:00:00', 'Reviewed'),
(3, 3, '2024-01-12 12:00:00', 'Pending'),
(4, 4, '2024-01-13 13:00:00', 'Accepted'),
(5, 5, '2024-01-14 14:00:00', 'Pending'),
(6, 6, '2024-01-15 15:00:00', 'Reviewed');

INSERT INTO Person_Experience (personID, experienceID) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7);