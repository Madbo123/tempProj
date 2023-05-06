package com.mapper.map.bfst_map.Controller.GUI;

import com.mapper.map.bfst_map.Controller.Dijkstra.DijkstraController;
import com.mapper.map.bfst_map.Model.Elements.Model;
import com.mapper.map.bfst_map.Utils.FXAnims;
import com.mapper.map.bfst_map.Utils.Highway;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

import static com.mapper.map.bfst_map.Utils.FXAnims.*;

public class MapController {



    //Controller for UI-elements on top of the canvas anchor - MB

    @FXML
    public AnchorPane mapMainAnchor, mapUIAnchor, mapCanvasAnchor;
    @FXML
    private Pane debugMenuBar, menuBar, toggleUIButton, toggleThemeButton, debugMenuButton, menuButton, hoverInfoPane, lengthIndicatorPane, closedEyeImage, searchPane, destinationPane, toggleDijkstraButton;
    @FXML
    private HBox debugMenuBarHBox;
    @FXML
    private VBox searchSuggestionBox, destinationSuggestionBox;
    @FXML
    private TextField searchBar, searchS1, searchS2, searchS3, searchS4, searchS5, destinationBar, destS1, destS2, destS3, destS4, destS5;
    @FXML
    private ImageView openEyeImage;
    @FXML
    private Button testButton1, testButton2, testButton3, testButton4, testButton5, testButton6, testButton7, testButton8;
    @FXML
    private Label XYLabel, LatLonLabel;



    boolean guiDisabled = false;
    boolean isDarkMode = false;
    protected CanvasController canvasController;
    protected Canvas canvas;

    //Default is small.osm
    public String filename = "data/small.osm";

    double ScreenW = Screen.getPrimary().getVisualBounds().getWidth(), ScreenH = Screen.getPrimary().getVisualBounds().getHeight();
    double prefWidth = 800, prefHeight = 600;

    public static Model model;


    public void initialize() {
        initAll();
    }


    public void setHoverDim(Node node) {
        FXAnims.initFadeHover(node);
    }


    public void toggleDebugMenu() {
        toggleNode(debugMenuBar);
    }

    //This is the most retarded fucking shit ever, too bad!
    public void toggleGUI() {
        if (!guiDisabled) {
            fadeNodeOut(debugMenuButton);
            fadeNodeOut(menuButton);
            fadeNodeOut(debugMenuBarHBox);
            fadeNodeOut(searchPane);
            fadeNodeOut(destinationPane);
            fadeNodeOut(hoverInfoPane);
            fadeNodeOut(lengthIndicatorPane);
            fadeNodeOut(toggleThemeButton);

            UIButtonToggle(toggleUIButton, openEyeImage, closedEyeImage);
            guiDisabled = true;
        } else {
            fadeNodeIn(debugMenuButton);
            fadeNodeIn(menuButton);
            fadeNodeIn(debugMenuBarHBox);
            fadeNodeIn(searchPane);
            fadeNodeIn(destinationPane);
            fadeNodeIn(hoverInfoPane);
            fadeNodeIn(lengthIndicatorPane);
            fadeNodeIn(toggleThemeButton);

            UIButtonToggle(toggleUIButton, openEyeImage, closedEyeImage);
            guiDisabled = false;
        }
    }

    public void toggleTheme() {
        DrawController.toggleColorTheme(isDarkMode, mapCanvasAnchor);
        isDarkMode = !isDarkMode;
        canvasController.redraw();
    }



    public void toggleDijkstra() {
        if (destinationPane.isDisabled()) {
            rotateNode(toggleDijkstraButton, 0.2, 180);
            translateNode(destinationPane, 0.25, 440, 0);
            fadeNodeIn(destinationPane);
        } else {
            rotateNode(toggleDijkstraButton, 0.2, 180);
            translateNode(destinationPane, 0.25, -440, 0);
            fadeNodeOut(destinationPane);
        }
    }

    private Iterable<String> getSuggestions(TextField barObject) {
        return Model.addressTST.keysWithPrefix(barObject.getText(), 5);
    }

    public void searchSuggestionInputChanged(KeyEvent event) {
        //Inverted isEmpty() method call.
        boolean hasText = !searchBar.getText().isEmpty();
        List<TextField> suggestionFields = List.of(searchS1, searchS2, searchS3, searchS4, searchS5);
        searchSuggestionToggle();

        for (TextField field : suggestionFields) {
            field.setText("");
        }

        if (hasText) {
            //Iterate through suggestions and fill out boxes. This can be implemented better by automatically generating the suggestion-boxes.
            int i = 0;
            for (String suggestion : getSuggestions(searchBar)) {
                if (i > 4) {
                    //Throws error if amount of suggestions somehow surpasses 5
                    throw new ArrayIndexOutOfBoundsException("More than five suggestions were generated during search");
                } else {
                    //Sets suggestions by index
                    suggestionFields.get(i).setText(suggestion);
                    i++;
                }
            }
        } else {
            //Disables suggestions if there are none.
            disableNode(searchSuggestionBox);
        }
    }

