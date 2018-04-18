package java_caht_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java_chat_interfaces.LoginDTO;
//import java_chat_interfaces.Users;
import javax.swing.JOptionPane;

public class ServerDAO2 {

    int count = 0;
    public PreparedStatement pst;
    static Connection con;
    Statement stmt;
    public ResultSet rs = null;

    public void DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("exception in class for name ");
            ex.printStackTrace();
        }
        String user = "root";
        String pass = "root";
        String location = "jdbc:mysql://localhost:3306/javachat";
        try {
            con = DriverManager.getConnection(location, user, pass);
            System.out.println("connected ");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static final Connection getconection() {
        return con;
    }

    public void insertUser(UsersServer user) {
        try {
            pst = con.prepareStatement("INSERT INTO users (first_name,last_name,gender,email,password,country) VALUES ( ? , ? , ?  , ? , ? , ? )");
//            File img = new File(user.getFis().getPath());
//            FileInputStream fin = new FileInputStream(img);
            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getCountry());
//            pst.setBinaryStream(7, (InputStream) fin, (int) img.length());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertAdmin(UsersServer admin) {
        try {
            pst = con.prepareStatement("INSERT INTO admin (first_name,last_name,gender,email,password,country) VALUES ( ? , ? , ?  , ?  , ? , ?)");
//            File img =admin.getFis();
//            FileInputStream fin = new FileInputStream(img);
            pst.setString(1, admin.getFirst_name());
            pst.setString(2, admin.getLast_name());
            pst.setString(3, admin.getGender());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getPassword());
            pst.setString(6, admin.getCountry());
//            pst.setBinaryStream(7, (InputStream) fin, (int) img.length());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int isUser(UsersServer user) {

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT user_id,email , password  FROM users ";
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                if ((user.getEmail().equals(email)) && (user.getPassword().equals(password))) {
                    return rs.getInt("user_id");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public boolean isAdmin(UsersServer admin) {

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM admin ";
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                if ((admin.getEmail().equals(email)) && (admin.getPassword().equals(password))) {
                    admin.setFirst_name(rs.getString("first_name"));
                    admin.setLast_name(rs.getString("last_name"));
                    admin.setUser_id(rs.getInt("admin_id"));
                    
                    if(rs.getBinaryStream("image")!=null){
                    InputStream in = rs.getBinaryStream("image");

                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + admin.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] bufr = new byte[10000];
                    int len = 0;
                    while ((len = in.read(bufr)) != -1) {
                        out.write(bufr, 0, len);
                    }

                    admin.setFis(img);
                    }
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
//            catch (FileNotFoundException ex) {
//            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return false;
    }

    public void deleteUser(int id) {
        String SQL = "DELETE FROM users WHERE user_id='" + id + "'";
        try {
            stmt.executeQuery(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("user deleted ");
    }

    public void deleteAdmin(int id) {
        String SQL = "DELETE FROM admin WHERE " + " admin_id='" + id + "'";
        try {
            stmt.executeQuery(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("admin deleted ");
    }

    public void updateUser(UsersServer user) {
        String sql;
        int check = 1;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            pst = con.prepareStatement("UPDATE  users set first_name=? ,last_name=?,gender=?,email=?,password=?,country=?,image=?");
            File img = new File(user.getFis().getPath());
            FileInputStream fin = new FileInputStream(img);
            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getCountry());
            pst.setBinaryStream(7, (InputStream) fin, (int) img.length());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateAdmin(UsersServer admin) {
        String sql;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst = con.prepareStatement("UPDATE  user set first_name=? ,last_name=?,gender=?,email=?,password=?,country=?,image=?");
            File img = new File(admin.getFis().getPath());
            FileInputStream fin = new FileInputStream(img);
            pst.setString(1, admin.getFirst_name());
            pst.setString(2, admin.getLast_name());
            pst.setString(3, admin.getGender());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getPassword());
            pst.setString(6, admin.getCountry());
            pst.setBinaryStream(7, (InputStream) fin, (int) img.length());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<UsersServer> getAllUsers() {

        ArrayList<UsersServer> all = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users  ");
            ResultSet rs3 = ps.executeQuery();
            while (rs3.next()) {
                UsersServer user = new UsersServer();
                count++;
                user.setUser_id(rs3.getInt("user_id"));
                user.setFirst_name(rs3.getString("first_name"));
                user.setLast_name(rs3.getString("last_name"));
                user.setEmail(rs3.getString("email"));
                user.setCountry(rs3.getString("country"));
                user.setGender(rs3.getString("gender"));
                InputStream in = rs3.getBinaryStream("image");
                user.setStatus(rs3.getString("status"));

                if (in != null) {

                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + user.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    user.setFis(img);
                }
                System.out.println(user.getStatus());

                all.add(user);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }

    public ArrayList<UsersServer> getOnline() {
        ArrayList<UsersServer> online = new ArrayList<>();
        try {
            PreparedStatement pss = con.prepareStatement("SELECT * from users ");
            ResultSet rs4 = pss.executeQuery();

            while (rs4.next()) {
                UsersServer user = new UsersServer();
                count++;
                user.setUser_id(rs4.getInt("user_id"));
                user.setFirst_name(rs4.getString("first_name"));
                user.setLast_name(rs4.getString("last_name"));
                user.setEmail(rs4.getString("email"));
                user.setCountry(rs4.getString("country"));
                user.setGender(rs4.getString("gender"));
                InputStream in = rs4.getBinaryStream("image");

                user.setStatus(rs4.getString("status"));
                if (in != null) {
                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + user.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }

                    user.setFis(img);
                }

                if (user.getStatus().equals("online")) {
                    online.add(user);
                }
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return online;
    }

    public ArrayList<UsersServer> getOfline() {
        ArrayList<UsersServer> ofline = new ArrayList<>();
        try {
            PreparedStatement pss = con.prepareStatement("SELECT * from users ");
            ResultSet rs4 = pss.executeQuery();

            while (rs4.next()) {
                UsersServer user = new UsersServer();
                count++;
                user.setUser_id(rs4.getInt("user_id"));
                user.setFirst_name(rs4.getString("first_name"));
                user.setLast_name(rs4.getString("last_name"));
                user.setEmail(rs4.getString("email"));
                user.setCountry(rs4.getString("country"));
                user.setGender(rs4.getString("gender"));
                InputStream in = rs4.getBinaryStream("image");
                user.setStatus(rs4.getString("status"));

                if (in != null) {

                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + user.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    user.setFis(img);
                }

                if (user.getStatus().equals("ofline")) {
                    ofline.add(user);
                }
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ofline;
    }

    public ArrayList<UsersServer> getAway() {
        ArrayList<UsersServer> away = new ArrayList<>();
        try {
            PreparedStatement pss = con.prepareStatement("SELECT * from users ");
            ResultSet rs4 = pss.executeQuery();

            while (rs4.next()) {
                UsersServer user = new UsersServer();
                count++;
                user.setUser_id(rs4.getInt("user_id"));
                user.setFirst_name(rs4.getString("first_name"));
                user.setLast_name(rs4.getString("last_name"));
                user.setEmail(rs4.getString("email"));
                user.setCountry(rs4.getString("country"));
                user.setGender(rs4.getString("gender"));
                InputStream in = rs4.getBinaryStream("image");
                user.setStatus(rs4.getString("status"));

                if (in != null) {
                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + user.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }

                    user.setFis(img);

                }

                if (user.getStatus().equals("away")) {
                    System.out.println("trrrrrrrrrrrrrr");
                    away.add(user);
                }
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("before away return");
        return away;
    }

    public ArrayList<UsersServer> getBusy() {
        ArrayList<UsersServer> busy = new ArrayList<>();
        try {
            PreparedStatement pss = con.prepareStatement("SELECT * from users ");
            ResultSet rs4 = pss.executeQuery();
            while (rs4.next()) {
                UsersServer user = new UsersServer();
                count++;
                user.setUser_id(rs4.getInt("user_id"));
                user.setFirst_name(rs4.getString("first_name"));
                user.setLast_name(rs4.getString("last_name"));
                user.setEmail(rs4.getString("email"));
                user.setCountry(rs4.getString("country"));
                user.setGender(rs4.getString("gender"));
                InputStream in = rs4.getBinaryStream("image");
                user.setStatus(rs4.getString("status"));

                if (in != null) {

                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + user.getFirst_name() + count + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    user.setFis(img);

                }

                if (user.getStatus().equals("busy")) {
                    busy.add(user);
                }
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return busy;
    }

    public int getAllUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users ");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getOnlineUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where status='online'");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getOflineUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where status='ofline'");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getAwayUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where status='away'");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getBusyUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where status='busy'");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getWomenUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where gender='female'");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

    public int getMenUsersNumber() {
        int allU = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from users where gender='male' ");
            ResultSet rs = ps.executeQuery();
            rs.last();
            allU = rs.getRow();
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allU;
    }

     public int saveImageToDatabase(File f,int i) {
        int id = 0;
        try {
            pst = con.prepareStatement("UPDATE  admin set image=? where admin_id=" + i);

            FileInputStream fin = new FileInputStream(f);

            pst.setBinaryStream(1,fin);
            id = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerDAO2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }
}
