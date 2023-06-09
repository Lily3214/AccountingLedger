package com.learntocode;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // display home screen using switch
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equalsIgnoreCase("X")) {
            System.out.println("Please select an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            input = scanner.nextLine();
            switch (input) {
                case "D", "d":
                    addDeposit();
                    break;
                case "P", "p":
                    makePayment();
                    break;
                case "L", "l":
                    displayLedger();
                    break;
                case "X", "x":
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    // add deposit using BufferedWriter
    public static void addDeposit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter deposit date(yyyy-MM-dd): ");
        String depositDate = scanner.nextLine();
        System.out.print("Enter deposit time (HH:mm:ss): ");
        String depositTime = scanner.nextLine();
        System.out.print("Enter deposit description: ");
        String depositDescription = scanner.nextLine();
        System.out.print("Enter deposit vendor: ");
        String depositVendor = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double depositAmount = Math.abs(scanner.nextDouble());
        scanner.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            bw.write(depositDate + "|" + depositTime + "|" + depositDescription + "|" + depositVendor + "|" + depositAmount);
            bw.newLine();
            System.out.println("Deposit added successfully.");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }


    // makePayment using BufferedWriter
    public static void makePayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter payment date(yyyy-MM-dd): ");
        String debitDate = scanner.nextLine();
        System.out.print("Enter payment time (HH:mm:ss): ");
        String debitTime = scanner.nextLine();
        System.out.print("Enter payment description: ");
        String debitDescription = scanner.nextLine();
        System.out.print("Enter payment vendor: ");
        String debitVendor = scanner.nextLine();
        System.out.print("Enter payment amount: ");
        double debitAmount = -Math.abs(scanner.nextDouble());
        scanner.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            bw.write(debitDate + "|" + debitTime + "|" + debitDescription + "|" + debitVendor + "|" + debitAmount);
            bw.newLine();
            System.out.println("Payment added successfully.");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    // Ledger options using switch
    public static void displayLedger() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equalsIgnoreCase("X")) {
            System.out.println("Please select the display option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            input = scanner.nextLine();
            switch (input) {
                case "A", "a":
                    displayAllEntries();
                    break;
                case "D", "d":
                    displayDeposits();
                    break;
                case "P", "p":
                    displayPayments();
                    break;
                case "R", "r":
                    runReport();
                    break;
                case "H", "h":
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    // displayAllEntries using BufferedReader. Reads a "transactions.csv" file.
    public static void displayAllEntries() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // displayDeposits using if. Displays the transactions that have positive value in the fifth column of the file.
    public static void displayDeposits() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (Double.parseDouble(parts[4]) > 0) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // displayPayments using if. Displays the transactions that have negative value in the fifth column of the file.
    public static void displayPayments() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (Double.parseDouble(parts[4]) < 0) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    // runReport Home screen using switch
    public static void runReport() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("0")) {
            System.out.println("Please select a report:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    monthToDateReport();
                    break;
                case "2":
                    previousMonthReport();
                    break;
                case "3":
                    yearToDateReport();
                    break;
                case "4":
                    previousYearReport();
                    break;
                case "5":
                    searchByVendorReport();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    // display monthToDateReport
    public static void monthToDateReport() {
        String csvFile = "transactions.csv";
        String line = "";
        String cvsSplitBy = "\\|";
        LocalDate currentDate = LocalDate.now();
        // DateTimeFormatter class is used to parse and format dates in "yyyy-MM-dd" pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentMonth = currentDate.getMonth().toString();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(cvsSplitBy);
                LocalDate transactionDate = LocalDate.parse(transaction[0], formatter);
                //  Reads each line of the CSV file and extracts the transaction date from it.
                String transactionMonth = transactionDate.getMonth().toString();

                // If the transaction date is in the current month, prints out that line.
                if (transactionMonth.equals(currentMonth)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // display previousMonthReport
    public static void previousMonthReport() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Get the date of the first day of the previous month
        LocalDate firstDayOfPreviousMonth = currentDate.minusMonths(1).withDayOfMonth(1);
        // Get the date of the last day of the previous month
        LocalDate lastDayOfPreviousMonth = firstDayOfPreviousMonth.withDayOfMonth(firstDayOfPreviousMonth.lengthOfMonth());

        // Read the CSV file
        String csvFile = "transactions.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\\|";

        // reads the date of the transactions from the
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(cvsSplitBy);
                LocalDate transactionDate = LocalDate.parse(transaction[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (transactionDate.isAfter(firstDayOfPreviousMonth.minusDays(1)) && transactionDate.isBefore(lastDayOfPreviousMonth.plusDays(1))) {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // display yearToDateReport
    public static void yearToDateReport() {
        String csvFile = "transactions.csv";
        String line = "";
        String cvsSplitBy = "\\|";
        // DateTimeFormatter class is used to parse and format dates in "yyyy-MM-dd" pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Get the current date and current year
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(cvsSplitBy);
                LocalDate transactionDate = LocalDate.parse(transaction[0], formatter);
                int transactionYear = transactionDate.getYear();
                // If the transaction date is in the current year, prints out that line.
                if (transactionYear == currentYear) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // display previousYearReport
    public static void previousYearReport() {
        String csvFile = "transactions.csv";
        String line = "";
        String cvsSplitBy = "\\|";

        // Get the first and last day of the previous year
        LocalDate firstDayOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate lastDayOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(365);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(cvsSplitBy);
                // Parse the transaction date from the CSV file
                LocalDate transactionDate = LocalDate.parse(transaction[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Check if the transaction date is within the previous year. The isAfter and isBefore of the transactionDate class are used to check if the transaction
                // date is after the first day of the previous year and before the last day of the previous year.
                if (transactionDate.isAfter(firstDayOfPreviousYear.minusDays(1)) && transactionDate.isBefore(lastDayOfPreviousYear.plusDays(1))) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void searchByVendorReport() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the vendor name: ");
        String vendorName = scanner.nextLine();
        // Display all entries about that vendor
        File file = new File("transactions.csv");
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] fields = line.split("\\|");
            // Checks if the vendor name in the current transaction matches the vendor name
            if (fields[3].equals(vendorName)) {
                System.out.println(line);
            }
        }
        fileScanner.close();
    }

}
