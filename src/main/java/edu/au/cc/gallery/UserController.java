package edu.au.cc.gallery;

import edu.au.cc.gallery.data.DB;
import edu.au.cc.gallery.data.Postgres;
import edu.au.cc.gallery.data.User;
import edu.au.cc.gallery.data.UserDAO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import static spark.Spark.*;

public class UserController {

    private static String listUsers() {

        try {
            StringBuffer sb = new StringBuffer();
            UserDAO dao = Postgres.getUserDAO();
            for (User u : dao.getUsers()) {
                sb.append(u.toString());
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

    private static String getUser(String username) {
        try {
            UserDAO dao = Postgres.getUserDAO();
            return dao.getUserByUsername(username).toString();
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    public String userAdminPage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        ArrayList<String> usersList = getAllUsers();

        ArrayList<String> userFullNamesList = getAllUserFullNames();

        model.put("users", usersList);
        model.put("userFullNames", userFullNamesList);


        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "admin.hbs"));
    }

    public String addUserPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<String, Object>();
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "addUser.hbs"));
    }

    public String modifyUserPage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", req.params(":user"));
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "modifyUser.hbs"));
    }

    public String deleteUserPage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", req.params(":user"));
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "deleteUser.hbs"));
    }

    public ArrayList<String> getAllUsers() throws Exception {

        ArrayList<String> users = DB.listUsers();
        return users;
    }

    public ArrayList<String> getAllUserFullNames() throws Exception {
        return DB.getUserFullNames();

    }

    public String addUser(Request req, Response res) throws Exception {
        DB.addUser(req.queryParams("userId"), req.queryParams("password"), req.queryParams("fullName"));
        return "Added user " + req.queryParams("userId") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";
    }

    public String modifyUser(Request req, Response res) throws Exception {
        DB.updateUser(req.params(":user"), req.queryParams("password"), req.queryParams("fullName"));
        return "Modified user " + req.params(":user") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";
    }

    public String deleteUser(Request req, Response res) throws Exception {
        DB.deleteUser(req.params(":user"));
        return "Deleted user " + req.params(":user") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";
    }

    public void addRoutes() {
        get("/admin", (req, res) -> userAdminPage(req, res));
        get("/admin/addUser", (req, res) -> addUserPage(req, res));
        get("/admin/modifyUser/:user", (req, res) -> modifyUserPage(req, res));
        get("/admin/deleteUser/:user", (req, res) -> deleteUserPage(req, res));
        post("/admin/addUser/add", (req, res) -> addUser(req, res));
        post("/admin/modifyUser/:user/modify", (req, res) -> modifyUser(req, res));
        post("/admin/deleteUser/:user/delete", (req, res) -> deleteUser(req, res));
    }


}



