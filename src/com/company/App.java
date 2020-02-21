package com.company;

import javax.xml.transform.Result;
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
            System.out.println("Välkommen till HolidayMaker!\n");
            System.out.println("Ange ett alternativ för att fortsätta: (0-3)");
            System.out.println("(1) Registrera ny kund");
            System.out.println("(2) Sök lediga rum");
            System.out.println("(3) Avboka/ändra bokningar");
            System.out.println("\n(0) Avsluta programmet");

            String selection = scanner.nextLine();

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
                    System.out.println("Vänligen välj ett giltigt alternativ! (0-3)");
                    break;
            }
        } while (true);
    }

    public void registerCustomer() throws SQLException {
        System.out.println("Ange kundens namn eller '0' för att återgå till huvudmenyn:");
        String customerName = scanner.nextLine();
        if (customerName.equals("0")) return;
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

        ArrayList<Integer> kundIds = new ArrayList<>();

        try {
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

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        while (true) {
            System.out.println("\nAnge kund-ID för bokningen eller '0' för att gå tillbaka: ");
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                if (!kundIds.contains(selection)) throw new IndexOutOfBoundsException();
                customerId = selection;
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt kund-ID eller '0' för att gå tillbaka");
            }
        }

        while (true) {
            System.out.println("Ange antal personer för bokningen eller '0' för att gå tillbaka:");
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

        for (String date : allDates) {
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

        for (int i = 0; i < datesAfterStartDate.length; i++) {
            selectedDates.add(datesAfterStartDate[i]);
            if (datesAfterStartDate[i].equals(endDate)) {
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

            switch (selection) {
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
        for (String date : selectedDates) {
            dateString += " r." + date + " = true AND";
        }

        ArrayList<Integer> hotel_ids = new ArrayList<>();
        ArrayList<SearchResult> sortedResults = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT b.boende_id, b.namn, b.pool, b.restaurang, b.kvällsunderhållning, b.barnklubb, b.avstånd_strand, b.avstånd_centrum, b.omdöme, r.pris, r.pris_halvpension, r.pris_helpension, r.pris_extrasäng " +
                    "FROM boende b JOIN rum_med_pris r ON b.boende_id = r.boende_id " +
                    "WHERE" + dateString + poolIsNecessary + restaurantIsNecessary + entertainmentIsNecessary + kidsClubIsNecessary + " avstånd_strand < " + distanceToBeach + " AND avstånd_centrum < " + distanceToCenter + " AND r.antal_sängar = " + numberOfPeople +
                    " GROUP BY b.namn;");

            if (!resultSet.next()) {
                System.out.println("Vi kunde inte hitta några lediga rum med de angivna kriterierna!");
                return;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                Statement anotherStatement = conn.createStatement();
                ResultSet amountOfRooms = anotherStatement.executeQuery("SELECT COUNT(r.rum_id) FROM rum r WHERE" + dateString + " r.boende_id = " + resultSet.getInt("boende_id") + " AND r.antal_sängar = " + numberOfPeople + ";");
                int rooms = 0;
                int price = resultSet.getInt("pris");

                while (amountOfRooms.next()) {
                    rooms = amountOfRooms.getInt(1);
                }

                if (extraBed) {
                    price += resultSet.getInt("pris_extrasäng");
                }

                if (mealCost.equals("half")) {
                    price += resultSet.getInt("pris_halvpension");
                } else if (mealCost.equals("whole")) {
                    price += resultSet.getInt("pris_helpension");
                }
                int totalCost = price * (selectedDates.size() - 1);

                int boende_id = resultSet.getInt("boende_id");
                String name = resultSet.getString("namn");
                boolean pool = resultSet.getBoolean("pool");
                boolean restaurant = resultSet.getBoolean("restaurang");
                boolean entertainment = resultSet.getBoolean("kvällsunderhållning");
                boolean kidsClub = resultSet.getBoolean("barnklubb");
                float disBeach = resultSet.getFloat("avstånd_strand");
                float disCenter = resultSet.getFloat("avstånd_centrum");
                float reviewScore = resultSet.getFloat("omdöme");

                hotel_ids.add(boende_id);

                sortedResults.add(new SearchResult(boende_id, name, pool, restaurant, entertainment, kidsClub, disBeach, disCenter, reviewScore, numberOfPeople, rooms, price, totalCost));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nEtt fel inträffade!\n");
        }

        for (SearchResult searchResult : sortedResults) {
            System.out.println(searchResult);
        }

        while (true) {
            System.out.println("Ange ett val (1-3) eller '0' för att avsluta sökningen\n");
            System.out.println("(1) Sortera efter pris");
            System.out.println("(2) Sortera efter omdöme");
            System.out.println("(3) Boka ett rum");


            String selection = scanner.nextLine();

            switch (selection) {
                case "1":
                    Collections.sort(sortedResults, SearchResult.sortByPrice);
                    for (SearchResult res : sortedResults) System.out.println(res);
                    break;
                case "2":
                    Collections.sort(sortedResults, SearchResult.sortByReview);
                    for (SearchResult res : sortedResults) System.out.println(res);
                    break;
                case "3":
                    int roomId = 0;
                    while (true) {
                        System.out.println("\nBoka ett rum mellan " + startDate + " och " + endDate + " genom att ange siffran till vänster om boendet eller 0 för att avsluta sökningen");
                        try {
                            int selection2 = Integer.parseInt(scanner.nextLine());
                            if (selection2 == 0) return;
                            if (!hotel_ids.contains(selection2)) {
                                throw new IndexOutOfBoundsException();
                            }
                            Statement statement = conn.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT r.rum_id FROM rum r WHERE" + dateString + " r.antal_sängar = " + numberOfPeople + " AND r.boende_id = " + selection2 + ";");

                            while (resultSet.next()) {
                                roomId = resultSet.getInt("rum_id");
                                break;
                            }
                            break;
                        } catch (Exception ex) {
                            System.out.println("Vänligen ange ett giltigt val!");
                        }
                    }

                    String takenDates = "";
                    selectedDates.remove(endDate);
                    for (int i = 0; i < selectedDates.size(); i++) {
                        takenDates += " " + selectedDates.get(i) + " = false";
                        if (i == selectedDates.size() - 1) break;
                        takenDates += ",";
                    }

                    try {
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Bokningen misslyckades!");
                    }
                    return;
                case "0":
                    return;
                default:
                    System.out.println("Vänligen ange ett giltigt val! (0-3)");
            }
        }
    }

    public void cancelBookings(){

        ArrayList<String> bookings = new ArrayList<>();
        ArrayList<Integer> bookingIds = new ArrayList<>();
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM bokningar");

            while (resultSet.next()){
                String row = "Boknings-ID: " + resultSet.getInt("bokning_id")
                        + ", Kund-ID: " + resultSet.getInt("kund_id")
                        + ", Kundens Namn: " + resultSet.getString("namn")
                        + ", Antal personer: " + resultSet.getInt("antal_personer")
                        + ", Måltidsval: " + resultSet.getString("måltider")
                        + ", Extrasäng: " + resultSet.getBoolean("extra_säng")
                        + ", Rums-ID: " + resultSet.getInt("rum_id")
                        + ", Startdatum: " + resultSet.getString("start_datum")
                        + ", Slutdatum: " + resultSet.getString("slut_datum");
                bookings.add(row);
                bookingIds.add(resultSet.getInt("bokning_id"));
            }
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Något gick fel! Du returneras nu till huvudmenyn!");
            return;
        }

        while (true) {
            System.out.println("Ange ett alternativ (0-2)");
            System.out.println("(1) Avboka en bokning");
            System.out.println("(2) Ändra en bokning\n");
            System.out.println("(0) Återgå till huvudmenyn");

            String selection = scanner.nextLine();

            switch (selection){
                case "1":
                    cancelBooking(bookings, bookingIds);
                    return;
                case "2":
                    changeBooking(bookings, bookingIds);
                    return;
                case "0":
                    return;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ (0-2)");
            }
        }
    }

    public void cancelBooking(ArrayList<String> bookings, ArrayList<Integer> bookingIds){
        for (String booking : bookings) System.out.println(booking);

        int selection;
        while (true) {
            System.out.println("\nAnge boknings-id för den bokning du vill avboka eller '0' för att gå tillbaka: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                for (int id : bookingIds){
                    if (selection == id){
                        try {
                            Statement statement = conn.createStatement();
                            statement.executeUpdate("DELETE FROM bokning WHERE bokning_id = " + id + ";");
                            System.out.println("Bokning nr " + id + " är nu avbokad!");
                        } catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("Avbokningen misslyckades!");
                        }
                        break;
                    }
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt boknings-id!");
            }
        }
    }

    public void changeBooking(ArrayList<String> bookings, ArrayList<Integer> bookingIds){
        for (String booking : bookings) System.out.println(booking);

        int selection;
        String change = "";
        while (true) {
            System.out.println("\nAnge boknings-id för den bokning du vill ändra eller '0' för att gå tillbaka: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                for (int id : bookingIds){
                    if (selection == id){
                        boolean menuBool1 = true;
                        while (menuBool1) {
                            System.out.println("Ange vilken del av bokningen kunden vill ändra eller '0' för att gå tillbaka: ");
                            System.out.println("(1) Måltidsval");
                            System.out.println("(2) Extrasäng");

                            String choice = scanner.nextLine();
                            switch (choice) {
                                case "1":
                                    boolean mealBool = true;
                                    do {
                                        System.out.println("Ange ett alternativ att ändra måltidsval till eller '0' för att gå tillbaka: ");
                                        System.out.println("(1) Inget");
                                        System.out.println("(2) Halvpension");
                                        System.out.println("(3) Helpension");

                                        String select = scanner.nextLine();

                                        switch (select) {
                                            case "1":
                                                change = "none";
                                                break;
                                            case "2":
                                                change = "half";
                                                break;
                                            case "3":
                                                change = "whole";
                                                break;
                                            case "0":
                                                break;
                                            default:
                                                System.out.println("Vänligen ange ett giltigt alternativ! (0-3)");
                                        }
                                        if (!change.equals("")) mealBool = false;
                                    } while (mealBool);

                                    try {
                                        Statement statement = conn.createStatement();
                                        statement.executeUpdate("UPDATE bokning SET måltider = '" + change + "' WHERE bokning_id = " + id + ";");
                                        System.out.println("Bokningen är nu ändrad!");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        System.out.println("Ändringen misslyckades!");
                                    }
                                    menuBool1 = false;
                                    break;
                                case "2":
                                    boolean extraBed = true;
                                    while (true) {
                                        System.out.println("Vill kunden ha en extrasäng? (y/n)");
                                        String yOrN = scanner.nextLine().toLowerCase();

                                        if (yOrN.equals("n")) {
                                            extraBed = false;
                                            break;
                                        } else if (yOrN.equals("y")) {
                                            break;
                                        } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
                                    }
                                    try {
                                        Statement statement = conn.createStatement();
                                        statement.executeUpdate("UPDATE bokning SET extra_säng = " + extraBed + " WHERE bokning_id = " + id + ";");
                                        System.out.println("Bokningen har nu ändrats!\n");
                                        break;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        System.out.println("Ändringen misslyckades!\n");
                                    }
                                    menuBool1 = false;
                                    break;
                                case "0":
                                    menuBool1 = false;
                                    break;
                                default:
                                    System.out.println("Vänligen ange ett giltigt alternativ! (0-2)");
                            }
                        }
                        break;
                    }
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt boknings-id!");
            }
        }
    }
}
