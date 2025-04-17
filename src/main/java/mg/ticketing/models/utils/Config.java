package mg.ticketing.models.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class Config implements ServletContextListener {
    public static String UPLOAD_PATH;
    public static String url;
    public static String user;
    public static String pwd;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        UPLOAD_PATH = context.getInitParameter("app.upload.path");
        url = context.getInitParameter("app.db.url");
        user = context.getInitParameter("app.db.user");
        pwd = context.getInitParameter("app.db.pwd");
        System.out.println("Chargé depuis web.xml : " + UPLOAD_PATH);
    }
}
