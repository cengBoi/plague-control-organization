package ops;
abstract class Vehicle {
    public String name;
    public int speed;
    public int price;
    public int supplyCap;
    public boolean isAvailable;

    public Vehicle() {}

    public Vehicle(String name, int speed, int price, int supplyCap) {
        this.name = name;
        this.speed = speed;
        this.price = price;
        this.supplyCap = supplyCap;
        isAvailable = true;
    }
}
