package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("AutoMessages")
public class AutoMessagesModel extends ParseObject {

    public static final String QUESTION = "question";
    public static final String RESPONSE = "response";

    public static void queryQuestion(String objectId, String message, QueryForQuestionListener callback){

        ParseConfig.getInBackground((config, e) -> {
            if (e == null) {
                Log.d("TAG", "Yay! Config was fetched from the server.");
            } else {
                Log.e("TAG", "Failed to fetch. Using Cached Config.");
                config = ParseConfig.getCurrentConfig();
            }

            // Get the message from config or fallback to default value
            String fakeSignupMessageUser = config.getString("female_signup_user_id", null);

            if (fakeSignupMessageUser != null && !fakeSignupMessageUser.isEmpty() && fakeSignupMessageUser.equals(objectId)){

                Log.d("TAG", "Yay! User is valid");

                ParseQuery<AutoMessagesModel> autoMessagesModelParseQuery = AutoMessagesModel.getAutoMessagesQuery();
                autoMessagesModelParseQuery.whereEqualTo(AutoMessagesModel.QUESTION, message);
                autoMessagesModelParseQuery.getFirstInBackground(new GetCallback<AutoMessagesModel>() {
                    @Override
                    public void done(AutoMessagesModel autoMessagesModel, ParseException e) {
                        if (e == null){
                            Log.d("TAG",String.format("autoMessagesModel %s", autoMessagesModel.getResponse()));
                            callback.onQueryResponseFound(autoMessagesModel);
                        }
                    }
                });
            }
        });
    }

    public static ParseQuery<AutoMessagesModel> getAutoMessagesQuery() {
        return  ParseQuery.getQuery(AutoMessagesModel.class);
    }

    public String getQuestion(){
        return getString(QUESTION);
    }

    public String getResponse(){
        return getString(RESPONSE);
    }

    public interface QueryForQuestionListener {
        void onQueryResponseFound(AutoMessagesModel autoMessagesModel);
    }
}
