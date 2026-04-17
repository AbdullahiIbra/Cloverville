package com.example.cloverville.controllers;

import com.example.cloverville.Main;
import com.example.cloverville.models.Resident;
import com.example.cloverville.models.VillageFile;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PointsController {

  @FXML
  private TableView<Resident> residentTable;
  @FXML
  private TableColumn<Resident, String> colName;
  @FXML
  private TableColumn<Resident, Number> colPoints;

  @FXML
  private Label lblCommunityPoints;

  private ObservableList<Resident> list;

  @FXML
  public void initialize() {

    colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

    colPoints.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPoints()));

    list = FXCollections.observableArrayList(
        Main.village.getResidents().getAll());

    residentTable.setItems(list);

    // Show the current total community points
    lblCommunityPoints.setText(
        "" + Main.village.getGreenActions().getCommunityPoints());
  }

  //
  // ADD BONUS POINTS TO A RESIDENT

  @FXML
  private void onAddBonus() {

    Resident selected = residentTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a resident first.");
      return;
    }

    Dialog<Integer> dialog = new Dialog<>();
    dialog.setTitle("Add Bonus Points");

    ButtonType saveBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

    TextField bonusField = new TextField();
    bonusField.setPromptText("Enter bonus points");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Bonus Points:"), 0, 0);
    grid.add(bonusField, 1, 0);

    dialog.getDialogPane().setContent(grid);

    final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveBtn);
    btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      try {
        Integer.parseInt(bonusField.getText().trim());
      } catch (NumberFormatException e) {
        showError("Invalid number.");
        event.consume();
      }
    });

    dialog.setResultConverter(btn -> {
      if (btn != saveBtn)
        return null;
      return Integer.parseInt(bonusField.getText().trim());
    });

    Integer bonus = dialog.showAndWait().orElse(null);

    if (bonus != null) {
      selected.setPoints(selected.getPoints() + bonus);
      refresh();
      showInfo("Bonus added to " + selected.getName());
    }
  }

  // RESET COMMUNITY POINTS ONLY

  @FXML
  private void onResetPersonalPoints() {

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
        "Reset ALL residents' personal points?");
    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

      for (Resident r : Main.village.getResidents().getAll()) {
        r.setPoints(0);
      }

      refresh();
      showInfo("All personal points reset to 0.");
    }
  }

  // SAVE

  @FXML
  private void onSave() {
    VillageFile.save(Main.village);
    showInfo("Points saved.");
  }

  // ---------------------------------------------------------

  private void refresh() {
    list.setAll(Main.village.getResidents().getAll());
    lblCommunityPoints.setText(
        "" + Main.village.getGreenActions().getCommunityPoints());
  }

  private void showError(String msg) {
    new Alert(Alert.AlertType.ERROR, msg).showAndWait();
  }

  private void showInfo(String msg) {
    new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
  }
}
