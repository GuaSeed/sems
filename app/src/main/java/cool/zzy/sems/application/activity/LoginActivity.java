package cool.zzy.sems.application.activity;

import cool.zzy.sems.application.R;
import cool.zzy.sems.application.fragment.LoginFragment;
import cool.zzy.sems.application.fragment.RegisterFragment;
import cool.zzy.sems.application.util.UserUtils;

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
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData() {
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        setCurrentFragment(loginFragment);
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.login_content;
    }


    @Override
    protected void onResume() {
        super.onResume();
        UserUtils.staticLogin(this);
    }
}
