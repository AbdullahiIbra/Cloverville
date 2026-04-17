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

public class CommunalTaskController {

  @FXML
  private TableView<CommunalTask> taskTable;
  @FXML
  private TableColumn<CommunalTask, String> colTaskName;
  @FXML
  private TableColumn<CommunalTask, Number> colPoints;

  @FXML
  private Label lblTaskName;
  @FXML
  private Label lblTaskPoints;
  @FXML
  private Label lblDescription;

  private ObservableList<CommunalTask> list;

  @FXML
  public void initialize() {

    colTaskName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTaskName()));

    colPoints.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTaskPoints()));

    list = FXCollections.observableArrayList(Main.village.getTasks().getAll());
    taskTable.setItems(list);

    taskTable.getSelectionModel().selectedItemProperty().addListener(
        (obs, old, sel) -> showDetails(sel));
  }

  // --------------------------------------------------------

  @FXML
  private void onAddTask() {
    CommunalTask t = openTaskForm(null);
    if (t != null) {
      Main.village.getTasks().addTask(t);
      refresh();
    }
  }

  @FXML
  private void onEditTask() {
    CommunalTask selected = taskTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a task to edit.");
      return;
    }

    CommunalTask updated = openTaskForm(selected);
    if (updated != null)
      refresh();
  }

  @FXML
  private void onDeleteTask() {
    CommunalTask selected = taskTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a task to delete.");
      return;
    }

    Alert a = new Alert(Alert.AlertType.CONFIRMATION,
        "Delete task \"" + selected.getTaskName() + "\"?");
    if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      Main.village.getTasks().removeTask(selected);
      refresh();
      showDetails(null);
    }
  }

  // COMPLETE TASK
  @FXML
  private void onCompleteTask() {
    CommunalTask selected = taskTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a task to complete.");
      return;
    }

    if (Main.village.getResidents().getAll().isEmpty()) {
      showError("No residents available.");
      return;
    }

    ChoiceDialog<Resident> dialog = new ChoiceDialog<>(
        Main.village.getResidents().getAll().get(0),
        Main.village.getResidents().getAll());
    dialog.setTitle("Complete Task");
    dialog.setHeaderText("Who completed the task?");
    dialog.setContentText("Select resident:");

    dialog.showAndWait().ifPresent(resident -> {

      selected.setTaskResident(resident);
      selected.transition();

      Main.village.getTasks().removeTask(selected);

      refresh();
      showDetails(null);

      showInfo(resident.getName() + " received "
          + selected.getTaskPoints() + " points.\nTask removed.");
    });
  }

  // SAVE
  @FXML
  private void onSave() {
    VillageFile.save(Main.village);
    showInfo("Communal tasks saved.");
  }

  // --------------------------------------------------------

  private CommunalTask openTaskForm(CommunalTask existing) {

    Dialog<CommunalTask> dialog = new Dialog<>();
    dialog.setTitle(existing == null ? "Add Task" : "Edit Task");

    ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

    TextField nameField = new TextField();
    TextField pointsField = new TextField();
    TextField descField = new TextField();

    if (existing != null) {
      nameField.setText(existing.getTaskName());
      pointsField.setText("" + existing.getTaskPoints());
      descField.setText(existing.getDescription());
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.addRow(0, new Label("Name:"), nameField);
    grid.addRow(1, new Label("Points:"), pointsField);
    grid.addRow(2, new Label("Description:"), descField);

    dialog.getDialogPane().setContent(grid);

    final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveBtn);
    btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      String name = nameField.getText().trim();
      String pointsText = pointsField.getText().trim();

      if (name.isEmpty()) {
        showError("Name is required.");
        event.consume();
        return;
      }

      try {
        int pts = Integer.parseInt(pointsText);
        if (pts < 0) {
          showError("Points cannot be negative.");
          event.consume();
          return;
        }
      } catch (NumberFormatException e) {
        showError("Points must be a valid number.");
        event.consume();
        return;
      }
    });

    dialog.setResultConverter(btn -> {
      if (btn != saveBtn)
        return null;

      // Data is valid here due to EventFilter
      String name = nameField.getText().trim();
      int pts = Integer.parseInt(pointsField.getText().trim());
      String desc = descField.getText().trim();

      if (existing == null) {
        return new CommunalTask(name, pts, desc, null);
      } else {
        existing.setTaskName(name);
        existing.setTaskPoints(pts);
        existing.setDescription(desc);
        return existing;
      }
    });

    return dialog.showAndWait().orElse(null);
  }

  // --------------------------------------------------------

  private void showDetails(CommunalTask t) {
    if (t == null) {
      lblTaskName.setText("-");
      lblTaskPoints.setText("-");
      lblDescription.setText("-");
      return;
    }

    lblTaskName.setText(t.getTaskName());
    lblTaskPoints.setText("" + t.getTaskPoints());
    lblDescription.setText(t.getDescription());
  }

  private void refresh() {
    list.setAll(Main.village.getTasks().getAll());
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
