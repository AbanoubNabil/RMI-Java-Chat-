/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_chat_interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author IROCK
 */
public interface Users {

    public int getUser_id();

    public void setUser_id(int user_id) ;

    public String getFirst_name();

    public void setFirst_name(String first_name);

    public String getLast_name();

    public void setLast_name(String last_name) ;

    public String getCountry();

    public void setCountry(String country);

    public String getGender();

    public void setGender(String gender) ;

    public String getEmail();

    public void setEmail(String email) ;

    public String getPassword() ;

    public void setPassword(String password);

    public Image getImage() ;

    public void setImage(Image image);

    public HashSet<Integer> getFriend_list();

    public void setFriend_list(HashSet<Integer> friend_list) ;
}
