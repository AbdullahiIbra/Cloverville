package com.example.cloverville.controllers;

import com.example.cloverville.Main;
import com.example.cloverville.models.GreenAction;
import com.example.cloverville.models.VillageFile;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class GreenActionController {

  @FXML
  private ListView<GreenAction> actionList;

  @FXML
  private Label lblName;
  @FXML
  private Label lblPoints;
  @FXML
  private Label lblDescription;

  private ObservableList<GreenAction> list;

  @FXML
  public void initialize() {

    list = FXCollections.observableArrayList(
        Main.village.getGreenActions().getAll());

    actionList.setItems(list);

    actionList.getSelectionModel().selectedItemProperty().addListener(
        (obs, old, sel) -> showDetails(sel));
  }

  // ACTION BUTTONS

  @FXML
  private void onAddAction() {
    GreenAction g = openActionForm(null);
    if (g != null) {
      Main.village.getGreenActions().addGreenAction(g);
      refresh();

      showInfo("Green action added.\nCommunity points increased by " + g.getPoints());
    }
  }

  @FXML
  private void onEditAction() {
    GreenAction selected = actionList.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select an action to edit.");
      return;
    }

    int oldPoints = selected.getPoints(); // Save old points before editing

    GreenAction updated = openActionForm(selected);
    if (updated != null) {
      int newPoints = updated.getPoints();
      int difference = newPoints - oldPoints;

      Main.village.getGreenActions().setCommunityPoints(
          Main.village.getGreenActions().getCommunityPoints() + difference);

      refresh();
      showDetails(updated);
    }
  }

  @FXML
  private void onDeleteAction() {
    GreenAction selected = actionList.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select an action to delete.");
      return;
    }

    Alert a = new Alert(Alert.AlertType.CONFIRMATION,
        "Delete \"" + selected.getTaskName() + "\"?");
    if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

      Main.village.getGreenActions().getAll().remove(selected);
      // Removing a green action does NOT remove community points
      refresh();
      showDetails(null);
    }
  }

  // SAVE

  @FXML
  private void onSave() {
    VillageFile.save(Main.village);
    showInfo("Green actions saved.");
  }

  // POPUP FORM FOR ADD & EDIT

  private GreenAction openActionForm(GreenAction existing) {

    Dialog<GreenAction> dialog = new Dialog<>();
    dialog.setTitle(existing == null ? "Add Green Action" : "Edit Green Action");

    ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

    TextField nameField = new TextField();
    TextField pointsField = new TextField();
    TextField descriptionField = new TextField();

    if (existing != null) {
      nameField.setText(existing.getTaskName());
      pointsField.setText("" + existing.getPoints());
      descriptionField.setText(existing.getDescription());
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.addRow(0, new Label("Name:"), nameField);
    grid.addRow(1, new Label("Points:"), pointsField);
    grid.addRow(2, new Label("Description:"), descriptionField);

    dialog.getDialogPane().setContent(grid);

    final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveBtn);
    btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      String name = nameField.getText().trim();
      String pointsText = pointsField.getText().trim();
      String description = descriptionField.getText().trim();

      if (name.isEmpty() || description.isEmpty()) {
        showError("All fields must be filled.");
        event.consume();
        return;
      }

      try {
        Integer.parseInt(pointsText);
      } catch (NumberFormatException e) {
        showError("Points must be a valid number.");
        event.consume();
        return;
      }
    });

    dialog.setResultConverter(btn -> {
      if (btn != saveBtn)
        return null;

      String name = nameField.getText().trim();
      int points = Integer.parseInt(pointsField.getText().trim());
      String description = descriptionField.getText().trim();

      if (existing == null) {
        // New green action → Adds community points
        return new GreenAction(name, points, description);
      } else {
        existing.setTaskName(name);
        existing.setPoints(points);
        existing.setDescription(description);
        return existing;
      }
    });

    return dialog.showAndWait().orElse(null);
  }

  // DETAILS PANEL

  private void showDetails(GreenAction g) {
    if (g == null) {
      lblName.setText("-");
      lblPoints.setText("-");
      lblDescription.setText("-");
      return;
    }

    lblName.setText(g.getTaskName());
    lblPoints.setText("" + g.getPoints());
    lblDescription.setText(g.getDescription());
  }

  // ------------------------------------------------------------
  private void refresh() {
    list.setAll(Main.village.getGreenActions().getAll());
  }

  private void showError(String msg) {
    new Alert(Alert.AlertType.ERROR, msg).showAndWait();
  }

  private void showInfo(String msg) {
    new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
  }
}
