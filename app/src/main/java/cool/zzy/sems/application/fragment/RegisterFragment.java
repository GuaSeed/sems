package cool.zzy.sems.application.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.LoginActivity;
import cool.zzy.sems.application.constant.Const;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.RegexUtils;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

import java.util.Objects;

/**
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 15:58
 * @since 1.0
 */
public class RegisterFragment extends BaseFragment {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private LinearLayout backLinearLayout;
    private AppCompatEditText emailEditText;
    private AppCompatEditText userNameEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatEditText replyPasswordEditText;
    private AppCompatButton registerButton;
    private AppCompatTextView loginTextView;
    private AppCompatCheckBox clauseCheckBox;

    private Bitmap tempBitmap;
    /**
     * 性别
     * true为男
     * false为女
     */
    private boolean gender;
    /**
     * 是否正在注册
     */
    private boolean isRegistering;

    private ProgressDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initViews(View rootView) {
        progressDialog = new ProgressDialog(Objects.requireNonNull(getLoginActivity()), getString(R.string.registering));
        backLinearLayout = rootView.findViewById(R.id.fragment_register_back);
        emailEditText = rootView.findViewById(R.id.fragment_register_email);
        userNameEditText = rootView.findViewById(R.id.fragment_register_user_name);
        passwordEditText = rootView.findViewById(R.id.fragment_register_password);
        replyPasswordEditText = rootView.findViewById(R.id.fragment_register_reply_password);
        registerButton = rootView.findViewById(R.id.fragment_register_button);
        loginTextView = rootView.findViewById(R.id.fragment_register_login);
        clauseCheckBox = rootView.findViewById(R.id.fragment_register_clause);
    }


    @Override
    protected void initData() {
        backLinearLayout.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
    }

    @Override
    protected void viewOnClick(View v) {
        LoginActivity loginActivity = getLoginActivity();
        switch (v.getId()) {
            case R.id.fragment_register_back:
            case R.id.fragment_register_login:
                loginActivity.setCurrentFragment(loginActivity.loginFragment);
                break;
            case R.id.fragment_register_button:
                if (checkRegisterArgs() && !isRegistering) {
                    // 注册
                    register();
                }
                break;
            default:
        }
    }

    private void register() {
        isRegistering = true;
        progressDialog.show();
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            User user = new User();
            user.setNickname(Objects.requireNonNull(userNameEditText.getText()).toString());
            user.setUkEmail(Objects.requireNonNull(emailEditText.getText()).toString());
            user.setPasswordHash(Objects.requireNonNull(passwordEditText.getText()).toString());
            user.setGender(gender);
            user.setIp(SemsApplication.instance.getIp());
            try {
                user = userService.register(user);
                LoginFragment.loginSuccess(getLoginActivity(), user);
            } catch (Exception e) {
                Toast.makeText(getLoginActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                registerCancel();
            }
        } else {
            registerCancel();
            DialogUtils.showConnectErrorDialog(this.getLoginActivity());
        }
    }

    private void registerCancel() {
        progressDialog.dismiss();
        isRegistering = false;
    }

    private boolean checkRegisterArgs() {
        if (!clauseCheckBox.isChecked()) {
            Toast.makeText(getLoginActivity(), R.string.clause_error, Toast.LENGTH_LONG).show();
            return false;
        }
        if (userNameEditText.getText() == null || "".equals(userNameEditText.getText().toString())) {
            userNameEditText.setError(getString(R.string.user_name_cannot_empty));
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
        if (replyPasswordEditText.getText() == null || "".equals(replyPasswordEditText.getText().toString())) {
            replyPasswordEditText.setError(getString(R.string.password_cannot_empty));
            return false;
        } else if (replyPasswordEditText.getText().toString().length() < Const.PASSWORD_MIN_LEN) {
            replyPasswordEditText.setError(getString(R.string.password_length_error));
            return false;
        }
        if (!passwordEditText.getText().toString().equals(replyPasswordEditText.getText().toString())) {
            replyPasswordEditText.setError(getString(R.string.reply_password_error));
            return false;
        }
        return true;
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        clearViewsData();
    }

    private void clearViewsData() {
        try {
            Objects.requireNonNull(userNameEditText.getText()).clear();
            Objects.requireNonNull(emailEditText.getText()).clear();
            Objects.requireNonNull(passwordEditText.getText()).clear();
            Objects.requireNonNull(replyPasswordEditText.getText()).clear();
            clauseCheckBox.setChecked(false);
        } catch (Exception ignored) {
        }
    }
}

