package DAO;

import DTO.FrindsListView;
import DTO.UserImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.LoginDTO;
import java_chat_interfaces.UserImageInt;
import java_chat_interfaces.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

public class ServerDAO {

    public static PreparedStatement pst;
    public static Connection con;
    public static Statement stmt;
    public ResultSet rs = null;

    static {
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

    public int insertUser(Users user) {
        int id = -1;
        try {
            pst = con.prepareStatement("INSERT INTO users (first_name,last_name,gender,email,password,country) VALUES ( ? , ? , ?  , ? , ? , ? )");

            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getCountry());

            id = pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public void insertAdmin(Users admin) {
        try {
            pst = con.prepareStatement("INSERT INTO users (first_name,last_name,gender,email,password,country,image) VALUES ( ? , ? , ?  , ? , ? , ? , ?)");

            pst.setString(1, admin.getFirst_name());
            pst.setString(2, admin.getLast_name());
            pst.setString(3, admin.getGender());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getPassword());
            pst.setString(6, admin.getCountry());
            pst.setBlob(7, (Blob) admin.getImage());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int isUser(LoginDTO user) {

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

    public int isAdmin(LoginDTO admin) {

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT user_id,email , password  FROM admin ";
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                if ((admin.getEmail().equals(email)) && (admin.getPassword().equals(password))) {
                    return rs.getInt("user_id");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void deleteUser(int id) {
        String SQL = "DELETE FROM users WHERE user_id='" + id + "'";
        try {
            stmt.executeQuery(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("user deleted ");
    }

    public void deleteAdmin(int id) {
        String SQL = "DELETE FROM admins WHERE " + " admin_id='" + id + "'";
        try {
            stmt.executeQuery(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("admin deleted ");
    }

    public void updateUser(Users user) {
        String sql;
        int check = 1;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            pst = con.prepareStatement("UPDATE  users set first_name=? ,last_name=?,gender=?,email=?,password=?,country=?");

            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getCountry());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void updateAdmin(Users admin) {
        String sql;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst = con.prepareStatement("UPDATE  users set first_name=? ,last_name=?,gender=?,email=?,password=?,country=?");

            pst.setString(1, admin.getFirst_name());
            pst.setString(2, admin.getLast_name());
            pst.setString(3, admin.getGender());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getPassword());
            pst.setString(6, admin.getCountry());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void addFriend(int myId, int reqId) {
        try {
            pst = con.prepareStatement("INSERT INTO frind_list (user_id , frind_id) VALUES ( ? , ? )");
            pst.setInt(1, myId);
            pst.setInt(2, reqId);
            pst.executeUpdate();
            pst = con.prepareStatement("INSERT INTO frind_list (user_id , frind_id) VALUES ( ? , ? )");
            pst.setInt(1, reqId);
            pst.setInt(2, myId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<FriendListViewInt> getFrindList(int id) {
        ArrayList<Integer> set = new ArrayList<>();
        ArrayList<FriendListViewInt> list = new ArrayList<>();

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT frind_id  FROM frind_list where user_id=" + id;
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {

                set.add(rs.getInt("frind_id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (set.size() > 0) {
            for (int i = 0; i < set.size(); i++) {

                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String SQL = "SELECT first_name,last_name,image,status FROM users where user_id=" + set.get(i);
                    rs = stmt.executeQuery(SQL);

                    while (rs.next()) {

                        FrindsListView flv = new FrindsListView();
                        flv.setFrindId(set.get(i));
                        flv.setName(rs.getString("first_name") + " " + rs.getString("last_name"));
                        flv.setStatus(rs.getString("status"));
                        File imgfile = getImageFromDataBase(set.get(i)).getUserImage();
                        if (imgfile != null) {

                            flv.setFrindImage(imgfile);

                        }

                        list.add(flv);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }

        }
        return list;
    }

    public Users getUserData(int id) {
        Users user = new pojo.Users();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM users where user_id= " + id;
            rs = stmt.executeQuery(SQL);

            rs.next();

            if (!rs.wasNull()) {
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUser_id(id);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public int saveImageToDatabase(UserImageInt userImage) {
        int id = 0;
        try {
            pst = con.prepareStatement("UPDATE  users set image=? where user_id=" + userImage.getUserId());

            FileInputStream fin = new FileInputStream(userImage.getUserImage());

            pst.setBinaryStream(1, (InputStream) fin, (int) userImage.getUserImage().length());
            id = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    public UserImageInt getImageFromDataBase(int id) {

        UserImageInt user = new UserImage();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT image from users where user_id=? ");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //count ++;

                InputStream in = rs.getBinaryStream("image");
                if (in == null) {

                    user.setUserImage(null);
                } else {
                    File img = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\" + id + ".jpg");
                    OutputStream out = new FileOutputStream(img);
                    byte[] buff = new byte[4096];
                    int len = 0;
                    if (in != null) {
                        while ((len = in.read(buff)) != -1) {
                            out.write(buff, 0, len);
                        }
                        user.setUserImage(img);
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;

    }

    public boolean checkIfFriendIsFounde(String email, int user_Id) {

        boolean foundInUsers = false;
        boolean foundInfriendList = false;
        int id = 0;

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT user_id,email  FROM users";
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {

                if (rs.getString("email").equals(email)) {

                    foundInUsers = true;
                    id = rs.getInt("user_id");
                }
            }
            if (id == user_Id) {

                if (foundInUsers) {

                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String SQL2 = "SELECT * FROM frind_list where user_id=" + user_Id;
                    ResultSet res = stmt.executeQuery(SQL2);
                    while (res.next()) {

                        if (res.getInt("frind_id") == id) {

                            foundInfriendList = true;
                        }
                    }
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return foundInfriendList;
    }

    public int addRequest(String email, int senderId) {
        int id = 0;
        int requestId = 0;

        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT user_id  FROM users where email='" + email + "'";
            rs = stmt.executeQuery(SQL);
            rs.next();
            id = rs.getInt("user_id");

            pst = con.prepareStatement("INSERT INTO friend_request (sender_id , reciver_id) VALUES ( ? , ? )");
            pst.setInt(1, senderId);
            pst.setInt(2, id);
            requestId = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return requestId;
    }

    public ArrayList<Integer> retriveRequestList(int userId) {

        ArrayList<Integer> requestList = new ArrayList<>();
        try {

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT sender_id FROM friend_request where reciver_id=" + userId;
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {

                requestList.add(rs.getInt("sender_id"));

            }

            String SQL2 = "DELETE FROM friend_request where reciver_id=" + userId;

            stmt.executeUpdate(SQL2);

        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return requestList;
    }

    public void updateUserStatus(int userId, String status) {

        try {
            pst = con.prepareStatement("UPDATE  users set status=? where user_id=?");
            pst.setString(1, status);
            pst.setInt(2, userId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        

    }
}
