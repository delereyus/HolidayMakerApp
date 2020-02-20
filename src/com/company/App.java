package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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
            System.out.println("\nKunden lyckades inte registreras!\n");
        }
    }

    public void searchForRooms() throws SQLException {

        int customerId;
        int numberOfPeople;
        String yesOrNo;
        float distanceToBeach;
        float distanceToCenter;
        boolean extraBed;
        String mealCost = "";
        String startDate;
        String endDate;
        String[] allDates;
        String[] datesAfterStartDate = new String[0];
        ArrayList<String> selectedDates = new ArrayList<>();

        String poolIsNecessary;
        String restaurantIsNecessary;
        String entertainmentIsNecessary;
        String kidsClubIsNecessary;

        // VÄLJA HOTELL SEDAN VÄLJA RUM, GÅ FRAM OCH TILLBAKA I MENYN, FIXA HUR BOKNINGAR PÅ DATUM/RUM SKA IMPLEMENTERAS

        ArrayList<Integer> kundIds = new ArrayList<>();

        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from kunder");

            while (resultSet.next()) {
                kundIds.add(resultSet.getInt("kund_id"));
                String row = "Kund-ID: " + resultSet.getInt("kund_id")
                        + ". Namn: " + resultSet.getString("namn")
                        + ", E-mail: " + resultSet.getString("e_mail")
                        + ", TelefonNr: " + resultSet.getString("telefon_nr");
                System.out.println(row);
            }

        }catch(Exception ex){
            ex.printStackTrace();
            return;
        }

        while (true){
            System.out.println("Ange kund-ID för bokningen eller 0 för att gå tillbaka: ");
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                if (!kundIds.contains(selection)) throw new IndexOutOfBoundsException();
                customerId = selection;
                break;
            } catch(Exception ex){
                System.out.println("Vänligen ange ett giltigt kund-ID eller 0 för att gå tillbaka");
            }
        }

        while (true) {
            System.out.println("Ange antal personer för bokningen eller 0 för att gå tillbaka:");
            try {
                numberOfPeople = Integer.parseInt(scanner.nextLine());
                if (numberOfPeople == 0) return;
                if (numberOfPeople < 0) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt heltal! (minst 1 person)");
            }
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM rum WHERE rum_id = 1;");
            ResultSetMetaData rsmd = rs.getMetaData();

            allDates = new String[rsmd.getColumnCount() - 4];

            for (int i = 0; i < allDates.length; i++) {
                allDates[i] = rsmd.getColumnLabel(i + 5);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Ett fel har inträffat, du returneras till huvudmenyn.");
            return;
        }

        for (String date : allDates){
            System.out.println(date);
        }

        int breakPoint;


        while (true) {
            System.out.println("Ange startdatum för vistelsen: (ex. '23_6')");
            startDate = scanner.nextLine();
            boolean correctDate = false;

            for (int i = 0; i < allDates.length; i++) {
                if (allDates[i].equals(startDate)) {
                    if (startDate.equals("31_7")) {
                        System.out.println("Det går inte att starta en vistelse på säsongens sista dag!");
                        break;
                    }
                    correctDate = true;
                    breakPoint = i + 1;
                    datesAfterStartDate = new String[allDates.length - breakPoint];
                    for (int x = breakPoint, y = 0; x < allDates.length; x++, y++) {
                        datesAfterStartDate[y] = allDates[x];
                    }
                    break;
                }
            }

            if (!correctDate) {
                System.out.println("Vänligen ange ett datum enligt formatet '23_6'");
            } else break;
        }


        while (true) {
            System.out.println("Ange slutdatum för vistelsen: (ex. '27_6')");
            endDate = scanner.nextLine();
            boolean correctDate = false;

            for (String date : datesAfterStartDate) {
                if (date.equals(endDate)) {
                    correctDate = true;
                    break;
                }
            }

            if (!correctDate) {
                System.out.println("Vänligen ange ett datum enligt formatet '27_6'");
            } else break;
        }


        selectedDates.add(startDate);

        for (int i = 0; i < datesAfterStartDate.length; i++){
            selectedDates.add(datesAfterStartDate[i]);
            if (datesAfterStartDate[i].equals(endDate)){
                break;
            }
        }

        while (true) {
            System.out.println("Måste boendet erbjuda swimmingpool? (y/n)");
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
            System.out.println("Måste boendet erbjuda restaurang? (y/n)");
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
            System.out.println("Måste boendet erbjuda kvällsunderhållning? (y/n)");
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
            System.out.println("Måste boendet erbjuda barnklubb? (y/n)");
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

        while (true) {
            System.out.println("Önskar kunden en extrasäng? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.equals("y")) {
                extraBed = true;
                break;
            } else if (yesOrNo.equals("n")) {
                extraBed = false;
                break;
            } else System.out.println("\nVänligen ange 'y' eller 'n'!\n");
        }

        boolean loop = true;

        while (loop) {
            System.out.println("Ange kundens val angående måltider (1-3):");
            System.out.println("(1) Inga måltider");
            System.out.println("(2) Halvpension");
            System.out.println("(3) Helpension");

            String selection = scanner.nextLine();

            switch (selection){
                case "1":
                    mealCost = "none";
                    loop = false;
                    break;
                case "2":
                    mealCost = "half";
                    loop = false;
                    break;
                case "3":
                    mealCost = "whole";
                    loop = false;
                    break;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ! (1-3)");
            }
        }

        String dateString = "";
        for (String date : selectedDates){
            dateString += " r." + date + " = true AND";
        }

        ArrayList<Integer> hotel_ids = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT b.boende_id, b.namn, b.pool, b.restaurang, b.kvällsunderhållning, b.barnklubb, b.avstånd_strand, b.avstånd_centrum, b.omdöme, r.pris, r.pris_halvpension, r.pris_helpension, r.pris_extrasäng " +
                    "FROM boende b JOIN rum_med_pris r ON b.boende_id = r.boende_id " +
                    "WHERE" + dateString + poolIsNecessary + restaurantIsNecessary + entertainmentIsNecessary + kidsClubIsNecessary + " avstånd_strand < " + distanceToBeach + " AND avstånd_centrum < " + distanceToCenter +" AND r.antal_sängar = " + numberOfPeople +
                    " GROUP BY b.namn;");

            if (!resultSet.next()){
                System.out.println("Vi kunde inte hitta några lediga rum med de angivna kriterierna!");
                return;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                Statement anotherStatement = conn.createStatement();
                ResultSet amountOfRooms = anotherStatement.executeQuery("SELECT COUNT(r.rum_id) FROM rum r WHERE" + dateString + " r.boende_id = " + resultSet.getInt("boende_id") + " AND r.antal_sängar = " + numberOfPeople + ";");
                int rooms = 0;
                int price = resultSet.getInt("pris");

                while(amountOfRooms.next()){
                    rooms = amountOfRooms.getInt(1);
                }

                if (extraBed){
                    price += resultSet.getInt("pris_extrasäng");
                }

                if (mealCost.equals("half")){
                    price += resultSet.getInt("pris_halvpension");
                } else if (mealCost.equals("whole")){
                    price += resultSet.getInt("pris_helpension");
                }

                hotel_ids.add(resultSet.getInt("boende_id"));

                String row = resultSet.getInt("boende_id") + ". Boende: " + resultSet.getString("namn")
                        + ", Pool: " + resultSet.getBoolean("pool")
                        + ", Restaurang: " + resultSet.getBoolean("restaurang")
                        + ", Kvällsunderhållning: " + resultSet.getBoolean("kvällsunderhållning")
                        + ", Barnklubb: " + resultSet.getBoolean("barnklubb")
                        + ", Avstånd till strand: " + resultSet.getFloat("avstånd_strand")
                        + ", Avstånd till centrum: " + resultSet.getFloat("avstånd_centrum")
                        + ", Omdöme: " + resultSet.getFloat("omdöme")
                        + ", Lediga rum för " + numberOfPeople + " personer: " + rooms
                        + ", Pris per natt: " + price + "\n";
                System.out.println(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nEtt fel inträffade!\n");
        }

        int roomId = 0;

        while (true) {
            System.out.println("\nBoka ett rum mellan " + startDate + " och " + endDate + " genom att ange siffran till vänster om boendet eller 0 för att avsluta sökningen");
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                if (!hotel_ids.contains(selection)){
                    throw new IndexOutOfBoundsException();
                }
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT r.rum_id FROM rum r WHERE" + dateString + " r.antal_sängar = " + numberOfPeople + " AND r.boende_id = " + selection +";");

                while (resultSet.next()){
                    roomId = resultSet.getInt("rum_id");
                    break;
                }
                break;
            } catch (Exception ex){
                System.out.println("Vänligen ange ett giltigt val!");
            }
        }

        String takenDates = "";
        for (String date : selectedDates){
            takenDates += " " + date + " = false";
            if (date.equals(endDate)) break;
            takenDates += " AND";
        }

        try{
            PreparedStatement statement = conn.prepareStatement("INSERT INTO bokning (kund_id, antal_personer, måltider, extra_säng, rum_id, start_datum, slut_datum) VALUES (?, ?, ?, ?, ?, ?, ?);");
            statement.setInt(1, customerId);
            statement.setInt(2, numberOfPeople);
            statement.setString(3, mealCost);
            statement.setBoolean(4, extraBed);
            statement.setInt(5, roomId);
            statement.setString(6, startDate);
            statement.setString(7, endDate);
            statement.executeUpdate();

            Statement statement2 = conn.createStatement();
            statement2.executeUpdate("UPDATE rum SET" + takenDates + " WHERE rum_id = " + roomId + ";");

            System.out.println("Bokningen är slutförd!");
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Bokningen misslyckades!");
        }
    }

    public void cancelBookings(){

    }
}
