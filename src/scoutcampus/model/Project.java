package scoutcampus.model;

public class Project {

    private int projectId;
    private String title;
    private String description;
    private String requiredSkills;
    private int teamSize;
    private int ownerId;
    private String ownerName;   // convenience field, populated by joins
    private String status;      // Open / In Progress / Closed
    private int applicantCount; // convenience field for popularity ranking

    public Project() {
    }

    public Project(int projectId, String title, String description,
                    String requiredSkills, int teamSize, int ownerId, String status) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.teamSize = teamSize;
        this.ownerId = ownerId;
        this.status = status;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }

    @Override
    public String toString() {
        return title;
    }
}
