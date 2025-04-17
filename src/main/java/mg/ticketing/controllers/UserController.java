package mg.ticketing.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import mg.framework.annotation.servlet.*;
import mg.framework.servlet.ModelView;
import mg.framework.servlet.Redirect;
import mg.framework.servlet.Session;
import mg.framework.utils.File;
import mg.ticketing.models.person.User;
import mg.ticketing.models.utils.UtilDb;
import mg.ticketing.models.utils.Utils;

import java.sql.Connection;

@Controller
@BaseUrl("user")
public class UserController {
    Session session;

    public void setSession(Session session){
        this.session = session;
    }

    @Url("login")
    @Post
    public ModelView login(@RequestParam("User") User user) {
        String page = "index.jsp";
        ModelView model = new ModelView();
        try {
            Connection connection = new UtilDb().getPgConnection();

            User user1 = user.login(connection);
            if (user1 != null) {
                session.add("user", user1);
                page = "app/flights?message=";
            }

            connection.close();
        } catch (Exception e) {
            model.addData("error", e.getMessage());
        }

        model.setUrl(page);
        return model;
    }

    @Url("logout")
    public Redirect logout(@RequestParam("User") User user) {
        Redirect redirect = new Redirect("index.jsp");
        try {
            Connection connection = new UtilDb().getPgConnection();
            session.delete("user");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redirect;
    }

    @Url("upload")
    @Post
    public ModelView upload(@RequestParam("picture") Part picture, @RequestParam("id") int id) {
        ModelView model = new ModelView();
        String url =  "pages/client/profil.jsp";
        try {
            Connection connection = new UtilDb().getPgConnection();
            User user = new User();
            user.setId(id);
            user = user.getById(connection);

            String file_name = user.getUsername()+user.getId()+ Utils.getFileExtension(picture);

            String message = user.upload(picture, file_name);
            model.addData("message", message);
            url+="?message="+ message;

            user.setPicture(file_name);
            user.update(connection);

            session.add("user", user);

            connection.close();
        } catch (Exception e) {
            url+="?message="+ e.getMessage();
        }
        model.setUrl(url);
        return model;
    }
}
