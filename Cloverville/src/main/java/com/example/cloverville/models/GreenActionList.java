package com.example.cloverville.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;

public class GreenActionList {

  private ArrayList<GreenAction> greenActionList = new ArrayList<>();
  private IntegerProperty communityPoints = new SimpleIntegerProperty(0);

  public GreenActionList() {
    communityPoints = new SimpleIntegerProperty(0);
  }

  public void addGreenAction(GreenAction g) {
    greenActionList.add(g);
    setCommunityPoints(getCommunityPoints() + g.getPoints());
  }

  public void removeGreenAction(GreenAction g) {
    greenActionList.remove(g);
    setCommunityPoints(getCommunityPoints() - g.getPoints());
  }

  public ArrayList<GreenAction> getAll() {
    return greenActionList;
  }

  public int getCommunityPoints() {
    return communityPoints.get();
  }

  public void setCommunityPoints(int value) {
    communityPoints.set(value);
  }

  public IntegerProperty communityPointsProperty() {
    return communityPoints;
  }
}
