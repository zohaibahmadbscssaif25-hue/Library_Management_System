-- ================================================
-- Library Management System (LMS)
-- Author: Zohaib Ahmad
-- Schema: lms_db
-- ================================================

CREATE DATABASE lms_db;
-- ================================================
-- TABLE 1: Book
-- ================================================
USE lms_db;

CREATE TABLE Book (
    BookID      INT PRIMARY KEY AUTO_INCREMENT,
    ISBN        VARCHAR(13) UNIQUE NOT NULL,
    Title       VARCHAR(200) NOT NULL,
    AuthorName  VARCHAR(100) NOT NULL,
    Genre       VARCHAR(50),
    PublishYear YEAR,
    TotalCopies INT DEFAULT 1,
    AvailCopies INT CHECK (AvailCopies >= 0)
);
-- ================================================
-- TABLE 2: Member
-- ================================================

CREATE TABLE Member (
    MemberID   INT PRIMARY KEY AUTO_INCREMENT,
    FullName   VARCHAR(100) NOT NULL,
    Email      VARCHAR(100) UNIQUE NOT NULL,
    Phone      VARCHAR(15),
    MemberType ENUM('Student', 'Faculty', 'Public') NOT NULL,
    JoinDate   DATE DEFAULT (CURRENT_DATE),
    Status     ENUM('Active', 'Suspended') DEFAULT 'Active'
);
-- ================================================
-- TABLE 3: Librarian
-- ================================================

CREATE TABLE Librarian (
    LibrarianID  INT PRIMARY KEY AUTO_INCREMENT,
    FullName     VARCHAR(100) NOT NULL,
    Username     VARCHAR(50) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Role         ENUM('Admin', 'Staff') DEFAULT 'Staff'
);
-- ================================================
-- TABLE 4: Borrowed
-- ================================================

CREATE TABLE Borrowed (
    BorrowID    INT PRIMARY KEY AUTO_INCREMENT,
    BookID      INT NOT NULL,
    MemberID    INT NOT NULL,
    LibrarianID INT NOT NULL,
    IssueDate   DATE NOT NULL,
    DueDate     DATE NOT NULL,
    ReturnDate  DATE,
    Status      ENUM('Borrowed', 'Returned', 'Overdue') DEFAULT 'Borrowed',

    FOREIGN KEY (BookID)      REFERENCES Book(BookID),
    FOREIGN KEY (MemberID)    REFERENCES Member(MemberID),
    FOREIGN KEY (LibrarianID) REFERENCES Librarian(LibrarianID)
);
-- ================================================
-- TABLE 5: Fine
-- ================================================

CREATE TABLE Fine (
    FineID     INT PRIMARY KEY AUTO_INCREMENT,
    BorrowID   INT NOT NULL,
    Amount     DECIMAL(6,2) CHECK (Amount > 0),
    PaidStatus ENUM('Unpaid', 'Paid') DEFAULT 'Unpaid',
    PaidDate   DATE,

    FOREIGN KEY (BorrowID) REFERENCES Borrowed(BorrowID)
);
-- ================================================
-- INSERT DATA: Book (8 rows)
-- ================================================

INSERT INTO Book (ISBN, Title, AuthorName, Genre, PublishYear, TotalCopies, AvailCopies) VALUES
('9780061960', 'To Kill a Mockingbird',  'Harper Lee',          'Fiction',   1960, 3, 2),
('9780743273', 'The Great Gatsby',       'F. Scott Fitzgerald', 'Fiction',   1925, 2, 2),
('9780140449', 'The Odyssey',            'Homer',               'Classic',   1902, 2, 1),
('9780590353', 'Harry Potter Part 1',    'J.K. Rowling',        'Fantasy',   1997, 4, 2),
('9780316769', 'The Catcher in the Rye', 'J.D. Salinger',       'Fiction',   1951, 2, 2),
('9780679720', 'Crime and Punishment',   'Fyodor Dostoevsky',   'Classic',   1902, 1, 1),
('9780195619', 'Database Systems',       'Ramez Elmasri',       'Education', 2015, 3, 2),
('9780132350', 'Clean Code',             'Robert C. Martin',    'Education', 2008, 2, 1);
-- ================================================
-- INSERT DATA: Member (5 rows)
-- ================================================

INSERT INTO Member (FullName, Email, Phone, MemberType, JoinDate, Status) VALUES
('Ali Hassan',     'ali.hassan@gmail.com',    '03001234567', 'Student', '2024-01-10', 'Active'),
('Sara Khan',      'sara.khan@gmail.com',     '03011234567', 'Student', '2024-02-15', 'Active'),
('Dr. Imran Raza', 'imran.raza@uni.edu.pk',   '03021234567', 'Faculty', '2023-09-01', 'Active'),
('Usman Tariq',    'usman.tariq@gmail.com',   '03031234567', 'Public',  '2024-03-20', 'Active'),
('Ayesha Noor',    'ayesha.noor@gmail.com',   '03041234567', 'Student', '2024-04-05', 'Suspended');
-- ================================================
-- INSERT DATA: Librarian (2 rows)
-- ================================================

INSERT INTO Librarian (FullName, Username, PasswordHash, Role) VALUES
('Zohaib Ahmad',  'zohaib_admin', 'hashed_password_123', 'Admin'),
('Fatima Malik',  'fatima_staff', 'hashed_password_456', 'Staff');
-- ================================================
-- INSERT DATA: Borrowed (6 rows)
-- ================================================

