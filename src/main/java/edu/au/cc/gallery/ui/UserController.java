package edu.au.cc.gallery.ui;

import edu.au.cc.gallery.data.DB;
import edu.au.cc.gallery.data.Postgres;
import edu.au.cc.gallery.data.User;
import edu.au.cc.gallery.data.UserDAO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static edu.au.cc.gallery.data.Postgres.getUserDAO;
import static spark.Spark.*;

public class UserController {

    private boolean authenticated = false;

    public String userAdminPage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("users", getUserDAO().getUsers());

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

    public String addUser(Request req, Response res) {
        try {
            UserDAO dao = Postgres.getUserDAO();
            dao.addUser(new User(req.queryParams("userId"), req.queryParams("password"), req.queryParams("fullName")));
            return "Added user " + req.queryParams("userId") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }

    }

    public String modifyUser(Request req, Response res) {
        try {

            UserDAO dao = Postgres.getUserDAO();
            User u = dao.getUserByUsername(req.params(":user"));
            u.setPassword(req.queryParams("password"));
            u.setFullName(req.queryParams("fullName"));

            dao.updateUser(u);
            return "Modified user " + req.params(":user") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }

    }

    public String deleteUser(Request req, Response res) throws Exception {
        try {
            UserDAO dao = Postgres.getUserDAO();
            dao.deleteUser(req.params(":user"));
            return "Deleted user " + req.params(":user") + "<!DOCTYPE html><html><head></head><body></br><a href=\"/admin\"><button>Return home</button></a><body></html>";

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }

    }

    private String login(Request req, Response resp) {
        authenticated = false;
        Map<String, Object> model = new HashMap<String, Object>();
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "login.hbs"));
    }

    private String loginPost(Request req, Response resp) {
        try {
            String username = req.queryParams("username");
            User u = getUserDAO().getUserByUsername(username);
            if (u == null || !u.getPassword().equals(req.queryParams("password"))) {
                return "Invalid user or password";
            }
            req.session().attribute("user", username);
            authenticated = true;
            resp.redirect("/");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "";

    }

    private boolean isAdmin(String username) {
        return username != null && username.equals("administrator");
    }

    private void checkAdmin(Request req, Response resp) {
        if (!isAdmin(req.session().attribute("user"))) {
            resp.redirect("/login");
            halt();
        }
    }

    private void checkAuthentication(Request req, Response resp) {

        if (!authenticated) {
            resp.redirect("/login");
            halt(401, "You must be logged in to view this page");
        }
    }

    private String home(Request req, Response resp) {
        Map<String, Object> model = new HashMap<String, Object>();
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "home.hbs"));
    }

    private String uploadImage(Request req, Response resp) {
        Map<String, Object> model = new HashMap<String, Object>();
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "uploadImage.hbs"));
    }

    private String viewImages(Request req, Response resp) {
        Map<String, Object> model = new HashMap<String, Object>();
        return new HandlebarsTemplateEngine()
                .render(new ModelAndView(model, "viewImages.hbs"));
    }

    public void addRoutes() {
        get("/admin", (req, res) -> userAdminPage(req, res));
        get("/admin/addUser", (req, res) -> addUserPage(req, res));
        get("/admin/modifyUser/:user", (req, res) -> modifyUserPage(req, res));
        get("/admin/deleteUser/:user", (req, res) -> deleteUserPage(req, res));
        post("/admin/addUser/add", (req, res) -> addUser(req, res));
        post("/admin/modifyUser/:user/modify", (req, res) -> modifyUser(req, res));
        post("/admin/deleteUser/:user/delete", (req, res) -> deleteUser(req, res));
        get("/login", (req, res) -> login(req, res));
        post("/login", (req, res) -> loginPost(req, res));
        get("/", (req, res) -> home(req, res));
        before("/", (req, res) -> checkAuthentication(req, res));
        before("/admin", (req, res) -> checkAuthentication(req, res));
        before("/uploadImage", (req, res) -> checkAuthentication(req, res));
        before("/viewImages", (req, res) -> checkAuthentication(req, res));
        before("/admin/*", (req, res) -> checkAdmin(req, res));
        get("/uploadImage", (req, res) -> uploadImage(req, res));
        get("/viewImages", (req, res) -> viewImages(req, res));
    }


}



