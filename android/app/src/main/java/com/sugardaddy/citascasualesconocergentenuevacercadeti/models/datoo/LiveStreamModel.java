package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Streaming")
public class LiveStreamModel extends ParseObject {

    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";
    public static final String KEY_STARTED_AT = "startedAt";

    public static final String AUTHOR = "author";

    public static final String VIEWS_LIVE = "views_live";
    public static final String VIEWERS_UID = "viewers_uid";
    public static final String VIEWERS_OBJECT_ID = "viewers_id";

    public static final String FOLLOWERS = "followers";

    public static final String STREAMING = "streaming";
    public static final String STREAMING_TIME = "streaming_time";
    public static final String STREAMING_GIFT = "streaming_gift";
    public static final String STREAMING_TOKENS = "streaming_tokens";

    public User getAuthor() {

        try {
            return (User) this.fetchIfNeeded().getParseObject(AUTHOR);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAuthor(User user){
        this.put(AUTHOR, user);
    }

    public void setViewersUid(int uid) {

        addUnique(VIEWERS_UID, uid);
    }

    public List<Integer> getViewersUid() {
        return getList(VIEWERS_UID);
    }

    public void setViewersObjectId(String objectId) {

        addUnique(VIEWERS_OBJECT_ID, objectId);
    }

    public List<String> getViewersObjectId() {
        return getList(VIEWERS_OBJECT_ID);
    }

    public void setViewsLive(int views) {
        increment(VIEWS_LIVE, views);
    }

    public int getViewsLive() {
        return getInt(VIEWS_LIVE);
    }

    public void setFollowers(int uid) {
        addUnique(FOLLOWERS, uid);
    }

    public void removeFollowers(int uid) {

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(uid);

        removeAll(FOLLOWERS, arrayList);
    }

    public List<Integer> getFollowers() {
        return getList(FOLLOWERS);
    }

    public void setStreamingTokens(int tokens) {
        increment(STREAMING_TOKENS, tokens);
    }

    public int getStreamingTokens() {
        return getInt(STREAMING_TOKENS);
    }

    public void setStreamingTime(String time) {
        put(STREAMING_TIME, time);
    }

    public int getStreamingTime() {
        return getInt(STREAMING_TIME);
    }

    public void setStreaming(boolean isStreaming) {
        put(STREAMING, isStreaming);
    }

    public boolean getStreaming() {
        return getBoolean(STREAMING);
    }

    public void setStartedAt(Long date) {
        put(KEY_STARTED_AT, date);
    }

    public Long getStartedAt() {
        return getLong(KEY_STARTED_AT);
    }

    ////////////////////////////////////////////////////////////////////

    public static ParseQuery<LiveStreamModel> getSteamingQuery(){
        return ParseQuery.getQuery(LiveStreamModel.class);
    }
}
