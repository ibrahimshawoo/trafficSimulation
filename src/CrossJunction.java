import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrossJunction {
    private TrafficLight trafficLightA, trafficLightB, trafficLightC, trafficLightD;
    private double trafficLightTimer = 0;
    private final Point centre;
    private final Point a = new Point(0, 0);
    private final Point a2 = new Point(0, 0);
    private final Point ja = new Point(0, 0);
    private final Point b = new Point(0, 0);
    private final Point b2 = new Point(0, 0);
    private final Point jb = new Point(0, 0);
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
    private Journey abJourney;
    private Journey acJourney;
    private Journey adJourney;
    private Journey bcJourney;
    private Journey bdJourney;
    private Journey baJourney;
    private Journey cdJourney;
    private Journey caJourney;
    private Journey cbJourney;
    private Journey daJourney;
    private Journey dbJourney;
    private Journey dcJourney;

    private final int laneWidth;
    private final List<Point> pointList = new ArrayList<>();
    private final List<Line> lineList = new ArrayList<>();
    private final List<TrafficLight> trafficLightList = new ArrayList<>();


    public CrossJunction(Point centre, Group root, int laneWidth, int rotation) {
        this.centre = centre;
        this.laneWidth = laneWidth;
        definePoints(centre);
        drawCrossJunction(root);
        rotate(rotation);
        setUpJourneys();
    }

    public void drawCrossJunction(Group root) {
        int lineWidth = 3;
        double centreX = centre.getX();
        double centreY = centre.getY();
        Line roadLeftTop = new Line(centreX - laneWidth * 6, centreY - laneWidth, centreX - laneWidth, centreY - laneWidth);
        roadLeftTop.setStrokeWidth(lineWidth);
        Line horizontalDashLeft = new Line(centreX - laneWidth * 6, centreY, centreX - laneWidth, centreY);
        horizontalDashLeft.setStrokeWidth(lineWidth);
        horizontalDashLeft.getStrokeDashArray().addAll(20d, 20d);
        Line roadLeftBottom = new Line(centreX - laneWidth * 6, centreY + laneWidth, centreX - laneWidth, centreY + laneWidth);
        roadLeftBottom.setStrokeWidth(lineWidth);

        Line roadRightTop = new Line(centreX + laneWidth * 6, centreY - laneWidth, centreX + laneWidth, centreY - laneWidth);
        roadRightTop.setStrokeWidth(lineWidth);
        Line horizontalDashRight = new Line(centreX + laneWidth * 6, centreY, centreX + laneWidth, centreY);
        horizontalDashRight.setStrokeWidth(lineWidth);
        horizontalDashRight.getStrokeDashArray().addAll(20d, 20d);
        Line roadRightBottom = new Line(centreX + laneWidth * 6, centreY + laneWidth, centreX + laneWidth, centreY + laneWidth);
        roadRightBottom.setStrokeWidth(lineWidth);

        Line roadBottomLeft = new Line(centreX - laneWidth, centreY + laneWidth, centreX - laneWidth, centreY + laneWidth * 6);
        roadBottomLeft.setStrokeWidth(lineWidth);
        Line verticalDashBottom = new Line(centreX, centreY + laneWidth, centreX, centreY + laneWidth * 6);
        verticalDashBottom.setStrokeWidth(lineWidth);
        verticalDashBottom.getStrokeDashArray().addAll(20d, 20d);
        Line roadBottomRight = new Line(centreX + laneWidth, centreY + laneWidth, centreX + laneWidth, centreY + laneWidth * 6);
        roadBottomRight.setStrokeWidth(lineWidth);

        Line roadTopLeft = new Line(centreX - laneWidth, centreY - laneWidth * 6, centreX - laneWidth, centreY - laneWidth);
        roadTopLeft.setStrokeWidth(lineWidth);
        Line verticalDashTop = new Line(centreX, centreY - laneWidth * 6, centreX, centreY - laneWidth);
        verticalDashTop.setStrokeWidth(lineWidth);
        verticalDashTop.getStrokeDashArray().addAll(20d, 20d);
        Line roadTopRight = new Line(centreX + laneWidth, centreY - laneWidth * 6, centreX + laneWidth, centreY - laneWidth);
        roadTopRight.setStrokeWidth(lineWidth);

        lineList.add(roadLeftTop);
        lineList.add(horizontalDashLeft);
        lineList.add(roadLeftBottom);

        lineList.add(roadRightTop);
        lineList.add(horizontalDashRight);
        lineList.add(roadRightBottom);

        lineList.add(roadBottomLeft);
        lineList.add(roadBottomRight);
        lineList.add(verticalDashBottom);

        lineList.add(roadTopLeft);
        lineList.add(roadTopRight);
        lineList.add(verticalDashTop);

        int halfLaneWidth = laneWidth / 2;
        this.trafficLightA = new TrafficLight(centre.x - halfLaneWidth * 3, centre.y - halfLaneWidth * 3, laneWidth);
        this.trafficLightB = new TrafficLight(centre.x + halfLaneWidth * 3, centre.y - halfLaneWidth * 3, laneWidth);
        this.trafficLightC = new TrafficLight(centre.x + halfLaneWidth * 3, centre.y + halfLaneWidth * 3, laneWidth);
        this.trafficLightD = new TrafficLight(centre.x - halfLaneWidth * 3, centre.y + halfLaneWidth * 3, laneWidth);
        this.trafficLightA.setGo();

        trafficLightList.add(trafficLightA);
        trafficLightList.add(trafficLightB);
        trafficLightList.add(trafficLightC);
        trafficLightList.add(trafficLightD);

        root.getChildren().addAll(roadTopLeft,
                roadTopRight,
                horizontalDashLeft,
                horizontalDashRight,
                verticalDashTop,
                verticalDashBottom,
                roadBottomLeft,
                roadBottomRight,
                roadLeftTop,
                roadLeftBottom,
                roadRightTop,
                roadRightBottom,
                trafficLightA,
                trafficLightB,
                trafficLightC,
                trafficLightD);
    }

    /**
     * Defines all points in a cross junction based off the centre point of the tile
     * Adds all points to pointList so that they can be rotated if needed
     *
     * @param centre the centre point of the tile that is used to calculate the position of everything in the cross junction
     */
    public void definePoints(Point centre) {
        //the centre of the lane is half a lane width
        int centreLane = laneWidth / 2;
        double centreX = centre.getX();
        double centreY = centre.getY();
        this.a.setLocation(centreX - laneWidth * 6, centreY - centreLane);
        this.a2.setLocation(centreX - laneWidth * 6, centreY + centreLane);
        this.ja.setLocation(centreX - (laneWidth + centreLane * 2), centreY - centreLane);
        this.b.setLocation(centreX - centreLane, centreY - laneWidth * 6);
        this.b2.setLocation(centreX + centreLane, centreY - laneWidth * 6);
        this.jb.setLocation(centreX + centreLane, centreY - laneWidth - centreLane * 2);
        this.c.setLocation(centreX + laneWidth * 6, centreY - centreLane);
        this.c2.setLocation(centreX + laneWidth * 6, centreY + centreLane);
        this.jc.setLocation(centreX + laneWidth + centreLane * 2, centreY + centreLane);
        this.d.setLocation(centreX - centreLane, centreY + laneWidth * 6);
        this.d2.setLocation(centreX + centreLane, centreY + laneWidth * 6);
        this.jd.setLocation(centreX - centreLane, centreY + laneWidth + centreLane * 2);
        this.turn1.setLocation(centreX - centreLane, centreY - centreLane);
        this.turn2.setLocation(centreX + centreLane, centreY - centreLane);
        this.turn3.setLocation(centreX + centreLane, centreY + centreLane);
        this.turn4.setLocation(centreX - centreLane, centreY + centreLane);


        pointList.add(a);
        pointList.add(a2);
        pointList.add(ja);
        pointList.add(b);
        pointList.add(b2);
        pointList.add(jb);
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
     * Pre-defines journeys through a cross junction
     */
    public void setUpJourneys() {
        this.abJourney = new Journey(a, ja, turn1, b);
        this.acJourney = new Journey(a, ja, turn1, c);
        this.adJourney = new Journey(a, ja, turn2, d2);
        this.bcJourney = new Journey(b2, jb, turn2, c);
        this.bdJourney = new Journey(b2, jb, turn2, d2);
        this.baJourney = new Journey(b2, jb, turn3, a2);
        this.cdJourney = new Journey(c2, jc, turn3, d2);
        this.caJourney = new Journey(c2, jc, turn3, a2);
        this.cbJourney = new Journey(c2, jc, turn4, b);
        this.daJourney = new Journey(d, jd, turn4, a2);
        this.dbJourney = new Journey(d, jd, turn1, b);
        this.dcJourney = new Journey(d, jd, turn1, c);
    }

    /**
     * Rotates a cross junction by however many degrees we would like
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
        if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightB.getNumberOfCarsWaiting()) {
            if (trafficLightB.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()) {
                if (trafficLightC.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                    //trafficLightD has the most
                    if (!trafficLightD.getIsSafe()) {
                        trafficLightA.setStop();
                        trafficLightB.setStop();
                        trafficLightC.setStop();
                        trafficLightD.setAmber();
                        // trafficLightD.setGo();
                    }
                } else {
                    //trafficLightC has the most
                    if (!trafficLightC.getIsSafe()) {
                        trafficLightA.setStop();
                        trafficLightB.setStop();
                        trafficLightC.setAmber();
                        trafficLightD.setStop();
                    }
                }
            } else if (trafficLightB.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                //trafficLightD has the most
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setAmber();
                }
            } else {
                //trafficLight B has the most
                if (!trafficLightB.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setAmber();
                    trafficLightC.setStop();
                    trafficLightD.setStop();
                }
            }
        } else if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()) {
            if (trafficLightC.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                //trafficLightD has the most
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setAmber();
                }
            } else {
                //trafficLightC has the most
                if (!trafficLightC.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setAmber();
                    trafficLightD.setStop();
                }
            }
        } else {
            if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                //trafficLightD has the most
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setAmber();
                }
            } else {
                //trafficLightA has the most
                if (!trafficLightA.getIsSafe()) {
                    trafficLightA.setAmber();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setStop();
                }
            }
        }
    }


    public Journey getAbJourney() {
        return abJourney;
    }

    public Journey getAcJourney() {
        return acJourney;
    }

    public Journey getAdJourney() {
        return adJourney;
    }

    public Journey getBcJourney() {
        return bcJourney;
    }

    public Journey getBdJourney() {
        return bdJourney;
    }

    public Journey getBaJourney() {
        return baJourney;
    }

    public Journey getCdJourney() {
        return cdJourney;
    }

    public Journey getCaJourney() {
        return caJourney;
    }

    public Journey getCbJourney() {
        return cbJourney;
    }

    public Journey getDaJourney() {
        return daJourney;
    }

    public Journey getDbJourney() {
        return dbJourney;
    }

    public Journey getDcJourney() {
        return dcJourney;
    }

    public TrafficLight getTrafficLightA() {
        return trafficLightA;
    }

    public TrafficLight getTrafficLightB() {
        return trafficLightB;
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

    public Point getBStart() {
        return b2;
    }

    public Point getCStart() {
        return c2;
    }

    public Point getDStart() {
        return d;
    }

    public Point getAEnd() {
        return a2;
    }

    public Point getBEnd() {
        return b;
    }

    public int getCentreX() {
        return this.centre.x;
    }

    public int getCentreY() {
        return this.centre.y;
    }
}
