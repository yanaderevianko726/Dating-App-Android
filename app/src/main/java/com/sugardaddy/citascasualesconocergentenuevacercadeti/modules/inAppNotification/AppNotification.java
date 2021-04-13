package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.inAppNotification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;


public class AppNotification {

    /*
     * Duration for AppNotification to show
     */
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;


    /*
     * Gravity for AppNotification to display
     * on the screen
     */
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_CENTER = 2;
    public static final int GRAVITY_BOTTOM = 3;


    //Build AppNotification for normal view
    public static void buildToast(Context context, String text, int duration, int gravity, boolean isError){

        if (context == null) return;

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setMargin(0f,0f);

        toast.setView(normalView((Activity) context, toast, text, isError));
        switch (gravity) {
            case GRAVITY_TOP:
                toast.setGravity(Gravity.TOP|Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            case GRAVITY_CENTER:
                toast.setGravity(Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            case GRAVITY_BOTTOM:
                toast.setGravity(Gravity.BOTTOM|Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
                break;
            default:
                toast.setGravity(Gravity.TOP|Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
                break;
        }

    }

    //Create normal view layout for toast
    private static View normalView(Activity activity, Toast toast, String text, boolean isError){

        //inflate view
        @SuppressLint("InflateParams") View custom_view = activity.getLayoutInflater().inflate(R.layout.notification_in_app, null);

        ImageView notiImage = custom_view.findViewById(R.id.notification_image);
        TextView notiText = custom_view.findViewById(R.id.notification_text);

        if (isError){
            notiImage.setBackground(activity.getResources().getDrawable(R.drawable.bg_notification_image_error));
            notiImage.setImageResource(R.drawable.ic_close);
        } else {
            notiImage.setBackground(activity.getResources().getDrawable(R.drawable.bg_notification_image_success));
            notiImage.setImageResource(R.drawable.ic_check);
        }

        notiText.setText(text);

        toast.setView(custom_view);
        toast.show();

        return custom_view;
    }

}
