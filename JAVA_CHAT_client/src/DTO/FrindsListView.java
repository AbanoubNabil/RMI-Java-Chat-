/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.File;
import java.io.Serializable;
import java_chat_interfaces.FriendListViewInt;
import javafx.scene.image.Image;

/**
 *
 * @author IROCK
 */
public class FrindsListView implements Serializable,FriendListViewInt{
    
    private int frindId;
    private String name;
    private File frindImage;
    private String status;

    @Override
    public int getFrindId() {
        return frindId;
    }

    @Override
    public void setFrindId(int frindId) {
        this.frindId = frindId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public File getFrindImage() {
        return frindImage;
    }

    @Override
    public void setFrindImage(File frindImage) {
        this.frindImage = frindImage;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
    
}
