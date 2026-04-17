package com.example.cloverville.models;

import java.util.ArrayList;

public class CommunalTaskList {

  private ArrayList<CommunalTask> tasks = new ArrayList<>();

  public void addTask(CommunalTask t) {
    tasks.add(t);
  }

  public void removeTask(CommunalTask t) {
    tasks.remove(t);
  }

  public ArrayList<CommunalTask> getAll() {
    return tasks;
  }
}
