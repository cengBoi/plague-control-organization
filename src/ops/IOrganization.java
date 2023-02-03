package ops;
import java.util.ArrayList;

public interface IOrganization {
    void buyHQ(int HQ_id);
    void advanceCure(int money);
    boolean buyVehicles(int hqId, String name);
    void moveVehicles(HQ hqDest, HQ hqSource, ArrayList<Vehicle> vehicles);
}
