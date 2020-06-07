package edu.au.cc.gallery.tools;

//import edu.au.cc.gallery.DB;

import edu.au.cc.gallery.DB;

import java.util.Scanner;

public class UserAdmin {
    public static void adminMenu() throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("** Welcome to the database admin homepage! **\n\nWhat would you like to do?: \n");
        System.out.println("1)\tList users\n2)\tAdd users\n3)\tEdit user\n4)\tDelete user\n5)\tQuit\n");
        System.out.print("Enter command: ");

        String selection = scanner.nextLine();

        if (selection.equals("1")) {
            String result = DB.listUsers();
            System.out.println(result);

        } else if (selection.equals("5")) {
            System.out.println("Goodbye! ");
        } else {
            System.out.println("Invalid selection. Goodbye. ");
        }



        //DB.demo();

    }
}

