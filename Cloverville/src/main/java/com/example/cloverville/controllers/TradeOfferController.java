package com.example.cloverville.controllers;

import com.example.cloverville.Main;
import com.example.cloverville.models.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class TradeOfferController {

  @FXML
  private TableView<TradeOffer> offerTable;

  @FXML
  private TableColumn<TradeOffer, String> colName;
  @FXML
  private TableColumn<TradeOffer, Number> colCost;
  @FXML
  private TableColumn<TradeOffer, String> colSeller;

  @FXML
  private Label lblItem;
  @FXML
  private Label lblCost;
  @FXML
  private Label lblSeller;
  @FXML
  private Label lblBuyer;

  private ObservableList<TradeOffer> list;

  @FXML
  public void initialize() {

    colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTaskName()));

    colCost.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTaskPoints()));

    colSeller.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSeller().getName()));

    list = FXCollections.observableArrayList(
        Main.village.getOffers().getAll());

    offerTable.setItems(list);

    offerTable.getSelectionModel().selectedItemProperty().addListener(
        (obs, old, sel) -> showDetails(sel));
  }

  // ADD OFFER

  @FXML
  private void onPostOffer() {
    TradeOffer t = openOfferForm(null);
    if (t != null) {
      Main.village.getOffers().addOffer(t);
      refresh();
    }
  }

  // ACCEPT OFFER → buyer pays seller, offer removed

  @FXML
  private void onAcceptOffer() {
    TradeOffer selected = offerTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select an offer to accept.");
      return;
    }

    if (Main.village.getResidents().getAll().isEmpty()) {
      showError("No residents available.");
      return;
    }

    ChoiceDialog<Resident> dialog = new ChoiceDialog<>(
        Main.village.getResidents().getAll().get(0),
        Main.village.getResidents().getAll());

    dialog.setTitle("Accept Offer");
    dialog.setHeaderText("Choose buyer:");
    dialog.setContentText("Buyer:");

    dialog.showAndWait().ifPresent(buyer -> {

      if (buyer.getPoints() < selected.getTaskPoints()) {
        showError("Buyer does not have enough points.");
        return;
      }

      selected.setTaskResident(buyer);
      selected.transition(); // points transfer

      Main.village.getOffers().removeOffer(selected);

      refresh();
      showDetails(null);

      showInfo("Offer accepted.\n" +
          buyer.getName() + " paid " +
          selected.getTaskPoints() + " points to " +
          selected.getSeller().getName());
    });
  }

  // REMOVE OFFER

  @FXML
  private void onRemoveOffer() {
    TradeOffer selected = offerTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select an offer to remove.");
      return;
    }

    Alert a = new Alert(Alert.AlertType.CONFIRMATION,
        "Remove \"" + selected.getTaskName() + "\"?");
    if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

      Main.village.getOffers().removeOffer(selected);
      refresh();
      showDetails(null);
    }
  }

  // SAVE

  @FXML
  private void onSave() {
    VillageFile.save(Main.village);
    showInfo("Trade offers saved.");
  }

  // POPUP FORM FOR ADD/EDIT OFFER

  private TradeOffer openOfferForm(TradeOffer existing) {

    Dialog<TradeOffer> dialog = new Dialog<>();
    dialog.setTitle(existing == null ? "Post Trade Offer" : "Edit Offer");

    ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

    TextField nameField = new TextField();
    TextField costField = new TextField();
    TextField descField = new TextField();

    ChoiceBox<Resident> sellerBox = new ChoiceBox<>();
    sellerBox.getItems().addAll(Main.village.getResidents().getAll());

    if (!sellerBox.getItems().isEmpty())
      sellerBox.setValue(sellerBox.getItems().get(0));

    if (existing != null) {
      nameField.setText(existing.getTaskName());
      costField.setText("" + existing.getTaskPoints());
      descField.setText(existing.getDescription());
      sellerBox.setValue(existing.getSeller());
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.addRow(0, new Label("Item:"), nameField);
    grid.addRow(1, new Label("Cost (points):"), costField);
    grid.addRow(2, new Label("Description:"), descField);
    grid.addRow(3, new Label("Seller:"), sellerBox);

    dialog.getDialogPane().setContent(grid);

    final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveBtn);
    btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      String name = nameField.getText().trim();
      String costText = costField.getText().trim();

      if (name.isEmpty()) {
        showError("Item name is required.");
        event.consume();
        return;
      }

      try {
        int cost = Integer.parseInt(costText);
        if (cost < 0) {
          showError("Cost cannot be negative.");
          event.consume();
          return;
        }
      } catch (NumberFormatException e) {
        showError("Cost must be a valid number.");
        event.consume();
        return;
      }
    });

    dialog.setResultConverter(btn -> {
      if (btn != saveBtn)
        return null;

      String name = nameField.getText().trim();
      int cost = Integer.parseInt(costField.getText().trim());
      String desc = descField.getText().trim();
      Resident seller = sellerBox.getValue();

      if (existing == null) {
        return new TradeOffer(name, cost, desc, null, seller);
      } else {
        existing.setTaskName(name);
        existing.setTaskPoints(cost);
        existing.setDescription(desc);
        existing.setSeller(seller);
        return existing;
      }
    });

    return dialog.showAndWait().orElse(null);
  }

  // DETAILS PANEL

  private void showDetails(TradeOffer o) {
    if (o == null) {
      lblItem.setText("-");
      lblCost.setText("-");
      lblSeller.setText("-");
      lblBuyer.setText("-");
      return;
    }

    lblItem.setText(o.getTaskName());
    lblCost.setText("" + o.getTaskPoints());
    lblSeller.setText(o.getSeller().getName());

    lblBuyer.setText(
        o.getTaskResident() == null ? "-" : o.getTaskResident().getName());
  }

  // -------------------------------------------------------------------------

  private void refresh() {
    list.setAll(Main.village.getOffers().getAll());
  }

  private void showError(String msg) {
    new Alert(Alert.AlertType.ERROR, msg).showAndWait();
  }

  private void showInfo(String msg) {
    new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
  }
}
