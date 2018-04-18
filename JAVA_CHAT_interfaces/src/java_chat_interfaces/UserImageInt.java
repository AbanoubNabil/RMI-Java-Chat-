/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_chat_interfaces;

import java.io.File;
import javafx.scene.image.Image;

/**
 *
 * @author IROCK
 */
public interface UserImageInt {

    public int getUserId();

    public void setUserId(int userId);

    public File getUserImage();

    public void setUserImage(File userImage);

}
