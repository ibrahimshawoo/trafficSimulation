import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLight extends Circle {

    Boolean isSafe;
    int numberOfCarsWaiting;

    public TrafficLight(int centreX, int centreY, int laneWidth) {
        //by default traffic light is red
        this.setCenterX(centreX);
        this.setCenterY(centreY);
        this.setRadius(laneWidth / 4);
        this.setFill(Color.RED);
        this.setStroke(Color.BLACK);
        this.isSafe = false;
    }

    public void setGo() {
        this.setFill(Color.GREEN);
        this.setIsSafe(true);
    }

    public void setStop() {
        this.setFill(Color.RED);
        this.setIsSafe(false);
    }

    int counter = 0;

    /**
     * Sets the traffic light to stop for about half a second before setting it to go
     */
    public void setAmber() {
        TrafficLight trafficLight = this;

        AnimationTimer amberTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                trafficLight.setStop();
                counter++;
                if (counter == 40) {
                    this.stop();
                    trafficLight.setGo();
                    counter = 0;
                }
            }
        };
        amberTimer.start();

    }

    //RED isSafe = false
    //GREEN isSafe = true
    public Boolean getIsSafe() {
        return this.isSafe;
    }

    //RED isSafe = false
    //GREEN isSafe = true
    public void setIsSafe(Boolean isSafe) {
        this.isSafe = isSafe;
    }

    public void addCarsWaiting() {
        this.numberOfCarsWaiting += 1;
    }

    public int getNumberOfCarsWaiting() {
        return this.numberOfCarsWaiting;
    }

    public void removeCar() {
        this.numberOfCarsWaiting -= 1;
    }
}
