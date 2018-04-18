package client;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import connectorClasses.ScenesConnector;
import controller.ChatViewController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.ClientInt;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.MessageInt;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import javax.swing.JOptionPane;

public class ClientImpl extends UnicastRemoteObject implements ClientInt {

    private ChatViewController controller;

    private int clientId = ScenesConnector.getUserId();

    public ClientImpl(ChatViewController c) throws RemoteException {

        controller = c;

    }

    @Override
    public void receive(MessageInt msg, int senderId) throws RemoteException {
        System.out.println(msg);
        ScenesConnector.setFriendId(senderId);
        ScenesConnector.setFriendChossedFlag(true);
        controller.display(msg, senderId);

    }

    @Override
    public void setId(int id) throws RemoteException {
        clientId = id;
    }

    @Override
    public int getId() throws RemoteException {

        return clientId;
    }

    @Override
    public void addFriendToList(FriendListViewInt item) throws RemoteException {
        ScenesConnector.getFriendsList().add(item);
    }

    @Override
    public void changeFriendStatus(int FriendId, String status) throws RemoteException {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < ScenesConnector.getFriendsList().size(); i++) {

                    if (ScenesConnector.getFriendsList().get(i).getFrindId() == FriendId) {

                        FriendListViewInt friendData = ScenesConnector.getFriendsList().get(i);
                        friendData.setStatus(status);
                        ScenesConnector.getFriendsList().remove(i);
                        ScenesConnector.getFriendsList().add(i, friendData);

                        System.out.println("status is changed");

                    }

                }
            }
        });

    }

    @Override
    public void showServerMessage(String message) throws RemoteException {

        System.out.println("message that recived from the server: " + message);

        controller.showServerMessage(message);
    }

    @Override
    public void requestToSendFile(int friendId) throws RemoteException {

        controller.showSendFileNotification(friendId);
    }

    @Override
    public void uploadFileToServer(String filePath, String extention, int senderId, int reciverId) throws RemoteException {

        System.out.println("insied uploadFileToServer in clientImpl: " + filePath);

        controller.uploadFileToServer(filePath, extention, senderId, reciverId);

    }

    @Override
    public void downloadFile(RemoteInputStream data, String ext) throws RemoteException {

        System.out.println("insied downloadFile method");

        String name = JOptionPane.showInputDialog("please enter file name");
//        TextInputDialog dialog = new TextInputDialog("File Name");
//        dialog.setTitle("File Name");
//        dialog.setHeaderText("Please enter File name");
//        Optional<String> fileName = dialog.showAndWait();
//        String name = null;
//        if (fileName != null) {
//            name = fileName.get();
//        }

        try {
            InputStream stream = RemoteInputStreamClient.wrap(data);
            FileOutputStream output = new FileOutputStream("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + name + "_chat." + ext);
            int chunk = 4096;
            byte[] result = new byte[chunk];
            int readBytes = 0;
            do {
                readBytes = stream.read(result);
                if (readBytes != -1) {
                    output.write(result, 0, readBytes);

                }
            } while (readBytes != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
