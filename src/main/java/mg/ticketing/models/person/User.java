package mg.ticketing.models.person;

import jakarta.servlet.http.Part;
import mg.framework.annotation.validation.Mail;
import mg.framework.annotation.validation.Required;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.utils.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

@Table(name = "users")
public class User extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String username;
    @Column
    @Mail
    @Required
    private String email;
    @Column
    @Required
    private String password;
    @Column
    private String role;
    @Column
    private String picture;

    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User login(Connection connection) throws Exception{
        String query = "WHERE email = '" + this.getEmail() + "' AND password = '" + this.getPassword()+"'";
        return (User) this.read(query, connection).stream().findFirst().orElseThrow(() -> new Exception("You have an error in your email or password"));
    }

    public User getById(Connection connection) {
        return (User) this.read("WHERE id = " + this.getId(), connection).stream().findFirst().orElse(null);
    }

    public void update(Connection connection) {
        this.update("picture","id", connection);
    }

    public String upload(Part part, String file_name) throws IOException{
        String PATH_DIRECTORY = Config.UPLOAD_PATH+file_name;
        File file = new File(PATH_DIRECTORY);
        if (file.exists()) {
            file.delete();
        }
        try(InputStream fileContent = part.getInputStream(); FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1){
                output.write(buffer, 0, bytesRead);
            }
        }
        return "Passport uploaded successfully!";
    }
}
