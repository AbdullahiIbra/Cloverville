package com.example.cloverville.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class AdminDesktopController {

  @FXML
  private StackPane contentArea;


  // Helper loader method

  private void loadPage(String fileName) {
    try {
      Parent root = FXMLLoader.load(
          getClass().getResource("/com/example/cloverville/" + fileName + ".fxml")
      );
      contentArea.getChildren().setAll(root);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("❌ Cannot load: " + fileName + ".fxml");
    }
  }


  // BUTTON ACTIONS


  @FXML
  private void showResidents() {
    loadPage("Resident");
  }

  @FXML
  private void showCommunalTasks() {
    loadPage("CommunalTask");
  }

  @FXML
  private void showGreenActions() {
    loadPage("GreenAction");
  }

  @FXML
  private void showTradeOffers() {
    loadPage("TradeOffer");
  }

  @FXML
  private void showPoints() {
    loadPage("Points");
  }
}
