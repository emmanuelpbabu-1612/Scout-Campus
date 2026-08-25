package scoutcampus.model;

import scoutcampus.interfaces.Applicant;

/**
 * INHERITANCE: Student extends the abstract User class.
 * Also implements the Applicant interface, since students are the
 * only users who apply to projects.
 */
public class Student extends User implements Applicant {

    private String department;
    private String year;
    private String bio;
    private String skills;            // comma-separated
    private String interests;
    private String github;
    private String linkedin;
    private String startupInterests;

    public Student() {
        super();
    }

    public Student(int id, String name, String email, String password,
                    String department, String year) {
        super(id, name, email, password);
        this.department = department;
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getStartupInterests() {
        return startupInterests;
    }

    public void setStartupInterests(String startupInterests) {
        this.startupInterests = startupInterests;
    }

    /**
     * POLYMORPHISM: this is the Student-specific version of displayDashboard().
     * Called through a User reference, the JVM picks this implementation at
     * runtime (dynamic dispatch).
     */
    @Override
    public void displayDashboard() {
        System.out.println("Student Dashboard for " + getName()
                + " | Dept: " + department + " | Year: " + year);
    }

    /**
     * Implementation of the Applicant interface contract.
     */
    @Override
    public void apply() {
        System.out.println(getName() + " is applying to a project.");
    }
}
