import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLight extends Circle {

    Boolean isSafe;
    public TrafficLight(int centreX, int centreY){
        //by default traffic light is red
        this.setCenterX(centreX);
        this.setCenterY(centreY);
        this.setRadius(25);
        this.setFill(Color.RED);
        this.setStroke(Color.BLACK);
        this.isSafe = false;
    }

    public void setGo(){
        this.setFill(Color.GREEN);
        this.setIsSafe(true);
    }

    public void setStop(){
        this.setFill(Color.RED);
        this.setIsSafe(false);
    }

    //RED isSafe = false
    //GREEN isSafe = true
    public Boolean getIsSafe(){
        return this.isSafe;
    }

    //RED isSafe = false
    //GREEN isSafe = true
    public void setIsSafe(Boolean isSafe){
        this.isSafe = isSafe;
    }



}
