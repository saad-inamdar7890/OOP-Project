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
use school;
-- Insert 70 new students
INSERT INTO student (active, student_id, student_name, dob, student_username, student_password) VALUES
(TRUE, 'S011', 'Student 11', '2000-01-01', 'student11', 'password123'),
(TRUE, 'S012', 'Student 12', '2000-01-02', 'student12', 'password123'),
(TRUE, 'S013', 'Student 13', '2000-01-03', 'student13', 'password123'),
(TRUE, 'S014', 'Student 14', '2000-01-04', 'student14', 'password123'),
(TRUE, 'S015', 'Student 15', '2000-01-05', 'student15', 'password123'),
(TRUE, 'S016', 'Student 16', '2000-01-06', 'student16', 'password123'),
(TRUE, 'S017', 'Student 17', '2000-01-07', 'student17', 'password123'),
(TRUE, 'S018', 'Student 18', '2000-01-08', 'student18', 'password123'),
(TRUE, 'S019', 'Student 19', '2000-01-09', 'student19', 'password123'),
(TRUE, 'S020', 'Student 20', '2000-01-10', 'student20', 'password123'),
(TRUE, 'S021', 'Student 21', '2000-01-11', 'student21', 'password123'),
(TRUE, 'S022', 'Student 22', '2000-01-12', 'student22', 'password123'),
(TRUE, 'S023', 'Student 23', '2000-01-13', 'student23', 'password123'),
(TRUE, 'S024', 'Student 24', '2000-01-14', 'student24', 'password123'),
(TRUE, 'S025', 'Student 25', '2000-01-15', 'student25', 'password123'),
(TRUE, 'S026', 'Student 26', '2000-01-16', 'student26', 'password123'),
(TRUE, 'S027', 'Student 27', '2000-01-17', 'student27', 'password123'),
(TRUE, 'S028', 'Student 28', '2000-01-18', 'student28', 'password123'),
(TRUE, 'S029', 'Student 29', '2000-01-19', 'student29', 'password123'),
(TRUE, 'S030', 'Student 30', '2000-01-20', 'student30', 'password123'),
(TRUE, 'S031', 'Student 31', '2000-01-21', 'student31', 'password123'),
(TRUE, 'S032', 'Student 32', '2000-01-22', 'student32', 'password123'),
(TRUE, 'S033', 'Student 33', '2000-01-23', 'student33', 'password123'),
(TRUE, 'S034', 'Student 34', '2000-01-24', 'student34', 'password123'),
(TRUE, 'S035', 'Student 35', '2000-01-25', 'student35', 'password123'),
(TRUE, 'S036', 'Student 36', '2000-01-26', 'student36', 'password123'),
(TRUE, 'S037', 'Student 37', '2000-01-27', 'student37', 'password123'),
(TRUE, 'S038', 'Student 38', '2000-01-28', 'student38', 'password123'),
(TRUE, 'S039', 'Student 39', '2000-01-29', 'student39', 'password123'),
(TRUE, 'S040', 'Student 40', '2000-01-30', 'student40', 'password123'),
(TRUE, 'S041', 'Student 41', '2000-01-31', 'student41', 'password123'),
(TRUE, 'S042', 'Student 42', '2000-02-01', 'student42', 'password123'),
(TRUE, 'S043', 'Student 43', '2000-02-02', 'student43', 'password123'),
(TRUE, 'S044', 'Student 44', '2000-02-03', 'student44', 'password123'),
(TRUE, 'S045', 'Student 45', '2000-02-04', 'student45', 'password123'),
(TRUE, 'S046', 'Student 46', '2000-02-05', 'student46', 'password123'),
(TRUE, 'S047', 'Student 47', '2000-02-06', 'student47', 'password123'),
(TRUE, 'S048', 'Student 48', '2000-02-07', 'student48', 'password123'),
(TRUE, 'S049', 'Student 49', '2000-02-08', 'student49', 'password123'),
(TRUE, 'S050', 'Student 50', '2000-02-09', 'student50', 'password123'),
(TRUE, 'S051', 'Student 51', '2000-02-10', 'student51', 'password123'),
(TRUE, 'S052', 'Student 52', '2000-02-11', 'student52', 'password123'),
(TRUE, 'S053', 'Student 53', '2000-02-12', 'student53', 'password123'),
(TRUE, 'S054', 'Student 54', '2000-02-13', 'student54', 'password123'),
(TRUE, 'S055', 'Student 55', '2000-02-14', 'student55', 'password123'),
(TRUE, 'S056', 'Student 56', '2000-02-15', 'student56', 'password123'),
(TRUE, 'S057', 'Student 57', '2000-02-16', 'student57', 'password123'),
(TRUE, 'S058', 'Student 58', '2000-02-17', 'student58', 'password123'),
(TRUE, 'S059', 'Student 59', '2000-02-18', 'student59', 'password123'),
(TRUE, 'S060', 'Student 60', '2000-02-19', 'student60', 'password123'),
(TRUE, 'S061', 'Student 61', '2000-02-20', 'student61', 'password123'),
(TRUE, 'S062', 'Student 62', '2000-02-21', 'student62', 'password123'),
(TRUE, 'S063', 'Student 63', '2000-02-22', 'student63', 'password123'),
(TRUE, 'S064', 'Student 64', '2000-02-23', 'student64', 'password123'),
(TRUE, 'S065', 'Student 65', '2000-02-24', 'student65', 'password123'),
(TRUE, 'S066', 'Student 66', '2000-02-25', 'student66', 'password123'),
(TRUE, 'S067', 'Student 67', '2000-02-26', 'student67', 'password123'),
(TRUE, 'S068', 'Student 68', '2000-02-27', 'student68', 'password123'),
(TRUE, 'S069', 'Student 69', '2000-02-28', 'student69', 'password123'),
(TRUE, 'S070', 'Student 70', '2000-02-29', 'student70', 'password123'),
(TRUE, 'S071', 'Student 71', '2000-03-01', 'student71', 'password123'),
(TRUE, 'S072', 'Student 72', '2000-03-02', 'student72', 'password123'),
(TRUE, 'S073', 'Student 73', '2000-03-03', 'student73', 'password123'),
(TRUE, 'S074', 'Student 74', '2000-03-04', 'student74', 'password123'),
(TRUE, 'S075', 'Student 75', '2000-03-05', 'student75', 'password123'),
(TRUE, 'S076', 'Student 76', '2000-03-06', 'student76', 'password123'),
(TRUE, 'S077', 'Student 77', '2000-03-07', 'student77', 'password123'),
(TRUE, 'S078', 'Student 78', '2000-03-08', 'student78', 'password123'),
(TRUE, 'S079', 'Student 79', '2000-03-09', 'student79', 'password123'),
(TRUE, 'S080', 'Student 80', '2000-03-10', 'student80', 'password123');

