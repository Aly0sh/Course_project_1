package com.company;

import java.sql.*;
import java.util.Scanner;
public class Main {
    private static String log;
    private static int Position;
    static String file_name = "apartment";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String [] args){
        Main authorization = new Main();

        if (authorization.open(file_name)){
            authorization.connect();
        }
    }
    Connection co;

    boolean open(String file_name){
        try{
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:D:\\INAI\\Java\\Курсовая\\course_workk\\" + file_name + ".db");
            return true;
        }
        catch (Exception error){
            System.out.println(error.getMessage());
            return false;
        }
    }


    void connect() {
        boolean end = false;
        int number_of_attempts = 0;
        while (!end) {
            open(file_name);
            System.out.print(ANSI_CYAN + "Print number of your position:\n" +
                    "1. Owner\n"+
                    "2. Delivery\n"+
                    "3. Foreman\n" +
                    "4. exit the program\n>>> " + ANSI_RESET
            );
            Scanner in = new Scanner(System.in);
            String pos = in.next();
            if (pos.equals("1")){
                try{
                    System.out.println(ANSI_YELLOW +"We have connected successfully" + ANSI_RESET);
                    System.out.println();
                    Statement statement = co.createStatement();
                    boolean connected = false;
                    while (connected == false) {
                        System.out.print(ANSI_CYAN + "Enter your login\n>>> ");
                        String login = in.next();
                        System.out.print("Enter your password\n>>> " + ANSI_RESET);
                        String password = in.next();
                        String query = "SELECT * " +
                                "FROM owners WHERE login LIKE '" + login + "' AND password LIKE '" + password + "'";
                        ResultSet resultSet = statement.executeQuery(query);
                        number_of_attempts++;
                        if (resultSet.next()) {
                            System.out.println(ANSI_YELLOW + "You have access to the program" + ANSI_RESET);
                            connected = true;
                            log = resultSet.getString("login");
                            Position=1;
                        } else {
                            System.out.println(ANSI_BLUE + "Attempts left: " + (3-number_of_attempts) + ANSI_RESET);
                            if (number_of_attempts == 3){
                                System.out.println(ANSI_RED + "No attempts left\n" + ANSI_RESET);
                                break;
                            }
                            System.out.println(ANSI_RED + "\nPlease try again\n" + ANSI_RESET);
                            System.out.println(ANSI_CYAN + "---------------------" + ANSI_RESET);
                        }
                    }
                    if (connected){
                        co.close();
                        Owner.main(log);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (pos.equals("2")){
                try{
                    System.out.println(ANSI_YELLOW +"We have connected successfully" + ANSI_RESET);
                    System.out.println();
                    Statement statement = co.createStatement();
                    boolean connected = false;
                    while (connected == false) {
                        System.out.print(ANSI_CYAN + "Enter your login\n>>> ");
                        String login = in.next();
                        System.out.print("Enter your password\n>>> " + ANSI_RESET);
                        String password = in.next();
                        String query = "SELECT * " +
                                "FROM delivery WHERE Login LIKE '" + login + "' AND Password LIKE '" + password + "'";
                        ResultSet resultSet = statement.executeQuery(query);
                        number_of_attempts++;
                        if (resultSet.next()) {
                            System.out.println(ANSI_YELLOW + "You have access to the program" + ANSI_RESET);
                            connected = true;
                            log = resultSet.getString("login");
                            Position = 2;
                        } else {
                            System.out.println(ANSI_BLUE + "Attempts left: " + (3-number_of_attempts) + ANSI_RESET);
                            if (number_of_attempts == 3){
                                System.out.println(ANSI_RED + "No attempts left\n" + ANSI_RESET);
                                break;
                            }
                            System.out.println(ANSI_RED + "\nPlease try again\n" + ANSI_RESET);
                            System.out.println(ANSI_CYAN + "---------------------" + ANSI_RESET);
                        }
                    }
                    if (connected){
                        co.close();
                        Deliveryman.main();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (pos.equals("3")){
                try {
                    System.out.println(ANSI_YELLOW +"We have connected successfully" + ANSI_RESET);
                    System.out.println();
                    Statement statement = co.createStatement();
                    boolean connected = false;
                    while (connected == false) {
                        System.out.print(ANSI_CYAN + "Enter your login\n>>> ");
                        String login = in.next();
                        System.out.print("Enter your password\n>>> " + ANSI_RESET);
                        String password = in.next();
                        String query = "SELECT * " +
                                "FROM password WHERE login LIKE '" + login + "' AND password LIKE '" + password + "'";
                        ResultSet resultSet = statement.executeQuery(query);
                        number_of_attempts++;

                        if (resultSet.next()) {
                            System.out.println(ANSI_YELLOW + "You have access to the program" + ANSI_RESET);
                            connected = true;
                            log = resultSet.getString("login");
                            Position = 3;
                        } else {
                            System.out.println(ANSI_BLUE + "Attempts left: " + (3-number_of_attempts) + ANSI_RESET);
                            if (number_of_attempts == 3){
                                System.out.println(ANSI_RED + "No attempts left\n" + ANSI_RESET);
                                break;
                            }
                            System.out.println(ANSI_RED + "\nPlease try again\n" + ANSI_RESET);
                            System.out.println(ANSI_CYAN + "---------------------" + ANSI_RESET);
                        }
                    }
                    if (connected){
                        co.close();
                        Foreman.main();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (pos.equals("4")){
                System.out.println(ANSI_YELLOW + "The program is over. We look forward to your return!" + ANSI_RESET);
                end = true;
            }
            else{
                System.out.println(ANSI_RED + "There is not such account!" + ANSI_RESET);
            }
            number_of_attempts = 0;
        }
    }
}