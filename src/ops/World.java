package ops;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class World {
    public double totalInfectedPeople;
    public int infectionRate;
    public Organization organization;
    public Connection connection;
    public ArrayList<City> cities;
    public int worldPopulation;
    public int day;

    public World() {
        totalInfectedPeople = 0;
        infectionRate = 4;
        organization = new Organization(5000000, "Plague Control Organization");
        cities = new ArrayList<>();
        connection = DBLayer.getInstance().connect();
        day = 1;
    }

    public void addCities() throws SQLException {
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery("select * from Cities");
        while (resultSet.next()) {
            String name = resultSet.getString("Name");
            int stateID = resultSet.getInt("StateID");
            boolean isNearCoast = resultSet.getString("IsNearCoast").equals("Yes");
            int population = resultSet.getInt("Population");
            var city = new City(name, stateID, isNearCoast, population);
            cities.add(city);
            worldPopulation += population;
        }
        resultSet.close();
    }
    
    
    public void increaseDay() {
		day++;
		
		for (HQ hq : organization.hqList) {
    		if (hq != null) {
    			int route_size = hq.routes.size();
    			for(int i = 0;i<route_size;i++) {
    				if(hq.routes.get(i) != null) {
    					hq.routes.get(i).remainingDay--;
    				}
        			if (hq.routes.get(i).remainingDay == 0) {
						organization.terminateRoute(hq.routes.get(i), hq);
					}
    			}
    			
			}
    	}
	}

    public void increaseTotalInfected() {
        int infected = 0;
        for (City city : cities) {
            infected += city.infectedPeople;
        }
        totalInfectedPeople = infected;
        infectionRate = (int) (100 * (totalInfectedPeople / worldPopulation));
    }
}
