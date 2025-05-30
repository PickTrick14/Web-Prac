DROP DOMAIN IF EXISTS email_address;

DROP TABLE IF EXISTS Education;
DROP TABLE IF EXISTS  Resume;
DROP TABLE IF EXISTS  Person_Experience;
DROP TABLE IF EXISTS  Response;
DROP TABLE IF EXISTS  Experience;
DROP TABLE IF EXISTS  Person;
DROP TABLE IF EXISTS  City;
DROP TABLE IF EXISTS  Vacancy;
DROP TABLE IF EXISTS  Company;
DROP TABLE IF EXISTS  Account;

CREATE DOMAIN email_address AS TEXT
  CHECK (
    VALUE ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
  );

CREATE TABLE City (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE Account (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password BIGINT NOT NULL,
    isEmployer BOOLEAN NOT NULL
);

CREATE TABLE Company (
    id SERIAL PRIMARY KEY,
    accountID INT REFERENCES Account(id) ON DELETE CASCADE,
    name TEXT NOT NULL
);

CREATE TABLE Vacancy (
    id SERIAL PRIMARY KEY,
    companyID INT REFERENCES Company(id) ON DELETE CASCADE,
    position TEXT NOT NULL,
    salary BIGINT CHECK (salary >= 0) NOT NULL,
    requirements TEXT DEFAULT 'No requirements'
);

CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    city INT REFERENCES City(id) ON DELETE SET NULL,
    accountID INT UNIQUE REFERENCES Account(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
	age INT CHECK(age >= 18) NOT NULL,
    contactInfo TEXT,
    isSearching BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Education (
    id SERIAL PRIMARY KEY,
    personID INT REFERENCES Person(id) ON DELETE CASCADE,
    institution TEXT NOT NULL,
    specialization TEXT NOT NULL,
    endYear INT CHECK (endYear >= 1900 AND endYear <= EXTRACT(YEAR FROM CURRENT_DATE))
);

CREATE TABLE Experience (
    id SERIAL PRIMARY KEY,
    personID INT REFERENCES Person(id) ON DELETE SET NULL,
    companyID INT REFERENCES  Company(id) ON DELETE SET NULL,
    position TEXT NOT NULL,
    salary BIGINT CHECK (salary >= 0),
    startDate DATE NOT NULL,
    endDate DATE CHECK (endDate >= startDate)
);

CREATE TABLE Resume (
    id SERIAL PRIMARY KEY,
    personID INT REFERENCES Person(id) ON DELETE CASCADE,
    wanted_position TEXT NOT NULL,
    wanted_salary BIGINT CHECK (wanted_salary >= 0) NOT NULL,
    skills TEXT DEFAULT 'No special skills'
);

CREATE TABLE Response (
    id SERIAL PRIMARY KEY,
    personID INT REFERENCES Person(id) ON DELETE CASCADE,
    vacancyID INT REFERENCES Vacancy(id) ON DELETE CASCADE,
    responseDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'Pending' CHECK(status in ('Pending', 'Reviewed', 'Accepted'))
);

CREATE TABLE Person_Experience (
    personID INT REFERENCES Person(id) ON DELETE CASCADE,
    experienceID INT REFERENCES Experience(id) ON DELETE CASCADE,
    PRIMARY KEY (personID, experienceID)
);