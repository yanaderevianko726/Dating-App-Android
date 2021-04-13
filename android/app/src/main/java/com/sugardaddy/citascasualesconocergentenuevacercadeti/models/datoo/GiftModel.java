package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Gift")
public class GiftModel extends ParseObject {

    public static final String SEND_GIFT_PARAM = "send_gift";
    public static final String CREDITS_PARAM = "credits";

    public static final String CREDITS = "Credits";
    public static final String FILE = "file";

    public int getCredits() {
        return getInt(CREDITS);
    }

    public ParseFile getGiftFile() {
        try {
            return this.fetchIfNeeded().getParseFile(FILE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static ParseQuery<GiftModel> getGiftQuery() {
        return  ParseQuery.getQuery(GiftModel.class);
    }
}
