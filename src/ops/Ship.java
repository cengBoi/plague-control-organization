package ops;
public class Ship extends Vehicle {
    public Ship() {}
    public Ship(String name, int speed, int price, int supplyCap) {
        super(name, speed, price, supplyCap);
    }

    public void info() {
        System.out.println("Ship");
    }
}
