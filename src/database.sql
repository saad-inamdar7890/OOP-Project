create schema school;
use school;
CREATE TABLE student (
    active BOOLEAN NOT NULL,
    student_id VARCHAR(10) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    student_username VARCHAR(50) NOT NULL UNIQUE,
    student_password VARCHAR(50) NOT NULL
);
INSERT INTO student (active, student_id, student_name, dob, student_username, student_password) VALUES
(TRUE, 'S001', 'Saad Inamdar', '2000-01-15', 'saadInam', 'donkey'),
(TRUE, 'S002', 'Bob Smith', '1999-05-22', 'bobsmith', 'password123'),
(TRUE, 'S003', 'Charlie Brown', '2001-07-30', 'charlieb', 'password123'),
(TRUE, 'S004', 'Diana Prince', '2000-03-12', 'dianap', 'password123'),
(TRUE, 'S005', 'Ethan Hunt', '1998-11-23', 'ethanh', 'password123'),
(FALSE, 'S006', 'Fiona Gallagher', '2002-08-10', 'fionag', 'password123'),
(TRUE, 'S007', 'George Harrison', '1999-02-05', 'georgeh', 'password123'),
(TRUE, 'S008', 'Hannah Montana', '2001-12-19', 'hannahm', 'password123'),
(FALSE, 'S009', 'Ian Somerhalder', '2000-06-25', 'ians', 'password123'),
(TRUE, 'S010', 'Jane Doe', '2000-04-17', 'janed', 'password123');

CREATE TABLE professors (
    active BOOLEAN NOT NULL,
    professor_id VARCHAR(10) PRIMARY KEY,
    professor_name VARCHAR(100) NOT NULL,
    professor_username VARCHAR(50) NOT NULL UNIQUE,
    professor_password VARCHAR(50) NOT NULL
);

INSERT INTO professors (active, professor_id, professor_name, professor_username, professor_password) VALUES
(TRUE, 'P001', 'John Doe', 'johndoe', 'password123'),
(TRUE, 'P002', 'Jane Smith', 'janesmith', 'password123'),
(TRUE, 'P003', 'Richard Roe', 'richardroe', 'password123'),
(TRUE, 'P004', 'Emily Davis', 'emilydavis', 'password123'),
(TRUE, 'P005', 'Michael Johnson', 'michaeljohnson', 'password123'),
(TRUE, 'P006', 'Sarah Miller', 'sarahmiller', 'password123'),
(TRUE, 'P007', 'David Wilson', 'davidwilson', 'password123'),
(TRUE, 'P008', 'Laura Moore', 'lauramoore', 'password123'),
(TRUE, 'P009', 'James Taylor', 'jamestaylor', 'password123'),
(TRUE, 'P010', 'Emma Brown', 'emmabrown', 'password123');
INSERT INTO professors (active, professor_id, professor_name, professor_username, professor_password) 
VALUES (TRUE, 'P011', 'Ankit Jaiswal', 'ankitJ', 'ankit');

CREATE TABLE course (
    course_id VARCHAR(10) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    professor_id VARCHAR(10),
    FOREIGN KEY (professor_id) REFERENCES professors(professor_id)
);

INSERT INTO course (course_id, course_name, professor_id) VALUES
('C001', 'Introduction to Programming', 'P011'),
('C002', 'Advanced Programming', 'P011'),
('C003', 'Data Structures', 'P011'),
('C004', 'Algorithms', 'P002'),
('C005', 'Database Systems', 'P003'),
('C006', 'Data Warehousing', 'P003'),
('C007', 'Operating Systems', 'P004'),
('C008', 'Distributed Systems', 'P004'),
('C009', 'Computer Networks', 'P005'),
('C010', 'Network Security', 'P005'),
('C011', 'Software Engineering', 'P006'),
('C012', 'Software Testing', 'P006'),
('C013', 'Artificial Intelligence', 'P007'),
('C014', 'Machine Learning', 'P007'),
('C015', 'Human-Computer Interaction', 'P008'),
('C016', 'Usability Engineering', 'P008'),
('C017', 'Cyber Security', 'P009'),
('C018', 'Digital Forensics', 'P009'),
('C019', 'Web Development', 'P010'),
('C020', 'Mobile App Development', 'P010');


CREATE TABLE result (
    student_id VARCHAR(10),
    course_id VARCHAR(10),
    marks INT,
    grade CHAR(2),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id)
);
INSERT INTO result (student_id, course_id, marks, grade) VALUES
('S001', 'C001', 85, 'A'),
('S001', 'C002', 78, 'B'),
('S001', 'C003', 92, 'A'),
('S002', 'C001', 88, 'A'),
('S002', 'C002', 74, 'C'),
('S002', 'C003', 81, 'B'),
('S003', 'C001', 95, 'A'),
('S003', 'C002', 87, 'A'),
('S003', 'C003', 90, 'A'),
('S004', 'C001', 77, 'C'),
('S004', 'C002', 82, 'B'),
('S004', 'C003', 85, 'A'),
('S005', 'C001', 66, 'D'),
('S005', 'C002', 79, 'C'),
('S005', 'C003', 73, 'C'),
('S006', 'C001', 80, 'B'),
('S006', 'C002', 88, 'A'),
('S006', 'C003', 84, 'B'),
('S007', 'C001', 91, 'A'),
('S007', 'C002', 76, 'C'),
('S007', 'C003', 89, 'A'),
('S008', 'C001', 70, 'C'),
('S008', 'C002', 83, 'B'),
('S008', 'C003', 78, 'B'),
('S009', 'C001', 82, 'B'),
('S009', 'C002', 85, 'A'),
('S009', 'C003', 80, 'B'),
('S010', 'C001', 79, 'C'),
('S010', 'C002', 90, 'A'),
('S010', 'C003', 87, 'A');
use school;
UPDATE professors
SET professor_username ='ankit'
WHERE professor_id = 'P011';

select * from student;
select * from professors;
select * from result;
select * from course;


