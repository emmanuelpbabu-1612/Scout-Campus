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
├── lib/                           # FlatLaf and MySQL Connector/J dependencies
│   └── flatlaf-3.6.2.jar
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
   **Warning:** `sql/schema.sql` starts with `DROP DATABASE IF EXISTS scoutcampus;`.
   It deletes and recreates that database, including all of its data. Only run
   it on a new/empty database or after making a backup.
   This creates the `scoutcampus` database, all tables, and a default admin:
   `admin@scoutcampus.com` / `admin123`.
3. Set the `DB_USER` and `DB_PASSWORD` environment variables to credentials for
   a MySQL account allowed to connect from `localhost`. The app connects to
   `jdbc:mysql://localhost:3306/scoutcampus` with its configured connection options.

In PowerShell, set credentials in the same terminal before launching the app:

```powershell
$env:DB_USER = "scoutcampus_app"
$env:DB_PASSWORD = Read-Host "Database password"
java -cp "build;lib/*" scoutcampus.Main
```

### 2. Dependencies
FlatLaf 3.6.2 is included in `lib/` and provides the dark Swing look and feel.
VS Code loads JARs from this folder through `.vscode/settings.json`.

MySQL Connector/J is required at runtime for database login and data access, but
is not included. Download the platform-independent JAR from
https://dev.mysql.com/downloads/connector/j/ and place it in `lib/`. The project
compiles without this driver, but database operations will report a missing
driver until it is added. The application checks for the driver at startup and
prints a clear warning to the console if it is missing; the login window still
opens, but database actions will fail until the JAR is added to the runtime
classpath.

### 3. Compile & Run

From the project folder in PowerShell:

```bash
# Compile (FlatLaf is required; Connector/J is optional for compilation)
javac -cp "lib/*" -d build (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)

# Run
java -cp "build;lib/*" scoutcampus.Main
```

In VS Code, open this project folder and run the **Scout Campus** launch
configuration. For a Windows distribution, include the compiled `build/` folder
and the complete `lib/` folder beside it, then launch with
`java -cp "build;lib/*" scoutcampus.Main`. Keep the Connector/J JAR in `lib/`
in the distribution so database login works.

### Verify the database

The local Windows `MySQL80` service is running, but the `mysql` command-line
client is not on PATH, so the database and its tables could not be confirmed
here. In MySQL Workbench, connect to the local server and run:

```sql
SHOW DATABASES LIKE 'scoutcampus';
USE scoutcampus;
SHOW TABLES;
```

If the database is missing and there is no data to preserve, open PowerShell in
the project folder and run:

```powershell
mysql -u root -p < .\sql\schema.sql
```

Enter the MySQL root password when prompted. If `mysql` is not recognized, use
the Workbench SQL editor to open and execute `sql/schema.sql`, or run `mysql.exe`
using its full path under the MySQL Server `bin` directory.

### Optional dedicated application account

Instead of using `root`, create a restricted account from Workbench or a root
MySQL session. Replace the sample password before running:

```sql
CREATE USER 'scoutcampus_app'@'localhost' IDENTIFIED BY 'replace-with-a-strong-password';
GRANT SELECT, INSERT, UPDATE ON scoutcampus.students TO 'scoutcampus_app'@'localhost';
GRANT SELECT, INSERT, UPDATE ON scoutcampus.projects TO 'scoutcampus_app'@'localhost';
GRANT SELECT, INSERT, UPDATE ON scoutcampus.applications TO 'scoutcampus_app'@'localhost';
GRANT SELECT, INSERT ON scoutcampus.opportunities TO 'scoutcampus_app'@'localhost';
GRANT SELECT, INSERT ON scoutcampus.saved_opportunities TO 'scoutcampus_app'@'localhost';
```

Set `DB_USER` to `scoutcampus_app` and `DB_PASSWORD` to the chosen password.
Run the schema first using an administrator account; the app account
intentionally does not have schema-management permissions.

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
