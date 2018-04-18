/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.File;
import java.io.Serializable;
import java_chat_interfaces.UserImageInt;
import javafx.scene.image.Image;

/**
 *
 * @author IROCK
 */
public class UserImage implements Serializable,UserImageInt{
    
   private int userId;
    private File userImage;

    @Override
    public int getUserId() {
        return userId;
        
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public File getUserImage() {
        return userImage;
    }

    @Override
    public void setUserImage(File userImage) {
        this.userImage = userImage;
    }
    
    
}
