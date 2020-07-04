package edu.au.cc.gallery;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class Postgres {
    public static UserDAO getUserDAO() throws SQLException, FileNotFoundException {
        return new PostgresUserDAO();
    }


}

