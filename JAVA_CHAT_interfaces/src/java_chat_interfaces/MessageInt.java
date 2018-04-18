/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_chat_interfaces;

/**
 *
 * @author IROCK
 */
public interface MessageInt {

    public String getFrom();

    public void setFrom(String from);

    public String getTo();

    public void setTo(String to);

    public String getDate();

    public void setDate(String date);

    public String getBody();

    public void setBody(String body);

    public int getSize();

    public void setSize(int size);

    public String getColor();

    public void setColor(String color);

    public String getDirection();

    public void setDirection(String direction);

    public String getFont();

    public void setFont(String font);

}
