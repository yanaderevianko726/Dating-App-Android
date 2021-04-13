package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import android.util.Log;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.DateUtils;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.Date;

@ParseClassName("Connections")
public class ConnectionListModel extends ParseObject {

    public static final String CLASS = "Connections";

    public static final String CONNECTION_TYPE_MESSAGE = "MESSAGE";
    public static final String CONNECTION_TYPE_FAVORITE = "FAVORITE";
    public static final String CONNECTION_TYPE_VISITOR = "VISITOR";

    public static final String MESSAGE_TYPE_CHAT = "CHAT";
    public static final String MESSAGE_TYPE_MATCHED = "MATCHED";
    public static final String MESSAGE_TYPE_IMAGE = "IMAGE";
    public static final String MESSAGE_TYPE_CALL = "CALL";

    public static final String COL_CONNECTION_TYPE = "type";
    public static final String COL_MESSAGE_TYPE = "messageType";
    public static final String COL_USER_FROM = "fromUser";
    public static final String COL_USER_TO = "toUser";
    public static final String COL_USER_FROM_ID = "fromUserId";
    public static final String COL_USER_TO_ID = "toUserId";
    public static final String COL_IMAGE = "image";
    public static final String COL_TEXT = "text";
    public static final String COL_READ = "read";
    public static final String COL_READ_SENDER = "read_sender";
    public static final String COL_READ_RECEIVER = "read_receiver";
    public static final String COL_COUNT = "count";
    public static final String COL_MESSAGE = "message";
    public static final String COL_MESSAGE_ID = "messageId";
    public static final String COL_CREATED_AT = "createdAt";
    public static final String COL_UPDATED_AT = "updatedAt";

    public static final String COL_HIDDEN_SENDER = "hiddenBySender";
    public static final String COL_HIDDEN_RECEIVER = "hiddenByReceiver";

    public static final String COL_CALLS = "call";

    //////////////// CONNECTION TYPE ///////////////////////////

    public void setConnectionType(String connectionType){
        this.put(COL_CONNECTION_TYPE, connectionType);
    }

    public String getConnectionType(){
        return this.getString(COL_CONNECTION_TYPE);
    }

    // Set and get User Fom  (User that is sending a message)

    public void setMessageType(String messageType){
        this.put(COL_MESSAGE_TYPE, messageType);
    }

    public String getColMessageType(){
        return this.getString(COL_MESSAGE_TYPE);
    }

    public void setUserFrom(User userFrom){
        this.put(COL_USER_FROM, userFrom);
    }

    public void setUserFromId(String userId){
        this.put(COL_USER_FROM_ID, userId);
    }

    public User getUserFrom(){
        return (User)this.getParseObject(COL_USER_FROM);
    }

    public String getUserFromId(){
        return this.getString(COL_USER_FROM_ID);
    }

    //Set and get User To  (User that is Receiving a message)

    public void setUserTo(User userTo){
        this.put(COL_USER_TO, userTo);
    }

    public void setUserToId(String userToId){
        this.put(COL_USER_TO_ID, userToId);
    }

    public User getUserTo(){
        return (User)this.getParseObject(COL_USER_TO);
    }

    public String getUserToId(){
        return this.getString(COL_USER_TO_ID);
    }

    // Count unread messages

    public int getCount() {
        return this.getInt(COL_COUNT);
    }

    public void setCount() {
        increment(COL_COUNT);
    }

    public void removetCount() {
        increment(COL_COUNT, -1);
    }

    public void resetCount() {
        put(COL_COUNT, 0);
    }

    // Set and get text message

    public String getText(){
        return this.getString(COL_TEXT);
    }

    public void setText(String text){
        this.put(COL_TEXT, text);
    }


    // Set and get read status

    public void setRead(boolean isRead){
        this.put(COL_READ, isRead);
    }

    public boolean isRead(){
        return this.getBoolean(COL_READ);
    }

    public void setReadBySender(boolean isRead){
        this.put(COL_READ_SENDER, isRead);
    }

    public boolean isReadBySender(){
        return this.getBoolean(COL_READ_SENDER);
    }

