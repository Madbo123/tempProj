package com.mapper.map.bfst_map.Controller.GUI;

import com.mapper.map.bfst_map.Model.Elements.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MenuController {

    @FXML
    private Button loadButton1, loadButton2, loadButton3, loadButtonCustom;

    private Stage primaryStage;

    protected static String filename = null;


    public void initialize() {
        initButtonText();
    }

    public void loadSmallOSM() {
        loadMap("data/small.osm");
    }

    public void loadKbhOSM() {
        loadMap("data/kbh.osm");
    }

    public void loadDenmarkOSM() {
        loadMap("data/denmark-latest.osm");
    }

    public void loadCustomOSM() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an OSM-file");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Osm, Obj and Zip files", "*.osm", "*.obj", "*.osm.zip"));

        try {
            File loadedFile = fileChooser.showOpenDialog(null);
            if (loadedFile != null) {
                loadMap(loadedFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMap(String file) {
        filename = file;

        try {
            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("Map.fxml"));

            Parent mapRoot = mapLoader.load();

            MapController mapController = mapLoader.getController();

            Scene scene = new Scene(mapRoot);

            primaryStage.setScene(scene);

            primaryStage.setX((mapController.ScreenW - primaryStage.getWidth()) / 2);
            primaryStage.setY((mapController.ScreenH - primaryStage.getHeight()) / 2);

            mapController.stageSetup();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initButtonText() {
        loadButton1.setText("Load Small.OSM");
        loadButton2.setText("Load Kbh.OSM");
        loadButton3.setText("Load Denmark.OSM");
    }

    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    protected static String getFilename() {
        return filename;
    }

}
