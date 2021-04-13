package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Calls")
public class CallsModel extends ParseObject {

    public static final String CALL_END_REASON_END = "TERMINATED";
    public static final String CALL_END_REASON_BUSY = "BUSY";
    public static final String CALL_END_REASON_GIVE_UP = "GIVE_UP";
    public static final String CALL_END_REASON_REFUSED = "REFUSED";
    public static final String CALL_END_REASON_NO_ANSWER = "NO_ANSWER";
    public static final String CALL_END_REASON_OFFLINE = "OFFLINE";

    public static final String SENDER_AUTHOR = "fromUser";
    public static final String SENDER_AUTHOR_ID = "fromUserId";
    //toUser
    public static final String RECEIVER_AUTHOR = "toUser";
    public static final String RECEIVER_AUTHOR_ID = "toUserId";

    public static final String DURATION = "duration";

    public static final String ACCEPTED = "accepted";

    public static final String CALL_END_REASON = "reason";

    public static final String CALL_TYPE_VOICE = "isVoiceCall";


    public User getSenderAuthor() {
        return (User) getParseObject(SENDER_AUTHOR);
    }

    public void setCallerAuthor(User senderAuthor){
        put(SENDER_AUTHOR, senderAuthor);
    }

    public User getReceiverAuthor() {
        return (User) getParseObject(RECEIVER_AUTHOR);
    }

    //I change User receiverAuthor to
    public void setReceiverAuthor(User receiverAuthor){
        put(RECEIVER_AUTHOR, receiverAuthor);
    }

    public String getDuration() {
        return getString(DURATION);
    }

    public void setDuration(String duration){
        put(DURATION, duration);
    }

    public boolean isAccepted() {
        try {
            return fetchIfNeeded().getBoolean(ACCEPTED);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setAccepted(boolean accepted){
        put(ACCEPTED, accepted);
    }

    public String getSenderAuthorId() {
        return getString(SENDER_AUTHOR_ID);
    }

    public void setCallerAuthorId(String senderAuthorId){
        put(SENDER_AUTHOR_ID, senderAuthorId);
    }

    public String getCallEndReason() {
        return getString(CALL_END_REASON);
    }

    public void setCallEndReason(String reason){
        put(CALL_END_REASON, reason);
    }

    public boolean isVoiceCall() {
        return getBoolean(CALL_TYPE_VOICE);
    }

    public void setVoiceCall(boolean voiceCall){
        put(CALL_TYPE_VOICE, voiceCall);
    }

    public String getReceiverAuthorId() {
        return getString(RECEIVER_AUTHOR_ID);
    }

    public void setReceiverAuthorId(String receiverAuthorId){
        put(RECEIVER_AUTHOR_ID, receiverAuthorId);
    }

    public static ParseQuery<CallsModel> getCallsParseQuery()
    {
        return  ParseQuery.getQuery(CallsModel.class);
    }
}
