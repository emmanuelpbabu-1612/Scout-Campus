-- ============================================================
-- Scout Campus: Student Startup & Project Collaboration Platform
-- Database Schema (MySQL)
-- ============================================================

DROP DATABASE IF EXISTS scoutcampus;
CREATE DATABASE scoutcampus;
USE scoutcampus;

-- ---------------------------------------------------------
-- Students
-- ---------------------------------------------------------
CREATE TABLE students (
    student_id        INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100)  NOT NULL,
    email              VARCHAR(100)  NOT NULL UNIQUE,
    department         VARCHAR(100),
    year               VARCHAR(20),
    password           VARCHAR(100)  NOT NULL,
    bio                TEXT,
    skills             TEXT,
    interests          TEXT,
    github             VARCHAR(150),
    linkedin           VARCHAR(150),
    startup_interests  TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- Admins
-- ---------------------------------------------------------
CREATE TABLE admins (
    admin_id    INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100),
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------
-- Projects
-- ---------------------------------------------------------
CREATE TABLE projects (
    project_id        INT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(150) NOT NULL,
    description        TEXT,
    required_skills    VARCHAR(255),
    team_size          INT DEFAULT 1,
    owner_id           INT NOT NULL,
    status             VARCHAR(30) DEFAULT 'Open',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- Applications  (Student applies to Project)
-- ---------------------------------------------------------
CREATE TABLE applications (
    application_id    INT AUTO_INCREMENT PRIMARY KEY,
    project_id         INT NOT NULL,
    student_id         INT NOT NULL,
    status             VARCHAR(30) DEFAULT 'Pending',   -- Pending / Accepted / Rejected
    applied_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (project_id, student_id)
);

-- ---------------------------------------------------------
-- Opportunities (posted by Admin)
-- ---------------------------------------------------------
CREATE TABLE opportunities (
    opportunity_id    INT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(150) NOT NULL,
    type               VARCHAR(50),   -- Hackathon / Competition / Workshop / Startup Event
    description        TEXT,
    deadline           DATE,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- Saved Opportunities (Student <-> Opportunity)
-- ---------------------------------------------------------
CREATE TABLE saved_opportunities (
    student_id        INT NOT NULL,
    opportunity_id     INT NOT NULL,
    PRIMARY KEY (student_id, opportunity_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (opportunity_id) REFERENCES opportunities(opportunity_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- Seed data
-- ---------------------------------------------------------
INSERT INTO admins (name, email, password) VALUES ('Super Admin', 'admin@scoutcampus.com', 'admin123');

-- ---------------------------------------------------------
-- Useful reporting queries (used by Admin > Reports screen)
-- ---------------------------------------------------------

-- Top skills on campus (skills column is a comma-separated list)
-- Run from Java after splitting each student's skills string, OR approximate with:
-- SELECT skills, COUNT(*) FROM students GROUP BY skills ORDER BY COUNT(*) DESC;

-- Most active students (by projects posted + applications sent)
-- SELECT s.name,
--        (SELECT COUNT(*) FROM projects p WHERE p.owner_id = s.student_id) AS projects_posted,
--        (SELECT COUNT(*) FROM applications a WHERE a.student_id = s.student_id) AS applications_sent
-- FROM students s
-- ORDER BY (projects_posted + applications_sent) DESC;

-- Project popularity ranking (by number of applications received)
-- SELECT p.title, COUNT(a.application_id) AS applicants
-- FROM projects p LEFT JOIN applications a ON p.project_id = a.project_id
-- GROUP BY p.project_id ORDER BY applicants DESC;
