import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TJunction {
    private TrafficLight trafficLightA, trafficLightC, trafficLightD;
    private double trafficLightTimer = 0;
    private final Point centre;
    private final Point a = new Point(0, 0);
    private final Point a2 = new Point(0, 0);
    private final Point ja = new Point(0, 0);
    private final Point c = new Point(0, 0);
    private final Point c2 = new Point(0, 0);
    private final Point jc = new Point(0, 0);
    private final Point d = new Point(0, 0);
    private final Point d2 = new Point(0, 0);
    private final Point jd = new Point(0, 0);
    private final Point turn1 = new Point(0, 0);
    private final Point turn2 = new Point(0, 0);
    private final Point turn3 = new Point(0, 0);
    private final Point turn4 = new Point(0, 0);
    private Journey acJourney = new Journey(a, ja, turn1, c);
    private Journey caJourney = new Journey(c2, jc, turn4, a2);
    private Journey adJourney = new Journey(a, ja, turn2, d2);
    private Journey cdJourney = new Journey(c2, jc, turn4, d2);
    private Journey daJourney = new Journey(d, jd, turn3, a2);
    private Journey dcJourney = new Journey(d, jd, turn1, c);
    private final int laneWidth;
    private final Group tJunctionGroup = new Group();
    private final List<Point> pointList = new ArrayList<>();
    private final List<Line> lineList = new ArrayList<>();
    private final List<TrafficLight> trafficLightList = new ArrayList<>();

    public TJunction(Point centre, Group root, int laneWidth, int rotation) {
        this.laneWidth = laneWidth;
        this.centre = centre;
        definePoints(centre);
        drawTJunction(root);
        rotate(rotation);
        setUpJourneys();
    }

    public void drawTJunction(Group root) {
        int lineWidth = 3;
        double centreX = centre.getX();
        double centreY = centre.getY();
        Line roadTop = new Line(centreX - laneWidth * 6, centreY - laneWidth, centreX + laneWidth * 6, centreY - laneWidth);
        roadTop.setStrokeWidth(lineWidth);
        Line horizontalDash = new Line(centreX - laneWidth * 6, centreY, centreX + laneWidth * 6, centreY);
        horizontalDash.setStrokeWidth(lineWidth);
        horizontalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadBottomLeft = new Line(centreX - laneWidth * 6, centreY + laneWidth, centreX - laneWidth, centreY + laneWidth);
        roadBottomLeft.setStrokeWidth(lineWidth);
        Line roadBottomRight = new Line(centreX + laneWidth, centreY + laneWidth, centreX + laneWidth * 6, centreY + laneWidth);
        roadBottomRight.setStrokeWidth(lineWidth);
        Line roadLeft = new Line(centreX - laneWidth, centreY + laneWidth, centreX - laneWidth, centreY + laneWidth * 6);
        roadLeft.setStrokeWidth(lineWidth);
        Line verticalDash = new Line(centreX, centreY + laneWidth, centreX, centreY + laneWidth * 6);
        verticalDash.setStrokeWidth(lineWidth);
        verticalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadRight = new Line(centreX + laneWidth, centreY + laneWidth, centreX + laneWidth, centreY + laneWidth * 6);
        roadRight.setStrokeWidth(lineWidth);

        lineList.add(roadTop);
        lineList.add(horizontalDash);
        lineList.add(roadBottomLeft);
        lineList.add(roadBottomRight);
        lineList.add(roadLeft);
        lineList.add(roadRight);
        lineList.add(verticalDash);

        int halfLaneWidth = laneWidth / 2;
        this.trafficLightA = new TrafficLight(centre.x - 3 * halfLaneWidth, centre.y - halfLaneWidth * 3, laneWidth);
        this.trafficLightC = new TrafficLight(centre.x + 3 * halfLaneWidth, centre.y + halfLaneWidth * 3, laneWidth);
        this.trafficLightD = new TrafficLight(centre.x - halfLaneWidth * 3, centre.y + 3 * halfLaneWidth, laneWidth);
        this.trafficLightA.setGo();

        trafficLightList.add(trafficLightA);
        trafficLightList.add(trafficLightC);
        trafficLightList.add(trafficLightD);

        tJunctionGroup.getChildren().addAll(roadTop,
                horizontalDash,
                roadBottomLeft,
                roadBottomRight,
                roadLeft,
                verticalDash,
                roadRight,
                trafficLightA,
                trafficLightC,
                trafficLightD);

        root.getChildren().addAll(tJunctionGroup);
    }

    /**
     * Defines all points in a T-Junction based off the centre point of the tile
     * Adds all points to pointList so that they can be rotated if needed
     *
     * @param centre the centre point of the tile that is used to calculate the position of everything in the T-Junction
     */
    public void definePoints(Point centre) {
        //the centre of the lane is half a lane width
        int centreLane = laneWidth / 2;
        double centreX = centre.getX();
        double centreY = centre.getY();
        this.a.setLocation(centreX - laneWidth * 6, centreY - centreLane);
        this.a2.setLocation(centreX - laneWidth * 6, centreY + centreLane);
        this.ja.setLocation(centreX - (laneWidth + centreLane * 2), centreY - centreLane);
        this.c.setLocation(centreX + laneWidth * 6, centreY - centreLane);
        this.c2.setLocation(centreX + laneWidth * 6, centreY + centreLane);
        this.jc.setLocation(centreX + (laneWidth + centreLane * 2), centreY + centreLane);
        this.d.setLocation(centreX - centreLane, centreY + laneWidth * 6);
        this.d2.setLocation(centreX + centreLane, centreY + laneWidth * 6);
        this.jd.setLocation(centreX - centreLane, centreY + (laneWidth + centreLane * 2));
        this.turn1.setLocation(centreX - centreLane, centreY - centreLane);
        this.turn2.setLocation(centreX + centreLane, centreY - centreLane);
        this.turn3.setLocation(centreX - centreLane, centreY + centreLane);
        this.turn4.setLocation(centreX + centreLane, centreY + centreLane);

        pointList.add(a);
        pointList.add(a2);
        pointList.add(ja);
        pointList.add(c);
        pointList.add(c2);
        pointList.add(jc);
        pointList.add(d);
        pointList.add(d2);
        pointList.add(jd);
        pointList.add(turn1);
        pointList.add(turn2);
        pointList.add(turn3);
        pointList.add(turn4);

    }

    /**
     * Rotates a T-Junction by however many degrees we would like
     *
     * @param rotation the degree of rotation desired
     */
    public void rotate(int rotation) {
        double radians = Math.toRadians(rotation);
        //rotate road
        for (Line line : lineList) {
            double newStartX = getCentreX() + (line.getStartX() - getCentreX()) * Math.cos(radians) - (line.getStartY() - getCentreY()) * Math.sin(radians);
            double newStartY = getCentreY() + (line.getStartX() - getCentreX()) * Math.sin(radians) + (line.getStartY() - getCentreY()) * Math.cos(radians);
            double newEndX = getCentreX() + (line.getEndX() - getCentreX()) * Math.cos(radians) - (line.getEndY() - getCentreY()) * Math.sin(radians);
            double newEndY = getCentreY() + (line.getEndX() - getCentreX()) * Math.sin(radians) + (line.getEndY() - getCentreY()) * Math.cos(radians);
            line.setStartX(newStartX);
            line.setStartY(newStartY);
            line.setEndX(newEndX);
            line.setEndY(newEndY);
        }

        //rotate traffic lights
        for (TrafficLight trafficLight : trafficLightList) {
            double newX = getCentreX() + (trafficLight.getCenterX() - getCentreX()) * Math.cos(radians) - (trafficLight.getCenterY() - getCentreY()) * Math.sin(radians);
            double newY = getCentreY() + (trafficLight.getCenterX() - getCentreX()) * Math.sin(radians) + (trafficLight.getCenterY() - getCentreY()) * Math.cos(radians);
            trafficLight.setCenterX(newX);
            trafficLight.setCenterY(newY);
        }
        //rotate the points
        for (Point point : pointList) {
            double newX = getCentreX() + (point.getX() - getCentreX()) * Math.cos(radians) - (point.getY() - getCentreY()) * Math.sin(radians);
            double newY = getCentreY() + (point.getX() - getCentreX()) * Math.sin(radians) + (point.getY() - getCentreY()) * Math.cos(radians);
            point.setLocation(newX, newY);
        }

    }

    /**
     * Pre-defines journeys through a T-Junction
     */
    public void setUpJourneys() {
        this.acJourney = new Journey(a, ja, turn1, c);
        this.caJourney = new Journey(c2, jc, turn4, a2);
        this.adJourney = new Journey(a, ja, turn2, d2);
        this.cdJourney = new Journey(c2, jc, turn4, d2);
        this.daJourney = new Journey(d, jd, turn3, a2);
        this.dcJourney = new Journey(d, jd, turn1, c);
    }

    /**
     * Calls changeLights() every four seconds
     *
     * @param frameRate the frameRate calculated by Controller.calculateFrameRate()
     */
    public void trafficLightController(double frameRate) {
        trafficLightTimer = trafficLightTimer + 1;
        double trafficLightTimerDivided = trafficLightTimer / (Math.round(frameRate * 4));
        if ((trafficLightTimerDivided == Math.floor(trafficLightTimerDivided)) && !Double.isInfinite(trafficLightTimerDivided)) {
            changeLights();
        }
    }

    /**
     * Checks which traffic light has the highest number of cars waiting then calls setAmber() for that traffic light
     * and setStop() for all other traffic lights at the junction
     */
    public void changeLights() {
        if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()) {
            //traffic light C has the most
            if (trafficLightC.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setAmber();
                }
            } else {//traffic light B has the most
                trafficLightA.setStop();
                trafficLightC.setAmber();
                trafficLightD.setStop();
            }
            //traffic light C has the most
        } else if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
            trafficLightA.setStop();
            trafficLightC.setStop();
            trafficLightD.setAmber();
        } else {//traffic light A has the most
            trafficLightA.setAmber();
            trafficLightC.setStop();
            trafficLightD.setStop();
        }
    }

    public Journey getAcJourney() {
        return this.acJourney;
    }

    public Journey getCaJourney() {
        return this.caJourney;
    }

    public Journey getAdJourney() {
        return this.adJourney;
    }

    public Journey getCdJourney() {
        return this.cdJourney;
    }

    public Journey getDaJourney() {
        return this.daJourney;
    }

    public Journey getDcJourney() {
        return this.dcJourney;
    }

    public TrafficLight getTrafficLightA() {
        return trafficLightA;
    }

    public TrafficLight getTrafficLightC() {
        return trafficLightC;
    }

    public TrafficLight getTrafficLightD() {
        return trafficLightD;
    }

    public Point getAStart() {
        return a;
    }

    public Point getCStart() {
        return c2;
    }

    public Point getDStart() {
        return d;
    }

    public int getCentreX() {
        return this.centre.x;
    }

    public int getCentreY() {
        return this.centre.y;
    }


}
