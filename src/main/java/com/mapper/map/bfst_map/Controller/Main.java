package com.mapper.map.bfst_map.Controller;

import com.mapper.map.bfst_map.Controller.GUI.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //String filename = "data/denmark.osm.zip.obj";


        //double starttime = System.currentTimeMillis();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setTitle("OSM Map");


        primaryStage.setScene(scene);
        primaryStage.show();



        //double endtime = System.currentTimeMillis();
        //System.out.println("================= Time to load model: " + (endtime - starttime) + "ms =================");

    }
}