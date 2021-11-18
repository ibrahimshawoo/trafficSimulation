import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.awt.*;

public class TJunction {
    private TrafficLight trafficLightA;
    private TrafficLight trafficLightB;
    private TrafficLight trafficLightC;
    private double trafficLightTimer = 0;
    private final Point centre;
    private final Point a = new Point(0,0);
    private final Point a2 = new Point(0,0);
    private final Point ja = new Point(0,0);
    private final Point b = new Point(0,0);
    private final Point b2 = new Point(0,0);
    private final Point jb = new Point(0,0);
    private final Point c = new Point(0,0);
    private final Point c2 = new Point(0,0);
    private final Point jc = new Point(0,0);
    private final Point turn1 = new Point(0,0);
    private final Point turn2 = new Point(0,0);
    private final Point turn3 = new Point(0,0);
    private final Point turn4 = new Point(0,0);
    private Journey abJourney = new Journey(a, ja, turn1, b);
    private Journey baJourney = new Journey(b2, jb, turn4, a2);
    private Journey acJourney = new Journey(a, ja, turn2, c2);
    private Journey bcJourney = new Journey(b2, jb, turn4, c2);
    private Journey caJourney = new Journey(c, jc, turn3, a2);
    private Journey cbJourney = new Journey(c, jc, turn1, b);
    private final int laneWidth = 100;

    public TJunction(Point centre, Group root) {
        this.centre = centre;
        definePoints(centre);
        drawTJunction(root);
    }

    public void drawTJunction(Group root){
        int lineWidth = 3;
        double centreX = centre.getX();
        double centreY = centre.getY();
        Line roadTop = new Line(centreX-laneWidth*6, centreY-laneWidth, centreX+laneWidth*6, centreY-laneWidth);
        roadTop.setStrokeWidth(lineWidth);
        Line horizontalDash = new Line(centreX-laneWidth*6, centreY, centreX+laneWidth*6, centreY);
        horizontalDash.setStrokeWidth(lineWidth);
        horizontalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadBottomLeft = new Line(centreX-laneWidth*6, centreY+laneWidth, centreX-laneWidth, centreY+laneWidth);
        roadBottomLeft.setStrokeWidth(lineWidth);
        Line roadBottomRight = new Line(centreX+laneWidth, centreY+laneWidth, centreX+laneWidth*6, centreY+laneWidth);
        roadBottomRight.setStrokeWidth(lineWidth);
        Line roadLeft = new Line(centreX-laneWidth, centreY+laneWidth, centreX-laneWidth, centreY+laneWidth*6);
        roadLeft.setStrokeWidth(lineWidth);
        Line verticalDash = new Line(centreX, centreY+laneWidth, centreX, centreY+laneWidth*6);
        verticalDash.setStrokeWidth(lineWidth);
        verticalDash.getStrokeDashArray().addAll(20d, 20d);
        Line roadRight = new Line(centreX+laneWidth, centreY+laneWidth, centreX+laneWidth, centreY+laneWidth*6);
        roadRight.setStrokeWidth(lineWidth);

        int halfLaneWidth = laneWidth/2;
        this.trafficLightA = new TrafficLight(ja.x-halfLaneWidth, centre.y-halfLaneWidth*3);
        this.trafficLightB = new TrafficLight(jb.x+halfLaneWidth, centre.y+halfLaneWidth*3);
        this.trafficLightC = new TrafficLight(centre.x-halfLaneWidth*3, jc.y+halfLaneWidth);
        this.trafficLightA.setGo();

        root.getChildren().addAll(roadTop,
                horizontalDash,
                roadBottomLeft,
                roadBottomRight,
                roadLeft,
                verticalDash,
                roadRight,
                trafficLightA,
                trafficLightB,
                trafficLightC);
    }