-- Insert results for the new students in courses C001, C002, and C003 with varied marks
INSERT INTO result (student_id, course_id, marks, grade) VALUES
('S011', 'C001', 85, 'A'), ('S011', 'C002', 78, 'B'), ('S011', 'C003', 92, 'A'),
('S012', 'C001', 58, 'D'), ('S012', 'C002', 64, 'C'), ('S012', 'C003', 71, 'C'),
('S013', 'C001', 95, 'A'), ('S013', 'C002', 87, 'A'), ('S013', 'C003', 90, 'A'),
('S014', 'C001', 77, 'C'), ('S014', 'C002', 82, 'B'), ('S014', 'C003', 85, 'A'),
('S015', 'C001', 66, 'D'), ('S015', 'C002', 79, 'C'), ('S015', 'C003', 73, 'C'),
('S016', 'C001', 80, 'B'), ('S016', 'C002', 88, 'A'), ('S016', 'C003', 84, 'B'),
('S017', 'C001', 91, 'A'), ('S017', 'C002', 76, 'C'), ('S017', 'C003', 89, 'A'),
('S018', 'C001', 70, 'C'), ('S018', 'C002', 83, 'B'), ('S018', 'C003', 78, 'B'),
('S019', 'C001', 82, 'B'), ('S019', 'C002', 85, 'A'), ('S019', 'C003', 80, 'B'),
('S020', 'C001', 79, 'C'), ('S020', 'C002', 90, 'A'), ('S020', 'C003', 87, 'A'),
('S021', 'C001', 85, 'A'), ('S021', 'C002', 78, 'B'), ('S021', 'C003', 92, 'A'),
('S022', 'C001', 88, 'A'), ('S022', 'C002', 74, 'C'), ('S022', 'C003', 81, 'B'),
('S023', 'C001', 95, 'A'), ('S023', 'C002', 87, 'A'), ('S023', 'C003', 90, 'A'),
('S024', 'C001', 77, 'C'), ('S024', 'C002', 82, 'B'), ('S024', 'C003', 85, 'A'),
('S025', 'C001', 66, 'D'), ('S025', 'C002', 79, 'C'), ('S025', 'C003', 73, 'C'),
('S026', 'C001', 80, 'B'), ('S026', 'C002', 88, 'A'), ('S026', 'C003', 84, 'B'),
('S027', 'C001', 91, 'A'), ('S027', 'C002', 76, 'C'), ('S027', 'C003', 89, 'A'),
('S028', 'C001', 70, 'C'), ('S028', 'C002', 83, 'B'), ('S028', 'C003', 78, 'B'),
('S029', 'C001', 82, 'B'), ('S029', 'C002', 85, 'A'), ('S029', 'C003', 80, 'B'),
('S030', 'C001', 79, 'C'), ('S030', 'C002', 90, 'A'), ('S030', 'C003', 87, 'A'),
('S031', 'C001', 85, 'A'), ('S031', 'C002', 78, 'B'), ('S031', 'C003', 92, 'A'),
('S032', 'C001', 88, 'A'), ('S032', 'C002', 74, 'C'), ('S032', 'C003', 81, 'B'),
('S033', 'C001', 95, 'A'), ('S033', 'C002', 87, 'A'), ('S033', 'C003', 90, 'A'),
('S034', 'C001', 77, 'C'), ('S034', 'C002', 82, 'B'), ('S034', 'C003', 85, 'A'),
('S035', 'C001', 66, 'D'), ('S035', 'C002', 79, 'C'), ('S035', 'C003', 73, 'C'),
('S036', 'C001', 80, 'B'), ('S036', 'C002', 88, 'A'), ('S036', 'C003', 84, 'B'),
('S037', 'C001', 91, 'A'), ('S037', 'C002', 76, 'C'), ('S037', 'C003', 89, 'A'),
('S038', 'C001', 70, 'C'), ('S038', 'C002', 83, 'B'), ('S038', 'C003', 78, 'B'),
('S039', 'C001', 82, 'B'), ('S039', 'C002', 85, 'A'), ('S039', 'C003', 80, 'B'),
('S040', 'C001', 79, 'C'), ('S040', 'C002', 90, 'A'), ('S040', 'C003', 87, 'A'),
('S041', 'C001', 85, 'A'), ('S041', 'C002', 78, 'B'), ('S041', 'C003', 92, 'A'),
('S042', 'C001', 88, 'A'), ('S042', 'C002', 74, 'C'), ('S042', 'C003', 81, 'B'),
('S043', 'C001', 95, 'A'), ('S043', 'C002', 87, 'A'), ('S043', 'C003', 90, 'A'),
('S044', 'C001', 77, 'C'), ('S044', 'C002', 82, 'B'), ('S044', 'C003', 85, 'A'),
('S045', 'C001', 66, 'D'), ('S045', 'C002', 79, 'C'), ('S045', 'C003', 73, 'C'),
('S046', 'C001', 80, 'B'), ('S046', 'C002', 88, 'A'), ('S046', 'C003', 84, 'B'),
('S047', 'C001', 91, 'A'), ('S047', 'C002', 76, 'C'), ('S047', 'C003', 89, 'A'),
('S048', 'C001', 70, 'C'), ('S048', 'C002', 83, 'B'), ('S048', 'C003', 78, 'B'),
('S049', 'C001', 82, 'B'), ('S049', 'C002', 85, 'A'), ('S049', 'C003', 80, 'B'),
('S050', 'C001', 79, 'C'), ('S050', 'C002', 90, 'A'), ('S050', 'C003', 87, 'A'),
('S051', 'C001', 85, 'A'), ('S051', 'C002', 78, 'B'), ('S051', 'C003', 92, 'A'),
('S052', 'C001', 88, 'A'), ('S052', 'C002', 74, 'C'), ('S052', 'C003', 81, 'B'),
('S053', 'C001', 95, 'A'), ('S053', 'C002', 87, 'A'), ('S053', 'C003', 90, 'A'),
('S054', 'C001', 77, 'C'), ('S054', 'C002', 82, 'B'), ('S054', 'C003', 85, 'A'),
('S055', 'C001', 66, 'D'), ('S055', 'C002', 79, 'C'), ('S055', 'C003', 73, 'C'),
('S056', 'C001', 80, 'B'), ('S056', 'C002', 88, 'A'), ('S056', 'C003', 84, 'B'),
('S057', 'C001', 91, 'A'), ('S057', 'C002', 76, 'C'), ('S057', 'C003', 89, 'A'),
('S058', 'C001', 70, 'C'), ('S058', 'C002', 83, 'B'), ('S058', 'C003', 78, 'B'),
('S059', 'C001', 82, 'B'), ('S059', 'C002', 85, 'A'), ('S059', 'C003', 80, 'B'),
('S060', 'C001', 79, 'C'), ('S060', 'C002', 90, 'A'), ('S060', 'C003', 87, 'A');


select * from student;
select * from professors;
select * from result;
select * from course;


