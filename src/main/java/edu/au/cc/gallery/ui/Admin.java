package edu.au.cc.gallery.ui;

import edu.au.cc.gallery.data.Postgres;
import edu.au.cc.gallery.data.User;
import edu.au.cc.gallery.data.UserDAO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class Admin {

    private static UserDAO getUserDAO() throws Exception {
        return Postgres.getUserDAO();
    }

    private String listUsers() {

        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("users", getUserDAO().getUsers());
            return new HandlebarsTemplateEngine()
                    .render(new ModelAndView(model, "users.hbs"));
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

    private String getUser(String username) {
        try {
            UserDAO dao = Postgres.getUserDAO();
            return dao.getUserByUsername(username).toString();
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    private String addUser(String username, String password, String fullName, Response r) {
        try {
            UserDAO dao = Postgres.getUserDAO();
            dao.addUser(new User(username, password, fullName));
            r.redirect("/admin");
            return "";
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    private String updateUser(String username, String password, String fullName, Response r) {
        try {
            UserDAO dao = Postgres.getUserDAO();
            dao.updateUser(dao.getUserByUsername(username));
            r.redirect("/admin");
            return "";
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    private String deleteUser(Request req, Response resp) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Delete user");
            model.put("message", "Are you sure you want to delete this user?");
            model.put("onYes", "/admin/deleteUserExec"+req.params(":username"));
            model.put("onNo", "/admin/users");
            return new HandlebarsTemplateEngine()
                    .render(new ModelAndView(model, "confirm.hbs"));
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String deleteUserExec(Request req, Response resp) {
        try {
            getUserDAO().deleteUser(req.params(":username"));
            resp.redirect("/admin/users");
            return null;
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    public void addRoutes() {
        get("/admin/users", (req, res) -> listUsers());
        get("/admin/users/:username", (req, res) -> getUser(req.params(":username")));
        get("/admin/addUser/:username/:password/:fullName",
                (req, res) -> addUser(req.params("username"), req.params(":password"), req.params(":fullName"), res));
        get("/admin/deleteUser/:username", (req, res) -> deleteUser(req, res));
        get("/admin/deleteUserExec/:username", (req, res) -> deleteUserExec(req, res));

    }
}