    public void definePoints(Point centre){
        //the centre of the lane is half a lane width
        int centreLane = laneWidth/2;
        double centreX = centre.getX();
        double centreY = centre.getY();
        this.a.setLocation(centreX-laneWidth*6, centreY-centreLane);
        this.a2.setLocation(centreX-laneWidth*6,centreY+centreLane);
        this.ja.setLocation(centreX-(laneWidth+centreLane),centreY-centreLane);
        this.b.setLocation(centreX+laneWidth*6, centreY-centreLane);
        this.b2.setLocation(centreX+laneWidth*6,centreY+centreLane);
        this.jb.setLocation(centreX+(laneWidth+centreLane), centreY+centreLane);
        this.c.setLocation(centreX-centreLane,centreY+laneWidth*6);
        this.c2.setLocation(centreX+centreLane, centreY+laneWidth*6);
        this.jc.setLocation(centreX-centreLane,centreY+(laneWidth+centreLane));
        this.turn1.setLocation(ja.getX()+laneWidth,a.getY());
        this.turn2.setLocation(jb.getX()-laneWidth,a.getY());
        this.turn3.setLocation(ja.getX()+laneWidth,b2.getY());
        this.turn4.setLocation(jb.getX()-laneWidth,b2.getY());
        this.abJourney = new Journey(a, ja, turn1, b);
        this.baJourney = new Journey(b2, jb, turn4, a2);
        this.acJourney = new Journey(a, ja, turn2, c2);
        this.bcJourney = new Journey(b2, jb, turn4, c2);
        this.caJourney = new Journey(c, jc, turn3, a2);
        this.cbJourney = new Journey(c, jc, turn1, b);

    }

    public void trafficLightController(double frameRate){
                trafficLightTimer = trafficLightTimer + 1;
                if(trafficLightA.getIsSafe() && trafficLightA.getNumberOfCarsWaiting()==0 ||
                        trafficLightB.getIsSafe() && trafficLightB.getNumberOfCarsWaiting()==0 ||
                        trafficLightC.getIsSafe() && trafficLightC.getNumberOfCarsWaiting()==0
                ){

                    double trafficLightTimerDivided = trafficLightTimer / (Math.round(frameRate) * 2);
                    if ((trafficLightTimerDivided == Math.floor(trafficLightTimerDivided)) && !Double.isInfinite(trafficLightTimerDivided)) {
                        changeLights();
                        trafficLightTimer = 0;
                    }
                }

                double trafficLightTimerDivided = trafficLightTimer / (Math.round(frameRate) * 4);
                if ((trafficLightTimerDivided == Math.floor(trafficLightTimerDivided)) && !Double.isInfinite(trafficLightTimerDivided)) {
                    changeLights();
                }
            }

    public void changeLights(){
        trafficLightA.setStop();
        trafficLightB.setStop();
        trafficLightC.setStop();
        if(trafficLightA.getNumberOfCarsWaiting() <= trafficLightB.getNumberOfCarsWaiting()){
            //traffic light C has the most
            if(trafficLightB.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()){
                if(!trafficLightC.getIsSafe()){
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setGo();
                }
            }else{//traffic light B has the most
                trafficLightA.setStop();
                trafficLightB.setGo();
                trafficLightC.setStop();
            }
            //traffic light C has the most
        }else if(trafficLightA.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()){
            trafficLightA.setStop();
            trafficLightB.setStop();
            trafficLightC.setGo();
        }else{//traffic light A has the most
            trafficLightA.setGo();
            trafficLightB.setStop();
            trafficLightC.setStop();
        }
    }

    public Journey getAbJourney() {
        return this.abJourney;
    }

    public Journey getBaJourney() {
        return this.baJourney;
    }

    public Journey getAcJourney() {
        return this.acJourney;
    }

    public Journey getBcJourney() {
        return this.bcJourney;
    }

    public Journey getCaJourney() {
        return this.caJourney;
    }

    public Journey getCbJourney() {
        return this.cbJourney;
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

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b2;
    }

    public Point getC() {
        return c;
    }

    public int getLaneWidth() {
        return laneWidth;
    }
}
