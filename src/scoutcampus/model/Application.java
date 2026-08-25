package scoutcampus.model;

public class Application {

    private int applicationId;
    private int projectId;
    private int studentId;
    private String status;       // Pending / Accepted / Rejected
    private String projectTitle; // convenience field for display
    private String studentName;  // convenience field for display

    public Application() {
    }

    public Application(int applicationId, int projectId, int studentId, String status) {
        this.applicationId = applicationId;
        this.projectId = projectId;
        this.studentId = studentId;
        this.status = status;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
