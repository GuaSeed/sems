package xyz.zzyitj.iface.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.BaiduAuthService;
import xyz.zzyitj.iface.fragment.LoginFragment;
import xyz.zzyitj.iface.fragment.RegisterFragment;
import xyz.zzyitj.iface.model.ApiUserDto;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.activity
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:03
 * @since 1.0
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private Fragment currentFragment;
    public LoginFragment loginFragment;
    public RegisterFragment registerFragment;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        initViews();
        //initToken();
    }

    private void init() {
        ApiUserDto apiUserDto = IFaceApplication.instance.getUser();
        if (apiUserDto != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initViews() {
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment(this);
        setCurrentFragment(loginFragment);
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }
        currentFragment = fragment;
        fragmentTransaction.add(R.id.login_content, currentFragment).commit();
    }

    /**
     * 初始化token
     */
    private void initToken() {
        if (IFaceApplication.instance.getApiToken() == null) {
            BaiduAuthService.getToken()
                    .subscribe(apiTokenDto -> {
                        if (apiTokenDto != null && apiTokenDto.getAccessToken() != null) {
                            IFaceApplication.instance.setApiToken(apiTokenDto);
                            Log.d(TAG, apiTokenDto.toString());
                        } else {
                            Toast.makeText(this, "token cannot be null.", Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "getToken error.", throwable);
                        Toast.makeText(this, "token error.", Toast.LENGTH_LONG).show();
                    });
        } else {
            Log.d(TAG, IFaceApplication.instance.getApiToken());
        }
    }
}
