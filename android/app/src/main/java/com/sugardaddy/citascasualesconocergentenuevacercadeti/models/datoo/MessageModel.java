package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Message")
public class MessageModel extends ParseObject {

    public static final String SENDER_AUTHOR = "fromUser";
    public static final String SENDER_AUTHOR_ID = "fromUserId";

    public static final String RECEIVER_AUTHOR = "toUser";
    public static final String RECEIVER_AUTHOR_ID = "toUserId";

    public static final String MESSAGE = "message";
    public static final String MESSAGE_FILE = "messageFile";
    public static final String IS_MESSAGE_FILE = "isMessageFile";
    public static final String MESSAGE_FILE_UPLOADED = "fileUploaded";
    public String imagePath;

    public static final String COL_READ = "read";

    public static final String COL_CONNECTION = "Connections";
    public static final String COL_CONNECTION_ID = "ConnectionsId";

    public static final String COL_CALLS = "call";

    public User getSenderAuthor() {
        return (User) this.getParseObject(SENDER_AUTHOR);
    }

    public void setSenderAuthor(User senderAuthor){
        put(SENDER_AUTHOR, senderAuthor);
    }

    public User getReceiverAuthor() {
        return (User) this.getParseObject(RECEIVER_AUTHOR);
    }

    //I change User receiverAuthor to
    public void setReceiverAuthor(User receiverAuthor){
        put(RECEIVER_AUTHOR, receiverAuthor);
    }

    public ParseFile getMessageFile() {
        return getParseFile(MESSAGE_FILE);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String message){
        put(MESSAGE, message);
    }

    public void setMessageFile(ParseFile messageFile){
        put(MESSAGE_FILE, messageFile);
    }

    public boolean isFileUploaded() {
        return getBoolean(MESSAGE_FILE_UPLOADED);
    }

    public void setFileUploaded(boolean uploaded){
        put(MESSAGE_FILE_UPLOADED, uploaded);
    }

    public boolean isMessageFile() {
        return getBoolean(IS_MESSAGE_FILE);
    }

    public void setMessageFile(boolean isfile){
        put(IS_MESSAGE_FILE, isfile);
    }

    public String getSenderAuthorId() {
        return getString(SENDER_AUTHOR_ID);
    }

    public void setSenderAuthorId(String senderAuthorId){
        put(SENDER_AUTHOR_ID, senderAuthorId);
    }

    public String getReceiverAuthorId() {
        return getString(RECEIVER_AUTHOR_ID);
    }

    public void setReceiverAuthorId(String receiverAuthorId){
        put(RECEIVER_AUTHOR_ID, receiverAuthorId);
    }

    public void setMessageOnList(ConnectionListModel connectionListModel) {

        put(COL_CONNECTION, connectionListModel);
    }

    public ConnectionListModel getMessageOnList() {

        return (ConnectionListModel) getParseObject(COL_CONNECTION);
    }

    public void setMessageOnListId(String messageId) {

        put(COL_CONNECTION_ID, messageId);
    }

    public String getMessageOnListId() {

        return getString(COL_CONNECTION_ID);
    }

    public void setRead(boolean isRead){
        put(COL_READ, isRead);
    }

    public boolean getRead(){
        try {
            return fetchIfNeeded().getBoolean(COL_READ);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CallsModel getCall() {
        return (CallsModel) getParseObject(COL_CALLS);
    }

    public void setCall(CallsModel callsModel){
        put(COL_CALLS, callsModel);
    }

    public static ParseQuery<MessageModel> getMessageParseQuery()
    {
        return  ParseQuery.getQuery(MessageModel.class);
    }
}
