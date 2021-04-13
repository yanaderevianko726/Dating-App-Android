package com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.parse.ParseCloud;

import java.util.HashMap;
import java.util.Map;

public class SendNotifications {

    private static final String PUSH_NOTIFICATION_SEND = "sendPush";

    private static final String PUSH_NOTIFICATION_INSTALLATION = "installation";
    public static final String PUSH_NOTIFICATION_SENDER = "senderId";
    public static final String PUSH_NOTIFICATION_SENDER_NAME = "senderName";
    private static final String PUSH_NOTIFICATION_RECEIVER = "receiverId";
    private static final String PUSH_NOTIFICATION_TITLE = "title";
    public static final String PUSH_NOTIFICATION_MESSAGE = "alert";
    private static final String PUSH_NOTIFICATION_CHAT = "chat";
    public static final String PUSH_NOTIFICATION_SENDER_AVATAR = "avatar";
    public static final String PUSH_NOTIFICATION_TYPE = "type";

    public static final String PUSH_TYPE_MESSAGES = "MESSAGES";
    public static final String PUSH_TYPE_MATCHES = "MATCHES";
    public static final String PUSH_TYPE_LIKED_YOU = "LIKED_YOU";
    public static final String PUSH_TYPE_VISITOR = "VISITOR";
    public static final String PUSH_TYPE_FAVORITED_YOU = "FAVORITED_YOU";
    public static final String PUSH_TYPE_DATOO_LIVE = "DATOO_LIVE";
    public static final String PUSH_TYPE_CRUSH = "CRUSH";

    public static void SendPush(User fromUser, User toUser, String type, String message){

        if(toUser.getInstallation() == null){ return;}

        Map<String, Object> params = new HashMap<>();

        params.put(PUSH_NOTIFICATION_INSTALLATION, toUser.getInstallation().getObjectId());
        params.put(PUSH_NOTIFICATION_RECEIVER, toUser.getObjectId());
        params.put(PUSH_NOTIFICATION_SENDER, fromUser.getObjectId());
        params.put(PUSH_NOTIFICATION_SENDER_NAME, fromUser.getColFullName());
        params.put(PUSH_NOTIFICATION_SENDER_AVATAR, fromUser.getProfilePhotos().get(fromUser.getAvatarPhotoPosition()).getUrl());

        if (type.equals(PUSH_TYPE_MESSAGES) && toUser.getPushNotificationsMassagesEnabled()){

            params.put(PUSH_NOTIFICATION_CHAT, message);
            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_message_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, String.format("%s %s %s", Application.getInstance().getBaseContext().getString(R.string.you_got), fromUser.getColFullName(), Application.getInstance().getBaseContext().getString(R.string.click_to_see)));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_MESSAGES);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        } else if (type.equals(PUSH_TYPE_MATCHES) && toUser.getPushNotificationsMatchesEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_match_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, String.format("%s %s %s", Application.getInstance().getBaseContext().getString(R.string.you_and), fromUser.getColFullName(), Application.getInstance().getBaseContext().getString(R.string.match_popup_view_message_two)));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_MATCHES);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        } else if (type.equals(PUSH_TYPE_LIKED_YOU) && toUser.getPushNotificationsLikedYouEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_like_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, Application.getInstance().getBaseContext().getString(R.string.notification_new_like));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_LIKED_YOU);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        } else if (type.equals(PUSH_TYPE_VISITOR) && toUser.getPushNotificationsProfileVisitorEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_visitor_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, Application.getInstance().getBaseContext().getString(R.string.notification_new_visitor));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_VISITOR);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        }  else if (type.equals(PUSH_TYPE_FAVORITED_YOU) && toUser.getPushNotificationsFavoritedYouEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_favorite_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, Application.getInstance().getBaseContext().getString(R.string.notification_new_favorite));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_FAVORITED_YOU);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        } else if (type.equals(PUSH_TYPE_DATOO_LIVE) && toUser.getPushNotificationsLiveEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_live_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, Application.getInstance().getBaseContext().getString(R.string.notification_new_live));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_DATOO_LIVE);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);

        } else if (type.equals(PUSH_TYPE_CRUSH) && toUser.getPushNotificationsLiveEnabled()){

            params.put(PUSH_NOTIFICATION_TITLE, Application.getInstance().getBaseContext().getString(R.string.notification_new_show_love_title));
            params.put(PUSH_NOTIFICATION_MESSAGE, Application.getInstance().getBaseContext().getString(R.string.notification_show_love));
            params.put(PUSH_NOTIFICATION_TYPE, PUSH_TYPE_CRUSH);

            ParseCloud.callFunctionInBackground(PUSH_NOTIFICATION_SEND, params);
        }
    }
}

