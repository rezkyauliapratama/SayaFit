package android.cybereye_community.com.sayafit.controller.activity;

import android.content.Intent;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.databinding.ActivityLoginBinding;
import android.cybereye_community.com.sayafit.databinding.LayoutEmptyBinding;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.handler.api.UserApi;
import android.cybereye_community.com.sayafit.view.LayoutEmptyInflate;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import timber.log.Timber;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 007;
    LayoutEmptyInflate layoutEmpty;
    ActivityLoginBinding binding;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        layoutEmpty = new LayoutEmptyInflate(this,binding.containerBody);
        layoutEmpty.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.signInButton.setScopes(gso.getScopeArray());

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                signIn();
            }
        });

        redirect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("Connection Failed : "+connectionResult.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Timber.e("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            layoutEmpty.setVisibility(View.VISIBLE);

            GoogleSignInAccount acct = result.getSignInAccount();
            Timber.e("RESULT : "+new Gson().toJson(acct));
            final UserTbl userTbl = new UserTbl();
            userTbl.email = acct.getEmail();
            userTbl.city = "";
            userTbl.gender = "";
            userTbl.nama= acct.getDisplayName();
            userTbl.token= acct.getIdToken();

            ApiClient.getInstance().user().post(userTbl).
                    getAsObject(UserApi.Response.class, new ParsedRequestListener<UserApi.Response>() {
                @Override
                public void onResponse(UserApi.Response response) {
                    Timber.e("RESULT : "+new Gson().toJson(response));
                    layoutEmpty.setVisibility(View.GONE);
                    if (response.value == 0 || response.value == 1){
                        Facade.getInstance().getManageUserTbl().add(userTbl);
                    }
                    redirect();
                }

                @Override
                public void onError(ANError anError) {
                    layoutEmpty.setVisibility(View.GONE);
                    Snackbar.make(binding.containerBody,anError.getMessage(),Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Timber.e("Sign Out success");
                    }
                });
    }

    private void redirect(){
        UserTbl userTbl = Facade.getInstance().getManageUserTbl().get();

        if (userTbl != null){
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }

}
