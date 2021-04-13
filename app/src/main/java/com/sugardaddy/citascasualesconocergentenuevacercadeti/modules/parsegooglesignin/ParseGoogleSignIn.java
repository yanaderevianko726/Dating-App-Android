package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.parsegooglesignin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import bolts.Continuation;
import bolts.Task;

public class ParseGoogleSignIn {

    private static final Object lock = new Object();
    private static String AUTH_TYPE = "google";
    private static int REQUEST_CODE_GOOGLE_SIGN_IN = 62987;
    @SuppressLint("StaticFieldLeak")
    private static GoogleSignInClient mGoogleSignInClient;
    private static String mClientId = null;
    private static boolean isInitialized = false;
    private static LogInCallback mCurrentCallback  = null;

    /**
     * Initializes [ParseGoogleLogin] with the [clientId]. If you have Firebase configured, you can
     * easily get the [clientId] value via context.getString(R.string.default_web_client_id)
     * @param context to get the server clientId
     */

    public static void initialize(Application context) {

        mClientId = context.getString(R.string.default_web_client_id);;
        synchronized (lock) {
            isInitialized = true;
        }
    }

    /**
     * @param user A [com.parse.ParseUser] object.
     * @return `true` if the user is linked to a Facebook account.
     */

    public static boolean isLinked(ParseUser user) {
        return user.isLinked(AUTH_TYPE);
    }

    private static void checkInitialization() {
        synchronized (lock) {
            if (!isInitialized) {
                throw new IllegalStateException(
                        "You must call ParseGoogleLogin.initialize() before using ParseGoogleLogin");
            }
        }
    }

    /**
     * Unlink a user from a Facebook account. This will save the user's data.
     *
     * @param user     The user to unlink.
     * @param callback A callback that will be executed when unlinking is complete.
     * @return A task that will be resolved when linking is complete.
     */

    public static Task<Void> unlinkInBackground(ParseUser user, SaveCallback callback)  {
        return callbackOnMainThreadAsync(unlinkInBackground(user), callback, false);
    }

    /**
     * Unlink a user from a Google account. This will save the user's data.
     *
     * @param user The user to unlink.
     * @return A task that will be resolved when unlinking has completed.
     */

    public static Task<Void> unlinkInBackground(ParseUser user) {
        checkInitialization();
        return user.unlinkFromInBackground(AUTH_TYPE).continueWith(task -> {
            if (task.isCompleted()){

            } else if (task.isFaulted()){

            }
            return null;
        });
    }

    /**
     * Link an existing Parse user with a Google account using authorization credentials that have
     * already been obtained.
     *
     * @param user        The Parse user to link with.
     * @param account Authorization credentials of a Google user.
     * @return A task that will be resolved when linking is complete.
     */

    public static Task<Void> linkInBackground(ParseUser user, GoogleSignInAccount account) {
        return user.linkWithInBackground(AUTH_TYPE, getAuthData(account));
    }


    /**
     * Log in using a Google.
     *
     * @param activity The activity which passes along the result via [onActivityResult]
     * @param callback The [LogInCallback] which is invoked on log in success or error
     */

    public static void LoginInBackGround(Activity activity, LogInCallback callback){

        checkInitialization();

        mCurrentCallback = callback;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .requestIdToken(mClientId)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        activity.startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);

    }

    public static void signIn(Intent signInIntent){

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
    }

    private static void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            LoginInBackground(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE_LOGIN", "signInResult:failed code=" + e.getStatusCode());

            mCurrentCallback.done(null, new ParseException(e));
        }
    }

    public static Map<String, String> getAuthData(GoogleSignInAccount account){

        Map<String, String> authData = new HashMap<>();
        authData.put("id", Objects.requireNonNull(account.getId()));
        authData.put("id_token", Objects.requireNonNull(account.getIdToken()));
        authData.put("email", Objects.requireNonNull(account.getEmail()));
        authData.put("photo_url", Objects.requireNonNull(account.getPhotoUrl()).toString());

        return authData;
    }

    public static void LoginInBackground(GoogleSignInAccount account){

        if (account != null){

            Log.d("GOOGLE_LOGIN", "Google Account logged");

            ParseUser.logInWithInBackground(AUTH_TYPE, getAuthData(account)).continueWith(task -> {
                if (task.isCompleted()){
                    onSignInCallbackResult(task.getResult(), null);

                } else if (task.isFaulted()){
                    onSignInCallbackResult(null, task.getError());

                } else {

                    onSignInCallbackResult(null, null);
                }
                return null;
            });

        }

    }

    public static void onSignInCallbackResult(ParseUser parseUser, Exception exception){

        ParseException e = (ParseException) exception;
        mCurrentCallback.done(parseUser, e);
    }


    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     *
     * @param requestCode The request code that's received by the Activity or Fragment.
     * @param resultCode  The result code that's received by the Activity or Fragment.
     * @param data        The result data that's received by the Activity or Fragment.
     * @return true if the result could be handled.
     */
    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_CODE_GOOGLE_SIGN_IN) {
            return false;
        } else  {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            signIn(data);
        }
        return true;
    }

    public static void Logout(){

        if (mGoogleSignInClient != null){
            mGoogleSignInClient.signOut();
        }

        mGoogleSignInClient = null;
    }

    /**
     * Calls the callback after a task completes on the main thread, returning a Task that completes
     * with the same result as the input task after the callback has been run.
     */
    private static <T> Task<T> callbackOnMainThreadAsync(
            Task<T> task, LogInCallback callback, boolean reportCancellation) {
        return callbackOnMainThreadInternalAsync(task, callback, reportCancellation);
    }

    /**
     * Calls the callback after a task completes on the main thread, returning a Task that completes
     * with the same result as the input task after the callback has been run.
     */
    private static <T> Task<T> callbackOnMainThreadAsync(
            Task<T> task, SaveCallback callback, boolean reportCancellation) {
        return callbackOnMainThreadInternalAsync(task, callback, reportCancellation);
    }

    /**
     * Calls the callback after a task completes on the main thread, returning a Task that completes
     * with the same result as the input task after the callback has been run. If reportCancellation
     * is false, the callback will not be called if the task was cancelled.
     */
    private static <T> Task<T> callbackOnMainThreadInternalAsync(
            Task<T> task, final Object callback, final boolean reportCancellation) {
        if (callback == null) {
            return task;
        }
        final Task<T>.TaskCompletionSource tcs = Task.create();
        task.continueWith((Continuation<T, Void>) task1 -> {
            if (task1.isCancelled() && !reportCancellation) {
                tcs.setCancelled();
                return null;
            }
            Task.UI_THREAD_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Exception error = task1.getError();
                        if (error != null && !(error instanceof ParseException)) {
                            error = new ParseException(error);
                        }
                        if (callback instanceof SaveCallback) {
                            ((SaveCallback) callback).done((ParseException) error);
                        } else if (callback instanceof LogInCallback) {
                            ((LogInCallback) callback).done(
                                    (ParseUser) task1.getResult(), (ParseException) error);
                        }
                    } finally {
                        if (task1.isCancelled()) {
                            tcs.setCancelled();
                        } else if (task1.isFaulted()) {
                            tcs.setError(task1.getError());
                        } else {
                            tcs.setResult(task1.getResult());
                        }
                    }
                }
            });
            return null;
        });
        return tcs.getTask();
    }
}
