package ops;
import java.sql.Connection;
import java.util.ArrayList;


public class Organization implements IOrganization{
    public int currentMoney;
    public String name;
    public ArrayList<HQ> hqList;
    public Cure cure;
    public Connection connection;

    public Organization(int currentMoney, String name) {
        this.currentMoney = currentMoney;
        this.name = name;
        cure = new Cure();
        connection = DBLayer.getInstance().connect();
        hqList = new ArrayList<>();
    }

    @Override
    public void buyHQ(int HQ_id) {
        try {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from HQ where Id = " + HQ_id);
            int price = resultSet.getInt("Price");
            if (/*resultSet.getString("IsBought").equals("No") && */currentMoney >= price) {
                String name = resultSet.getString("Name");
                int stateID = resultSet.getInt("StateID");
                String isNearCoast = resultSet.getString("IsNearCoast");
                int id = resultSet.getInt("Id");
                String isBought = "Yes";
                hqList.add(new HQ(name, stateID, isNearCoast, id, isBought));
                currentMoney -= price;

            }
            else
                System.out.println("Cannot buy the HQ due to currency or you already have it.");
            resultSet.close();
        } catch (Exception e) {
            System.out.println("(SQL Exception Error)");
        }

    }


    @Override
    public void advanceCure(int money) {
        if (money > 0 && currentMoney >= money) {
            cure.cureRate += money / 100000.0;
            currentMoney -= money;
        }
        else {
            System.out.println("Insufficient money.");
        }
    }

    @Override
    public boolean buyVehicles(int hqID, String name) {
    	boolean flag = false;
        try {
            var factory = new VehicleFactory();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from Vehicles where Name = '" + name + "'");
            int price = resultSet.getInt("Price");
            if (/*resultSet.getString("IsBought").equals("No") && */currentMoney >= price) {
                String type = resultSet.getString("Type");
                int speed = resultSet.getInt("Speed");
                int supplyCap = resultSet.getInt("SupplyCap");
                HQ selectedHQ = hqList.get(hqID - 1);
               
                if (selectedHQ != null) {
                	selectedHQ.vehicles.add(factory.createVehicle(type, name, speed, price, supplyCap));
                    currentMoney -= price;
                    flag = true;
				}
            }
            else {
            	System.out.println("Cannot buy the vehicle due to currency or you already have it.");
            }
            resultSet.close();
            return flag;
        } catch (Exception e) {
            System.out.println("(SQL Exception Error)");
            return flag;
        }
    }

    @Override
    public void moveVehicles(HQ hqDest, HQ hqSource, ArrayList<Vehicle> selectedVehicles) {
        hqDest.vehicles.addAll(selectedVehicles);
        hqSource.vehicles.removeAll(selectedVehicles);
    }

    public void terminateRoute(Route route, HQ hq) {
        hq.routes.remove(route);
        currentMoney += route.monetaryGain;
    }
}
