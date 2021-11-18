import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.awt.*;
import java.util.*;
import java.util.List;

public class Main extends Application {
    final long[] frameTimes = new long[100];
    int frameTimeIndex = 0 ;
    boolean arrayFilled = false ;
    double frameRate = 0;
    double addCarTimer = 0;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //set up scene
        Group root = new Group();
        Scene scene = new Scene(root);
        //the centre of the junction is the centre of where to two roads meet
        Point centre = new Point(480,160);
        //create a draw a TJunction with the centre at the centre point
        TJunction tJunction = new TJunction(centre,root);
        List<Car> allCars = new ArrayList<>();

        //calculate frame rate https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-getting-the-frame-rate-of-a-javafx-application
        AnimationTimer frameRateMeter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                }
            }
        };

        frameRateMeter.start();

        //spawn cars
        AnimationTimer addCarAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                addCarTimer = addCarTimer + 1;
                double addCarTimerDivided = addCarTimer / Math.round(frameRate)*0.5;
                if ((addCarTimerDivided == Math.floor(addCarTimerDivided)) && !Double.isInfinite(addCarTimerDivided)) {
                    addCar(root, allCars, tJunction);

                }
            }
        };
        addCarAnimationTimer.start();

        //control the traffic lights
        AnimationTimer TJunctionAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tJunction.trafficLightController(frameRate);
            }
        };
        TJunctionAnimationTimer.start();


        //show animation
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    //chooses a random path for car to follow, spawns car, makes car follow given path
    public void addCar(Group root, List<Car> allCars,TJunction tJunction) {
        //choose random path (0-5)
        Random random = new Random();
        int pathNumber = random.nextInt(5);
        //creates a new car
        int carSize = tJunction.getLaneWidth()/50;
        Car car = new Car(carSize);
        car.setTJunction(tJunction);
        String junctionType = car.getJunctionType();
        if (junctionType.equals("T")){
            car.setJourneyTJunction(pathNumber);
        }
        allCars.add(car);
        //gives the journey the relevant traffic light and tell the traffic light a car is waiting
        setTrafficLight(car, tJunction.getTrafficLightA(), tJunction.getTrafficLightB(), tJunction.getTrafficLightC());
        //add the car to the scene
        root.getChildren().addAll(car);
        //animate the car
        animateCar(car, root, allCars);
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
                        //remove the car from the waiting list of the traffic light
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



    //sets the traffic light for the journey
    public void setTrafficLight(Car car, TrafficLight trafficLightA, TrafficLight trafficLightB, TrafficLight trafficLightC) {
        String startPoint = car.getJourneyStartPointTJunction();
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

