package pibes.yallegue.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import pibes.yallegue.R;
import pibes.yallegue.home.HomeActivity;
import pibes.yallegue.preference.AppPreferences;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    @Bind(R.id.image_background)
    ImageView imageBackground;

    @Bind(R.id.login_button)
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupBackground();
        if (isLogin()) {
            launchHomeActivity();
        } else {
            callbackRegistration();
        }
    }


    private void callbackRegistration() {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, this);
    }


    private boolean isLogin() {
        return AppPreferences.getInstance(this).isLongin();
    }

    private void setupBackground() {
        Picasso.with(this)
                .load(R.drawable.background_login)
                .transform(new EffectBlur(this, 10))
                .noFade()
                .into(imageBackground);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        AppPreferences.getInstance(this).saveLogin(true);
        launchHomeActivity();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public void launchHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
