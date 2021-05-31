CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE employee (
id UUID DEFAULT uuid_generate_v4(),
name VARCHAR(100) NOT NULL,
email VARCHAR(100),
date_of_birth DATE NOT NULL,
phone VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member (
id UUID DEFAULT uuid_generate_v4(),
date_of_birth DATE NOT NULL,
email VARCHAR(100) UNIQUE,
expires DATE NOT NULL,
gender VARCHAR(6) NOT NULL,
name VARCHAR(100) NOT NULL,
phone VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gift_code (
code BIGINT,
employee_id UUID,
member_id UUID,
expires DATE NOT NULL,
PRIMARY KEY (code),
FOREIGN KEY (member_id) REFERENCES member (id)
);
