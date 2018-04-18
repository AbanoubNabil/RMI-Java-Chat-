package java_caht_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//java_chat_interfaces.Users 
public class UsersServer implements Serializable{

    private int user_id;
    private String first_name;
    private String last_name;
    private String country;
    private String gender;
    private String email;
    private String password;
    private String imgPath;
    private File img;
    private ImageView image;
    HashSet<Integer> friend_list;
    String status;

    public UsersServer() {
    }
    
    public UsersServer( String first_name, String last_name, String country, String gender, String email, String password, String imgPath ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.img = new File(imgPath);
        
    }
    
    public String getStatus() {
        return status;
    }
    
    public File getFis() {
        return img;
    }

    public void setFis(File fis) {
        this.img = fis;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Image getImage(String imgPath) throws FileNotFoundException {
        InputStream inputStream;
        inputStream = new FileInputStream(imgPath);

        Image myimage = new Image(inputStream);

        return myimage;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public HashSet<Integer> getFriend_list() {
        return friend_list;
    }

    public void setFriend_list(HashSet<Integer> friend_list) {
        this.friend_list = friend_list;
    }

    
}
