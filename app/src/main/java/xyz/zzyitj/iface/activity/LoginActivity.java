package xyz.zzyitj.iface.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.fragment.LoginFragment;
import xyz.zzyitj.iface.fragment.RegisterFragment;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        initViews();
        //initToken();
    }

    private void init() {
//        ApiUserDto apiUserDto = IFaceApplication.instance.getUser();
//        if (apiUserDto != null) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
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
}
