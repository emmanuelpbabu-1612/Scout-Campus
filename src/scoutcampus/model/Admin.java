package scoutcampus.model;

/**
 * INHERITANCE: Admin extends the abstract User class.
 */
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    /**
     * POLYMORPHISM: Admin's own version of displayDashboard(),
     * distinct from Student's implementation.
     */
    @Override
    public void displayDashboard() {
        System.out.println("Admin Dashboard for " + getName()
                + " | Manage opportunities & view reports");
    }
}
