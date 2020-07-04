package edu.au.cc.gallery.data;

import org.postgresql.util.PSQLException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileNotFoundException;

import java.util.ArrayList;

public class DB {

    private static final String dbUrl = "jdbc:postgresql://image-gallery.cfunveg3cqlp.us-west-2.rds.amazonaws.com/image_gallery";
    private static final String dbUser = "image_gallery";
    private static final String dbPassword = "Jas33per";
    private Connection connection;

    public static String buildDbUrl() {
        StringBuilder sb = new StringBuilder("jdbc:postgresql://");
        sb.append(System.getenv("PG_HOST"));
        sb.append("/");
        sb.append(System.getenv("IG_DATABASE"));
        String result = sb.toString();
        return result;
    }

    public static ArrayList listUsers() throws Exception {

        DB db = new DB();
        db.connect();
        ResultSet rs = db.execute("select username,password,full_name from users");

        ArrayList<String> users = new ArrayList<>();

        while (rs.next()) {

            users.add(rs.getString(1));

        }
        rs.close();
        db.close();

        return users;
    }

    public static ArrayList getUserFullNames() throws Exception {

        DB db = new DB();
        db.connect();
        ResultSet rs = db.execute("select full_name from users");

        ArrayList<String> users = new ArrayList<>();

        while (rs.next()) {

            users.add(rs.getString(1));

        }
        rs.close();
        db.close();

        return users;
    }

    public static void addUser(String username, String password, String fullName) throws Exception {

        DB db = new DB();
        db.connect();

        try {
            db.execute("INSERT INTO users VALUES (?,?,?)",
                    new String[]{username, password, fullName});
        } catch (PSQLException e) {
            System.out.println("Error: user with username " + username + " already exists");
        }

        db.close();

    }

    public static void updateUser(String username, String password, String fullName) throws Exception {

        DB db = new DB();
        db.connect();

        try {
            if (password != null && !password.isEmpty()) {

                db.execute("update users set password=? where username=?",
                        new String[]{password, username});
            }
            if (fullName != null && !fullName.isEmpty()) {
                db.execute("update users set full_name=? where username=?",
                        new String[]{fullName, username});
            }

        } catch (Exception e) {
            System.out.println("Something went wrong. ");
        }

        db.close();

    }

    public static boolean doesUserExist(String username) throws Exception {

        DB db = new DB();
        db.connect();

        ResultSet rs = db.executeWithValues("select username,password,full_name from users where username = ?",
                new String[]{username});

        if (!rs.next()) {
            rs.close();
            db.close();
            return false;
        } else {
            rs.close();
            db.close();
            return true;
        }

    }

    public static void deleteUser(String username) throws Exception {
        DB db = new DB();
        db.connect();

        try {
            db.execute("DELETE FROM users WHERE username = ?",
                    new String[]{username});
        } catch (PSQLException e) {
            System.out.println("Error: user with username " + username + " does not exist");
        }

        db.close();
    }

    public static void demo() throws Exception {
        DB db = new DB();
        db.connect();
        db.execute("update users set password=? where username=?",
                new String[]{"monkey", "fred"});
        ResultSet rs = db.execute("select username,password,full_name from users");
        while (rs.next()) {
            System.out.println("user: " + rs.getString(1) + ","
                    + rs.getString(2) + ","
                    + rs.getString(3));
        }
        rs.close();
        db.close();
    }

    private String getPassword() {
        return dbPassword;
    }

    public void connect() throws FileNotFoundException, SQLException {
        connection = DriverManager.getConnection(dbUrl, dbUser, getPassword());
    }

    public ResultSet execute(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet executeWithValues(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public void execute(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < values.length; i++) {
            stmt.setString(i + 1, values[i]);
        }
        stmt.execute();
    }

    public void close() throws SQLException {
        connection.close();
    }

}




