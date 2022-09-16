package com.company;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Foreman {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    static Scanner in = new Scanner(System.in);
    static String file_name = "apartment";

    public static void main(String... args) {
        Foreman foreman = new Foreman();
        foreman.menu();
    }

    Connection co;

    boolean open(String file_name) {
        boolean connect = false;
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:D:\\INAI\\Java\\Курсовая\\course_workk\\" + file_name + ".db");
            connect = true;
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
        return connect;
    }

    // 1 command
    void selectOwners() {
        try {
            String query = "SELECT * FROM owners";
            ResultSet rs = co.createStatement().executeQuery(query);
            String name, phone, last_Name;
            System.out.println(ANSI_YELLOW + "Surname                       Name                          Phone");
            System.out.println("-------                       ----                          -----" + ANSI_RESET);
            while (rs.next()) {
                last_Name = rs.getString("last name");
                name = rs.getString("name");
                phone = rs.getString("phone number");
                System.out.print(last_Name);
                for (int i = last_Name.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(name);
                for (int i = name.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.print(phone);
                for (int i = phone.length(); i < 30; i++) {
                    System.out.print(" ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    Map<String, Integer> getMaterialList(String name) {
        Map<String, Integer> matList = new HashMap<String, Integer>();
        try {
            String query = "SELECT * FROM ordered_materials";
            ResultSet rs = co.createStatement().executeQuery(query);
            while (rs.next()) {
                if ((rs.getString("owner_name")).equals(name)) {
                    if (matList.containsKey(rs.getString("name_of_material"))){
                        matList.put(rs.getString("name_of_material") , matList.get(rs.getString("name_of_material")) + rs.getInt("number_or_square_meter"));
                    }
                    else {
                        matList.put(rs.getString("name_of_material"), rs.getInt("number_or_square_meter"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (matList.isEmpty()) {
            System.out.println(ANSI_RED + "Owner name entered incorrectly" + ANSI_RESET);
        }
        return matList;
    }


    // 2 command
    void showMaterials() {
        System.out.print(ANSI_CYAN + "Enter the name of the owner of the apartment, the materials of which you want to display >>> " + ANSI_RESET);
        Map<String, Integer> matList = getMaterialList(in.nextLine());
        for(Map.Entry<String, Integer> i: matList.entrySet()){
            System.out.println(i.getKey()+" - "+i.getValue());
        }
    }

    // 3 command
    void maxPriceMaterial(){
        int price=0;
        String nameOfMaterial="";
        System.out.print(ANSI_CYAN + "Enter the name of the owner of the apartment >>> " + ANSI_RESET);
        Map <String, Integer> listMat=getMaterialList(in.next());
        if (!listMat.isEmpty()){
            try{
                String query="SELECT * FROM material_price";
                ResultSet rs=co.createStatement().executeQuery(query);
                for(Map.Entry<String, Integer> i: listMat.entrySet()){
                    while (rs.next()) {
                        if ((rs.getString("name_of_material")).equals(i.getKey())) {
                            if((rs.getInt("price"))>price){
                                price=rs.getInt("price");
                                nameOfMaterial=rs.getString("name_of_material");
                            }
                            break;
                        }
                    }
                }
                rs.close();
                System.out.println(ANSI_YELLOW + nameOfMaterial+" - "+price + ANSI_RESET);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    // 4 command
    void minPriceMaterial(){
        int price=999999999;
        String nameOfMaterial="";
        System.out.print(ANSI_CYAN + "Enter the name of the owner of the apartment >>> " + ANSI_RESET);
        Map <String, Integer> listMat=getMaterialList(in.next());
        if (!listMat.isEmpty()){
            try{
                String query="SELECT * FROM 'material_price'";
                ResultSet rs=co.createStatement().executeQuery(query);
                for(Map.Entry<String, Integer> i: listMat.entrySet()){
                    while (rs.next()) {
                        if ((rs.getString("name_of_material")).equals(i.getKey())) {
                            if((rs.getInt("price"))<price){
                                price=rs.getInt("price");
                                nameOfMaterial=rs.getString("name_of_material");
                            }
                            break;
                        }
                    }
                }
                rs.close();
                System.out.println(ANSI_YELLOW + nameOfMaterial+" - "+price + ANSI_RESET);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    // 5 command
    void averPriceMaterial(){
        int price=0;
        int count=0;
        System.out.print(ANSI_CYAN +"Enter the name of the owner of the apartment >>> " + ANSI_RESET);
        Map <String, Integer> listMat=getMaterialList(in.next());
        if (!listMat.isEmpty()){
            try{
                String query="SELECT * FROM 'material_price'";
                ResultSet rs=co.createStatement().executeQuery(query);
                for(Map.Entry<String, Integer> i: listMat.entrySet()){
                    while (rs.next()) {
                        if ((rs.getString("name_of_material")).equals(i.getKey())) {
                            price+=(rs.getInt("price")*i.getValue());
                            count+=i.getValue();
                            break;
                        }
                    }

                }
                rs.close();
                System.out.println(ANSI_YELLOW + "Average price of materials - "+(price/count) + ANSI_RESET);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }



    // 6 command
    void insert() {
        try {
            String name = in.nextLine();
            System.out.print(ANSI_CYAN + "Enter the manufacturer of material >>> ");
            String manuf = in.nextLine();
            System.out.print("Enter the price of material >>> " + ANSI_RESET);
            int price = in.nextInt();
            String query = "INSERT INTO material_price ('name_of_material', 'manufacturer', 'price') " +
                    "VALUES ('" + name + "', '" + manuf + "', '" + price + "');";
            Statement rs = co.createStatement();
            rs.executeUpdate(query);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    String[] delete() {
        String[] deletedmat = new String[0];
        String name_of_material = in.nextLine();
        try {
            String query = "SELECT * FROM material_price";
            ResultSet rs = co.createStatement().executeQuery(query);
            while (rs.next()) {
                if (rs.getString("name_of_material").equals(name_of_material)) {
                    String name = rs.getString("name_of_material");
                    String manufacturer = rs.getString("manufacturer");
                    String price = rs.getString("price");
                    deletedmat = new String[]{name, manufacturer, price};
                    break;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (deletedmat.length == 3) {
            String query = "DELETE FROM material_price WHERE name_of_material = \"" + name_of_material + "\";";
            try {
                Statement statement = co.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            System.out.println(ANSI_YELLOW + "This name of material is not exist" + ANSI_YELLOW);
        }
        return deletedmat;
    }


    void update() {
        try {
            System.out.print(ANSI_CYAN + "Enter the name of the material to update >>> " + ANSI_RESET);
            String[] delmat = delete();
            if (delmat.length != 0) {
                System.out.println(ANSI_YELLOW + "Tap to the \"Enter\", if you're not going to change anything" + ANSI_RESET);
                System.out.print(ANSI_CYAN + "Enter the updated name of the material >>> ");
                String name = in.nextLine();
                if (name.equals("")) {
                    name = delmat[0];
                }
                System.out.print("Enter the manufacturer of material >>> ");
                String manuf = in.nextLine();
                if (manuf.equals("")) {
                    manuf = delmat[1];
                }
                System.out.print("Enter the price of material >>> " + ANSI_RESET);
                String price = in.nextLine();
                if (price.equals("")) {
                    price = delmat[2];
                }
                String query = ("INSERT INTO material_price (\"name_of_material\", \"manufacturer\", \"price\") " +
                        "VALUES ('" + name + "', '" + manuf + "', '" + price + "');");
                try {
                    Statement statement = co.createStatement();
                    statement.executeUpdate(query);
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    void menu() {
        System.out.println(ANSI_YELLOW + "\nHello, Foreman!" + ANSI_RESET);
        String help = "List of foreman command:\n" +
                "1. Show list of owners\n" +
                "2. Show ordered material list of owner\n" +
                "3. Show max price ordered material of owner\n" +
                "4. Show min price ordered material of owner\n" +
                "5. Show average price of ordered materials of owner\n" +
                "6. Add new material\n" +
                "   Delete existing material\n" +
                "   Edit existing material\n" +
                "7. Logout";
        String h = "Enter \"help\" command to show list of command";
        System.out.println(ANSI_YELLOW + h + ANSI_RESET);
        String com;
        while (true) {
            open(file_name);
            System.out.print(ANSI_CYAN + "Enter the command >>> " + ANSI_RESET);
            com = in.next();
            if (com.equals("help")) {
                System.out.println(ANSI_YELLOW + help + ANSI_RESET);
            } else if (com.equals("1")) {
                selectOwners();
            } else if (com.equals("2")) {
                in.nextLine();
                showMaterials();
            } else if (com.equals("3")) {
                maxPriceMaterial();
            } else if (com.equals("4")) {
                in.nextLine();
                minPriceMaterial();
            } else if (com.equals("5")) {
                averPriceMaterial();
            } else if (com.equals("6")) {
                System.out.println(ANSI_YELLOW + "1. Add new material\n" +
                        "2. Delete existing material\n" +
                        "3. Edit existing material" + ANSI_RESET);
                System.out.print(ANSI_CYAN + "Choose command >>> " + ANSI_RESET);
                com = in.next();
                if (com.equals("1")) {
                    in.nextLine();
                    System.out.print(ANSI_CYAN + "Enter the name of the material to insert >>> " + ANSI_RESET);
                    insert();
                } else if (com.equals("2")) {
                    in.nextLine();
                    System.out.print(ANSI_CYAN + "Enter the name of the material to delete >>> " + ANSI_RESET);
                    delete();
                } else if (com.equals("3")) {
                    in.nextLine();
                    update();
                } else {
                    System.out.println(ANSI_RED + "Command entered incorrect" + ANSI_RESET);
                }
            } else if (com.equals("7")) {
                System.out.println(ANSI_YELLOW + "You logged out" + ANSI_RESET);
                break;
            } else {
                System.out.println(ANSI_RED + "Command entered incorrect" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + h + ANSI_RESET);
            }
            System.out.println(ANSI_CYAN +"\n---------------------------------------------------------------------------\n" + ANSI_RESET);
        }
    }
}
