package com.example.cloverville.models;

import java.util.ArrayList;

public class TradeOfferList {

  private ArrayList<TradeOffer> offers = new ArrayList<>();

  public void addOffer(TradeOffer t) {
    offers.add(t);
  }

  public void removeOffer(TradeOffer t) {
    offers.remove(t);
  }

  public ArrayList<TradeOffer> getAll() {
    return offers;
  }
}
