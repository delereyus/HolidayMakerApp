package com.company;

import java.sql.*;
import java.util.ArrayList;
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
        } while (true);
    }

    public void registerCustomer() throws SQLException {
        System.out.println("Ange kundens namn:");
        String customerName = scanner.nextLine();
        System.out.println("Ange kundens e-mail:");
        String customerEmail = scanner.nextLine();
        System.out.println("Ange kundens telefonnr:");
        String customerPhoneNo = scanner.nextLine();

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO kunder SET namn = ?, e_mail = ?, telefon_nr = ?;");
            statement.setString(1, customerName);
            statement.setString(2, customerEmail);
            statement.setString(3, customerPhoneNo);
            statement.executeUpdate();
            System.out.println("\nKunden är nu registrerad!\n");
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("\nKunden lyckades inte läggas in i systemet!\n");
        }
    }

    public void searchForRooms() throws SQLException {

        int numberOfPeople;
        String yesOrNo;
        float distanceToBeach;
        float distanceToCenter;
        String startDate;
        String endDate;
        String[] datesForChecking;

        String poolIsNecessary;
        String restaurantIsNecessary;
        String entertainmentIsNecessary;
        String kidsClubIsNecessary;


        while (true) {
            System.out.println("Ange antal personer för bokningen eller 0 för att gå tillbaka:");
            try {
                numberOfPeople = Integer.parseInt(scanner.nextLine());
                if (numberOfPeople < 0) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt heltal! (minst 1 person)");
            }
        }

        if (numberOfPeople == 0) return;

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM datum WHERE rum_id = 1;");
            ResultSetMetaData rsmd = rs.getMetaData();

            datesForChecking = new String[rsmd.getColumnCount() - 1];

            for (int i = 0; i < datesForChecking.length; i++) {
                datesForChecking[i] = rsmd.getColumnLabel(i + 2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Ett fel har inträffat, du returneras till huvudmenyn.");
            return;
        }

        for (String date : datesForChecking){
            System.out.println(date);
        }

        while (true) {
            System.out.println("Ange startdatum för vistelsen: (ex. '23/6')");
            startDate = scanner.nextLine();
            boolean correctDate = false;

            for (String date : datesForChecking) {
                if (date.equals(startDate)) {
                    correctDate = true;
                    break;
                }
            }

            if (!correctDate) {
                System.out.println("Vänligen ange ett datum enligt formatet '23/6'");
            } else break;
        }

        while (true) {
            System.out.println("Ange slutdatum för vistelsen: (ex. '27/6')");
            endDate = scanner.nextLine();
            boolean correctDate = false;

            for (String date : datesForChecking) {
                if (date.equals(endDate)) {
                    correctDate = true;
                    break;
                }
            }

            if (!correctDate) {
                System.out.println("Vänligen ange ett datum enligt formatet '27/6'");
            } else break;
        }

        while (true) {
            System.out.println("Ska boendet erbjuda swimmingpool? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.equals("y")) {
                poolIsNecessary = " pool = 1 AND";
                break;
            } else if (yesOrNo.equals("n")) {
                poolIsNecessary = "";
                break;
            }
        }

        while (true) {
            System.out.println("Ska boendet erbjuda restaurang? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();

            if (yesOrNo.equals("y")) {
                restaurantIsNecessary = " restaurang = 1 AND";
                break;
            } else if (yesOrNo.equals("n")) {
                restaurantIsNecessary = "";
                break;
            }
        }

        while (true) {
            System.out.println("Ska boendet erbjuda kvällsunderhållning? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();

            if (yesOrNo.equals("y")) {
                entertainmentIsNecessary = " kvällsunderhållning = 1 AND";
                break;
            } else if (yesOrNo.equals("n")) {
                entertainmentIsNecessary = "";
                break;
            }
        }

        while (true) {
            System.out.println("Ska boendet erbjuda barnklubb? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();


            if (yesOrNo.equals("y")) {
                kidsClubIsNecessary = " barnklubb = 1 AND";
                break;
            } else if (yesOrNo.equals("n")) {
                kidsClubIsNecessary = "";
                break;
            }
        }

        while (true) {
            System.out.println("Är avstånd till närmaste strand viktigt för kunden? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.equals("y")) {
                do {
                    System.out.println("Ange maximalt avstånd från stranden i km:");
                    try {
                        distanceToBeach = Float.parseFloat(scanner.nextLine());
                        if (distanceToBeach < 0) throw new IndexOutOfBoundsException();
                        break;
                    } catch (Exception ex) {
                        System.out.println("\nVänligen ange ett giltigt avstånd!\n");
                    }
                } while (true);
                break;
            } else if (yesOrNo.equals("n")) {
                distanceToBeach = 999.0f;
                break;
            } else System.out.println("\nVänligen ange 'y' eller 'n'!\n");
        }

        while (true) {
            System.out.println("Är avstånd till centrum viktigt för kunden? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.equals("y")) {
                do {
                    System.out.println("Ange maximalt avstånd från centrum i km:");
                    try {
                        distanceToCenter = Float.parseFloat(scanner.nextLine());
                        if (distanceToCenter < 0) throw new IndexOutOfBoundsException();
                        break;
                    } catch (Exception ex) {
                        System.out.println("\nVänligen ange ett giltigt avstånd!\n");
                    }
                } while (true);
                break;
            } else if (yesOrNo.equals("n")) {
                distanceToCenter = 999.0f;
                break;
            } else System.out.println("\nVänligen ange 'y' eller 'n'!\n");
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT namn, pool, restaurang, kvällsunderhållning, barnklubb, avstånd_strand, avstånd_centrum, omdöme " +
                    "FROM boende WHERE" + poolIsNecessary + restaurantIsNecessary + entertainmentIsNecessary + kidsClubIsNecessary + " avstånd_strand < " + distanceToBeach + " AND avstånd_centrum < " + distanceToCenter +";");

            while (resultSet.next()) {
                String row = "Hotell: " + resultSet.getString("namn")
                        + ", Pool: " + resultSet.getBoolean("pool")
                        + ", Restaurang: " + resultSet.getBoolean("restaurang")
                        + ", Kvällsunderhållning: " + resultSet.getBoolean("kvällsunderhållning")
                        + ", Barnklubb: " + resultSet.getBoolean("barnklubb")
                        + ", Avstånd till strand: " + resultSet.getFloat("avstånd_strand")
                        + ", Avstånd till centrum: " + resultSet.getFloat("avstånd_centrum")
                        + ", Omdöme: " + resultSet.getFloat("omdöme");
                System.out.println(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nEtt fel inträffade!\n");
        }
    }

    public void cancelBookings(){

    }
}
