/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectorClasses;

import DTO.FrindsListView;
import java_chat_interfaces.ChatServerInt;
import java_chat_interfaces.FriendListViewInt;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author IROCK
 */
public class ScenesConnector {

    private static Stage stage;
    private static ChatServerInt chRef;
    private static int userId;
    private static String userName;
    private static int friendId;
    private static String friendName;
    private static Image userImage;
    private static Image friendImage;
    private static ObservableList<FriendListViewInt> friendsList;
    private static boolean friendChossedFlag;
    private static int senderRequestId;
    private static String fileChoosedName;
    private static String fileExtention;
    private static int fileSenderRequestId;
    private static String fileName;
    private static String fileExt;
    private static String registryIp;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ScenesConnector.stage = stage;
    }

    public static ChatServerInt getChRef() {
        return chRef;
    }

    public static void setChRef(ChatServerInt chRef) {
        ScenesConnector.chRef = chRef;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        ScenesConnector.userId = userId;
    }

    public static int getFriendId() {
        return friendId;
    }

    public static void setFriendId(int friendId) {
        ScenesConnector.friendId = friendId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        ScenesConnector.userName = userName;
    }

    public static String getFriendName() {
        return friendName;
    }

    public static void setFriendName(String friendName) {
        ScenesConnector.friendName = friendName;
    }

    public static ObservableList<FriendListViewInt> getFriendsList() {
        return friendsList;
    }

    public static void setFriendsList(ObservableList<FriendListViewInt> friendsList) {
        ScenesConnector.friendsList = friendsList;
    }

    public static boolean isFriendChossedFlag() {
        return friendChossedFlag;
    }

    public static void setFriendChossedFlag(boolean _friendChossedFlag) {
        friendChossedFlag = _friendChossedFlag;
    }

    public static Image getUserImage() {
        return userImage;
    }

    public static void setUserImage(Image userImage) {
        ScenesConnector.userImage = userImage;
    }

    public static Image getFriendImage() {
        return friendImage;
    }

    public static void setFriendImage(Image friendImage) {
        ScenesConnector.friendImage = friendImage;
    }

    public static int getSenderRequestId() {
        return senderRequestId;
    }

    public static void setSenderRequestId(int senderRequestId) {
        ScenesConnector.senderRequestId = senderRequestId;
    }

    public static String getFileChoosedName() {
        return fileChoosedName;
    }

    public static void setFileChoosedName(String fileChoosedName) {
        ScenesConnector.fileChoosedName = fileChoosedName;
    }

    public static String getFileExtention() {
        return fileExtention;
    }

    public static void setFileExtention(String fileExtention) {
        ScenesConnector.fileExtention = fileExtention;
    }

    public static int getFileSenderRequestId() {
        return fileSenderRequestId;
    }

    public static void setFileSenderRequestId(int fileSenderRequestId) {
        ScenesConnector.fileSenderRequestId = fileSenderRequestId;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        ScenesConnector.fileName = fileName;
    }

    public static String getFileExt() {
        return fileExt;
    }

    public static void setFileExt(String fileExt) {
        ScenesConnector.fileExt = fileExt;
    }

    public static String getRegistryIp() {
        return registryIp;
    }

    public static void setRegistryIp(String registryIp) {
        ScenesConnector.registryIp = registryIp;
    }
    

}
