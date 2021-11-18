import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;

import java.awt.*;

public class Journey {
    private final Point start;
    private Point end;
    private Point stop;
    private Point turn;
    private TrafficLight trafficLight;

    private Polyline startToStop;
    private Polyline stopToEnd;

    public Journey(Point start, Point stop, Point turn, Point end) {
        this.start = start;
        this.startToStop = new Polyline(start.getX(), start.getY(), stop.getX(), stop.getY());
        this.stopToEnd = new Polyline(stop.getX(), stop.getY(), turn.getX(), turn.getY(), end.getX(), end.getY());
    }


    public String getJourneyStartPointTJunction(TJunction tJunction) {
        Point a = tJunction.getA();
        Point b = tJunction.getB();
        Point c = tJunction.getC();

        if (start.equals(a)) {
            return "a";
        } else if (start.equals(b)) {
            return "b";
        } else {
            return "c";
        }
    }

    public String getJourneyStartPointCross() {
        Point a = new Point(-100, 410);
        Point b = new Point(990, -100);
        Point c = new Point(2000, 490);
        Point d = new Point(910, 1000);

        if (start.equals(a)) {
            return "a";
        } else if (start.equals(b)) {
            return "b";
        } else if (start.equals(c)){
            return "c";
        } else {
            return "d";
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

    public Point getStart() {
        return start;
    }

    public String getJourneyAsString(){
        return start.toString() + " " + stop.toString();
    }
}