    public void searchSuggestionToggle() {
        if (searchBar.isFocused() && searchSuggestionBox.isDisabled() && !searchBar.getText().isEmpty()) {
            enableNode(searchSuggestionBox);
        } else if (searchBar.getText().isEmpty()) {
            disableNode(searchSuggestionBox);
        }
    }

    public void searchSuggestionFill(MouseEvent event) {
        try {
            TextField selectedSuggestion = (TextField) event.getSource();
            searchBar.setText(selectedSuggestion.getText());
            disableNode(searchSuggestionBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Copy paste salad, fikser det senereTM

    public void destinationSuggestionInputChanged(KeyEvent event) {

        //Inverted isEmpty() method call.
        boolean hasText = !destinationBar.getText().isEmpty();
        List<TextField> suggestionFields = List.of(destS1, destS2, destS3, destS4, destS5);
        destinationSuggestionToggle();

        for (TextField field : suggestionFields) {
            field.setText("");
        }

        if (hasText) {
            //Iterate through suggestions and fill out boxes. This can be implemented better by automatically generating the suggestion-boxes.
            int i = 0;
            for (String suggestion : getSuggestions(destinationBar)) {
                if (i > 4) {
                    //Throws error if amount of suggestions somehow surpasses 5
                    throw new ArrayIndexOutOfBoundsException("More than five suggestions were generated during search");
                } else {
                    //Sets suggestions by index
                    suggestionFields.get(i).setText(suggestion);
                    i++;
                }
            }
        } else {
            //Disables suggestions if there are none.
            disableNode(destinationSuggestionBox);
        }
    }

    public void destinationSuggestionToggle() {
        if (destinationBar.isFocused() && destinationSuggestionBox.isDisabled() && !destinationBar.getText().isEmpty()) {
            enableNode(destinationSuggestionBox);
        } else if (destinationBar.getText().isEmpty()) {
            disableNode(destinationSuggestionBox);
        }
    }

    public void destinationSuggestionFill(MouseEvent event) {
        try {
            TextField selectedSuggestion = (TextField) event.getSource();
            destinationBar.setText(selectedSuggestion.getText());
            disableNode(destinationSuggestionBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //HEJ PATRICK, DET HER ER DIJKSTRA KNAP METODEN
    public void getPath(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            removeFocus(); //Removing the dropdown by manipulating focus

            if ((!searchBar.getText().isEmpty() | !destinationBar.getText().isEmpty()) && !destinationBar.isDisabled()) {
                String source = searchBar.getText();
                String target = destinationBar.getText();
                String transportType = "car"; //Get transport type here. Placeholder is "car"

                //BUG HER =====================================================================================================================
                //Hvis denne kaldes med specifikke addresser, opstår en invocationexception. Waypoint types kan vidst ikke castes til roads atm.
                Model.dijkstraController.calculateShortestPath(source, target, transportType);
            } else {
                throw new RuntimeException("Tried to search with empty/null textfield");
            }
        }
    }

    public void removeFocus() {
        //Remove focus from any given element by setting the focus to be the entire UI anchor.
        mapUIAnchor.requestFocus();
    }





    public void timedRedraws(int count) {
        double timeStart = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            canvasController.redraw();
        }
        double timeEnd = System.currentTimeMillis();
        System.out.println("================= Runtime for " + count + " redraws: " + (timeEnd - timeStart) + "ms =================");
        //System.out.println("================= Avg for " + count + " redraws: " + ((timeEnd - timeStart) / count) + "ms =================");
    }

    public void testTimedRedraws() {
        timedRedraws(60);
    }



    private void initAll() {
        filename = MenuController.getFilename();
        DrawController.initColorMap();
        Highway.loadData();

        initModel();
        initCanvas();
        initView();
        initAnims();
    }

    private void initModel() {
        try {
            model = Model.load(filename);
        } catch (Exception e) {
            System.out.println("Error loading model");
            e.printStackTrace();
        }

    }

    private void initCanvas() {
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("MapCanvas.fxml"));

        try {
            canvas = canvasLoader.load();
            canvasController = canvasLoader.getController();
            canvasController.mapController = this;
            mapCanvasAnchor.getChildren().add(canvas);
        } catch (Exception e) {
            System.out.println("Error loading canvas");
            e.printStackTrace();
        }


    }

    private void initView() {
        if (ScreenW >= 2100 && ScreenH >= 1200) {
            prefWidth = 1920;
            prefHeight = 1080;
        } else if (ScreenW >= 1900 && ScreenH >= 1000) {
            prefWidth = 1600;
            prefHeight = 900;
        } else if(ScreenW >= 1400 && ScreenH >= 750) {
            prefWidth = 1280;
            prefHeight = 720;
        }

        resizeScene(prefWidth, prefHeight);

    }

    private void initAnims() {
        //Hover dim
        setHoverDim(toggleUIButton);
        setHoverDim(toggleThemeButton);
        setHoverDim(debugMenuButton);
        setHoverDim(menuButton);
        setHoverDim(toggleDijkstraButton);
        setHoverDim(testButton1);
        setHoverDim(testButton2);
        setHoverDim(testButton3);
        setHoverDim(testButton4);
        setHoverDim(testButton5);
        setHoverDim(testButton6);
        setHoverDim(testButton7);
        setHoverDim(testButton8);
    }

    //Below two methods should only be called externally after the map has initialized.

    private void initSBoxListeners() {
        //Listeners could be generated from a template via method as they exist currently, but this way allows for diversification of behaviour

        //These listeners disable suggestion boxes if the focus is moved from the search bars. A delay of 100ms is used to allow for execution of various minor methods
        //Timer threads are used to prevent drift, threads are daemon to allow for impartial termination of thread.
        try {
            final ChangeListener<Boolean> focusChangedListener1 = (observable, oldValue, newValue) -> {
                if (!newValue) {
                    new java.util.Timer(true).schedule(new java.util.TimerTask() {
                       @Override
                       public void run() {
                           disableNode(searchSuggestionBox);
                       }
                    },
                    100
                    );
                } else {
                    if (searchSuggestionBox.isDisabled() && !searchBar.getText().isEmpty()) {
                        enableNode(searchSuggestionBox);
                    }
                }
            };

            searchBar.focusedProperty().addListener(focusChangedListener1);

            final ChangeListener<Boolean> focusChangedListener2 = (observable, oldValue, newValue) -> {
                if (!newValue) {
                    new java.util.Timer(true).schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            disableNode(destinationSuggestionBox);
                        }
                    },
                    100
                    );

                } else {
                    if (destinationSuggestionBox.isDisabled() && !destinationBar.getText().isEmpty()) {
                        enableNode(destinationSuggestionBox);
                    }
                }
            };

            destinationBar.focusedProperty().addListener(focusChangedListener2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initResizeListener() {
        final Stage primaryStage = getPrimaryStage();
        //Basic ChangeListener for resizing.
        final ChangeListener<Number> resizeListener = new ChangeListener<>() {

            //Timer should be daemon, otherwise it will keep JVM from terminating in many cases.
            //Please remember to do this if adding a timer somewhere. - MB
            final Timer timer = new Timer(true);
            TimerTask task = null;
            final long delayTime = 200;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (task != null) {
                    task.cancel();
                }

                task = new TimerTask() {
                    @Override
                    public void run() {
                        //S.O.P For testing purposes
                        //System.out.println("Resizing...");
                        resizeScene(primaryStage.getWidth(), primaryStage.getHeight());
                    }
                };

                timer.schedule(task, delayTime);

            }
        };


        primaryStage.widthProperty().addListener(resizeListener);
        primaryStage.heightProperty().addListener(resizeListener);
    }





    //Resize method.
    private void resizeScene(double prefWidth, double prefHeight) {
        mapMainAnchor.setPrefSize(prefWidth, prefHeight);
        mapUIAnchor.setPrefSize(prefWidth, prefHeight);
        mapCanvasAnchor.setPrefSize(prefWidth, prefHeight);
        canvas.setWidth(prefWidth);
        canvas.setHeight(prefHeight);
        canvasController.redraw();
    }

    public void stageSetup() {
        initResizeListener();
        initSBoxListeners();
    }




    private Stage getPrimaryStage() {
        return (Stage)mapCanvasAnchor.getScene().getWindow();
    }

    protected void updateInfoLabel(MouseEvent event) {
        XYLabel.setText("X: " + event.getX() + "  |  Y: " + event.getY());
        LatLonLabel.setText("Lon: " + Math.round(event.getX() / 0.56F) + "  |  Lat: " + Math.round(event.getY() * -1));
    }
}
