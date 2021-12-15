import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;


public class Car extends StackPane {
    public Journey journey;

    //image from https://www.freeiconspng.com/downloadimg/34859#google_vignette
    private final Image carImage = new Image("car.png");
    private PathTransition pathTransition;
    private Boolean hasCrashed = false;
    private Car crashedCar;
    private Duration crashDuration;
    private TJunction tJunction;
    private CrossJunction crossJunction;
    boolean isAdded = false;

    private String currentPath;


    public Car(double carSize, List<Car> allCars) {
        Rectangle car = createCar(carSize);
        Rectangle bounds = createBounds(carSize);
        this.getChildren().addAll(bounds, car);
        double x = -100;
        double y = -100;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.collisionHandler(allCars);
    }

    /**
     * Creates a rectangle that will act as the bounds around the car that will be used for collision detection
     *
     * @param carSize the variable based on laneWidth used to calculate the size of the car bounds
     */
    private Rectangle createBounds(double carSize) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(5);
        rectangle.setWidth(carSize * 1.5);
        rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }

    /**
     * Creates a rectangle that will act as a car and fills it with an image to make it look like a real car
     *
     * @param carSize the variable based on laneWidth used to calculate the size of the car
     */
    private Rectangle createCar(double carSize) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(carSize / 2);
        rectangle.setWidth(carSize);
        rectangle.setFill(new ImagePattern(carImage));
        return rectangle;
    }

    public Journey getJourney() {
        return journey;
    }

    public PathTransition getPathTransition() {
        return this.pathTransition;
    }

    public void setPathTransition(PathTransition pathTransition) {
        this.pathTransition = pathTransition;
    }

    public String getJourneyStartPointTJunction() {
        return this.journey.getJourneyStartPointTJunction(tJunction);
    }

    public String getJourneyStartPointCross() {
        return this.journey.getJourneyStartPointCross(crossJunction);
    }

    /**
     * Sets a car its journey within a T-Junction
     *
     * @param pathNumber the random integer between 0 and 5 that decides what journey the car gets
     */
    public void setJourneyTJunction(int pathNumber) {
        switch (pathNumber) {
            case 0:
                this.journey = tJunction.getAcJourney();
                break;
            case 1:
                this.journey = tJunction.getAdJourney();
                break;
            case 2:
                this.journey = tJunction.getCaJourney();
                break;
            case 3:
                this.journey = tJunction.getCdJourney();
                break;
            case 4:
                this.journey = tJunction.getDaJourney();
                break;
            case 5:
                this.journey = tJunction.getDcJourney();
        }
    }

    /**
     * Sets a car its journey within a cross junction
     *
     * @param pathNumber the random integer between 0 and 11 that decides what journey the car gets
     */
    public void setJourneyCrossJunction(int pathNumber) {
        switch (pathNumber) {
            case 0:
                this.journey = crossJunction.getAbJourney();
                break;
            case 1:
                this.journey = crossJunction.getAdJourney();
                break;
            case 2:
                this.journey = crossJunction.getAcJourney();
                break;
            case 3:
                this.journey = crossJunction.getBaJourney();
                break;
            case 4:
                this.journey = crossJunction.getBcJourney();
                break;
            case 5:
                this.journey = crossJunction.getBdJourney();
                break;
            case 6:
                this.journey = crossJunction.getCaJourney();
                break;
            case 7:
                this.journey = crossJunction.getCbJourney();
                break;
            case 8:
                this.journey = crossJunction.getCdJourney();
                break;
            case 9:
                this.journey = crossJunction.getDbJourney();
                break;
            case 10:
                this.journey = crossJunction.getDaJourney();
                break;
            case 11:
                this.journey = crossJunction.getDcJourney();
                break;
        }
    }

    public Boolean getHasCrashed() {
        return hasCrashed;
    }

    public void setHasCrashed(Boolean hasCrashed) {
        this.hasCrashed = hasCrashed;

    }

    public Car getCrashedCar() {
        return this.crashedCar;
    }

    public void setCrashedCar(Car car) {
        this.crashedCar = car;
    }

    public Duration getCrashDuration() {
        return crashDuration;
    }

    public void setCrashDuration(Duration crashDuration) {
        this.crashDuration = crashDuration;
    }

    public void setTJunction(TJunction tJunction) {
        this.tJunction = tJunction;
    }

    public void setCrossJunction(CrossJunction crossJunction) {
        this.crossJunction = crossJunction;
    }

    public boolean getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(boolean added) {
        isAdded = added;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    /**
     * Checks every frame to see if car.hasCrashed is set to false. If it is, it checks to see if the car is currently
     * colliding with another car by calling checkCollisions(allCars). If car.hasCrashed is set to true, it checks to
     * see if the car is still crashing into another car. If not, then the car is told to continue its path from where
     * it stopped and car.hasCrashed is set to false.
     *
     * @param allCars the list of all cars currently on the map
     */
    public void collisionHandler(List<Car> allCars) {
        Car car = this;
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
    }

    /**
     * Checks every car to see if it is currently colliding with any other car in the list allCars. The car
     * that is crashing into another car is car2 and the car being crashed into is car1. If they are colliding,
     * car2.hasCrashed is set to true and car2.crashedCar is set to car1. It is also noted where in car2's path it
     * crashed and this is saved to car2.crashDuration. Car2's path is then paused.
     *
     * @param allCars the list of all cars currently on the map
     */
    private void checkCollisions(List<Car> allCars) {
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
