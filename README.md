# Library-Managment-System
# 📚 Library Management System

A Java desktop application built with **Java Swing (GUI)** and **MySQL (JDBC)** as an Object-Oriented Programming semester project. The system provides a fully graphical interface for librarians to manage books, members, borrowing transactions, fines, and reports — secured behind a login screen with SHA-256 password hashing.

---

## 👤 Group Members

| Full Name | CMS / Student ID | Section |
|-----------|-----------------|---------|
| Zohaib Ahmad | 023-25-0191 | C |

---

## 🎯 Purpose

The Library Management System replaces manual library tracking with a clean, menu-driven desktop GUI. It allows a librarian to:

- Log in securely (credentials verified against a hashed password in the database)
- Add, search, update, and delete books in the catalog
- Register and manage library members
- Issue and return books (14-day due dates set automatically)
- Track overdue books and calculate fines (10 PKR per overdue day)
- Mark fines as paid
- Generate summary reports (books by genre, fines by status, members by type)

**Target Users:** Librarians and library administrators.

---

## 🗂️ Project Structure

```
LibraryManagementSystem/
│
├── gui/
│   ├── LoginFrame.java     # Login window — authenticates librarian before entry
│   └── main.java           # Main dashboard — sidebar navigation + CardLayout panels
│
├── dao/
│   ├── Book.java           # Book CRUD and search
│   ├── Borrowed.java       # Issue, return, and borrow history
│   ├── Fine.java           # Fine calculation, retrieval, and payment
│   ├── Member.java         # Member CRUD and status management
│   ├── Reports.java        # Summary reports
│   ├── LoginDao.java       # Librarian login with SHA-256 password hashing
│   ├── Output.java         # Shared output router → Swing JTextArea or console
│   └── dao.java            # Utility/test DAO
│
└── db/
    └── ConnectionDb.java   # Singleton MySQL JDBC connection
```

---

## 🖥️ GUI Overview

### Login Screen (`LoginFrame.java`)
- Styled login form with username and password fields
- On submit, calls `LoginDao.login()` — verifies SHA-256 hashed password against the `Librarian` table
- On success, opens the main dashboard; on failure, shows an inline error message
- Checks database connectivity before displaying the form

### Main Dashboard (`main.java`)
- **900 × 620** Swing `JFrame` with a **dark blue sidebar** and **CardLayout** content area
- Sidebar is divided into 5 sections, each with clickable navigation buttons:

| Section | Buttons |
|---------|---------|
| 📖 **Books** | All Books, Search by Name, Search by ISBN, Available Books, Add Book, Update Copies, Delete Book |
| 👤 **Members** | All Members, Add Member, Update Status |
| 📦 **Borrowed** | Issue Book, Return Book, All Borrowed, Overdue Books, Borrow History |
| 💰 **Fines** | Unpaid Fines, Calculate Fine, Mark Fine Paid |
| 📊 **Reports** | Books by Genre, Total Fines, Members by Type |

- Each panel contains input fields and a scrollable **JTextArea** output area
- Results from every DAO call are displayed live in that output area via `dao.Output`

---

## 🗂️ DAO Classes & Key Methods

### `Book.java`
| Method | Description |
|--------|-------------|
| `getAllBooks()` | Lists all books (ID + Title) |
| `searchBookByName(String)` | Searches by title using SQL `LIKE` |
| `searchBookByISBN(String)` | Searches by ISBN |
| `AvailableBooks()` | Lists books with available copies > 0 |
| `AddBooks(...)` | Inserts a new book (ISBN, Title, Author, Genre, Year, Copies) |
| `UpdateBookcopies(int, int)` | Updates available copy count by Book ID |
| `DeleteBook(int)` | Deletes a book and cascades to its borrow records |

### `Borrowed.java`
| Method | Description |
|--------|-------------|
| `issueBook(int, int, int)` | Issues a book; auto-sets 14-day due date via `DATE_ADD` |
| `returnBook(int)` | Marks borrow record as `Returned` with today's date |
| `getAllBorrowedBooks()` | Lists all active borrows with member and book info |
| `getOverdueBooks()` | Lists overdue books with member phone number |
| `getMemberBorrowHistory(int)` | Full borrow history for a given member ID |

### `Fine.java`
| Method | Description |
|--------|-------------|
| `getUnpaidFines()` | Lists all unpaid fines with member and book details |
| `calculateFine()` | Calculates 10 PKR/day fine for all overdue books |
| `markFinePaid(int)` | Sets fine status to `Paid` and records today's payment date |

