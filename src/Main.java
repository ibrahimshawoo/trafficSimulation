import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        //set up scene
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 600);
        drawScene(root);

//        Car car1 = new Car(4);
//
//        root.getChildren().addAll(car1);
//        animateCar(car1, car1.getPath(), root);


        //show animation
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public void drawScene(Group root) {
        int lineWidth = 3;
        Line roadTop = new Line(0, 100, 1000, 100);
        roadTop.setStrokeWidth(lineWidth);
        Line horizontalDash = new Line(0, 200, 1000, 200);
        horizontalDash.setStrokeWidth(lineWidth);
        horizontalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadBottomLeft = new Line(0, 300, 400, 300);
        roadBottomLeft.setStrokeWidth(lineWidth);
        Line roadBottomRight = new Line(600, 300, 1000, 300);
        roadBottomRight.setStrokeWidth(lineWidth);
        Line roadLeft = new Line(400, 300, 400, 700);
        roadLeft.setStrokeWidth(lineWidth);
        Line verticalDash = new Line(500, 300, 500, 700);
        verticalDash.setStrokeWidth(lineWidth);
        verticalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadRight = new Line(600, 300, 600, 700);
        roadRight.setStrokeWidth(lineWidth);

        //add button
        Button addCarButton = new Button("Add Car");
        addCarButton.setLayoutX(100);
        addCarButton.setLayoutY(500);
        addCarButton.setMinHeight(50);
        addCarButton.setMinWidth(100);
        addCarButton.setOnAction(event -> addCarButtonEvent(root));
        root.getChildren().addAll(roadTop, horizontalDash, roadBottomLeft, roadBottomRight, roadLeft, verticalDash, roadRight, addCarButton);
    }

    //animates car to follow given path
    public void animateCar(Car car, Polyline line, Group root) {
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2500));
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setNode(car);
        pathTransition.setPath(line);
        pathTransition.play();
        pathTransition.setOnFinished((finish) -> root.getChildren().remove(car));
    }

    //chooses a random path for car to follow, spawns car, makes car follow given path
    public void addCarButtonEvent(Group root) {
        System.out.println("button pressed");
        //choose random path (0-5)
        Random random = new Random();
        int pathNumber = random.nextInt(6);
        System.out.println(pathNumber);
        Car car = new Car(pathNumber);
        System.out.println(car.getPath());
        root.getChildren().addAll(car);
        animateCar(car, car.getPath(), root);
    }
}
