/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * VisualClient.java
 */

package solution.lesson4.visualclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the Visual Client application
 * 
 * @author Jim Weaver @JavaFXpert
 */
public class VisualClient extends Application {
  
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("VisualClient.fxml"));
    
    Scene scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
