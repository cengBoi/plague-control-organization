package ops;
import java.util.ArrayList;
import java.util.Arrays;

public class HQ {
    public String name;
    public int stateID;
    public String isNearCoast;
    public int id;
    public String isBought;
    public ArrayList<Vehicle> vehicles;
    public ArrayList<Route> routes;

    public HQ(String name, int stateID, String isNearCoast, int id, String isBought) {
        this.name = name;
        this.stateID = stateID;
        this.isNearCoast = isNearCoast;
        this.id = id;
        this.isBought = isBought;
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
    }


    public boolean startRoute(City destination, Vehicle vehicle) {
    	if (destination.isNearCoast == isNearCoast.equals("Yes") && destination.stateID == stateID) {
            int monetaryGain = 0;
            monetaryGain += 100 * vehicle.supplyCap;
            routes.add(new Route(2, monetaryGain, 0.1, destination, vehicle.name, name));
            return true;
        }
        return false;
    }
}
