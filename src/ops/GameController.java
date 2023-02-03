package ops;
import java.sql.*;
import java.util.Scanner;

import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class GameController {
	public static GameController gController = null;
    public String userType;
    public World world;
    public Connection connection;
    public boolean isItNewGame;

    private GameController() {
        world = new World();
        connection = DBLayer.getInstance().connect();
    }
    
    public static GameController getInstance() {
        if (gController == null)
        	gController = new GameController();
        return gController;
    }
    
    public boolean routeHandler(int hqID, String destination, String vehicleStr) {
    	HQ foundHq = null;
    	Vehicle foundVeh = null;
    	City foundCity = null;
    	
    	boolean flag = false;
    	for (HQ hq : world.organization.hqList) {
    		if (hq.id == hqID) {
				foundHq = hq;
				flag = true;
				break;
			}
    	}
    	
    	if(flag) {
    		flag= false;
    		for (Vehicle vehicle : foundHq.vehicles) {
        		if (vehicle.name.equals(vehicleStr)) {
        			foundVeh = vehicle;
        			flag = true;
        			break;
    			}
        	}
        	
    		if(flag) {
    			flag= false;
    			for (City city : world.cities) {
            		if (city.name.equals(destination)) {
            			foundCity = city;
            			flag = true;
            			break;
        			}
            	}
            	if(flag)
            		return foundHq.startRoute(foundCity, foundVeh); 		
    		}
        	
    	}
    	
    	return false;
    	
    }

    public boolean loadSave(int WorldID, String userType) throws SQLException {
    	world.addCities();
        var statement = connection.createStatement();
        ResultSet resultSet;
        this.userType = userType;
        if (userType.equals("overseer")) {
        	resultSet = statement.executeQuery("select * from Save_World where WorldID = " + WorldID + ";");
        	
        	if (resultSet == null) {
				return false;
			}
        }
        else {
        	resultSet = statement.executeQuery("select * from Save_World");
		}
        world.totalInfectedPeople = resultSet.getInt("TotalInfectedPeople");
        world.infectionRate = resultSet.getInt("InfectionRate");
        world.organization.currentMoney = resultSet.getInt("OrganizationMoney");
        world.day = resultSet.getInt("Day");

        resultSet = statement.executeQuery("select * from Cities");
        while (resultSet.next()) {
            for (City city : world.cities) {
                if (resultSet.getString("Name").equals(city.name)) {
                    int infectedPeople = resultSet.getInt("InfectedPeople");
                    int infectedPeoplePercent = resultSet.getInt("InfectedPeoplePercent");
                    city.infectedPeople = infectedPeople;
                    city.infectedPeoplePercent = infectedPeoplePercent;
                    break;
                }
            }
        }
        resultSet = statement.executeQuery("select * from HQ where IsBought = 'Yes'");
        while (resultSet.next()) {
            String name = resultSet.getString("Name");
            int stateID = resultSet.getInt("StateID");
            String isNearCoast = resultSet.getString("IsNearCoast");
            int id = resultSet.getInt("Id");
            String isBought = resultSet.getString("IsBought");
            var hq = new HQ(name, stateID, isNearCoast, id, isBought);
            world.organization.hqList.add(hq);
        }
        resultSet = statement.executeQuery("select * from Vehicles where IsBought = 'Yes'");
        while (resultSet.next()) {
            String vehicleType = resultSet.getString("Type");
            String name = resultSet.getString("Name");
            int speed = resultSet.getInt("Speed");
            int price = resultSet.getInt("Price");
            int supplyCap = resultSet.getInt("SupplyCap");
            var factory = new VehicleFactory();
            var vehicle = factory.createVehicle(vehicleType, name, speed, price, supplyCap);
            int id = resultSet.getInt("HQ_Id");
            world.organization.hqList.get(id).vehicles.add(vehicle);
            //world.organization.buyVehicles(resultSet.getString("HQ_Name"), resultSet.getString("Name"));
        }
        resultSet = statement.executeQuery("select * from Routes");
        while (resultSet.next()) {
            for (HQ hq : world.organization.hqList) {
                if (hq.name.equals(resultSet.getString("HQ_Name"))) {
                    int remainingDay = resultSet.getInt("RemainingDay");
                    int monetaryGain = resultSet.getInt("MonetaryGain");
                    double infectionRateChange = resultSet.getDouble("InfectionRateChange");
                    String destinationName = resultSet.getString("Destination");
                    City destination = null;
                    for (City city : world.cities) {
                        if (city.name.equals(destinationName)) {
                            destination = city;
                            String vehicle = resultSet.getString("Vehicle");
                            String HQ_Name = resultSet.getString("HQ_Name");
                            var route = new Route(remainingDay, monetaryGain, infectionRateChange, destination,
                                    vehicle, HQ_Name);
                            hq.routes.add(route);
                            return true;
                        }
                    }
                }
            }
        }
        resultSet.close();
        return true;
        
    }


    public void start() throws SQLException {
                var statement = connection.createStatement();
                String str = "update Save_World set TotalInfectedPeople = 0, InfectionRate = 0, OrganizationMoney = 0;";
                statement.executeUpdate(str);
                str = "update Cities set 'InfectedPeople' = 0, 'InfectedPeoplePercent' = 0;";
                statement.executeUpdate(str);
                str = "update Vehicles set 'IsBought' = 'No', 'HQ_Id' = 0;";
                statement.executeUpdate(str);
                str = "update HQ set 'IsBought' = 'No';";
                statement.executeUpdate(str);
                str = "DELETE FROM Routes;";
                statement.executeUpdate(str);
                world = new World();
                world.addCities();
                statement.close();

    }
    public void end() {

    }
    public void save() throws SQLException, InterruptedException {
       try {
           var statement = connection.createStatement();
           String str = """
                   CREATE TABLE IF NOT EXISTS "Save_World" (
                   \t"TotalInfectedPeople"\tINTEGER,
                   \t"InfectionRate"\tINTEGER,
                   \t"OrganizationMoney"\tINTEGER
                   );""" +  "UPDATE Save_World SET 'TotalInfectedPeople' = " + world.totalInfectedPeople +
                   ", 'InfectionRate' = " + world.infectionRate + ", 'OrganizationMoney' = " +
                   world.organization.currentMoney + ", WorldID = 1, Day = " + world.day + ";";
           
           statement.executeUpdate(str);
           statement.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
        try {
            var statement = connection.createStatement();
            for (City city : world.cities) {
                String str = "update Cities set 'InfectedPeople' = 0, 'InfectedPeoplePercent' = 0;";
                statement.executeUpdate(str);
            }
            for (City city : world.cities) {
                String str = "update Cities set 'InfectedPeople' = " + city.infectedPeople +
                        ", 'InfectedPeoplePercent' = " + city.infectedPeoplePercent + " where Name = '" +
                        city.name + "';";
                statement.executeUpdate(str);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            var statement = connection.createStatement();
            for (HQ hq : world.organization.hqList) {
                for (Vehicle vehicle : hq.vehicles) {
                    String str = "update Vehicles set 'IsBought' = 'No', 'HQ_Id' = NULL;";
                    statement.executeUpdate(str);
                }
            }
            for (HQ hq : world.organization.hqList) {
                for (Vehicle vehicle : hq.vehicles) {
                    String str = "update Vehicles set 'IsBought' = 'Yes', 'HQ_Id' = '" + hq.name +
                            "' where Name = '" + vehicle.name + "';";
                    statement.executeUpdate(str);
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            var statement = connection.createStatement();
            for (HQ hq : world.organization.hqList) {
                String str = "update HQ set 'IsBought' = 'No';";
                statement.executeUpdate(str);
            }
            for (HQ hq : world.organization.hqList) {
                String str = "update HQ set 'IsBought' = 'Yes' where name = '" + hq.name + "';";
                statement.executeUpdate(str);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            var statement = connection.createStatement();
            String str = "DELETE FROM Routes;";
            statement.executeUpdate(str);
            statement.close();
            for (HQ hq : world.organization.hqList) {
                for (Route route : hq.routes) {
                    if (hq.routes.size() == 0)
                        break;
                    str = "insert into Routes(IsOnRoute, RemainingDay, MonetaryGain, InfectionRateChange," +
                            " Destination, Vehicle, HQ_Name) values (?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(str);
                    if (route.isOnRoute)
                        preparedStatement.setString(1, "Yes");
                    else
                        preparedStatement.setString(1, "No");
                    preparedStatement.setInt(2, route.remainingDay);
                    preparedStatement.setInt(3, route.monetaryGain);
                    preparedStatement.setDouble(4, route.infectionRateChange);
                    preparedStatement.setString(5, route.destination.name);
                    preparedStatement.setString(6, route.vehicle);
                    preparedStatement.setString(7, route.HQ_Name);
                    preparedStatement.executeUpdate();
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        var preparedStatement = connection.prepareStatement("insert or replace into main.Save_World (TotalInfectedPeople, InfectionRate) " +
//                "values (" + world.totalInfectedPeople + ", " + world.infectionRate + ")");
//        preparedStatement.executeUpdate();
    }
    public boolean loginAsAdmin(JTextField username, JPasswordField password) {

    	
    	return logInCheck(username.getText(), String.valueOf(password.getPassword()), "admin");
    }
    public boolean loginAsOS(JTextField username, JPasswordField password) {
    	
    	return logInCheck(username.getText(), String.valueOf(password.getPassword()), "overseer");
    }

    public boolean logInCheck(String username, String password, String userType) {
    	boolean flag = false;
    	try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Users;");
            while (resultSet.next()) {
                if (resultSet.getString("Username").equals(username) &&
                        resultSet.getString("Password").equals(password) && resultSet.getString("UserType").equals(userType)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                userType = resultSet.getString("UserType");
            }

            else
                System.out.println("Wrong username or password. Please try again!");
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void register(JTextField username, JPasswordField password) {
        
    	try {
    		var statement = connection.createStatement();
    		var resultSet = statement.executeQuery("select * from Users where Username = '" + username.getText() + "';");
    		if (resultSet.isClosed()) {
    			//resultSet.close();
    			PreparedStatement create = connection.prepareStatement("insert or replace into main.Users " +
                        "(Username, Password, UserType) values ('" + username.getText() + "', '" + String.valueOf(password.getPassword()) + "', 'overseer');");
                create.executeUpdate();
                create.close();
                userType = "overseer";
			}
    		else {
    			System.out.println("Already exists.");
    		}
    		resultSet.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewWorldState() {

    }
}
