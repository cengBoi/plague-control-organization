package ops;
public class VehicleFactory {

    public Vehicle createVehicle(String vehicleType, String name, int speed, int price, int supplyCap) {
        if (vehicleType == null || vehicleType.isEmpty())
            return null;
        return switch (vehicleType) {
            case "Plane" -> new Plane(name, speed, price, supplyCap);
            case "Truck" -> new Truck(name, speed, price, supplyCap);
            case "Ship" -> new Ship(name, speed, price, supplyCap);
            default -> throw new IllegalArgumentException("Unknown vehicle type " + vehicleType);
        };
    }
}
