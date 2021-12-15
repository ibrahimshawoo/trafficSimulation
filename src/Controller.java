import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    private double frameRate = 0;
    private double addCarTimer = 0;
    /*All calculations regarding the visual elements are based upon the constant lane width
     e.g. the size of the car is a proportion of the lane width and the length of the roads is
     a multiple of lane width. */
    private final int laneWidth = 45;
    private final Object[][] grid = new Object[3][2];
    private final List<Car> allCars = new ArrayList<>();
    private int counter = 0;

    public Controller(){}
    /**
     * Calculates the frame rate of the system the animation is playing in
     * https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-getting-the-frame-rate-of-a-javafx-application
     */
    public void calculateFrameRate() {
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
                    double calculated = Math.round(1_000_000_000.0 / elapsedNanosPerFrame);
                    if (calculated % 2 == 0) {
                        frameRate = calculated + 1;
                    } else {
                        frameRate = calculated;
                    }
                }
                counter = counter + 1;
                if (counter >= 120) {
                    this.stop();
                }
            }
        };
        frameRateMeter.start();
    }

    /**
     * Sets up the grid of junctions as well as starting the traffic lights and starts the spawning of cars
     *
     * @param root the Group to which all assets of the animation must be placed to be added and visible during the animation
     */
    public void setUp(Group root) {

        int tileWidth = laneWidth * 12;

        /* iterate through the array to add the appropriate junctions. Edges are t-junctions and
        all other tiles are cross junctions */
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                /*calculate the centre of the tile. The creation of the junction is based around the
                centre point */
                double xPos = (0.5 * tileWidth) + (i * tileWidth);
                double yPos = (0.5 * tileWidth) + (j * tileWidth);
                Point newCenter = new Point((int) xPos, (int) yPos);
                //if the position in the array is the corners create a cross junction
                if (i == 0 && j == 0 || i == grid.length - 1 && j == grid[i].length - 1 || i == 0 && j == grid[i].length - 1 || i == grid.length - 1 && j == 0) {
                    int finalI = i;
                    int finalJ = j;
                    /*dependent on which corner, set the rotation. This is so that the cars spawn in
                    the correct place */
                    if (i == 0 && j == 0) {
                        grid[i][j] = new CrossJunction(newCenter, root, laneWidth, 0);
                    } else if (i == 0 && j == grid[i].length - 1) {
                        grid[i][j] = new CrossJunction(newCenter, root, laneWidth, 270);
                    } else if (i == grid.length - 1 && j == 0) {
                        grid[i][j] = new CrossJunction(newCenter, root, laneWidth, 90);
                    } else if (i == grid.length - 1 && j == grid[i].length - 1) {
                        grid[i][j] = new CrossJunction(newCenter, root, laneWidth, 180);
                    }
                    //as this is a corner tile this is entry point, so spawn cars from here
                    //also start the traffic lights
                    AnimationTimer CrossJunctionAnimationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {

                            spawnCars(root, allCars, (CrossJunction) grid[finalI][finalJ]);
                            ((CrossJunction) grid[finalI][finalJ]).trafficLightController(frameRate);
                        }
                    };
                    CrossJunctionAnimationTimer.start();
                } else {
                    /*If the position in array is not the corners or the edge, create a cross junction.
                    Only start traffic lights. No need to rotate. Do not spawn cars */
                    if (i != grid.length - 1 && j != grid[i].length - 1 && i != 0 && j != 0) {
                        grid[i][j] = new CrossJunction(newCenter, root, laneWidth, 0);
                        int finalI = i;
                        int finalJ = j;
                        AnimationTimer CrossJunctionAnimationTimer = new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                ((CrossJunction) grid[finalI][finalJ]).trafficLightController(frameRate);
                            }
                        };
                        CrossJunctionAnimationTimer.start();
                        /* if the position in the array is the edge, create a t-junction. Rotate to
                        correct orientation and start the traffic lights */
                    } else {
                        if (i == 0) {
                            grid[i][j] = new TJunction(newCenter, root, laneWidth, 270);
                        } else if (j == grid[i].length - 1) {
                            grid[i][j] = new TJunction(newCenter, root, laneWidth, 180);
                        } else if (i == grid.length - 1) {
                            grid[i][j] = new TJunction(newCenter, root, laneWidth, 90);
                        } else if (j == 0) {
                            grid[i][j] = new TJunction(newCenter, root, laneWidth, 0);
                        }
                        int finalI1 = i;
                        int finalJ1 = j;
                        AnimationTimer TJunctionAnimationTimer = new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                ((TJunction) grid[finalI1][finalJ1]).trafficLightController(frameRate);
                            }
                        };
                        TJunctionAnimationTimer.start();
                    }
                }
            }
        }
    }

    /**
     * This function is called within an animation timer, so once in each frame
     * The actual body of the function is only executed every a certain number of frames
     * @param root the Group to which all assets of the animation must be placed to be added and visible during the animation
     * @param allCars the list of all cars in the animation
     * @param crossJunction the cross junction into which the cars are being spawned into
     */
    public void spawnCars(Group root, List<Car> allCars, CrossJunction crossJunction) {
        addCarTimer = addCarTimer + 1;
        double addCarTimerDivided = addCarTimer / Math.round(frameRate * 5);
        if ((addCarTimerDivided == Math.floor(addCarTimerDivided)) && !Double.isInfinite(addCarTimerDivided)) {
            //choose random path through the initial cross junction
            Random random = new Random();
            int pathNumber = random.nextInt(6);
            //create a new car
            Car car = new Car(laneWidth, allCars);
            car.setCrossJunction(crossJunction);
            car.setJourneyCrossJunction(pathNumber);
            allCars.add(car);
            //give the journey the relevant traffic light
            setTrafficLightCrossJunction(car, crossJunction.getTrafficLightA(), crossJunction.getTrafficLightB(), crossJunction.getTrafficLightC(), crossJunction.getTrafficLightD());
            //add the car to the scene
            root.getChildren().addAll(car);
            //animate the car
            animateCar(car, root);
        }
    }

    /**
     * This function is responsible for animating the car through the grid of junctions
     * It first animates the first path (from the entry point to the traffic lights) it then calls the
     * method runPath2() which animates the second path (from the traffic lights to the exit point)
     * runPath2() also calls animateCar() when the car reaches the exit point so that the car can traverse
     * the next junction
     * @param car the car that is to be animated
     * @param root the Group to which all assets of the animation must be placed to be added and
     *             visible during the animation
     */
    public void animateCar(Car car, Group root) {
        //create path transition
        PathTransition path1 = new PathTransition();
        //set the duration of the transition, this determines the cars speed
        path1.setDuration(Duration.millis(1660));
        //allow car to rotate if it makes a turn
        path1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //add the car to the path
        path1.setNode(car);
        //set the car's path transition
        car.setPathTransition(path1);
        //set the path to the first part of the journey
        path1.setPath(car.getJourney().getStartToStop());
        //tell the car it is on path 1
        car.setCurrentPath("path1");
        //play the animation
        path1.play();

        //called when car gets to the traffic light (end of path 1)
        path1.setOnFinished(actionEvent -> {
            //get the traffic light the car is waiting at
            TrafficLight trafficLight = car.getJourney().getTrafficLight();
            //each frame check the status of the traffic light
            AnimationTimer animationTimer2 = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    //check the traffic light, if it's not safe check on the next frame
                    Boolean isSafe = trafficLight.getIsSafe();
                    //if it's safe
                    if (isSafe) {
                        //stop checking the traffic light
                        this.stop();
                        runPath2(trafficLight, car, root);
                    }
                }
            };
            animationTimer2.start();
        });
    }

    /**
     * This function animates the car through the second path (from the traffic lights to the exit point
     * of the junction). Once the second path is finished, the journey through the next junction must be
     * calculated. It must be determined which jun
     * @param trafficLight the traffic light that the car is waiting at
     * @param car the car that is to be animated
     * @param root the Group to which all assets of the animation must be placed to be added and
     *             visible during the animation
     */
    public void runPath2(TrafficLight trafficLight, Car car, Group root) {
        //remove the car from the waiting list of the traffic light
        trafficLight.removeCar();
        car.setCurrentPath("path2");
        //start the car going on the second path
        PathTransition path2 = new PathTransition();
        path2.setDuration(Duration.millis(2500));
        //allow car to rotate if it makes a turn
        path2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //add the car to the path
        path2.setNode(car);
        //set the path to the second part of the journey
        path2.setPath(car.getJourney().getStopToEnd());
        car.setPathTransition(path2);
        car.setIsAdded(false);
        //play the animation
        path2.play();

        //when the animation is finished remove the car
        path2.setOnFinished(actionEvent -> {

            Random random = new Random();
            Point endPoint = car.getJourney().getEnd();
            for (int k = 0; k < grid.length; k++) {
                for (int l = 0; l < grid[k].length; l++) {
                    //corners (cross junctions)
                    int newPathNumber;
                    if (k == 0 && l == 0 || k == grid.length - 1 && l == grid[k].length - 1 || k == 0 && l == grid[k].length - 1 || k == grid.length - 1 && l == 0) {
                        CrossJunction currentCrossJunction = (CrossJunction) grid[k][l];
                        //finding start point of the next journey
                        if (endPoint.equals(currentCrossJunction.getAStart())) {
                            car.setTJunction(null);
                            newPathNumber = random.nextInt(3);
                            //setting next junction
                            car.setCrossJunction(currentCrossJunction);
                            //setting cars new journey
                            car.setJourneyCrossJunction(newPathNumber);

                            setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                            animateCar(car, root);
                        } else if (endPoint.equals(currentCrossJunction.getBStart())) {
                            car.setTJunction(null);
                            newPathNumber = random.nextInt(3) + 3;
                            car.setCrossJunction(currentCrossJunction);
                            car.setJourneyCrossJunction(newPathNumber);

                            setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                            animateCar(car, root);
                        } else if (endPoint.equals(currentCrossJunction.getCStart())) {
                            car.setTJunction(null);
                            newPathNumber = random.nextInt(3) + 6;
                            car.setCrossJunction(currentCrossJunction);
                            car.setJourneyCrossJunction(newPathNumber);

                            setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                            animateCar(car, root);
                        } else if (endPoint.equals(currentCrossJunction.getDStart())) {
                            car.setTJunction(null);
                            newPathNumber = random.nextInt(3) + 9;
                            car.setCrossJunction(currentCrossJunction);
                            car.setJourneyCrossJunction(newPathNumber);

                            setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                            animateCar(car, root);
                        }
                        if (endPoint == currentCrossJunction.getAEnd() || endPoint == currentCrossJunction.getBEnd()) {
                            root.getChildren().remove(car);
                            allCars.remove(car);
                        }
                    } else {
                        //cross junctions in the middle
                        if (k != grid.length - 1 && l != grid[k].length - 1 && k != 0 && l != 0) {
                            CrossJunction currentCrossJunction = (CrossJunction) grid[k][l];
                            if (endPoint.equals(currentCrossJunction.getAStart())) {
                                car.setTJunction(null);
                                newPathNumber = random.nextInt(2);
                                car.setCrossJunction(currentCrossJunction);
                                car.setJourneyCrossJunction(newPathNumber);

                                setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                                animateCar(car, root);
                            } else if (endPoint.equals(currentCrossJunction.getBStart())) {
                                car.setTJunction(null);
                                newPathNumber = random.nextInt(2) + 3;
                                car.setCrossJunction(currentCrossJunction);
                                car.setJourneyCrossJunction(newPathNumber);

                                setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                                animateCar(car, root);
                            } else if (endPoint.equals(currentCrossJunction.getCStart())) {
                                car.setTJunction(null);
                                newPathNumber = random.nextInt(2) + 6;
                                car.setCrossJunction(currentCrossJunction);
                                car.setJourneyCrossJunction(newPathNumber);

                                setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                                animateCar(car, root);
                            } else if (endPoint.equals(currentCrossJunction.getDStart())) {
                                car.setTJunction(null);
                                newPathNumber = random.nextInt(2) + 9;
                                car.setCrossJunction(currentCrossJunction);
                                car.setJourneyCrossJunction(newPathNumber);

                                setTrafficLightCrossJunction(car, currentCrossJunction.getTrafficLightA(), currentCrossJunction.getTrafficLightB(), currentCrossJunction.getTrafficLightC(), currentCrossJunction.getTrafficLightD());

                                animateCar(car, root);
                            }
                        } else {
                            //t junctions
                            TJunction currentTJunction = (TJunction) grid[k][l];
                            if (endPoint.equals(currentTJunction.getAStart())) {
                                car.setCrossJunction(null);
                                newPathNumber = random.nextInt(2);
                                car.setTJunction(currentTJunction);
                                car.setJourneyTJunction(newPathNumber);

                                setTrafficLightTJunction(car, currentTJunction.getTrafficLightA(), currentTJunction.getTrafficLightC(), currentTJunction.getTrafficLightD());

                                animateCar(car, root);
                            } else if (endPoint.equals(currentTJunction.getCStart())) {
                                car.setCrossJunction(null);
                                newPathNumber = random.nextInt(2) + 2;
                                car.setTJunction(currentTJunction);
                                car.setJourneyTJunction(newPathNumber);

                                setTrafficLightTJunction(car, currentTJunction.getTrafficLightA(), currentTJunction.getTrafficLightC(), currentTJunction.getTrafficLightD());

                                animateCar(car, root);
                            } else if (endPoint.equals(currentTJunction.getDStart())) {
                                car.setCrossJunction(null);
                                newPathNumber = random.nextInt(2) + 4;
                                car.setTJunction(currentTJunction);
                                car.setJourneyTJunction(newPathNumber);

                                setTrafficLightTJunction(car, currentTJunction.getTrafficLightA(), currentTJunction.getTrafficLightC(), currentTJunction.getTrafficLightD());

                                animateCar(car, root);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Assigns a car its specific traffic light in a cross junction
     * Calls Journey.setTrafficLight() which tells the traffic light an extra car is now waiting to go through it
     *
     * @param trafficLightA traffic light A
     * @param trafficLightB traffic light B
     * @param trafficLightC traffic light C
     * @param trafficLightD traffic light D
     * @param car the car that is being assigned a traffic light
     */
    public void setTrafficLightCrossJunction(Car car, TrafficLight trafficLightA, TrafficLight trafficLightB, TrafficLight trafficLightC, TrafficLight trafficLightD) {
        String startPoint = car.getJourneyStartPointCross();
        switch (startPoint) {
            case "a":
                car.journey.setTrafficLight(trafficLightA, car);
                break;
            case "b":
                car.journey.setTrafficLight(trafficLightB, car);
                break;
            case "c":
                car.journey.setTrafficLight(trafficLightC, car);
                break;
            case "d":
                car.journey.setTrafficLight(trafficLightD, car);
                break;
        }
    }

    /**
     * Assigns a car its specific traffic light in a T-Junction
     * Calls Journey.setTrafficLight() which tells the traffic light an extra car is now waiting to go through it
     *
     * @param trafficLightA traffic light A
     * @param trafficLightC traffic light C
     * @param trafficLightD traffic light D
     * @param car the car that is being assigned a traffic light
     */
    public void setTrafficLightTJunction(Car car, TrafficLight trafficLightA, TrafficLight trafficLightC, TrafficLight trafficLightD) {
        String startPoint = car.getJourneyStartPointTJunction();
        switch (startPoint) {
            case "a":
                car.journey.setTrafficLight(trafficLightA, car);
                break;
            case "c":
                car.journey.setTrafficLight(trafficLightC, car);
                break;
            case "d":
                car.journey.setTrafficLight(trafficLightD, car);
        }
    }

}
