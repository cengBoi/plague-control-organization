package ops;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, InterruptedException {
    	var gameController = GameController.getInstance();
        //gameController.world.addCities();
        //gameController.login();
        gameController.start();
        /*
        gameController.world.organization.buyHQ(2);
        gameController.world.organization.buyHQ(1);
        gameController.world.organization.buyVehicles("Hope’s Base", "Boeing 747 Dreamlifter");
        gameController.world.organization.buyVehicles("Hope’s Base", "Monster");
        gameController.world.organization.buyVehicles("Hope’s Base", "Rambo");
        gameController.world.organization.buyVehicles("Hope’s Base", "Black Beauty");

        HQ hq1 = gameController.world.organization.hqList.get(0);
        HQ hq2 = gameController.world.organization.hqList.get(1);
        var vehicles = gameController.world.organization.hqList.get(0).vehicles;
        gameController.world.organization.moveVehicles(hq2, hq1, vehicles);


        var vehicle = gameController.world.organization.hqList.get(1).vehicles.get(1);
        gameController.world.organization.hqList.get(1).startRoute(gameController.world.cities.get(2), vehicle);

        var list = gameController.world.organization.hqList;
        var hq = gameController.world.organization.hqList.get(0);

        System.out.println(Arrays.toString(list.toArray()));
        System.out.println(gameController.world.organization.currentMoney);
        System.out.println(gameController.world.organization.hqList.get(1).routes);
        //gameController.world.organization.terminateRoute(gameController.world.organization.hqList.get(1).routes.get(0), gameController.world.organization.hqList.get(1));
        System.out.println(gameController.world.organization.hqList.get(1).routes);
        System.out.println(gameController.world.organization.currentMoney);

        System.out.println(gameController.world.cities.get(2).infectedPeoplePercent + "  -  " + gameController.world.cities.get(2).infectedPeople);
        for (int i = 0; i < 100; i++) {
            gameController.world.cities.get(2).increaseInfected();
            System.out.println(gameController.world.cities.get(2).infectedPeoplePercent + "  -  " + gameController.world.cities.get(2).infectedPeople);
        }
        gameController.world.increaseTotalInfected();
        gameController.save();

//        var db = DBLayer.getInstance();
//        var connection = db.connect();
//        showCities(connection);
//        showVehicles(connection);
//        var statement = connection.createStatement();
//        statement.execute("""
//                create table if not exists TestTable as\s
//                select Name, Population \s
//                from Cities""");
//        var resultSet = statement.executeQuery("select * from Cities;");
//        var cities = new ArrayList<City>();
//
//        while (resultSet.next()) {
//            String name = resultSet.getString("Name");
//            int stateID = resultSet.getInt("StateID");
//            boolean isNearCoast = resultSet.getString("IsNearCoast").equals("Yes");
//            int population = resultSet.getInt("Population");
//            var city = new City(name, stateID, isNearCoast, 0, population, 0);
//            cities.add(city);
//        }
//        System.out.println(Arrays.toString(cities.toArray()));

         */
    }

    static public void showVehicles(Connection connection) {
        try {
            var vehicles = new ArrayList<Vehicle>();
            var vehicles_trial = new Vehicle[] {
                    new Plane(),
                    new Truck(),
                    new Ship()
            };
            var vehicleFactory = new VehicleFactory();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from Vehicles;");
            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                String name = resultSet.getString("Name");
                int speed = resultSet.getInt("Speed");
                int price = resultSet.getInt("Price");
                int supplyCap = resultSet.getInt("SupplyCap");
                var newVehicle = vehicleFactory.createVehicle(type, name, speed, price, supplyCap);
                vehicles.add(newVehicle);
                System.out.println("Class type: " + newVehicle.getClass());
            }
            System.out.println("Vehicles: ");
            System.out.println(Arrays.toString(vehicles.toArray()));
//            while (resultSet.next()) {
//                System.out.printf("%-20s%-20s\n", resultSet.getString("Type"),
//                        resultSet.getString("Name"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void showCities(Connection connection) {
        try {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from Cities;");
            System.out.println("Cities: ");
            while (resultSet.next()) {
                System.out.printf("%-20s%-20s\n", resultSet.getString("Name"),
                        resultSet.getInt("Population"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) {
        var db = DBLayer.getInstance();
        var connection = db.connect();

        int status;
        String username, password;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Log in or sign in?(1 or 2): ");
        status = scanner.nextInt();
        scanner.nextLine();
        if (status == 3) {
            showUsers(connection);
            System.exit(0);
        }

        System.out.print("username: ");
        username = scanner.nextLine();
        System.out.print("password: ");
        password = scanner.nextLine();

        if (status == 1)
            logInCheck(connection, username, password);
        else if (status == 2)
            signIn(connection, username, password);
        else
            System.out.println("Invalid operation entered!");
    }


    static public void logInCheck(Connection connection, String username, String password) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from people;");
            boolean flag = false;
//            while (resultSet.next()) {
//                System.out.printf("%-30s%-20s\n", resultSet.getString("name"), resultSet.getString("surname"));
//            }
            while (resultSet.next()) {
                if (resultSet.getString("name").equals(username) && resultSet.getString("surname").equals(password)) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                System.out.println("Logged in!");
            else
                System.out.println("Wrong username or password. Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void signIn(Connection connection, String username, String password) {
        try {
            PreparedStatement create = connection.prepareStatement("insert or replace into main.people " +
                    "(name, surname) values ('" + username + "', '" + password + "');");
            create.executeUpdate();
            create.close();
            System.out.println("Signed in!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void showUsers(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from people;");
            System.out.println("USERS: ");
            while (resultSet.next()) {
                System.out.printf("%-20s%-20s\n", resultSet.getString("name"),
                        resultSet.getString("surname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
