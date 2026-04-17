package com.example.cloverville.models;

public class CommunalTask extends PersonalPointTransition {

  public CommunalTask(String taskName, int taskPoints, String description, Resident taskResident) {
    super(taskName, taskPoints, description, taskResident);
  }

  @Override
  public void transition() {
    // Give points to the resident who completed the task
    if (getTaskResident() != null) {
      int newPoints = getTaskResident().getPoints() + getTaskPoints();
      getTaskResident().setPoints(newPoints);
    }
  }

  @Override
  public String toString() {
    return getTaskName();
  }
}
