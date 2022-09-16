package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Owner {
    static Scanner in = new Scanner(System.in);
    static String login;
    static String file_name = "apartment";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String login_of_owner){
        Owner owner = new Owner();
        login = login_of_owner;
        owner.menu();

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

    void close(){
        try{
            co.close();
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
    }

    void print_building_materials(){
        try{
            Statement statement = co.createStatement();
            String query =
                    "SELECT * " + "From " + login;
            ResultSet rs = statement.executeQuery(query);
            System.out.println(ANSI_YELLOW + "name of material              manufacturer                    price");
            System.out.println("----------------              ------------                    -----" + ANSI_RESET);
            while(rs.next()){
                String name_of_material = rs.getString("name_of_material");
                String manufacturer = rs.getString("manufacturer");
                int price = rs.getInt("price");
                Object[] objects = {name_of_material, manufacturer, price};
                for (Object o:objects){
                    System.out.print(o + " ");
                    for (int k = 0; k < (30-o.toString().length()); k++){
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    boolean found;
    String material_n;
    String manufactureR;
    void find_building_material(String material_or_manufacturer_name){
        try{
            Statement statement = co.createStatement();
            String query =
                    "SELECT * " + "From " + login;
            ResultSet rs = statement.executeQuery(query);

            System.out.println(ANSI_YELLOW + "name of material              manufacturer                    price");
            System.out.println("----------------              ------------                    -----" + ANSI_RESET);

            while(rs.next()){
                String name_of_material = rs.getString("name_of_material");
                String manufacturer = rs.getString("manufacturer");
                int price = rs.getInt("price");
                Object[] objects = {name_of_material, manufacturer, price};
                for (Object o:objects){
                    if ((material_or_manufacturer_name.equals(name_of_material)) || (material_or_manufacturer_name.equals(manufacturer))){
                        System.out.print(o + " ");
                        for (int k = 0; k < (30-o.toString().length()); k++){
                            System.out.print(" ");
                        }
                        material_n = name_of_material;
                        manufactureR = manufacturer;
                        found = true;
                    }
                }
                System.out.println();
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void order_materials(String material, int quantity){
        try{
            String query =
                    "INSERT INTO ordered_materials ('owner_name', 'name_of_material', " +
                            "'manufacturer', 'number_or_square_meter', 'date_of_ordered') " +
                            "VALUES ('" + login + "', '" + material + "', '" + manufactureR + "', '" +
                            quantity + "', '" + LocalDate.now() + "');";
            Statement statement = co.createStatement();
            statement.executeUpdate(query);
            statement.close();

            System.out.println("Rows added");
        }
        catch(Exception error){
            System.out.println(error);
        }
    }

    void cancel_order(){
        try{
            Scanner in = new Scanner(System.in);
            System.out.print(ANSI_CYAN + "enter name of material >>> " + ANSI_RESET);
            String material = in.next();

            String query =
                    "DELETE FROM ordered_materials WHERE owner_name = '" + login + "' AND name_of_material = '" + material + "';";

            Statement statement = co.createStatement();
            statement.executeUpdate(query);
            statement.close();

        }
        catch(Exception error){
            System.out.println(error);
        }
    }


    void print_ordered_materials() {
        try{
            Statement statement = co.createStatement();
            String query =
                    "SELECT * " + "From ordered_materials";
            ResultSet rs = statement.executeQuery(query);

            System.out.println(ANSI_YELLOW + "owner name                    name of material              " +
                    "number or square meter          date of ordered");
            System.out.println("----------                    ----------------              " +
                    "----------------------          ---------------" + ANSI_RESET);

            while(rs.next()){
                String owner_name = rs.getString("owner_name");
                String name_of_material = rs.getString("name_of_material");
                int number_or_square_meter = rs.getInt("number_or_square_meter");
                String date_of_ordered= rs.getString("date_of_ordered");
                Object[] objects = {owner_name, name_of_material, number_or_square_meter, date_of_ordered};
                for (Object o:objects){
                    System.out.print(o + " ");
                    for (int k = 0; k < (30-o.toString().length()); k++){
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            rs.close();
            statement.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void menu(){
        open(file_name);
        System.out.println(ANSI_YELLOW + "\nHello, " + login + "!" + ANSI_RESET);
        System.out.println();
        boolean end = false;
        String commands = "List of owner command\n" +
                "0 - Logout\n" +
                "1 - show all list of building materials\n" +
                "2 - find building material\n" +
                "3 - order\n" +
                "4 - cancel the order\n" +
                "5 - show ordered list\n";
        System.out.println(ANSI_YELLOW + "Enter \"help\" to show list of command" + ANSI_RESET);
        while(!end){
            System.out.println(ANSI_CYAN + "--------------------");
            System.out.print("Enter the command >>> " + ANSI_RESET);
            String menu_number = in.next();

            switch (menu_number){
                case "0":
                    end = true;
                    close();
                    System.out.println(ANSI_YELLOW + "You logged out" + ANSI_RESET);
                    break;
                case "1":
                    print_building_materials();
                    break;
                case "2":
                    System.out.print(ANSI_CYAN + "Input the name of material >>> " + ANSI_RESET);
                    String searching_material = in.next();
                    in.nextLine();
                    find_building_material(searching_material);
                    break;
                case "3":
                    try {
                        System.out.print(ANSI_CYAN + "Input the name of material >>> " + ANSI_RESET);
                        String material_name = in.next();
                        in.nextLine();
                        System.out.print(ANSI_CYAN + "Input the number of material >>> " + ANSI_RESET);
                        int quantity = in.nextInt();
                        find_building_material(material_name);
                        if (found){
                            order_materials(material_name, quantity);
                        }
                        else {
                            System.out.println(ANSI_YELLOW + "can't be ordered" + ANSI_YELLOW);
                        }
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    found = false;

                    break;
                case "4":
                    cancel_order();
                    break;
                case "5":
                    print_ordered_materials();
                    break;
                case "help":
                    System.out.println(ANSI_YELLOW + commands + ANSI_RESET);
                    break;
                default:
                    System.out.println(ANSI_RED + "Command entered incorrect" + ANSI_RESET);
                    System.out.println(ANSI_YELLOW + "Enter \"help\" to show list of command" + ANSI_RESET);
            }
            System.out.println("\n");
        }
    }
}
