import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        List<Car> allCars = new ArrayList<Car>();
        //set up scene
        Group root = new Group();
        Scene scene = new Scene(root);


        drawScene(root,allCars);

        //show animation
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //draws the scene
    public void drawScene(Group root, List<Car> allCars) {

        //junction
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

        //traffic lights
        TrafficLight trafficLightA = new TrafficLight(450,50);
        TrafficLight trafficLightB = new TrafficLight(700,350);
        TrafficLight trafficLightC = new TrafficLight(350,350);
        trafficLightA.setGo();

        //traffic light control buttons
        final ToggleGroup trafficLightControls = new ToggleGroup();
        RadioButton radioButtonA = new RadioButton("Traffic Light A");
        radioButtonA.setToggleGroup(trafficLightControls);
        radioButtonA.setSelected(true);
        RadioButton radioButtonB = new RadioButton("Traffic Light B");
        radioButtonB.setToggleGroup(trafficLightControls);
        RadioButton radioButtonC = new RadioButton("Traffic Light C");
        radioButtonC.setToggleGroup(trafficLightControls);
        HBox trafficLightButtonBox = new HBox(radioButtonA,radioButtonB,radioButtonC);
        trafficLightButtonBox.setLayoutX(50);
        trafficLightButtonBox.setLayoutY(600);
        trafficLightButtonBox.setSpacing(10);

        radioButtonA.setUserData(trafficLightA);
        radioButtonB.setUserData(trafficLightB);
        radioButtonC.setUserData(trafficLightC);

        trafficLightControls.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                TrafficLight oldTrafficLight = (TrafficLight) oldValue.getUserData();
                oldTrafficLight.setStop();
                TrafficLight newTrafficLight = (TrafficLight) newValue.getUserData();
                newTrafficLight.setGo();
            }
        });

        //add car button
        Button addCarButton = new Button("Add Car");
        addCarButton.setLayoutX(100);
        addCarButton.setLayoutY(500);
        addCarButton.setMinHeight(50);
        addCarButton.setMinWidth(100);
        addCarButton.setOnAction(event -> addCarButtonEvent(root, allCars));

        //add everything
        root.getChildren().addAll(
                roadTop,
                horizontalDash,
                roadBottomLeft,
                roadBottomRight,
                roadLeft,
                verticalDash,
                roadRight,
                addCarButton,
                trafficLightA,
                trafficLightB,
                trafficLightC, trafficLightButtonBox);
    }

    //animates car to follow given path
    public void animateCar(Car car, Polyline line, Group root, List<Car> allCars) {
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2500));
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setNode(car);
        pathTransition.setPath(line);

        //method is called each frame of the animation
        AnimationTimer collisionTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkCollisions(allCars);
            }
        };

        pathTransition.play();

        //called when car gets to the end of its path
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                root.getChildren().remove(car);
                allCars.remove(car);
            }
        });
    }

    //chooses a random path for car to follow, spawns car, makes car follow given path
    public void addCarButtonEvent(Group root, List<Car> allCars) {
        System.out.println("button pressed");
        //choose random path (0-5)
        Random random = new Random();
        int pathNumber = random.nextInt(6);
        System.out.println(pathNumber);
        Car car = new Car(pathNumber);
        allCars.add(car);
        System.out.println(car.getPath());
        root.getChildren().addAll(car);
        animateCar(car, car.getPath(), root, allCars);
    }

    //handles collisions
    public void checkCollisions(List<Car> allCars){
        //check for collisions
        //iterate over all cars in group
        //for each car in group iterate over each car in the group and check for a collision
        for(Car car : allCars){
            for (Car car2 : allCars) {
                if(car == car2){
                    break;
                }else {
                    if (car.getBoundsInParent().intersects(car2.getBoundsInParent())){
                    }
                }
            }
        }

    }




}
