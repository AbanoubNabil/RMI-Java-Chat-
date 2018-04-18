package java_chat_interfaces;

import com.healthmarketscience.rmiio.RemoteInputStream;
import java.io.InputStream;
import java.rmi.*;
import java.util.ArrayList;
import java.util.HashSet;
import javafx.collections.ObservableList;

public interface ChatServerInt extends Remote {

    void tellOthers(MessageInt msg, int user_id, int friend_id) throws RemoteException;

    int register(ClientInt cliebntRef) throws RemoteException;

    void unRegister(ClientInt cliebntRef) throws RemoteException;

    int login(LoginDTO user) throws RemoteException;

    int registerUserToDB(Users user) throws RemoteException;

    ArrayList getFriendList(int id) throws RemoteException;

    Users getUserData(int id) throws RemoteException;

    int setImageToDataBase(UserImageInt userImg) throws RemoteException;

    UserImageInt getImageToDataBase(int userId) throws RemoteException;

    boolean checkIfFriendIsFounde(String email, int userId) throws RemoteException;

    int addRequest(String email, int senderId) throws RemoteException;

    ArrayList<Integer> retriveRequestList(int userId) throws RemoteException;

    void addFriend(int myId, int reqId) throws RemoteException;

    FriendListViewInt getFriendData(int FriendId) throws RemoteException;

    void addMeToHisFriendList(FriendListViewInt myData, int friendId) throws RemoteException;

    void changeUserStatus(int userId, String Status) throws RemoteException;

    void TellOtheFiles(RemoteInputStream data, String ext,int senderId ,int reciverId)throws RemoteException;
    
    void requestToSendFile(int userId,int friendId)throws RemoteException;
    
    void uploadFileToServer(String filePath,String extention,int senderId,int reciverId)throws RemoteException;

}
