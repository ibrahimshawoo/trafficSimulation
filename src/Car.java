import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;


public class Car extends StackPane {

    private Point crossa = new Point(-100, 410);
    private Point crossja = new Point(800, 410);
    private Point crossa2 = new Point(-100, 490);
    private Point crossja2 = new Point(800, 490);

    private Point crossb = new Point(910, -100);
    private Point crossjb = new Point(910, 300);
    private Point crossb2 = new Point(990, -100);
    private Point crossjb2 = new Point(990, 300);

    private Point crossc = new Point(2000, 410);
    private Point crossjc = new Point(1100, 410);
    private Point crossc2 = new Point(2000, 490);
    private Point crossjc2 = new Point(1100, 490);

    private Point crossd = new Point(910, 1000);
    private Point crossjd = new Point(910, 600);
    private Point crossd2 = new Point(990, 1000);
    private Point crossjd2 = new Point(990, 600);

    private Point crossturn1 = new Point(910, 410);
    private Point crossturn2 = new Point(990, 410);
    private Point crossturn3 = new Point(990, 490);
    private Point crossturn4 = new Point(910, 490);

    private Journey crossabJourney = new Journey(crossa, crossja, crossturn1, crossb);
    private Journey crossbaJourney = new Journey(crossb2, crossjb2, crossturn3, crossa2);
    private Journey crossacJourney = new Journey(crossa, crossja, crossturn2, crossc);
    private Journey crosscaJourney = new Journey(crossc2, crossjc2, crossturn3, crossa2);
    private Journey crossadJourney = new Journey(crossa, crossja, crossturn2, crossd2);
    private Journey crossdaJourney = new Journey(crossd, crossjd, crossturn4, crossa2);
    private Journey crossbcJourney = new Journey(crossb2, crossjb2, crossturn2, crossc);
    private Journey crosscbJourney = new Journey(crossc2, crossjc2, crossturn4, crossb);
    private Journey crossbdJourney = new Journey(crossb2, crossjb2, crossturn3, crossd2);
    private Journey crossdbJourney = new Journey(crossd, crossjd, crossturn4, crossb);
    private Journey crosscdJourney = new Journey(crossc2, crossjc2, crossturn3, crossd2);
    private Journey crossdcJourney = new Journey(crossd, crossjd, crossturn1, crossc);

    public Journey journey;


    private Rectangle car;
    private Rectangle bounds;
    //image from https://www.freeiconspng.com/downloadimg/34859#google_vignette
    private Image carImage = new Image("car.png");
    private PathTransition pathTransition;
    private Boolean hasCrashed = false;
    private Car crashedCar;
    private Duration crashDuration;

    private TJunction tJunction;
    private CrossJunction crossJunction;
    private String junctionType;


    public Car(int carSize) {
        this.car = createCar(carSize);
        this.bounds = createBounds();
        this.getChildren().addAll(bounds, car);
        double x = -100;
        double y = -100;
        this.setTranslateX(x);
        this.setTranslateY(y);
    }


    private Rectangle createBounds() {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setWidth(60);
        rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }

    private Rectangle createCar(int carSize) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(20*carSize);
        rectangle.setWidth(40*carSize);
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
        return this.journey.getJourneyStartPointCross();
    }

    public void setJourneyTJunction(int pathNumber) {
        System.out.println(pathNumber);
        switch (pathNumber) {
            case 0:
                System.out.println("case 0");
                this.journey = tJunction.getAbJourney();
                break;
            case 1:
                System.out.println("case 1");
                this.journey = tJunction.getBaJourney();
                break;
            case 2:
                System.out.println("case 2");
                this.journey = tJunction.getAcJourney();
                break;
            case 3:
                System.out.println("case 3");
                this.journey = tJunction.getBcJourney();
                break;
            case 4:
                System.out.println("case 4");
                this.journey = tJunction.getCaJourney();
                break;
            case 5:
                System.out.println("case 5");
                this.journey = tJunction.getCbJourney();
        }
    }

    public Journey getJourneyFromPathNumberCrossJunction(int pathNumber){
        switch (pathNumber){
            case 6:
                return crossabJourney;
            case 7:
                return crossbaJourney;
            case 8:
                return crossacJourney;
            case 9:
                return crosscaJourney;
            case 10:
                return crossadJourney;
            case 11:
                return crossdaJourney;
            case 12:
                return crossbcJourney;
            case 13:
                return crosscbJourney;
            case 14:
                return crossbdJourney;
            case 15:
                return crossdbJourney;
            case 16:
                return crosscdJourney;
            case 17:
                return crossdcJourney;
            default:
                return null;
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

    public TJunction getTJunction() {
        return tJunction;
    }

    public void setTJunction(TJunction tJunction) {
        this.tJunction = tJunction;
        this.junctionType = "T";
    }

    public CrossJunction getCrossJunction() {
        return crossJunction;
    }

    public void setCrossJunction(CrossJunction crossJunction) {
        this.crossJunction = crossJunction;
        this.junctionType = "C";
    }

    public String getJunctionType() {
        return junctionType;
    }

    public void setJunctionType(String junctionType) {
        this.junctionType = junctionType;
    }
}

