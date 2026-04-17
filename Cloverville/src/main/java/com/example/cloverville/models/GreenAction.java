package com.example.cloverville.models;

public class GreenAction {
  private String taskName;
  private int points;
  private String description;

  public GreenAction(String taskName, int points, String description) {
    this.taskName = taskName;
    this.points = points;
    this.description = description;
  }

  public void setTaskName(String taskName) { this.taskName = taskName; }
  public String getTaskName() { return taskName; }

  public void setPoints(int points) { this.points = points; }
  public int getPoints() { return points; }

  public void setDescription(String description) { this.description = description; }
  public String getDescription() { return description; }

  @Override
  public String toString() {
    return taskName;
  }
}
