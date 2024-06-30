package com.icloudgdrive;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.List;

@ReactModule(name = IcloudGdriveModule.NAME)
public class IcloudGdriveModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  public static final String NAME = "IcloudGdrive";
  private static final int RC_SIGN_IN = 9001;
  private GoogleSignInClient mGoogleSignInClient;
  private Promise signInPromise;

  public IcloudGdriveModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(this);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void SignInWithGoogle(String clientId, int modeValue, Promise promise) throws IllegalArgumentException {

    signInPromise = promise;

    android.app.Activity activity = getCurrentActivity();

    Mode mode = Mode.fromValue(modeValue);
    GoogleSignInOptions gso;

    switch (mode) {
      case Appdata:
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestEmail()
          .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
          .requestServerAuthCode(clientId)
          .requestIdToken(clientId)
          .build();
        break;
      case Documents:
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestScopes(new Scope(Scopes.DRIVE_FULL))
          .requestServerAuthCode(clientId)
          .requestIdToken(clientId)
          .build();
        break;
      case Both:
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestScopes(new Scope(Scopes.DRIVE_FULL), new Scope(Scopes.DRIVE_FULL))
          .requestServerAuthCode(clientId)
          .requestIdToken(clientId)
          .build();
        break;
      default:
        throw new IllegalArgumentException("Auth mode not understood");
    }


    mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    activity.startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        String authCode = account.getServerAuthCode();

        WritableMap map = new WritableNativeMap();
        map.putString("authCode", authCode);
        signInPromise.resolve(map);
      } catch (ApiException e) {
        signInPromise.reject("signInResult:failed code=" + e.getStatusCode(), e);
      }
    }
  }

  @Override
  public void onNewIntent(Intent intent) {
    // Not needed here
  }
}
