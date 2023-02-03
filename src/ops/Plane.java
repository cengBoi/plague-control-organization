package ops;
public class Plane extends Vehicle {
    public Plane() {}
    public Plane(String name, int speed, int price, int supplyCap) {
        super(name, speed, price, supplyCap);
    }
    public void info() {
        System.out.println("Plane");
    }
}
