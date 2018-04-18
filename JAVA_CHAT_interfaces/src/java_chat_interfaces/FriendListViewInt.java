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
public interface FriendListViewInt {
    
     public int getFrindId();

    public void setFrindId(int frindId);

    public String getName();

    public void setName(String name);

    public File getFrindImage();

    public void setFrindImage(File frindImage);

    public String getStatus();

    public void setStatus(String status);
    

    
}
