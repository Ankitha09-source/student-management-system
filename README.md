# 🎓 EduManage — Student Management System

EduManage is a web-based Student Management System developed using Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. It helps organize student information, course records, and academic results through separate administrator and student interfaces.

## ✨ Features

- **Admin Access:** Administrator login and account registration.
- **Student Registration:** Students can create accounts using their registered email addresses.
- **Student Login:** Secure password-based authentication.
- **Student Management:** Maintain student profile information.
- **Course Management:** Organize course records.
- **Academic Results:** Manage and view student marks.
- **Responsive UI:** Clean interface designed for desktop and mobile screens.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot | Backend application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| MySQL | Relational database |
| HTML5 | Page structure |
| CSS3 | Styling and responsive layouts |
| JavaScript | Frontend interactions |
| Maven | Build and dependency management |

## 📁 Project Structure

```text
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/example/student_management/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/
│   │       └── application.properties
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## ⚙️ Setup and Installation

### Prerequisites

- Java 17
- MySQL 8 or a compatible version
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Ankitha09-source/student-management-system.git
cd student-management-system
```

### 2. Create the database

Create a MySQL database:

```sql
CREATE DATABASE student_management;
```

### 3. Configure database credentials

Configure the following environment variables before starting the application:

```text
DB_URL=jdbc:mysql://localhost:3306/student_management
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

Keep your actual database password private. Do not commit credentials to GitHub.

### 4. Run the application

On Windows PowerShell, set the environment variables in your terminal, then run:

```powershell
.\mvnw.cmd spring-boot:run
```

The application uses port **8081** by default.

### 5. Open the application

Visit:

http://localhost:8081/index.html

## 🔐 Security Notes

- Passwords are handled using password hashing.
- Access control is configured using Spring Security.
- Database credentials should be supplied through environment variables.
- Review authorization rules and restrict access to sensitive student records before production deployment.

## 👩‍💻 Author

**Ankitha H.**

GitHub: [Ankitha09-source](https://github.com/Ankitha09-source)

---

*Built as a Java and Spring Boot learning project.*
