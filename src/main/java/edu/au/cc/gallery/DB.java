package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB {
    private static final String dbUrl = "jdbc:postgresql://demo-database-2.cfunveg3cqlp.us-west-2.rds.amazonaws.com/image_gallery";
    private Connection connection;

    private String getPassword() throws FileNotFoundException {
        try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))) {
            String result = br.readLine();
            return result;
        } catch (IOException e) {
            System.out.println("Error opening pwd file");
            System.exit(1);
        }
        return null;
    }

    public void connect() throws FileNotFoundException, SQLException {

        //Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword());

    }

    public ResultSet execute(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public void execute(String query, String[] values) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < values.length; i++) {
            stmt.setString(i+1, values[i]);
        }
        stmt.execute();
    }

    public void close() throws SQLException {
        connection.close();
    }

//    public static void demo() throws Exception {
//        DB db = new DB();
//        db.connect();
//        db.execute("update users set password=? where username=?",
//                new String[] {"monkey", "fred"});
//        ResultSet rs = db.execute("select username,password,full_name from users");
//        while(rs.next()) {
//            System.out.println("user: "+rs.getString(1)+","
//                    +rs.getString(2)+","
//                    +rs.getString(3));
//        }
//        rs.close();
//        db.close();
//    }

    public static String listUsers() throws Exception {
        StringBuilder sb = new StringBuilder("\n");

        DB db = new DB();
        db.connect();
        //db.execute("update users set password=? where username=?",
         //       new String[] {"monkey", "fred"});
        ResultSet rs = db.execute("select username,password,full_name from users");
        sb.append("username\tpassword\tfull name\n");
        sb.append("-----------------------------------------\n");
        while(rs.next()) {
            sb.append(rs.getString(1)+"\t\t"
                    +rs.getString(2)+"\t\t"
                    +rs.getString(3)+"\n");
        }
        rs.close();
        db.close();


        return sb.toString();
    }

}


