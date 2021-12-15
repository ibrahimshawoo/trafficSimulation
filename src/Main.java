import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    /**
     * Main method of the simulation
     * Sets up the scene and animation and plays the animation
     *
     * @param primaryStage the stage in which the animation plays
     */
    @Override
    public void start(Stage primaryStage) {

        //set up scene
        Group root = new Group();
        Scene scene = new Scene(root);
        Controller controller = new Controller();
        //set up animation
        controller.calculateFrameRate();
        controller.setUp(root);

        //show animation
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
