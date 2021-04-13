package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.calls;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.EngineConfig;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.MyEngineEventHandler;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.WorkerThread;


import io.agora.rtc.RtcEngine;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.AGEventHandler.EVENT_TYPE_ON_RELOGIN_NEEDED;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().addOnGlobalLayoutListener(this);
                }
                initUIandEvent();
            }
        });
    }

    protected abstract void initUIandEvent();

    protected abstract void deInitUIandEvent();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        deInitUIandEvent();
        super.onDestroy();
    }


    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.v("AGORA","checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }

        if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
            ((Application) getApplication()).initWorkerThread();
        }

        return true;
    }

    protected RtcEngine rtcEngine() {
        return ((Application) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((Application) getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((Application) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((Application) getApplication()).getWorkerThread().eventHandler();
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show());
    }

    final void needReLogin(int type, Object... data) {
        if (EVENT_TYPE_ON_RELOGIN_NEEDED == type) {
            final boolean banned = (boolean) data[0];

            runOnUiThread(() -> {
                showLongToast("Logout, " + (banned ? "try again later" : "banned by server"));

                Intent intent = new Intent();
                intent.putExtra("result", "finish");
                setResult(RESULT_OK, intent);

                finish();
            });
        }
    }
}