### `Member.java`
| Method | Description |
|--------|-------------|
| `GetAllMembers()` | Lists all members (ID, Name, Email, Phone, Type, Status) |
| `AddMember(...)` | Registers a new member |
| `UpdateMemberStatus(String, int)` | Updates status to `ACTIVE`, `INACTIVE`, or `BANNED` |

### `Reports.java`
| Method | Description |
|--------|-------------|
| `getTotalBooksByGenre()` | Book count grouped by genre |
| `getTotalFines()` | Fine totals grouped by Paid/Unpaid |
| `getMemberCountByType()` | Member count grouped by member type |

### `LoginDao.java`
- Accepts username + password from `LoginFrame`
- Hashes the password using **SHA-256** (`java.security.MessageDigest`)
- Queries the `Librarian` table and returns the librarian's full name on success, or `null` on failure

### `Output.java`
- Static utility used by every DAO class instead of `System.out.println`
- Routes output to the active panel's `JTextArea` via `SwingUtilities.invokeLater()` for thread safety
- Falls back to console if no GUI area is registered

### `ConnectionDb.java` — Singleton Pattern
- Implements the **Singleton design pattern** — only one `Connection` instance is created for the entire application
- Connects to `jdbc:mysql://localhost:3306/lms_db`

---

## ⚙️ Key OOP & Java Features Demonstrated

| Feature | Where Applied |
|---------|--------------|
| **Encapsulation** | Private fields in all DAO and DB classes; public methods only |
| **Singleton Pattern** | `ConnectionDb` — one shared DB connection across the whole app |
| **Abstraction** | DAO classes hide all SQL complexity from the GUI layer |
| **Separation of Concerns** | `gui` → view, `dao` → business logic, `db` → connection |
| **Functional Interfaces** | `Consumer1` and `Consumer2` defined in `main.java` for button actions (Java 8+) |
| **Exception Handling** | Every JDBC call wrapped in `try-catch`; errors displayed in GUI |
| **Security** | SHA-256 password hashing in `LoginDao` using `java.security.MessageDigest` |
| **Thread Safety** | `Output.println()` uses `SwingUtilities.invokeLater()` for safe Swing updates |
| **SQL Injection Prevention** | `PreparedStatement` used in all parameterized queries |
| **CardLayout Navigation** | GUI panels switched without opening new windows |

---

## 🗄️ Database: `lms_db`

| Table | Purpose |
|-------|---------|
| `Book` | ISBN, Title, AuthorName, Genre, PublishYear, AvailCopies, TotalCopies |
| `Member` | MemberID, FullName, Email, Phone, MemberType, JoinDate, Status |
| `Borrowed` | BorrowID, BookID, MemberID, LibrarianID, IssueDate, DueDate, ReturnDate, Status |
| `Fine` | FineID, BorrowID, Amount, PaidStatus, PaidDate |
| `Librarian` | LibrarianID, FullName, Username, PasswordHash |

---

## 🚀 How to Run

### Prerequisites
- **JDK 17** or later
- **MySQL 8.x** running locally
- **MySQL JDBC Driver** — `mysql-connector-j-x.x.x.jar`

### 1. Set up the database
```sql
CREATE DATABASE lms_db;
USE lms_db;
-- Run your SQL table-creation and seed script here
```

### 2. Configure the connection
In `db/ConnectionDb.java`, update if needed:
```java
private String url      = "jdbc:mysql://localhost:3306/lms_db";
private String user     = "root";
private String password = "your_password";
```

### 3. Compile
```bash
javac -cp .;mysql-connector-j-x.x.x.jar db/*.java dao/*.java gui/*.java
```
*(Linux/macOS: replace `;` with `:`)*

### 4. Run
```bash
java -cp .;mysql-connector-j-x.x.x.jar gui.main
```

### Running in an IDE (Recommended)
1. Open the project in **IntelliJ IDEA** or **Eclipse**
2. Add `mysql-connector-j-x.x.x.jar` to **Project Libraries**
3. Set `gui.main` as the **Run Configuration** main class
4. Click **Run**

---

## 🎬 Demo Video

▶️ **[Watch the demo on YouTube](https://youtu.be/UyySuBs1ypw)**

*(Replace with your actual YouTube URL before submission.)*

---

## 🔗 GitHub Repository

📂 **[https://github.com/YOUR_USERNAME/LibraryManagementSystem](https://github.com/zohaibahmadbscssaif25-hue/Library_Management_System/edit/main/RE)**


---



---

*Spring 2026 — OOP Course Project — Section C*
