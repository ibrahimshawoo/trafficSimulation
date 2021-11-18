import javafx.scene.Group;

import java.awt.*;

public class CrossJunction {
    private TrafficLight trafficLightA, trafficLightB, trafficLightC,trafficLightD;
    private double trafficLightTimer = 0;

    public CrossJunction(TrafficLight trafficLightA, TrafficLight trafficLightB, TrafficLight trafficLightC, TrafficLight trafficLightD, Point centre, Group root) {
        //super(centre,root);
        this.trafficLightA = trafficLightA;
        this.trafficLightB = trafficLightB;
        this.trafficLightC = trafficLightC;
        this.trafficLightD = trafficLightD;
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

    public void changeLights() {
        trafficLightA.setStop();
        trafficLightB.setStop();
        trafficLightC.setStop();
        trafficLightD.setStop();
        if (trafficLightA.getNumberOfCarsWaiting() <= trafficLightB.getNumberOfCarsWaiting()) {
            if (trafficLightB.getNumberOfCarsWaiting() <= trafficLightC.getNumberOfCarsWaiting()) {
                if (trafficLightC.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                    //trafficLightD has the most
                    if (!trafficLightD.getIsSafe()) {
                        trafficLightA.setStop();
                        trafficLightB.setStop();
                        trafficLightC.setStop();
                        trafficLightD.setGo();
                    }
                } else {
                    //trafficLightC has the most
                    if (!trafficLightC.getIsSafe()) {
                        trafficLightA.setStop();
                        trafficLightB.setStop();
                        trafficLightC.setGo();
                        trafficLightD.setStop();
                    }
                }
            } else if (trafficLightB.getNumberOfCarsWaiting() <= trafficLightD.getNumberOfCarsWaiting()) {
                //trafficLightD has the most
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setGo();
                }
            } else {
                //trafficLight B has the most
                if (!trafficLightB.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setGo();
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
                    trafficLightD.setGo();
                }
            } else {
                //trafficLightC has the most
                if (!trafficLightC.getIsSafe()) {
                    trafficLightA.setStop();
                    trafficLightB.setStop();
                    trafficLightC.setGo();
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
                    trafficLightD.setGo();
                }
            } else {
                //trafficLightA has the most
                if (!trafficLightD.getIsSafe()) {
                    trafficLightA.setGo();
                    trafficLightB.setStop();
                    trafficLightC.setStop();
                    trafficLightD.setStop();
                }
            }
        }
    }
}
