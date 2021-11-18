import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import java.awt.*;


public class TestCar extends StackPane {

    //set points
    Point a = new Point(0, 150);
    Point ja = new Point(320, 150);
    Point a2 = new Point(0, 250);
    Point ja2 = new Point(400, 250);

    Point b = new Point(1000, 150);
    Point jb = new Point(600, 150);
    Point b2 = new Point(1000, 250);
    Point jb2 = new Point(680, 250);

    Point c = new Point(450, 700);
    Point jc = new Point(450, 380);
    Point c2 = new Point(550, 700);
    Point jc2 = new Point(550, 300);

    Point turn1 = new Point(450,150);
    Point turn2 = new Point(550,150);
    Point turn3 = new Point(450,250);
    Point turn4 = new Point(550,250);

    //set up paths
    Polyline ab = new Polyline(a.getX(), a.getY(), b.getX(), b.getY());
    Polyline ba = new Polyline(b2.getX(), b2.getY(), a2.getX(), a2.getY());
    Polyline ac = new Polyline(a.getX(), a.getY(), c2.getX(), a.getY(), c2.getX(), c2.getY());
    Polyline bc = new Polyline(b2.getX(), b2.getY(), c2.getX(), b2.getY(), c2.getX(), c2.getY());
    Polyline ca = new Polyline(c.getX(), c.getY(), c.getX(), a2.getY(), a2.getX(), a2.getY());
    Polyline cb = new Polyline(c.getX(), c.getY(), c.getX(), b.getY(), b.getX(), b.getY());

    Journey abJourney = new Journey(a, ja,turn1,b);
    Journey baJourney = new Journey(b2,jb2,turn4,a2);
    Journey acJourney = new Journey(a,ja,turn2,c2);
    Journey bcJourney = new Journey(b2,jb2,turn4,c2);
    Journey caJourney = new Journey(c,jc,turn3,a2);
    Journey cbJourney = new Journey(c,jc,turn1,b);

    //polyline for if path number fails
    Polyline fail = new Polyline(0,10, 100,10);

    Polyline path;
    Journey journey;
    double previousX;
    double previousY;

    Image carImage = new Image("car.png");



    public TestCar (int pathNumber){
        Rectangle car = createCar();
        Rectangle bounds = createBounds();
        this.getChildren().addAll(bounds,car);
        this.path = getPathFromPathNumber(pathNumber);
        this.journey = getJourneyFromPathNumber(pathNumber);


    }

    private Rectangle createBounds() {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(60);
        rectangle.setWidth(100);
        //rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }

    private Rectangle createCar() {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(40);
        rectangle.setWidth(80);
        rectangle.setFill(new ImagePattern(carImage));
        //rectangle.setX(this.getJourney().getStart().getX());
        //rectangle.setY(this.getJourney().getStart().getY());
        return rectangle;
    }


    public double getPreviousX() {
        return this.previousX;
    }

    public void setPreviousX(double previousX) {
        this.previousX = previousX;
    }

    public double getPreviousY() {
        return this.previousY;
    }

    public void setPreviousY(double previousY) {
        this.previousY = previousY;
    }

    public Polyline getPath() {
        return path;
    }

    public Journey getJourney() {
        return journey;
    }

//    public String getJourneyStartPoint(){
//        //return this.journey.getJourneyStartPointTJunction();
//    }

    //path numbers 0-5
    private Polyline getPathFromPathNumber(int pathNumber){
        switch (pathNumber) {
            case 0:
                return ab;
            case 1:
                return ba;
            case 2:
                return ac;
            case 3:
                return bc;
            case 4:
                return ca;
            case 5:
                return cb;
            default:
                return fail;
        }
    }

    private Journey getJourneyFromPathNumber(int pathNumber){
        switch (pathNumber) {
            case 0:
                return abJourney;
            case 1:
                return baJourney;
            case 2:
                return acJourney;
            case 3:
                return bcJourney;
            case 4:
                return caJourney;
            case 5:
                return cbJourney;
            default:
                return null;
        }
    }

}
