package cool.zzy.sems.application.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.fragment.LoginFragment;
import cool.zzy.sems.application.fragment.RegisterFragment;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.activity
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:03
 * @since 1.0
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    public LoginFragment loginFragment;
    public RegisterFragment registerFragment;

    @Override
    protected void init() {
        Log.d(TAG, "init: ");
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        staticLogin(this);
    }

    private static void staticLogin(Activity activity) {
        User user = SemsApplication.instance.getUser();
        if (user != null) {
            UserService userService = SemsApplication.instance.getUserService();
            if (userService != null) {
                if (userService.signIn(user.getUkEmail(), user.getPasswordHash()) != null) {
                    if (activity.getClass() != MainActivity.class) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }
                } else {
                    Toast.makeText(activity, R.string.login_fail, Toast.LENGTH_LONG).show();
                }
            } else {
                DialogUtils.showConnectErrorDialog(activity);
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.login_content;
    }

    @Override
    protected void initViews() {
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        setCurrentFragment(loginFragment);
    }
}
