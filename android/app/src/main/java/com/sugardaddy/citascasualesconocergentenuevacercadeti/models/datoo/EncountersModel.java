package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import android.text.TextUtils;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@ParseClassName("Encounters")
public class EncountersModel extends ParseObject {
    public static String COL_FROM_USER = "from_user";
    public static String COL_TO_USER = "to_user";
    public static String COL_LIKED = "liked";

    public static String COL_SEEN = "seen";

    public User getFromUser(){
        return (User)this.getParseObject(COL_FROM_USER);
    }

    public User getToUser(){
        return (User)this.getParseObject(COL_TO_USER);
    }

    public void setMatch(boolean match){
        if(match){
            this.put(COL_LIKED, "YES");
        } else {
            this.put(COL_LIKED, "NO");
        }
    }

    public static EncountersModel newMatch(User fromUser, User toUser, boolean match){
        EncountersModel newMatch = new EncountersModel();
        newMatch.setFromUser(fromUser);
        newMatch.setToUser(toUser);
        newMatch.setMatch(match);
        newMatch.setSeen(false);
        return newMatch;
    }

    public static void newMatch(final User fromUser, User toUser, boolean match, boolean seen, SaveCallback callback){
        EncountersModel newMatch = new EncountersModel();
        newMatch.setFromUser(fromUser);
        newMatch.setToUser(toUser);
        newMatch.setMatch(match);
        newMatch.setSeen(seen);
        newMatch.saveInBackground(callback);
    }

    public static void queryMatch(User user, final QueryMatchListener callback){
        ParseQuery<EncountersModel> isMatchedQuery = getQuery();
        User currentUser = (User)User.getCurrentUser();

        isMatchedQuery.whereEqualTo(COL_FROM_USER, user);
        isMatchedQuery.whereEqualTo(COL_TO_USER, currentUser);
        isMatchedQuery.whereEqualTo(COL_LIKED, "YES");
        isMatchedQuery.getFirstInBackground((encountersModel, e) -> {
            if (encountersModel != null) {
                Log.d("myapp",String.format("encountersModel %s", encountersModel.isMatch()));
                callback.onQueryMatchSuccess(encountersModel);

                setMatchToMine(user);

            } else {
                Log.d("myapp","encountersModel is null");
                callback.onQueryMatchError(e);
            }
        });
    }

    public static void queryMatchSingleUser(User user, final QueryMatchProfileListener callback){


        ParseQuery<EncountersModel> isMatchedQuery = getQuery();
        User currentUser = (User)User.getCurrentUser();

        isMatchedQuery.whereEqualTo(COL_FROM_USER, currentUser);
        isMatchedQuery.whereEqualTo(COL_TO_USER, user);

        isMatchedQuery.getFirstInBackground((encountersModel, e) -> {
            if (encountersModel != null) {

                callback.onQueryMatchSuccess(true);

            } else {

                if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                    callback.onQueryMatchSuccess(false);

                } else {

                    callback.onQueryMatchError(e);
                }
            }
        });
    }

    private static void setMatchToMine(User user){

        ParseQuery<EncountersModel> isMatchedQuery = getQuery();
        User currentUser = (User)User.getCurrentUser();

        isMatchedQuery.whereEqualTo(COL_FROM_USER, currentUser);
        isMatchedQuery.whereEqualTo(COL_TO_USER, user);
        isMatchedQuery.whereEqualTo(COL_LIKED, "YES");
        isMatchedQuery.getFirstInBackground((encountersModel1, e1) -> {

            if (e1 == null){

                encountersModel1.setSeen(true);
                encountersModel1.saveEventually();
            }
        });
    }

    public interface QueryMatchListener {
        void onQueryMatchSuccess(EncountersModel encountersModel);
        void onQueryMatchError(ParseException error);
    }

    public interface QueryMatchProfileListener {
        void onQueryMatchSuccess(boolean isLiked);
        void onQueryMatchError(ParseException error);
    }

    public static ParseQuery<EncountersModel> getQuery(){
        return new ParseQuery<EncountersModel>(EncountersModel.class);
    }


    public void setFromUser(User fromUser){
        this.put(COL_FROM_USER, fromUser);
    }

    public void setToUser(User toUser){
        this.put(COL_TO_USER, toUser);
    }

    public String getMatch(){
        return this.getString(COL_LIKED);
    }

    public boolean isMatch(){
        return TextUtils.equals(getMatch(), "YES");
    }

    public void setSeen(boolean seen){
        this.put(COL_SEEN, seen);
    }

    public static void userMatches(User user, FindCallback<EncountersModel> callback){
        ParseQuery<EncountersModel> userMatchesQuery = getQuery();
        userMatchesQuery.whereEqualTo(COL_FROM_USER, user);
        userMatchesQuery.include(COL_TO_USER);
        userMatchesQuery.findInBackground(callback);
    }


}
