package com.example.cloverville.controllers;

import com.example.cloverville.Main;
import com.example.cloverville.models.MyDate;
import com.example.cloverville.models.Resident;
import com.example.cloverville.models.VillageFile;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ResidentController {

  @FXML
  private TableView<Resident> residentTable;
  @FXML
  private TableColumn<Resident, String> colName;
  @FXML
  private TableColumn<Resident, Number> colAge;
  @FXML
  private TableColumn<Resident, String> colGender;
  @FXML
  private TableColumn<Resident, Number> colPoints;

  private ObservableList<Resident> list;

  @FXML
  public void initialize() {

    colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
    colAge.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAge()));
    colGender.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getGender())));
    colPoints.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPoints()));

    list = FXCollections.observableArrayList(Main.village.getResidents().getAll());
    residentTable.setItems(list);
  }

  // ------------------------------------------------------------

  @FXML
  private void onAddResident() {
    Resident r = openResidentForm(null);
    if (r != null) {
      Main.village.getResidents().add(r);
      refresh();
    }
  }

  @FXML
  private void onEditResident() {
    Resident selected = residentTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a resident to edit.");
      return;
    }

    Resident updated = openResidentForm(selected);
    if (updated != null)
      refresh();
  }

  @FXML
  private void onDeleteResident() {
    Resident selected = residentTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a resident to delete.");
      return;
    }

    Alert a = new Alert(Alert.AlertType.CONFIRMATION,
        "Delete " + selected.getName() + "?");

    if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      Main.village.getResidents().remove(selected);
      refresh();
    }
  }

  @FXML
  private void onSave() {
    VillageFile.save(Main.village);
    showInfo("Residents saved.");
  }

  @FXML
  private void onCancel() {
    refresh();
  }

  // ------------------------------------------------------------

  private Resident openResidentForm(Resident existing) {

    Dialog<Resident> dialog = new Dialog<>();
    dialog.setTitle(existing == null ? "Add Resident" : "Edit Resident");

    ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

    // INPUT FIELDS
    TextField nameField = new TextField();
    DatePicker birthDatePicker = new DatePicker();
    ChoiceBox<String> genderBox = new ChoiceBox<>();
    genderBox.getItems().addAll("M", "F");

    // FILL DATA WHEN EDITING
    if (existing != null) {
      nameField.setText(existing.getName());
      birthDatePicker.setValue(java.time.LocalDate.of(
          existing.getBirthDay().getYear(),
          existing.getBirthDay().getMonth(),
          existing.getBirthDay().getDay()));
      genderBox.setValue(String.valueOf(existing.getGender()).toUpperCase());
    } else {
      genderBox.setValue("M");
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.addRow(0, new Label("Name:"), nameField);
    grid.addRow(1, new Label("Birth Date:"), birthDatePicker);
    grid.addRow(2, new Label("Gender:"), genderBox);

    dialog.getDialogPane().setContent(grid);

    // PREVENT CLOSING ON ERROR
    final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButton);
    btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      String name = nameField.getText().trim();
      java.time.LocalDate birthDate = birthDatePicker.getValue();
      String genderVal = genderBox.getValue();

      if (name.isEmpty() || birthDate == null) {
        showError("Name and birth date are required.");
        event.consume(); // Stop the dialog from closing
        return;
      }

      if (birthDate.isAfter(java.time.LocalDate.now())) {
        showError("Birth date cannot be in the future.");
        event.consume();
        return;
      }

      if (birthDate.isBefore(java.time.LocalDate.now().minusYears(125))) {
        showError("Age cannot exceed 125 years.");
        event.consume();
        return;
      }

      if (genderVal == null || genderVal.isEmpty()) {
        showError("Select gender.");
        event.consume();
        return;
      }
    });

    // CONVERT RESULT
    dialog.setResultConverter(btn -> {
      if (btn != saveButton)
        return null;

      // We assume data is valid because of the EventFilter
      String name = nameField.getText().trim();
      java.time.LocalDate birthDate = birthDatePicker.getValue();
      String genderVal = genderBox.getValue();

      MyDate date = new MyDate(
          birthDate.getDayOfMonth(),
          birthDate.getMonthValue(),
          birthDate.getYear());
      char g = genderVal.toUpperCase().charAt(0);

      if (existing == null) {
        return new Resident(name, 0, g, date);
      } else {
        existing.setName(name);
        existing.setBirthDay(date);
        existing.setGender(g);
        return existing;
      }
    });

    return dialog.showAndWait().orElse(null);
  }

  // ------------------------------------------------------------

  private void refresh() {
    list.setAll(Main.village.getResidents().getAll());
  }

  private void showError(String msg) {
    Alert a = new Alert(Alert.AlertType.ERROR, msg);
    a.showAndWait();
  }

  private void showInfo(String msg) {
    Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
    a.showAndWait();
  }
}
