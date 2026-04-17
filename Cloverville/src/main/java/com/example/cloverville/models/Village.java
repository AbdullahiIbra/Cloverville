package com.example.cloverville.models;

public class Village {

  private String name;

  private ResidentList residents;
  private CommunalTaskList tasks;
  private GreenActionList greenActions;
  private TradeOfferList offers;

  public Village(String name) {
    this.name = name;

    residents = new ResidentList();
    tasks = new CommunalTaskList();
    greenActions = new GreenActionList();
    offers = new TradeOfferList();
  }

  public String getName() {
    return name;
  }

  public ResidentList getResidents() {
    return residents;
  }

  public CommunalTaskList getTasks() {
    return tasks;
  }

  public GreenActionList getGreenActions() {
    return greenActions;
  }

  public TradeOfferList getOffers() {
    return offers;
  }
}
