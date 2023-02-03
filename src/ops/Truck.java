package ops;
public class Truck extends Vehicle {
    public Truck() {}
    public Truck(String name, int speed, int price, int supplyCap) {
        super(name, speed, price, supplyCap);
    }

    public void info() {
        System.out.println("Truck");
    }
}
