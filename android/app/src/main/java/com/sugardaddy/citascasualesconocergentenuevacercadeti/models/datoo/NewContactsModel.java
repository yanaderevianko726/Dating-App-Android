package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;


@ParseClassName("NewContacts")
public class NewContactsModel extends ParseObject {

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

    public static ParseQuery<NewContactsModel> getNewContactsQuery(){
        return ParseQuery.getQuery(NewContactsModel.class);
    }
}
