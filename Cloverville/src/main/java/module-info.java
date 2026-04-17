module com.example.cloverville {
  requires javafx.controls;
  requires javafx.fxml;

  opens com.example.cloverville to javafx.fxml;
  opens com.example.cloverville.controllers to javafx.fxml;
  opens com.example.cloverville.models to javafx.base;

  exports com.example.cloverville;
  exports com.example.cloverville.controllers;
}
