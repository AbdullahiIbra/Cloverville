package com.example.cloverville.models;

import java.time.LocalDate;

public class Resident {

  private String name;
  private int points;
  private char gender;
  private MyDate birthDay;

  public Resident(String name, int points, char gender, MyDate birthDay) {
    this.name = name;
    this.points = points;
    this.gender = gender;
    this.birthDay = birthDay;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public int getPoints() { return points; }
  public void setPoints(int points) { this.points = points; }

  public char getGender() { return gender; }
  public void setGender(char gender) { this.gender = gender; }

  public MyDate getBirthDay() { return birthDay; }
  public void setBirthDay(MyDate birthDay) { this.birthDay = birthDay; }

  // Calculated age (simple)
  public int getAge() {
    int currentYear = LocalDate.now().getYear();
    return currentYear - birthDay.getYear();
  }

  @Override
  public String toString() {
    return name; // for dropdowns
  }
}
