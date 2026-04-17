package com.example.cloverville;

import com.example.cloverville.models.Village;
import com.example.cloverville.models.VillageFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  public static Village village;

  @Override
  public void start(Stage stage) throws Exception {
    village = VillageFile.load();

    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/cloverville/AdminDesktop.fxml"));
    Scene scene = new Scene(loader.load(), 1200, 800);
    stage.setTitle("CloverVille System");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
