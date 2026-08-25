# Scout Campus
**A Java-Based Student Startup and Project Collaboration Platform Using OOP, Swing GUI, and JDBC**

A campus platform where students create profiles, post project/startup ideas,
find teammates, apply to projects, and discover hackathons/events — built to
demonstrate every core OOP concept with a real Swing + MySQL stack.

## Project Structure

```
ScoutCampus/
├── sql/
│   └── schema.sql                 # Run this first to create the database
├── lib/                           # Put mysql-connector-j-x.x.x.jar here
├── src/scoutcampus/
│   ├── Main.java                  # Entry point
│   ├── model/                     # User (abstract), Student, Admin, Project, Application, Opportunity
│   ├── interfaces/
│   │   └── Applicant.java
│   ├── dao/                       # JDBC data access: DBConnection, StudentDAO, ProjectDAO,
│   │                                ApplicationDAO, OpportunityDAO
│   └── gui/                       # Swing screens: LoginFrame, RegisterFrame, StudentDashboard,
│                                     AdminDashboard, CreateProjectFrame, BrowseProjectsFrame,
│                                     ApplicationsFrame, OpportunityBoardFrame, ProfileFrame
└── README.md
```

## Setup

### 1. Database
1. Install MySQL and start the server.
2. Run the schema:
   ```
   mysql -u root -p < sql/schema.sql
   ```
   This creates the `scoutcampus` database, all tables, and a default admin:
   `admin@scoutcampus.com` / `admin123`.
3. Open `src/scoutcampus/dao/DBConnection.java` and update `USER` / `PASSWORD`
   to match your local MySQL credentials.

### 2. MySQL Connector/J
Download **MySQL Connector/J** (the JDBC driver) and place the `.jar` in `lib/`.
Get it from: https://dev.mysql.com/downloads/connector/j/ (choose "Platform Independent").

### 3. Compile & Run

From the `ScoutCampus/` folder:

```bash
# Compile
javac -cp "lib/mysql-connector-j-*.jar" -d build $(find src -name "*.java")

# Run
java -cp "build:lib/mysql-connector-j-*.jar" scoutcampus.Main
```

On Windows, replace `:` with `;` in the `-cp` classpath, and `$(find src -name "*.java")`
with a listing of your `.java` files (or open the project in an IDE like IntelliJ/Eclipse,
add the jar to Build Path, and run `Main.java` directly — this is the easiest route for
a classroom demo).

## How It Maps to the Requirements

| Requirement | Where |
|---|---|
| Encapsulation | Private fields + getters/setters in every `model/` class |
| Inheritance | `Student` and `Admin` both extend abstract `User` |
| Abstraction | `User.displayDashboard()` is abstract |
| Polymorphism | `user.displayDashboard()` behaves differently for `Student` vs `Admin`; `Applicant.apply()` called through the interface reference in `BrowseProjectsFrame` |
| Interface | `Applicant`, implemented by `Student` |
| Exception Handling | Every DAO call in the GUI layer is wrapped in `try/catch (SQLException)` with a `JOptionPane` error dialog |
| Skill-based search | `ProjectDAO.searchBySkill()` + search bar in Browse Projects |
| Project popularity ranking | `ProjectDAO.getPopularityRanking()` + "Sort by Popularity" button |
| Top Skills on Campus report | `StudentDAO.getTopSkills()` on the Admin Reports tab |
| Most active student leaderboard | `StudentDAO.getMostActiveStudents()` on the Admin Reports tab |

## Notes for the Demo
- Register a few students with different skills, have them post projects, then
  apply to each other's projects and accept/reject from the Applications screen —
  this is what makes the "Top Skills" and "Popularity" reports look populated.
- The admin login checkbox on the login screen switches between the `students`
  and `admins` tables.
