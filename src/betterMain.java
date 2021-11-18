import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.awt.*;
import java.util.*;
import java.util.List;

public class betterMain extends Application {
    final long[] frameTimes = new long[100];
    int frameTimeIndex = 0;
    boolean arrayFilled = false;
    double frameRate = 0;
    double addCarTimer = 0;
    double trafficLightTimer = 0;
    List<Car> allCars = new ArrayList<Car>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //set up scene
        Group root = new Group();
        Scene scene = new Scene(root, 1900, 900);

        //show animation
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer frameRateMeter = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
                }
            }
        };

        frameRateMeter.start();

        drawScene(root, allCars);
    }

    //draws the scene
    public void drawScene(Group root, List<Car> allCars) {

        //junction
        int lineWidth = 3;

        //left horizontal
        Line horizontalDashLeft = new Line(0, 450, 870, 450);
        horizontalDashLeft.setStrokeWidth(lineWidth);
        horizontalDashLeft.getStrokeDashArray().addAll(20d, 20d);

        Line roadLeftTop = new Line(0, 370, 870, 370);
        roadLeftTop.setStrokeWidth(lineWidth);

        Line roadLeftBottom = new Line(0, 530, 870, 530);
        roadLeftBottom.setStrokeWidth(lineWidth);

        TrafficLight trafficLightA = new TrafficLight(830, 330);


        //top vertical
        Line verticalDashTop = new Line(950, 0, 950, 370);
        verticalDashTop.setStrokeWidth(lineWidth);
        verticalDashTop.getStrokeDashArray().addAll(20d, 20d);

        Line roadTopLeft = new Line(870, 0, 870, 370);
        roadTopLeft.setStrokeWidth(lineWidth);

        Line roadTopRight = new Line(1030, 0, 1030, 370);
        roadTopRight.setStrokeWidth(lineWidth);

        TrafficLight trafficLightB = new TrafficLight(1070, 330);


        //right horizontal
        Line horizontalDashRight = new Line(1030, 450, 1900, 450);
        horizontalDashRight.setStrokeWidth(lineWidth);
        horizontalDashRight.getStrokeDashArray().addAll(20d, 20d);

        Line roadRightTop = new Line(1030, 370, 1900, 370);
        roadRightTop.setStrokeWidth(lineWidth);

        Line roadRightBottom = new Line(1030, 530, 1900, 530);
        roadRightBottom.setStrokeWidth(lineWidth);

        TrafficLight trafficLightC = new TrafficLight(1070, 570);

        //bottom vertical

        Line verticalDashBottom = new Line(950, 530, 950, 900);
        verticalDashBottom.setStrokeWidth(lineWidth);
        verticalDashBottom.getStrokeDashArray().addAll(20d, 20d);

        Line roadBottomLeft = new Line(870, 530, 870, 900);
        roadBottomLeft.setStrokeWidth(lineWidth);

        Line roadBottomRight = new Line(1030, 530, 1030, 900);
        roadBottomRight.setStrokeWidth(lineWidth);

        TrafficLight trafficLightD = new TrafficLight(830, 570);


        trafficLightA.setGo();

        Point centre = new Point(950, 450);
        CrossJunction crossJunction = new CrossJunction(trafficLightA, trafficLightB, trafficLightC, trafficLightD, centre, root);
        AnimationTimer addCarAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                addCarTimer = addCarTimer + 1;
                double addCarTimerDivided = addCarTimer / Math.round(frameRate);
                if ((addCarTimerDivided == Math.floor(addCarTimerDivided)) && !Double.isInfinite(addCarTimerDivided)) {
                    addCarButtonEvent(root, allCars, trafficLightA, trafficLightB, trafficLightC, trafficLightD, crossJunction);
                }
            }
        };
        addCarAnimationTimer.start();

        AnimationTimer CrossJunctionAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                crossJunction.trafficLightController(frameRate);
            }
        };
        CrossJunctionAnimationTimer.start();

        //add car button
        Button addCarButton = new Button("Add Car");
        addCarButton.setLayoutX(100);
        addCarButton.setLayoutY(500);
        addCarButton.setMinHeight(50);
        addCarButton.setMinWidth(100);
        addCarButton.setOnAction(event -> addCarButtonEvent(root, allCars, trafficLightA, trafficLightB, trafficLightC, trafficLightD, crossJunction));

        //add everything to scene
        root.getChildren().addAll(
                roadTopLeft,
                roadTopRight,
                verticalDashTop,

                roadLeftTop,
                roadLeftBottom,
                horizontalDashLeft,

                roadRightTop,
                roadRightBottom,
                horizontalDashRight,

                roadBottomLeft,
                roadBottomRight,
                verticalDashBottom,

                trafficLightA,
                trafficLightB,
                trafficLightC,
                trafficLightD);
    }

    //animates car to follow given path
    public void animateCar(Car car, Group root, List<Car> allCars) {
        //create part one of the journey
        //create path transition
        PathTransition path1 = new PathTransition();
        path1.setDuration(Duration.millis(2500));
        //allow car to rotate if it makes a turn
        path1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //add the car to the path
        path1.setNode(car);
        //set the car's path transition
        car.setPathTransition(path1);
        //set the path to the first part of the journey
        path1.setPath(car.getJourney().getStartToStop());


        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!car.getHasCrashed()) {
                    checkCollisions(allCars);
                } else {
                    Car crashedCar = car.getCrashedCar();
                    if (!(car.getBoundsInParent().intersects(crashedCar.getBoundsInParent()))) {
                        car.getPathTransition().playFrom(car.getCrashDuration());
                        car.setHasCrashed(false);
                    }
                }
            }
        };
        animationTimer.start();
        //play the animation
        path1.play();
        //called when car gets to the junction
        path1.setOnFinished(actionEvent -> {
            //start an animation timer to check the traffic light
            AnimationTimer animationTimer2 = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    //check the traffic light
                    TrafficLight trafficLight = car.getJourney().getTrafficLight();
                    Boolean isSafe = trafficLight.getIsSafe();
                    //if it's safe
                    if (isSafe) {
                        //remove the car from the waiting list of the traffic light\
                        trafficLight.removeCar();
                        //stop the animation timer
                        this.stop();
                        //start the car going on the second path
                        PathTransition path2 = new PathTransition();
                        path2.setDuration(Duration.millis(2500));
                        //allow car to rotate if it makes a turn
                        path2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        //add the car to the path
                        path2.setNode(car);
                        //set the path to the second part of the journey
                        path2.setPath(car.getJourney().getStopToEnd());
                        //play the animation
                        path2.play();
                        //when the animation is finished remove the car
                        path2.setOnFinished(actionEvent -> {
                            root.getChildren().remove(car);
                            allCars.remove(car);
                        });
                    }
                }
            };
            animationTimer2.start();
        });
    }

    //chooses a random path for car to follow, spawns car, makes car follow given path
    public void addCarButtonEvent(Group root, List<Car> allCars, TrafficLight trafficLightA, TrafficLight trafficLightB, TrafficLight trafficLightC, TrafficLight trafficLightD, CrossJunction crossJunction) {
        //choose random path (0-5)
        Random random = new Random();
        int pathNumber = random.nextInt(12) + 6;
        //creates a new car
        Car car = new Car(100);
        car.getJourneyFromPathNumberCrossJunction(pathNumber);
        allCars.add(car);
        //gives the journey the relevant traffic light and tell the traffic light a car is waiting
        setTrafficLight(car, trafficLightA, trafficLightB, trafficLightC, trafficLightD);
        //add the car to the scene
        root.getChildren().addAll(car);
        //animate the car
        animateCar(car, root, allCars);
    }

    //sets the traffic light for the journey
    public void setTrafficLight(Car car, TrafficLight trafficLightA, TrafficLight trafficLightB, TrafficLight trafficLightC, TrafficLight trafficLightD) {
        String startPoint = car.getJourneyStartPointCross();
        switch (startPoint) {
            case "a":
                car.journey.setTrafficLight(trafficLightA);
                trafficLightA.addCarsWaiting();
                break;
            case "b":
                car.journey.setTrafficLight(trafficLightB);
                trafficLightB.addCarsWaiting();
                break;
            case "c":
                car.journey.setTrafficLight(trafficLightC);
                trafficLightC.addCarsWaiting();
                break;
            case "d":
                car.journey.setTrafficLight(trafficLightD);
                trafficLightD.addCarsWaiting();
                break;
        }
    }

    //handles collisions
    public void checkCollisions(List<Car> allCars) {
        int allCarsSize = allCars.size();

        //iterate through all cars
        for (Car car1 : allCars) {
            //get the index of the next car after the car we're checking,
            // this stops us comparing against cars we've already checked
            int nextCarIndex = allCars.indexOf(car1) + 1;
            //check against all the other cars
            for (; nextCarIndex < allCarsSize; nextCarIndex++) {
                Car car2 = allCars.get(nextCarIndex);

                if (car1.getBoundsInParent().intersects(car2.getBoundsInParent())) {
                    car2.setHasCrashed(true);
                    car2.setCrashedCar(car1);
                    PathTransition pathTransition = car2.getPathTransition();
                    car2.setCrashDuration(pathTransition.getCurrentTime());
                    pathTransition.pause();
                }
            }
        }
    }
}

