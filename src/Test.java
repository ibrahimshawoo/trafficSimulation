import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.awt.*;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        Point centre = new Point(300,300);
        TJunction tJunction = new TJunction(centre,root);
        Car car = new Car(100);
        car.setTJunction(tJunction);
        String junctionType = car.getJunctionType();
        if (junctionType.equals("T")){
            car.setJourneyTJunction(2);
        }
        PathTransition path1 = new PathTransition();
        path1.setDuration(Duration.millis(2500));
        //allow car to rotate if it makes a turn
        path1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //add the car to the path
        path1.setNode(car);
        //set the car's path transition
        car.setPathTransition(path1);
        //set the path to the first part of the journey
        path1.setPath(car.getJourney().getStartToStop());
        path1.play();

    }
}
