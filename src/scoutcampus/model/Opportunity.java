package scoutcampus.model;

import java.sql.Date;

public class Opportunity {

    private int opportunityId;
    private String title;
    private String type;   // Hackathon / Competition / Workshop / Startup Event
    private String description;
    private Date deadline;

    public Opportunity() {
    }

    public Opportunity(int opportunityId, String title, String type,
                        String description, Date deadline) {
        this.opportunityId = opportunityId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.deadline = deadline;
    }

    public int getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(int opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return title;
    }
}
