package server;

import DAO.ServerDAO;
import com.healthmarketscience.rmiio.RemoteInputStream;

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Vector;
import java_chat_interfaces.ChatServerInt;
import java_chat_interfaces.ClientInt;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.LoginDTO;
import java_chat_interfaces.MessageInt;
import java_chat_interfaces.UserImageInt;
import java_chat_interfaces.Users;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServerInt {

    Vector<ClientInt> clientsVector = new Vector<ClientInt>();
    ServerDAO dbDao = new ServerDAO();

    public ChatServerImpl() throws RemoteException {
    }

    public int register(ClientInt clientRef) throws RemoteException {

        clientsVector.add(clientRef);

        System.out.println("Client added");
        return clientRef.getId();

    }

    public void unRegister(ClientInt clientRef) throws RemoteException {
        clientsVector.remove(clientRef);

        System.out.println("Client removed");
    }

    @Override
    public void tellOthers(MessageInt msg, int user_id, int friend_id) throws RemoteException {

        System.out.println("Message received: " + msg);
        for (ClientInt clientRef : clientsVector) {
            try {
                if (clientRef.getId() == friend_id) {
                    clientRef.receive(msg, user_id);
                }

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }

        }
    }

    @Override
    public int login(LoginDTO user) throws RemoteException {

        return dbDao.isUser(user);

    }

    @Override
    public int registerUserToDB(Users user) throws RemoteException {
        return dbDao.insertUser(user);
    }

    @Override
    public ArrayList<FriendListViewInt> getFriendList(int id) throws RemoteException {

        return dbDao.getFrindList(id);

    }

    @Override
    public Users getUserData(int id) throws RemoteException {
        return dbDao.getUserData(id);
    }

    @Override
    public int setImageToDataBase(UserImageInt userImg) throws RemoteException {
        return dbDao.saveImageToDatabase(userImg);
    }

    @Override
    public UserImageInt getImageToDataBase(int userId) throws RemoteException {

        return dbDao.getImageFromDataBase(userId);
    }

    @Override
    public boolean checkIfFriendIsFounde(String email, int userId) throws RemoteException {
        return dbDao.checkIfFriendIsFounde(email, userId);
    }

    @Override
    public int addRequest(String email, int senderId) throws RemoteException {

        return dbDao.addRequest(email, senderId);
    }

    @Override
    public ArrayList<Integer> retriveRequestList(int userId) throws RemoteException {

        return dbDao.retriveRequestList(userId);
    }

    @Override
    public void addFriend(int myId, int reqId) throws RemoteException {

        dbDao.addFriend(myId, reqId);
    }

    @Override
    public FriendListViewInt getFriendData(int FriendId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addMeToHisFriendList(FriendListViewInt myData, int friendId) throws RemoteException {

        for (ClientInt clientRef : clientsVector) {
            try {
                if (clientRef.getId() == friendId) {
                    clientRef.addFriendToList(myData);
                }

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }

        }
    }

    @Override
    public void changeUserStatus(int userId, String Status) throws RemoteException {

        dbDao.updateUserStatus(userId, Status);
        System.out.println("Status received: " + Status);
        for (ClientInt clientRef : clientsVector) {
            try {
                if (clientRef.getId() == userId) {
                    continue;
                }
                clientRef.changeFriendStatus(userId, Status);

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }

        }
    }

    public void sendMessageToAllUsers(String msg) throws RemoteException {

        System.out.println("Message received: " + msg);
        for (ClientInt clientRef : clientsVector) {
            try {
                clientRef.showServerMessage(msg);

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }

        }
    }

  

    @Override
    public void requestToSendFile(int userId, int friendId) throws RemoteException {

        System.out.println("Id received: " + friendId);
        for (ClientInt clientRef : clientsVector) {
            if (clientRef.getId() == friendId) {
                try {
                    clientRef.requestToSendFile(userId);

                } catch (RemoteException ex) {
                    System.out.println("Can not send message to client");
                    ex.printStackTrace();

                }
            }

        }
    }

    @Override
    public void TellOtheFiles(RemoteInputStream data, String ext, int senderId,int reciverId) throws RemoteException {
        
        System.out.println("insied serve TellOtheFiles method");
        
         for (ClientInt clientRef : clientsVector) {
            try {
                if (clientRef.getId() == reciverId) {
                    clientRef.downloadFile(data, ext);
                }

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }
    }
    }

    @Override
    public void uploadFileToServer(String filePath, String extention, int senderId,int reciverId) throws RemoteException {
        
        System.out.println("insied serve uploadFileToServer method "+filePath);
         for (ClientInt clientRef : clientsVector) {
             
            try {
                if (clientRef.getId() == senderId) {
                    clientRef.uploadFileToServer(filePath, extention, senderId,reciverId);
                   
                }

            } catch (RemoteException ex) {
                System.out.println("Can not send message to client");
                ex.printStackTrace();

            }

        }
        
    }

}
