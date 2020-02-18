package com.company;

import java.sql.*;
import java.util.Scanner;

public class App {

    Scanner scanner = new Scanner(System.in);
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/holidaymaker", "root", "?");

    public App() throws SQLException {
    }

    public void run() throws SQLException {

        do {
        System.out.println("Välkommen till HolidayMaker!");
        System.out.println("Ange ett alternativ för att fortsätta: (0-3)");
        System.out.println("(1) Registrera ny kund");
        System.out.println("(2) Sök lediga rum");
        System.out.println("(3) Avboka rum / bokningar");
        System.out.println("\n(0) Avsluta programmet");

        String selection;


            selection = scanner.nextLine();

            switch (selection) {
                case "1":
                    registerCustomer();
                    break;
                case "2":
                    searchForRooms();
                    break;
                case "3":
                    cancelBookings();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Vänligen välj ett alternativ mellan 0-3!");
                    break;
            }
        } while(true);
    }

    public void registerCustomer(){

    }

    public void searchForRooms() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from rum;");

        while (resultSet.next()){
            String row = "id: " + resultSet.getString("rum_id")
                    + ", name: " + resultSet.getString("antal_sängar")
                    + ", type: " + resultSet.getString("prisklass")
                    + ", email: " + resultSet.getString("tillgänglighet");
            System.out.println(row);
        }
    }

    public void cancelBookings(){

    }
}