INSERT INTO Borrowed (BookID, MemberID, LibrarianID, IssueDate, DueDate, ReturnDate, Status) VALUES
(1, 1, 1, '2024-04-01', '2024-04-15', '2024-04-13', 'Returned'),
(4, 2, 2, '2024-04-05', '2024-04-19', '2024-04-19', 'Returned'),
(7, 3, 1, '2024-04-10', '2024-04-24', NULL,          'Borrowed'),
(8, 4, 2, '2024-03-01', '2024-03-15', NULL,          'Overdue'),
(2, 1, 1, '2024-03-10', '2024-03-24', NULL,          'Overdue'),
(3, 2, 2, '2024-04-20', '2024-05-04', NULL,          'Borrowed');


-- ================================================
-- INSERT DATA: Fine (3 rows)
-- only for BorrowID 4 and 5 which are Overdue
-- ================================================

INSERT INTO Fine (BorrowID, Amount, PaidStatus, PaidDate) VALUES
(22, 150.00, 'Unpaid', NULL),
(23, 200.00, 'Unpaid', NULL),
(20, 50.00,  'Paid',   '2024-04-20');
SELECT BorrowID, BookID, Status FROM Borrowed;
-- ================================================
-- Library Management System (LMS)
-- Author: Zohaib Ahmad
-- Schema: lms_db
-- Total Queries: 20
-- ================================================

USE lms_db;

-- ================================================
-- BOOK MANAGEMENT (7 Queries)
-- ================================================

-- Q1: View all books
SELECT * FROM Book;

-- Q2: Search book by Title
SELECT * FROM Book
WHERE Title LIKE '%Harry Potter%';

-- Q3: Search book by ISBN
SELECT * FROM Book
WHERE ISBN = '9780590353';

-- Q4: Check available books only
SELECT BookID, Title, AuthorName, AvailCopies
FROM Book
WHERE AvailCopies > 0;

-- Q5: Add a new book
INSERT INTO Book (ISBN, Title, AuthorName, Genre, PublishYear, TotalCopies, AvailCopies)
VALUES ('9781234567890', 'New Book Title', 'Author Name', 'Fiction', 2020, 3, 3);

-- Q6: Update book copies
UPDATE Book
SET TotalCopies = 5, AvailCopies = 5
WHERE BookID = 17;

-- Q7: Delete a book
DELETE FROM Book
WHERE BookID = 17;

-- ================================================
-- MEMBER MANAGEMENT (4 Queries)
-- ================================================

-- Q8: View all members
SELECT * FROM Member;

-- Q9: View only active members
SELECT * FROM Member
WHERE Status = 'Active';

-- Q10: Add a new member
INSERT INTO Member (FullName, Email, Phone, MemberType, JoinDate, Status)
VALUES ('Ahmed Ali', 'ahmed@gmail.com', '03009876543', 'Student', CURDATE(), 'Active');

-- Q11: Suspend a member
UPDATE Member
SET Status = 'Suspended'
WHERE MemberID = 1;

-- ================================================
-- ISSUE AND RETURN BOOKS (5 Queries)
-- ================================================

-- Q12: Issue a book to a member
INSERT INTO Borrowed (BookID, MemberID, LibrarianID, IssueDate, DueDate, Status)
VALUES (17, 1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'Borrowed');

-- Q13: Return a book
UPDATE Borrowed
SET ReturnDate = CURDATE(), Status = 'Returned'
WHERE BorrowID = 19;

-- Q14: View all currently borrowed books
SELECT b.BorrowID, bk.Title, m.FullName, b.IssueDate, b.DueDate, b.Status
FROM Borrowed b
JOIN Book bk ON b.BookID = bk.BookID
JOIN Member m ON b.MemberID = m.MemberID
WHERE b.Status = 'Borrowed';

-- Q15: View all overdue books
SELECT b.BorrowID, bk.Title, m.FullName, m.Phone, b.DueDate
FROM Borrowed b
JOIN Book bk ON b.BookID = bk.BookID
JOIN Member m ON b.MemberID = m.MemberID
WHERE b.Status = 'Overdue';

-- Q16: Full borrow history of a member
SELECT bk.Title, b.IssueDate, b.DueDate, b.ReturnDate, b.Status
FROM Borrowed b
JOIN Book bk ON b.BookID = bk.BookID
WHERE b.MemberID = 1;

-- ================================================
-- FINE CALCULATION (3 Queries)
-- ================================================

-- Q17: View all unpaid fines with member details
SELECT m.FullName, bk.Title, f.Amount, f.PaidStatus
FROM Fine f
JOIN Borrowed b ON f.BorrowID = b.BorrowID
JOIN Member m   ON b.MemberID = m.MemberID
JOIN Book bk    ON b.BookID   = bk.BookID
WHERE f.PaidStatus = 'Unpaid';

-- Q18: Calculate fine using overdue days (10 PKR per day)
SELECT m.FullName, bk.Title, b.DueDate,
       DATEDIFF(CURDATE(), b.DueDate) AS OverdueDays,
       DATEDIFF(CURDATE(), b.DueDate) * 10 AS FineAmount
FROM Borrowed b
JOIN Member m ON b.MemberID = m.MemberID
JOIN Book bk  ON b.BookID   = bk.BookID
WHERE b.Status = 'Overdue';

-- Q19: Mark fine as paid
UPDATE Fine
SET PaidStatus = 'Paid', PaidDate = CURDATE()
WHERE FineID = 1;

-- ================================================
-- REPORTS AND STATISTICS (4 Queries)
-- ================================================

-- Q20: Total books per genre
SELECT Genre, COUNT(*) AS TotalBooks
FROM Book
GROUP BY Genre;



-- Q22: Total fines collected
SELECT PaidStatus, SUM(Amount) AS TotalAmount
FROM Fine
GROUP BY PaidStatus;

-- Q23: Count members by type
SELECT MemberType, COUNT(*) AS Total
FROM Member
GROUP BY MemberType;

select  count(*) from Member group by MemberType ;