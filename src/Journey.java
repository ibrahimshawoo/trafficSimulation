import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;

import java.awt.*;

public class Journey {
    private Point start;
    private Point end;
    private Point stop;
    private Point turn;
    private TrafficLight trafficLight;

    private Polyline startToStop;
    private Polyline stopToEnd;
    public Journey (Point start, Point stop,Point turn, Point end){
        this.start = start;
        this.startToStop = new Polyline(start.getX(), start.getY(), stop.getX(), stop.getY());
        this.stopToEnd = new Polyline(stop.getX(),stop.getY(),turn.getX(),turn.getY(),end.getX(),end.getY());
    }

    public String getJourneyStartPoint(){
        Point a = new Point(0,150);
        Point b = new Point(1000,250);
        Point c = new Point(450,700);
        if(start.equals(a)){
            return "a";
        }else if(start.equals(b)){
            return "b";
        }else {
            return "c";
        }

    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public Polyline getStartToStop() {
        return startToStop;
    }

    private void setStartToStop(Polyline startToStop) {
        this.startToStop = startToStop;
    }

    public Polyline getStopToEnd() {
        return stopToEnd;
    }

    private void setStopToEnd(Polyline stopToEnd) {
        this.stopToEnd = stopToEnd;
    }
}
