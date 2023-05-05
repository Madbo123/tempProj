package com.mapper.map.bfst_map.Utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public class FXAnims {

    private static boolean isCurrentlyPlaying = false;

    public static void initFadeHover(Node node) {

        try {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.0);

            node.setEffect(colorAdjust);

            node.setOnMouseEntered(e -> {

                Timeline fadeInTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                        new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(0.15), new KeyValue(colorAdjust.brightnessProperty(), -0.2, Interpolator.LINEAR)
                        ));
                fadeInTimeline.setCycleCount(1);
                fadeInTimeline.setAutoReverse(false);
                fadeInTimeline.play();

            });

            node.setOnMouseExited(e -> {

                Timeline fadeOutTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                        new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(0.15), new KeyValue(colorAdjust.brightnessProperty(), 0, Interpolator.LINEAR)
                        ));
                fadeOutTimeline.setCycleCount(1);
                fadeOutTimeline.setAutoReverse(false);
                fadeOutTimeline.play();

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fadeNodeOut(Node node) {
        try {
            Timeline fadeInTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                    new KeyValue(node.opacityProperty(), node.opacityProperty().getValue(), Interpolator.EASE_BOTH)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ));
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            fadeInTimeline.setOnFinished(e -> disableNode(node));
            fadeInTimeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fadeNodeIn(Node node) {
        try {
            enableNode(node);
            Timeline fadeInTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                    new KeyValue(node.opacityProperty(), node.opacityProperty().getValue(), Interpolator.EASE_BOTH)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_BOTH)
                    ));
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            fadeInTimeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toggleNode(Node node) {
        if (node.isVisible() && !node.isDisabled()) {
            disableNode(node);
        } else if (!node.isVisible() && node.isDisabled()) {
            enableNode(node);
        }
    }

    public static void enableNode(Node node) {
        node.setVisible(true);
        node.setManaged(true);
        node.setDisable(false);
    }

    public static void disableNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
        node.setDisable(true);
    }


    public static void translateNode(Node node, double duration, int x, int y) {
        try {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(node);
            translateTransition.setDuration(Duration.seconds(duration));
            translateTransition.setCycleCount(1);
            translateTransition.setInterpolator(Interpolator.EASE_IN);
            if (x != 0) {
                translateTransition.setByX(x);
            }
            if (y != 0) {
                translateTransition.setByY(y);
            }
            translateTransition.setAutoReverse(false);
            translateTransition.setOnFinished(e -> isCurrentlyPlaying = false);
            translateTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UIButtonToggle(Node button, Node openEye, Node closedEye) {
        if (!openEye.isDisabled() && !isCurrentlyPlaying) {
            isCurrentlyPlaying = true;
            fadeNodeOut(openEye);

            try {
                enableNode(closedEye);
                closedEye.setOpacity(0);

                Timeline fadeInTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                        new KeyValue(closedEye.opacityProperty(), closedEye.opacityProperty().getValue(), Interpolator.EASE_BOTH)),
                        new KeyFrame(Duration.seconds(0.15), new KeyValue(closedEye.opacityProperty(), 1, Interpolator.EASE_BOTH)
                        ));
                fadeInTimeline.setCycleCount(1);
                fadeInTimeline.setAutoReverse(false);
                fadeInTimeline.setOnFinished(e -> translateNode(button, 0.15, 315, 0));
                fadeInTimeline.play();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (!isCurrentlyPlaying) {
            isCurrentlyPlaying = true;
            translateNode(button, 0.15, -315, 0);
            fadeNodeOut(closedEye);

            try {
                enableNode(openEye);
                openEye.setOpacity(0);

                Timeline fadeInTimeline = new Timeline(new KeyFrame(Duration.seconds(0),
                        new KeyValue(openEye.opacityProperty(), openEye.opacityProperty().getValue(), Interpolator.EASE_BOTH)),
                        new KeyFrame(Duration.seconds(0.15), new KeyValue(openEye.opacityProperty(), 1, Interpolator.EASE_BOTH)
                        ));
                fadeInTimeline.setCycleCount(1);
                fadeInTimeline.setAutoReverse(false);
                fadeInTimeline.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return;
        }

    }


}
