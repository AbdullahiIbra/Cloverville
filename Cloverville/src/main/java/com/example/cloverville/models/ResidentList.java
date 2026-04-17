package com.example.cloverville.models;

import java.util.ArrayList;

public class ResidentList {

  private ArrayList<Resident> residents = new ArrayList<>();

  public void add(Resident r) {
    residents.add(r);
  }

  public void remove(Resident r) {
    residents.remove(r);
  }

  public ArrayList<Resident> getAll() {
    return residents;
  }

  public int size() {
    return residents.size();
  }
}
