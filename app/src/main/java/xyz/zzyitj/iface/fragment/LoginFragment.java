package xyz.zzyitj.iface.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.SemsApplication;
import xyz.zzyitj.iface.activity.LoginActivity;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.constant.Const;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.DialogUtils;
import xyz.zzyitj.iface.util.RegexUtils;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:13
 * @since 1.0
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private View rootView;

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
        progressDialog.show();
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            User user = userService.signIn(Objects.requireNonNull(emailEditText.getText()).toString(),
                    Objects.requireNonNull(passwordEditText.getText()).toString());
            loginCancel();
            if (user == null) {
                loginFail(user);
            } else {
                loginSuccess(user);
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

    private void loginSuccess(User user) {
        SemsApplication.instance.putUser(user);
        Toast.makeText(getLoginActivity(), String.format("用户: %s 登录成功!", user.getUkEmail()),
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(this.getLoginActivity(), MainActivity.class));
        this.getLoginActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        progressDialog = new ProgressDialog(Objects.requireNonNull(getLoginActivity()), getString(R.string.logging));
        emailEditText = rootView.findViewById(R.id.fragment_login_account);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register);
        clauseCheckBox = rootView.findViewById(R.id.fragment_login_clause);
        clauseText = rootView.findViewById(R.id.fragment_login_clause_text);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_login_button:
                if (checkLoginArgs()) {
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
}
