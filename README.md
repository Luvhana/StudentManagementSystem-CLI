# 🎓 Student Management System – Java (Console-Based)

A simple and interactive console-based Student Management System built using **Java** and **Object-Oriented Programming (OOP)** principles. This project allows students to register, enroll in courses, attempt MCQ exams, and track their progress. Admins can manage courses and view student performance.

---

## 🚀 Features

### 👩‍🎓 Student Functionality
- Register and log in with credentials
- Enroll in one course at a time
- Attempt exams (5 MCQs per course)
- View results with pass/fail status
- Use **Training Mode** to practice questions with hints
- Track progress in training mode

### 👨‍🏫 Admin Functionality
- Log in using hardcoded credentials (`admin/admin123`)
- Add new courses with 5 MCQs each
- Delete existing courses
- View all registered students
- View all student exam results

---

## 🧠 Concepts Used
- Java OOP (Abstraction, Inheritance, Encapsulation, Polymorphism)
- ArrayList and HashMap for data storage
- Exception handling for user inputs
- Clean, modular code structure

---

## 🗂️ Project Structure
```
📁 StudentManagementSystem
│
├── User.java                   (Abstract class)
├── Admin.java                  (Admin operations)
├── Student.java                (Student functionalities)
├── Course.java                 (Stores course details)
├── Question.java               (Stores MCQ data)
├── ExamResult.java             (Stores exam scores & status)
└── StudentManagementSystem.java (Main class with application entry point)
```


---

## 🧪 Preloaded Sample Courses

- **Data Structures**
- **Operating Systems**

Each contains 5 MCQs and can be modified or deleted by the admin.

---

## 📝 Getting Started

### ✅ Requirements
- Java 8 or higher
- Any IDE or terminal with `javac` and `java`

### 🛠️ Compile & Run

```bash
javac StudentManagementSystem.java
java StudentManagementSystem



