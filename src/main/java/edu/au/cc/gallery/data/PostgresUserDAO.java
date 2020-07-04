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
}

