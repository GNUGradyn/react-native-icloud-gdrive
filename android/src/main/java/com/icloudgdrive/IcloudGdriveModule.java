package com.icloudgdrive;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import java.util.Collections;
import java.util.List;

@ReactModule(name = IcloudGdriveModule.NAME)
public class IcloudGdriveModule extends ReactContextBaseJavaModule {
  public static final String NAME = "IcloudGdrive";
  private GoogleSignInClient mGoogleSignInClient;

  public IcloudGdriveModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void SetupGoogleDrive(String clientId, int modeValue) throws IllegalArgumentException {

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


    mGoogleSignInClient = GoogleSignIn.getClient(getCurrentActivity(), gso);
  }
}
