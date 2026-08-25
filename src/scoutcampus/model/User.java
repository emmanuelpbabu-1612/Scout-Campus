package scoutcampus.model;

/**
 * Abstract base class for every person who logs into Scout Campus.
 * Demonstrates ENCAPSULATION (private fields + public getters/setters)
 * and ABSTRACTION (the displayDashboard() contract every subclass must fulfil).
 */
public abstract class User {

    private int id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * ABSTRACTION + the hook for POLYMORPHISM:
     * Student and Admin each implement this differently.
     */
    public abstract void displayDashboard();
}
