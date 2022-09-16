package com.company;

import java.sql.*;
import java.util.Scanner;

public class Deliveryman {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static void main(String ... args) {
        Deliveryman deliveryman = new Deliveryman();
        if (deliveryman.open()){
            deliveryman.menu();
        }
    }

    Connection co;
    boolean open(){
        boolean connect=false;
        try{
            Class.forName("org.sqlite.JDBC");
            co= DriverManager.getConnection("jdbc:sqlite:D:\\INAI\\Java\\Курсовая\\course_workk\\apartment.db");
            connect=true;
        }
        catch (Exception error){
            System.out.println(error.getMessage());
        }
        return connect;
    }
    void command1(){
        try {
            String query = "SELECT * FROM ordered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            String material, manufacturer, owner, number, date;
            System.out.println(ANSI_YELLOW + "Material                      Manufacturer                  Owner" +
                    "                         Number                              Date");
            System.out.println("________                      ____________                  _____" +
                    "                         ______                       ___________" + ANSI_RESET);
            while (rs.next()) {
                owner = rs.getString("owner_name");
                material = rs.getString("name_of_material");
                manufacturer = rs.getString("manufacturer");
                number = rs.getString("number_or_square_meter");
                date = rs.getString("date_of_ordered");
                System.out.print(material);
                for (int i = material.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(manufacturer);
                for (int i = manufacturer.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(owner);
                for (int i = owner.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(number);
                for (int i = number.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(date);
                for (int i = date.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    void command2(){
        try {
            String query = "SELECT * FROM delivered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            String owner, material, manufacturer, number, cost;
            System.out.println(ANSI_YELLOW + "Owner                         Material                      Manufacturer" +
                    "                  Number                        Shipping rate");
            System.out.println("_____                         ________                      ____________" +
                    "                  ______                        _____________" + ANSI_RESET);
            while (rs.next()) {
                owner = rs.getString("owner_name");
                material = rs.getString("name_of_material");
                manufacturer = rs.getString("manufacturer");
                number = rs.getString("number");
                cost = rs.getString("shipping_rate");
                System.out.print(owner);
                for (int i = owner.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(material);
                for (int i = material.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(manufacturer);
                for (int i = manufacturer.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(number);
                for (int i = number.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(cost);
                for (int i = cost.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    boolean found = false;

    String owner_name;
    String material_name;
    String manufactureR;
    int quantity;
    void find_building_material(String material_n, String owner_n){
        try{
            Statement statement = co.createStatement();
            String query =
                    "SELECT * " +
                            "From ordered_materials";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                String owner_Name = rs.getString("owner_name");
                String name_of_material = rs.getString("name_of_material");
                String manufacturer = rs.getString("manufacturer");
                int n = rs.getInt("number_or_square_meter");
                Object[] objects = {name_of_material, manufacturer};
                for (Object o:objects){
                    if ((name_of_material.equals(material_n)) & (owner_Name.equals(owner_n))){
                        owner_name = owner_n;
                        material_name = material_n;
                        manufactureR = manufacturer;
                        quantity = n;
                        found = true;
                    }
                }
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void deliver_materials(int shipping_rate){
        try{
            String query =
                    "INSERT INTO delivered_materials ('owner_name', 'name_of_material', 'manufacturer', 'number', 'shipping_rate') " +
                            "VALUES ('" + owner_name + "', '" + material_name + "', '" + manufactureR + "', '" +quantity + "', '" + shipping_rate + "');";
            Statement statement = co.createStatement();
            statement.executeUpdate(query);
            statement.close();

            System.out.println(ANSI_YELLOW + "Row added" + ANSI_RESET);
        }
        catch(Exception error){
            System.out.println(error);
        }
    }

    void delete_order(){
        try{
            String query =
                    "DELETE FROM ordered_materials WHERE name_of_material = '" + material_name + "';";


            Statement statement = co.createStatement();
            statement.executeUpdate(query);
            statement.close();

            System.out.println(ANSI_YELLOW + "Row deleted" + ANSI_RESET);
        }
        catch(Exception error){
            System.out.println(error);
        }
    }

    void command3(){
        try{
            Scanner in = new Scanner(System.in);
            System.out.print(ANSI_CYAN + "Input the name of material >>> ");
            String material_n = in.next();

            System.out.print("Input the name of owner >>> " + ANSI_RESET);
            String owner_n = in.next();
            find_building_material(material_n, owner_n);

            if (found == true){
                System.out.print(ANSI_CYAN + "enter a shipping rate >>> " + ANSI_RESET);
                int shipping_rate = in.nextInt();
                deliver_materials(shipping_rate);
                delete_order();
            }
            material_name = "";
            owner_name = "";
            manufactureR = "";
            found = false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    void command4(){
        try {
            String query = "SELECT * FROM delivered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            String material, count;
            System.out.println(ANSI_YELLOW + "Material                      Count");
            System.out.println("________                      _____" + ANSI_RESET);
            while (rs.next()) {
                material = rs.getString("name_of_material");
                count = rs.getString("number");
                System.out.print(material);
                for (int i = material.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(count);
                for (int i = count.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    void command5(){
        try {
            String query = "SELECT * FROM ordered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            String material, count, cost;
            System.out.println(ANSI_YELLOW + "Material                      Count                             date");
            System.out.println("________                      _____                         _____________" + ANSI_RESET);
            while (rs.next()) {
                material = rs.getString("name_of_material");
                count = rs.getString("number_or_square_meter");
                cost = rs.getString("date_of_ordered");
                System.out.print(material);
                for (int i = material.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(count);
                for (int i = count.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(cost);
                for (int i = cost.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    void command6(){
        try {
            String query = "SELECT * FROM delivered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            String material; int count, cost;
            System.out.println(ANSI_YELLOW + "Material                      Count                         Shipping rate");
            System.out.println("________                      _____                         _____________" + ANSI_RESET);
            float summa = 0;
            while(rs.next()){
                material = rs.getString("name_of_material");
                count = rs.getInt("number");
                cost = rs.getInt("shipping_rate");
                summa += cost * count;
                Object[] objects = {material, count, cost};
                for (Object o:objects){
                    System.out.print(o + " ");
                    for (int k = 0; k < (30-o.toString().length()); k++){
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            System.out.println(ANSI_YELLOW + "--------------------------------");
            System.out.println("My earning:     " + summa +" com" + ANSI_RESET);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void close(){
        try {
            co.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void menu(){
        System.out.println(ANSI_YELLOW + "\nHello, Deliveryman!" + ANSI_RESET);
        String help = "1 - print ordered materials\n" +
                "2 - print list of delivered\n" +
                "3 - Deliver the order\n" +
                "4 - Print number of delivered\n" +
                "5 - Print number of ordered\n" +
                "6 - Print my earning\n" +
                "7 - Logout\n";
        String h = "Enter \"help\" command to show list of command";
        System.out.println(ANSI_YELLOW + h + ANSI_RESET);

        Scanner in = new Scanner(System.in);
        boolean end = true;
        while (end==true) {
            System.out.println(ANSI_CYAN + "--------------------");
            System.out.print("Enter the command >>> " + ANSI_RESET);
            String choise = in.next();
            if (choise.equals("1")){
                command1();
            }
            else if (choise.equals("2")){
                command2();
            }
            else if (choise.equals("3")){
                command3();
            }
            else if (choise.equals("4")){
                command4();
            }
            else if (choise.equals("5")){
                command5();
            }
            else if (choise.equals("6")){
                command6();
            }
            else if (choise.equals("7")){
                close();
                System.out.println(ANSI_YELLOW + "You logged out" + ANSI_RESET);
                end=false;
            }
            else if(choise.equals("help")){
                System.out.println(ANSI_YELLOW + help + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_RED + "Command entered incorrect" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + h + ANSI_RESET);
            }
        }
    }
}
