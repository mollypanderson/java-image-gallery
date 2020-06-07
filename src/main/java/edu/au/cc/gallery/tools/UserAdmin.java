package edu.au.cc.gallery.tools;

import edu.au.cc.gallery.DB;

import java.util.Scanner;

public class UserAdmin {
    public static void adminMenu() throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("** Welcome to the database admin homepage! **\n\nWhat would you like to do?: \n");
        System.out.println("1)\tList users\n2)\tAdd users\n3)\tEdit user\n4)\tDelete user\n5)\tQuit\n");

        String selection = "";

        while (!selection.equals("5")) {

            System.out.print("Enter command: ");

            selection = scanner.nextLine();

            if (selection.equals("1")) {
                String result = DB.listUsers();
                System.out.println(result);

            } else if (selection.equals("2")) {
                System.out.print("Username> ");
                String username = scanner.nextLine();
                System.out.print("Password> ");
                String password = scanner.nextLine();
                System.out.print("Full name> ");
                String fullName = scanner.nextLine();

                DB.addUser(username, password, fullName);

            } else if (selection.equals("3")) {


            } else if (selection.equals("4")) {
                System.out.print("Enter username to delete> ");
                String username = scanner.nextLine();
                System.out.print("\nAre you sure that you want to delete " + username + "? ");
                String delete = scanner.nextLine();

                if (delete.equalsIgnoreCase("yes")) {
                    DB.deleteUser(username);
                    System.out.println("Deleted. ");
                }
                
            } else if (selection.equals("5")) {
                System.out.println("Goodbye! ");

            } else {
                System.out.println("Invalid selection. ");
            }
        }

    }
}

