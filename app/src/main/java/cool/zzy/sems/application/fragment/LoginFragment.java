package cool.zzy.sems.application.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.LoginActivity;
import cool.zzy.sems.application.activity.MainActivity;
import cool.zzy.sems.application.constant.Const;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.RegexUtils;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

import java.util.Objects;

/**
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:13
 * @since 1.0
 */
public class LoginFragment extends BaseFragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatButton loginButton;
    private AppCompatButton registerButton;
    private AppCompatCheckBox clauseCheckBox;
    private AppCompatTextView clauseText;

    private ProgressDialog progressDialog;

    private boolean isLogin = false;

    private void loginCancel() {
        progressDialog.dismiss();
        isLogin = false;
    }

    private void login() {
        isLogin = true;
        progressDialog.show();
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            User user = userService.signIn(Objects.requireNonNull(emailEditText.getText()).toString(),
                    Objects.requireNonNull(passwordEditText.getText()).toString());
            loginCancel();
            if (user == null) {
                loginFail(user);
            } else {
                loginSuccess(getLoginActivity(), user);
            }
        } else {
            loginCancel();
            DialogUtils.showConnectErrorDialog(this.getLoginActivity());
        }
    }

    private void loginFail(User user) {
        SemsApplication.instance.removeUser();
        Toast.makeText(getLoginActivity(), R.string.login_fail, Toast.LENGTH_LONG).show();
    }

    public static void loginSuccess(LoginActivity loginActivity, User user) {
        SemsApplication.instance.putUser(user);
        Toast.makeText(loginActivity, String.format("用户: %s 登录成功!", user.getUkEmail()),
                Toast.LENGTH_LONG).show();
        loginActivity.startActivity(new Intent(loginActivity, MainActivity.class));
        loginActivity.finish();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews(View rootView) {
        progressDialog = new ProgressDialog(Objects.requireNonNull(getLoginActivity()), getString(R.string.logging));
        emailEditText = rootView.findViewById(R.id.fragment_login_account);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register);
        clauseCheckBox = rootView.findViewById(R.id.fragment_login_clause);
        clauseText = rootView.findViewById(R.id.fragment_login_clause_text);
    }

    @Override
    protected void initData() {
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_login_button:
                if (checkLoginArgs() && !isLogin) {
                    login();
                }
                break;
            case R.id.fragment_login_register:
                LoginActivity loginActivity = getLoginActivity();
                loginActivity.setCurrentFragment(loginActivity.registerFragment);
                break;
            default:
        }
    }

    private boolean checkLoginArgs() {
        if (!clauseCheckBox.isChecked()) {
            Toast.makeText(getLoginActivity(), R.string.clause_error, Toast.LENGTH_LONG).show();
            return false;
        }
        if (emailEditText.getText() == null || "".equals(emailEditText.getText().toString())) {
            emailEditText.setError(getString(R.string.email_cannot_empty));
            return false;
        } else if (!RegexUtils.isEmail(emailEditText.getText())) {
            emailEditText.setError(getString(R.string.email_format_error));
            return false;
        }
        if (passwordEditText.getText() == null || "".equals(passwordEditText.getText().toString())) {
            passwordEditText.setError(getString(R.string.password_cannot_empty));
            return false;
        } else if (passwordEditText.getText().toString().length() < Const.PASSWORD_MIN_LEN) {
            passwordEditText.setError(getString(R.string.password_length_error));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearViewsData();
    }

    private void clearViewsData() {
        try {
            Objects.requireNonNull(emailEditText.getText()).clear();
            Objects.requireNonNull(passwordEditText.getText()).clear();
            clauseCheckBox.setChecked(false);
        } catch (Exception ignored) {
        }
    }
}
