package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Withdrawn")
public class WithdrawModel extends ParseObject {

    public static final String TYPE_WITHDRAW = "withdraw";
    public static final String TYPE_EXCHANGE = "exchange";

    public static final String AUTHOR = "author";
    public static final String TYPE = "type";
    public static final String TOKENS = "tokens";
    public static final String CREDITS = "credits";
    public static final String COMPLETED = "completed";
    public static final String PAYPAL_EMAIL = "paypal";

    public static ParseQuery<WithdrawModel> getWithdrawQuery() {
        return  ParseQuery.getQuery(WithdrawModel.class);
    }

    public User getAuthor(){
        return (User) getParseObject(AUTHOR);
    }

    public void setAuthor(User user) {
        put(AUTHOR, user);
    }

    public int getCredits() {
        return getInt(CREDITS);
    }

    public void setCredits(int credits) {
         put(CREDITS, credits);
    }

    public int getTokens() {
        return getInt(TOKENS);
    }

    public void setTokens(int credits) {
        put(TOKENS, credits);
    }

    public String getPayPalEmail() {
        return getString(PAYPAL_EMAIL);
    }

    public void setPayPalEmail(String paypalEmail) {
        put(PAYPAL_EMAIL, paypalEmail);
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setType(String type) {
        put(TYPE, type);
    }

    public boolean isCompleted() {
        return getBoolean(COMPLETED);
    }

    public void setCompleted(boolean completed) {
        put(COMPLETED, completed);
    }
}
