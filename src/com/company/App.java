package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class App {

    Scanner scanner = new Scanner(System.in);
    Connection conn = DriverManager.getConnection("jdbc:mysql://", "", "");

    public App() throws SQLException {
    }

    public void run() throws Exception {

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

    public void registerCustomer() throws Exception {
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

    public void searchForRooms() throws Exception {

        int customerId;
        int numberOfPeople;
        String yesOrNo;
        float distanceToBeach;
        float distanceToCenter;
        boolean extraBed;
        String mealCost = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString;
        String endDateString;
        java.util.Date startDate = null;
        java.util.Date endDate = null;

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
            System.out.println("Ange antal personer för bokningen (1-5) eller '0' för att gå tillbaka:");
            try {
                numberOfPeople = Integer.parseInt(scanner.nextLine());
                if (numberOfPeople == 0) return;
                if (numberOfPeople < 0 ||numberOfPeople > 5) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt alternativ! (0-5)");
            }
        }

        while (true) {
            System.out.println("Ange startdatum för vistelsen: (ex. '2020-06-23')");
            try {
                startDateString = scanner.nextLine();
                startDate = f.parse(startDateString);
                if (startDate.before(f.parse("2020-08-01")) && startDate.after(f.parse("2020-05-31"))) {
                    if (startDateString.equals("2020-07-31")) {
                        System.out.println("Det går inte att starta en vistelse på säsongens sista dag!");
                    }
                    break;
                } else throw new IndexOutOfBoundsException();
            } catch (Exception ex){
                System.out.println("Vänligen ange ett datum enligt formatet '2020-06-23'");
            }
        }

        while (true) {
            System.out.println("Ange slutdatum för vistelsen: (ex. '2020-06-27')");
            try {
                endDateString = scanner.nextLine();
                endDate = f.parse(endDateString);
                if (endDate.after(startDate) && endDate.before(f.parse("2020-08-01"))){
                    break;
                } else throw new IndexOutOfBoundsException();
            } catch (Exception ex){
                System.out.println("Vänligen ange ett datum enligt formatet '2020-06-27'");
            }
        }

        long diff = endDate.getTime() - startDate.getTime();
        long nights = (diff / (1000*60*60*24));

        while (true) {
            System.out.println("Måste boendet erbjuda swimmingpool? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.equals("y")) {
                poolIsNecessary = "pool = 1 AND ";
                break;
            } else if (yesOrNo.equals("n")) {
                poolIsNecessary = "";
                break;
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
        }

        while (true) {
            System.out.println("Måste boendet erbjuda restaurang? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();

            if (yesOrNo.equals("y")) {
                restaurantIsNecessary = "restaurang = 1 AND ";
                break;
            } else if (yesOrNo.equals("n")) {
                restaurantIsNecessary = "";
                break;
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
        }

        while (true) {
            System.out.println("Måste boendet erbjuda kvällsunderhållning? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();

            if (yesOrNo.equals("y")) {
                entertainmentIsNecessary = "kvällsunderhållning = 1 AND ";
                break;
            } else if (yesOrNo.equals("n")) {
                entertainmentIsNecessary = "";
                break;
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
        }

        while (true) {
            System.out.println("Måste boendet erbjuda barnklubb? (y/n)");
            yesOrNo = scanner.nextLine().toLowerCase();

            if (yesOrNo.equals("y")) {
                kidsClubIsNecessary = "barnklubb = 1 AND ";
                break;
            } else if (yesOrNo.equals("n")) {
                kidsClubIsNecessary = "";
                break;
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
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
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
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
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
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
            } else System.out.println("Vänligen ange ett giltigt alternativ! (y/n)");
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

        ArrayList<Integer> hotel_ids = new ArrayList<>();
        ArrayList<SearchResult> sortedResults = new ArrayList<>();

        java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
        java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT b.boende_id, b.namn, b.pool, b.restaurang, b.kvällsunderhållning, b.barnklubb, b.avstånd_strand, b.avstånd_centrum, b.omdöme, r.pris, r.pris_halvpension, r.pris_helpension, r.pris_extrasäng, r.checkin, r.checkout " +
                    "FROM boende b LEFT JOIN rum_pris_bokningar r ON b.boende_id = r.boende_id " +
                    "WHERE (" + poolIsNecessary + restaurantIsNecessary + entertainmentIsNecessary + kidsClubIsNecessary + "avstånd_strand < " + distanceToBeach + " AND avstånd_centrum < " + distanceToCenter + " AND r.antal_sängar = " + numberOfPeople + ") AND (r.checkin is null OR ('" + startDateSql + "' < r.checkin" + " OR '" + startDateSql + "' >= r.checkout)" + " AND ('" + endDateSql + "' <= r.checkin OR '" + endDateSql + "' > r.checkout))" +
                    " GROUP BY b.namn;");

            if (!resultSet.next()) {
                System.out.println("Vi kunde inte hitta några lediga rum med de angivna kriterierna!");
                return;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                Statement anotherStatement = conn.createStatement();
                ResultSet amountOfRooms = anotherStatement.executeQuery("SELECT COUNT(r.rum_id) FROM rum_pris_bokningar r WHERE (r.boende_id = " + resultSet.getInt("boende_id") + " AND r.antal_sängar = " + numberOfPeople + ") AND (r.checkin is null OR ('" + startDateSql + "' < r.checkin" + " OR '" + startDateSql + "' >= r.checkout)" + " AND ('" + endDateSql + "' <= r.checkin OR '" + endDateSql + "' > r.checkout));");
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

                long totalCost = price * (nights);

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
                        System.out.println("\nBoka ett rum mellan " + startDateString + " och " + endDateString + " genom att ange siffran till vänster om boendet eller '0' för att avsluta sökningen:");
                        try {
                            int selection2 = Integer.parseInt(scanner.nextLine());
                            if (selection2 == 0) return;
                            if (!hotel_ids.contains(selection2)) {
                                throw new IndexOutOfBoundsException();
                            }
                            Statement statement = conn.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT r.rum_id FROM rum_pris_bokningar r WHERE (r.antal_sängar = " + numberOfPeople + " AND r.boende_id = " + selection2 + ") AND (r.checkin is null OR ('" + startDateSql + "' < r.checkin" + " OR '" + startDateSql + "' >= r.checkout)" + " AND ('" + endDateSql + "' <= r.checkin OR '" + endDateSql + "' > r.checkout));");

                            while (resultSet.next()) {
                                roomId = resultSet.getInt("rum_id");
                                break;
                            }
                            break;
                        } catch (Exception ex) {
                            System.out.println("Vänligen ange ett giltigt alternativ!\n");
                        }
                    }

                    try {
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO bokningar (kund_id, antal_personer, måltider, extra_säng, rum_id, checkin, checkout) VALUES (?, ?, ?, ?, ?, ?, ?);");
                        statement.setInt(1, customerId);
                        statement.setInt(2, numberOfPeople);
                        statement.setString(3, mealCost);
                        statement.setBoolean(4, extraBed);
                        statement.setInt(5, roomId);
                        statement.setDate(6, startDateSql);
                        statement.setDate(7, endDateSql);
                        statement.executeUpdate();

                        System.out.println("Bokningen är slutförd!\n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Bokningen misslyckades!\n");
                    }
                    return;
                case "0":
                    return;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ! (0-3)\n");
            }
        }
    }

    public void cancelBookings(){

        ArrayList<String> bookings = new ArrayList<>();
        ArrayList<Integer> bookingIds = new ArrayList<>();
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM bokningarview");

            if (!resultSet.next()) {
                System.out.println("Det finns inga bokningar i systemet!\n");
                return;
            }

            resultSet.beforeFirst();

            while (resultSet.next()){
                String row = "Boknings-ID: " + resultSet.getInt("bokning_id")
                        + ", Kund-ID: " + resultSet.getInt("kund_id")
                        + ", Kundens Namn: " + resultSet.getString("namn")
                        + ", Antal personer: " + resultSet.getInt("antal_personer")
                        + ", Måltidsval: " + resultSet.getString("måltider")
                        + ", Extrasäng: " + resultSet.getBoolean("extra_säng")
                        + ", Rums-ID: " + resultSet.getInt("rum_id")
                        + ", Startdatum: " + resultSet.getString("checkin")
                        + ", Slutdatum: " + resultSet.getString("checkout");
                bookings.add(row);
                bookingIds.add(resultSet.getInt("bokning_id"));
            }
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Något gick fel! Du returneras nu till huvudmenyn!\n");
            return;
        }

        while (true) {
            System.out.println("Ange ett alternativ (0-2):");
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
                    System.out.println("Vänligen ange ett giltigt alternativ! (0-2)\n");
            }
        }
    }

    public void cancelBooking(ArrayList<String> bookings, ArrayList<Integer> bookingIds){
        for (String booking : bookings) System.out.println(booking);

        int selection;
        while (true) {
            System.out.println("Ange boknings-id för den bokning du vill avboka eller '0' för att gå tillbaka: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                if (!bookingIds.contains(selection)) throw new IndexOutOfBoundsException();
                for (int id : bookingIds){
                    if (selection == id){
                        try {
                            Statement statement = conn.createStatement();
                            statement.executeUpdate("DELETE FROM bokningar WHERE bokning_id = " + id + ";");
                            System.out.println("Bokning nr " + id + " är nu avbokad!\n");
                        } catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("Avbokningen misslyckades!\n");
                        }
                        break;
                    }
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt boknings-id!\n");
            }
        }
    }

    public void changeBooking(ArrayList<String> bookings, ArrayList<Integer> bookingIds){
        for (String booking : bookings) System.out.println(booking);

        int selection;
        while (true) {
            System.out.println("Ange boknings-id för den bokning du vill ändra eller '0' för att gå tillbaka: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection == 0) return;
                if (!bookingIds.contains(selection)) throw new IndexOutOfBoundsException();
                for (int id : bookingIds){
                    if (selection == id){
                        chooseWhatToChange(id);
                        break;
                    }
                }
                break;
            } catch (Exception ex) {
                System.out.println("Vänligen ange ett giltigt boknings-id!\n");
            }
        }
    }

    public boolean extraBed(){
        while (true) {
            System.out.println("Vill kunden ha en extrasäng? (y/n):");
            String yOrN = scanner.nextLine().toLowerCase();

            if (yOrN.equals("n")) {
                return false;
            } else if (yOrN.equals("y")) {
                return true;
            } else {
                System.out.println("Vänligen ange ett giltigt alternativ! (y/n)\n");
            }
        }
    }

    public void chooseWhatToChange(int id){
        while (true) {
            System.out.println("Ange vilken del av bokningen kunden vill ändra eller '0' för att gå tillbaka: ");
            System.out.println("(1) Måltidsval");
            System.out.println("(2) Extrasäng");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    String change = getMealType();
                    if (change == null) break;
                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate("UPDATE bokningar SET måltider = '" + change + "' WHERE bokning_id = " + id + ";");
                        System.out.println("Bokningen har nu ändrats!\n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Ändringen misslyckades!\n");
                    }
                    break;
                case "2":
                    boolean extraBed = extraBed();
                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate("UPDATE bokningar SET extra_säng = " + extraBed + " WHERE bokning_id = " + id + ";");
                        System.out.println("Bokningen har nu ändrats!\n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("Ändringen misslyckades!\n");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ! (0-2)\n");
            }
        }
    }

    public String getMealType(){
        do {
            System.out.println("Ange ett alternativ att ändra måltidsval till eller '0' för att gå tillbaka: ");
            System.out.println("(1) Inget");
            System.out.println("(2) Halvpension");
            System.out.println("(3) Helpension");

            String select = scanner.nextLine();

            switch (select) {
                case "1":
                    return "none";
                case "2":
                    return "half";
                case "3":
                    return "whole";
                case "0":
                    return null;
                default:
                    System.out.println("Vänligen ange ett giltigt alternativ! (0-3)\n");
            }
        } while (true);
    }
}
