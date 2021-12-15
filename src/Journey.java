import javafx.scene.shape.Polyline;

import java.awt.*;

public class Journey {
    private final Point start;
    private final Point end;
    private TrafficLight trafficLight;

    private final Polyline startToStop;
    private final Polyline stopToEnd;

    public Journey(Point start, Point stop, Point turn, Point end) {
        this.start = start;
        this.startToStop = new Polyline(start.getX(), start.getY(), stop.getX(), stop.getY());
        this.stopToEnd = new Polyline(stop.getX(), stop.getY(), turn.getX(), turn.getY(), end.getX(), end.getY());
        this.end = end;
    }

    public String getJourneyStartPointTJunction(TJunction tJunction) {
        Point a = tJunction.getAStart();
        Point c = tJunction.getCStart();
        Point d = tJunction.getDStart();

        if (start.equals(a)) {
            return "a";
        } else if (start.equals(c)) {
            return "c";
        } else if (start.equals(d)) {
            return "d";
        } else {
            return "error";
        }
    }

    public String getJourneyStartPointCross(CrossJunction crossJunction) {
        Point a = crossJunction.getAStart();
        Point b = crossJunction.getBStart();
        Point c = crossJunction.getCStart();

        if (start.equals(a)) {
            return "a";
        } else if (start.equals(b)) {
            return "b";
        } else if (start.equals(c)) {
            return "c";
        } else {
            return "d";
        }
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight, Car car) {
        this.trafficLight = trafficLight;
        if (!car.getIsAdded()) {
            trafficLight.addCarsWaiting();
            car.setIsAdded(true);
        }
    }

    public Polyline getStartToStop() {
        return startToStop;
    }

    public Polyline getStopToEnd() {
        return stopToEnd;
    }

    public Point getEnd() {
        return this.end;
    }
}
