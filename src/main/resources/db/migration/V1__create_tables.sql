-- Create adverse_action table
CREATE TABLE adverse_action (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT,
    pre_notice_date DATE,
    post_notice_date DATE,
    status ENUM ('CANCELED','DISPUTE','PENDING','SCHEDULED','UNDELIVERABLE')
);

-- Create candidate table
CREATE TABLE candidate (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT,
    created_at DATE NOT NULL,
    dob DATE NOT NULL,
    zipcode INT NOT NULL,
    drivers_license_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    social_security_number VARCHAR(255) NOT NULL
);

-- Create court_search table
CREATE TABLE court_search (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    reported_at DATE NOT NULL,
    name VARCHAR(255) NOT NULL,
    status ENUM ('CLEAR','CONSIDER')
);

-- Create report table
CREATE TABLE report (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    adjudication_created_at DATE,
    adjudication_completed_at DATE,
    turn_around_time INT,
    package_type VARCHAR(255),
    adjudication ENUM ('ENGAGE','PRE_ADVERSE_ACTION'),
    adjudication_status ENUM ('CLEAR','CONSIDER')
);

-- Create user table
CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);