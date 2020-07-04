package edu.au.cc.gallery.data;

import java.util.List;

public interface UserDAO {
    List<User> getUsers() throws Exception;

    User getUserByUsername(String username) throws Exception;
}

