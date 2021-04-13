package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Follow")
public class FollowModel extends ParseObject {

    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";

    public static final String FROM_AUTHOR = "from_author";
    public static final String TO_AUTHOR = "to_author";

    public User getFromAuthor() {
        return (User) getParseObject(FROM_AUTHOR);
    }

    public User getToAuthor() {
        return (User) getParseObject(TO_AUTHOR);
    }

    public void setFromAuthor(User user){

        this.put(FROM_AUTHOR, user);
    }

    public void setToAuthor(User user){

        this.put(TO_AUTHOR, user);
    }
    ////////////////////////////////////////////////////////////////////

    public static void queryFollowSingleUser(User user, final QueryFollowListener callback){


        ParseQuery<FollowModel> isMatchedQuery = getQuery();
        User currentUser = (User)User.getCurrentUser();

        isMatchedQuery.whereEqualTo(FROM_AUTHOR, currentUser);
        isMatchedQuery.whereEqualTo(TO_AUTHOR, user);

        isMatchedQuery.getFirstInBackground((followModel, e) -> {
            if (followModel != null) {

                callback.onQueryFollowSuccess(true);

            } else {

                if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                    callback.onQueryFollowSuccess(false);

                } else {

                    callback.onQueryFollowError(e);
                }
            }
        });
    }

    public interface QueryFollowListener {
        void onQueryFollowSuccess(boolean isFollowing);
        void onQueryFollowError(ParseException error);
    }

    public static ParseQuery<FollowModel> getQuery(){
        return ParseQuery.getQuery(FollowModel.class);
    }
}
