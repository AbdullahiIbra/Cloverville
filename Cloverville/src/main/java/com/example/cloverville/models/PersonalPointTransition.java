package com.example.cloverville.models;

public abstract class PersonalPointTransition {
  private String taskName;
  private int taskPoints;
  private String description;
  private Resident taskResident;  // used in trade offers + communal tasks

  public PersonalPointTransition(String taskName, int points, String description, Resident taskResident) {
    this.taskName = taskName;
    this.taskPoints = points;
    this.description = description;
    this.taskResident = taskResident;
  }

  public String getTaskName() { return taskName; }
  public void setTaskName(String taskName) { this.taskName = taskName; }

  public int getTaskPoints() { return taskPoints; }
  public void setTaskPoints(int taskPoints) { this.taskPoints = taskPoints; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Resident getTaskResident() { return taskResident; }
  public void setTaskResident(Resident taskResident) { this.taskResident = taskResident; }

  public abstract void transition();
}
