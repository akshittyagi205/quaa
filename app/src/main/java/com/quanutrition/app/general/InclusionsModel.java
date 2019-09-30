package com.quanutrition.app.general;

public class InclusionsModel {

    String name,description;
    boolean done;

    public InclusionsModel(String name,String description, boolean done) {
        this.name = name;
        this.done = done;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