    public void setReadByReceiver(boolean isRead){
        this.put(COL_READ_RECEIVER, isRead);
    }

    public boolean isReadByReceiver(){
        return this.getBoolean(COL_READ_RECEIVER);
    }

    //  get and set image in message or image message

    public String getMessageImageUrl(){
        String messageImageUrl = "";
        if(this.getParseFile(COL_IMAGE) != null){
            messageImageUrl = this.getParseFile(COL_IMAGE).getUrl();
        }
        return messageImageUrl;
    }

    public void setImage(ParseFile image){
        put(COL_IMAGE, image);
    }

    // get time

    public String getTime(){

        Date messageDate = this.getUpdatedAt();
        return DateUtils.getTimeAgo(messageDate.getTime());
    }

    // These may be needed

    public void setMessage(MessageModel message) {

        put(COL_MESSAGE, message);
        put(COL_HIDDEN_SENDER, false);
        put(COL_HIDDEN_RECEIVER, false);
    }

    public void setSenderHidden(boolean hidden) {

        put(COL_HIDDEN_SENDER, hidden);
    }

    public void setReceiverHidden(boolean hidden) {

        put(COL_HIDDEN_RECEIVER, hidden);
    }

    public MessageModel getMessage() {

        return (MessageModel) this.getParseObject(COL_MESSAGE);
    }

    public void setMessageId(String messageId) {

        put(COL_MESSAGE_ID, messageId);
    }

    public String getMessageId() {

        return this.getString(COL_MESSAGE_ID);
    }
    public CallsModel getCall() {
        return (CallsModel) getParseObject(COL_CALLS);
    }

    public void setCall(CallsModel callsModel){
        put(COL_CALLS, callsModel);
    }


    public static void queryUserConversation(User user, final QueryUserConversationListener callback){

        ParseQuery<ConnectionListModel> messageQuery;

        User currentUser = (User)User.getCurrentUser();

        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, currentUser);
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, user);
        messageFromQuery.whereEqualTo(COL_CONNECTION_TYPE, CONNECTION_TYPE_MESSAGE);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, user);
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, currentUser);
        messageToQuery.whereEqualTo(COL_CONNECTION_TYPE, CONNECTION_TYPE_MESSAGE);

        messageQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));

        messageQuery.whereEqualTo(COL_CONNECTION_TYPE, CONNECTION_TYPE_MESSAGE);

        messageQuery.getFirstInBackground((encountersModel, e) -> {
            if (encountersModel != null) {

                callback.onQueryExist(true);

            } else {

                if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                    callback.onQueryExist(false);

                } else {

                    callback.onQueryError(e);
                }
            }
        });
    }

    public static void queryFavorite(User user, final QueryFavoriteListener callback){
        ParseQuery<ConnectionListModel> isFavoritesQuery = getConnectionsQuery();
        User currentUser = (User)User.getCurrentUser();

        isFavoritesQuery.whereEqualTo(COL_USER_FROM, currentUser);
        isFavoritesQuery.whereEqualTo(COL_USER_TO, user);
        isFavoritesQuery.whereEqualTo(COL_CONNECTION_TYPE, CONNECTION_TYPE_FAVORITE);
        isFavoritesQuery.getFirstInBackground((favoriteModel, e) -> {
            if (favoriteModel != null) {

                callback.onQueryFavorited(favoriteModel);

            } else {

                Log.d("myapp","favorite is null");

                if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                    callback.onQueryUnFavorited();

                } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                    callback.onQueryFavoriteError(e);
                }

            }
        });
    }

    public interface QueryFavoriteListener {
        void onQueryFavorited(ConnectionListModel favoriteModel);
        void onQueryUnFavorited();
        void onQueryFavoriteError(ParseException error);
    }

    public interface QueryUserConversationListener {
        void onQueryExist(boolean exist);
        void onQueryError(ParseException error);
    }

    public static ParseQuery<ConnectionListModel> getConnectionsQuery(){
        return new ParseQuery<>(ConnectionListModel.class);
    }
}
