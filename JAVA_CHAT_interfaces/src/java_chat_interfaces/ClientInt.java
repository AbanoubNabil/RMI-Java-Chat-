package java_chat_interfaces;

import com.healthmarketscience.rmiio.RemoteInputStream;
import java.io.InputStream;
import java.rmi.*;

public interface ClientInt extends Remote {

    void receive(MessageInt msg, int senderId) throws RemoteException;

    void setId(int id) throws RemoteException;

    int getId() throws RemoteException;

    void addFriendToList(FriendListViewInt item) throws RemoteException;

    void changeFriendStatus(int FriendId, String status) throws RemoteException;

    void showServerMessage(String message) throws RemoteException;

    void requestToSendFile(int friendId) throws RemoteException;

    void uploadFileToServer(String filePath, String extention, int senderId ,int reciverId) throws RemoteException;

    void downloadFile(RemoteInputStream data, String ext) throws RemoteException;

}
