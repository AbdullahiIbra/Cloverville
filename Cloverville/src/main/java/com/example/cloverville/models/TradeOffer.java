package com.example.cloverville.models;

public class TradeOffer extends PersonalPointTransition {

  private Resident seller; // the resident who is offering the service/item

  public TradeOffer(String taskName, int taskPoints, String description, Resident buyer, Resident seller) {
    super(taskName, taskPoints, description, buyer);
    this.seller = seller;
  }

  public Resident getSeller() {
    return seller;
  }

  public void setSeller(Resident seller) {
    this.seller = seller;
  }

  @Override
  public void transition() {
    // Transfer points from buyer → seller
    Resident buyer = getTaskResident();

    if (buyer == null || seller == null) {
      return;
    }

    buyer.setPoints(buyer.getPoints() - getTaskPoints());
    seller.setPoints(seller.getPoints() + getTaskPoints());
  }

  @Override
  public String toString() {
    return getTaskName();
  }
}
