package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("LiveMessage")
public class LiveMessageModel extends ParseObject {

    public static final String MESSAGE_TYPE_COMMENT = "COMMENT";
    public static final String MESSAGE_TYPE_FOLLOW = "FOLLOW";
    public static final String MESSAGE_TYPE_GIFT = "GIFT";

    public static final String SENDER_AUTHOR = "author";
    public static final String SENDER_AUTHOR_ID = "authorId";

    public static final String LIVE_STREAM = "liveStream";
    public static final String LIVE_STREAM_ID = "liveStreamId";

    public static final String GIFT_LIVE = "giftLive";
    public static final String GIFT_LIVE_ID = "giftLiveId";

    public static final String MESSAGE = "message";
    public static final String MESSAGE_TYPE = "messageType";

    public User getSenderAuthor() {
        return (User) getParseObject(SENDER_AUTHOR);
    }

    public void setSenderAuthor(User senderAuthor){
        put(SENDER_AUTHOR, senderAuthor);
    }

    public String getSenderAuthorId() {
        return getString(SENDER_AUTHOR_ID);
    }

    public void setSenderAuthorId(String senderAuthorId){
        put(SENDER_AUTHOR_ID, senderAuthorId);
    }


    public LiveStreamModel getLiveStream() {
        return (LiveStreamModel) getParseObject(LIVE_STREAM);
    }

    public void setLiveStream(LiveStreamModel liveStream){
        put(LIVE_STREAM, liveStream);
    }

    public GiftModel getLiveGift() {
        return (GiftModel) getParseObject(GIFT_LIVE);
    }

    public void setLiveGift(GiftModel giftModel){
        put(GIFT_LIVE, giftModel);
    }

    public String getLiveGiftId() {
        return getString(GIFT_LIVE_ID);
    }

    public void setLiveGiftId(String giftId){
        put(GIFT_LIVE_ID, giftId);
    }

    public String getLiveStreamId() {
        return getString(LIVE_STREAM_ID);
    }

    public void setLiveStreamId(String receiverAuthorId){
        put(LIVE_STREAM_ID, receiverAuthorId);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String message){
        put(MESSAGE, message);
    }

    public String getMessageType() {
        return getString(MESSAGE_TYPE);
    }

    public void setMessageType(String type){
        put(MESSAGE_TYPE, type);
    }


    public static ParseQuery<LiveMessageModel> getLiveMessageQuery()
    {
         return  ParseQuery.getQuery(LiveMessageModel.class);
    }
}
