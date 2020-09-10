package com.theakatsuki.hiredevelopers.Model;

public class Hire {
    String hireId;
    String userId;
    String hiringUserId;
    String budget;
    String description;
    String rejection;
    Boolean invitation;

    public Hire(String hireId, String userId, String hiringUserId, String budget, String description, String rejection, Boolean invitation) {
        this.hireId = hireId;
        this.userId = userId;
        this.hiringUserId = hiringUserId;
        this.budget = budget;
        this.description = description;
        this.rejection = rejection;
        this.invitation = invitation;
    }

    public String getHireId() {
        return hireId;
    }

    public void setHireId(String hireId) {
        this.hireId = hireId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHiringUserId() {
        return hiringUserId;
    }

    public void setHiringUserId(String hiringUserId) {
        this.hiringUserId = hiringUserId;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public Boolean getInvitation() {
        return invitation;
    }

    public void setInvitation(Boolean invitation) {
        this.invitation = invitation;
    }

    public Hire() {
    }
}
