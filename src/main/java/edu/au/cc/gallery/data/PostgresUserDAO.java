package edu.au.cc.gallery.data;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PostgresUserDAO implements UserDAO {
    private DB connection;

    public PostgresUserDAO() throws FileNotFoundException, SQLException {
        connection = new DB();
        connection.connect();
    }

    public List<User> getUsers() throws SQLException {
        List<User> result = new ArrayList<>();
        ResultSet rs = connection.execute("select username,password,full_name from users");

        while(rs.next()) {
            result.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));

        }

        rs.close();
        return result;
    }

    public User getUserByUsername(String username) throws SQLException {
        List<User> result = new ArrayList<>();
        ResultSet rs = connection.executeWithValues("select username,password,full_name from users where username=?", new String[] {username});

        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3));

        }

        return null;
    }

    public void addUser(User u) throws SQLException {
        connection.execute("insert into users(username,password,full_name) values (?,?,?)",
                new String[] {u.getUsername(), u.getPassword(), u.getFullName()});
    }

    public void updateUser(User u) throws SQLException {
        try {
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {

                connection.execute("update users set password=? where username=?",
                        new String[]{u.getPassword(), u.getUsername()});
            }
            if (u.getFullName() != null && !u.getFullName().isEmpty()) {
                connection.execute("update users set full_name=? where username=?",
                        new String[]{u.getFullName(), u.getUsername()});
            }

        } catch (Exception e) {
            System.out.println("Something went wrong. ");
        }
    }

    public void deleteUser(String username) throws SQLException {
        connection.execute("DELETE FROM users WHERE username = ?",
                new String[]{username});
    }

}

