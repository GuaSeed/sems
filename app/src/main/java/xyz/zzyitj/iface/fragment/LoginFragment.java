package xyz.zzyitj.iface.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import xyz.zzyitj.iface.ui.ProgressDialog;

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
            User user = userService.signIn(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
            loginCancel();
            if (user == null) {
                Toast.makeText(getLoginActivity(), "账户或密码错误！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getLoginActivity(), String.format("用户: %s 登录成功!", user.getUkEmail()),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            loginCancel();
            AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
                    .setTitle("Error")
                    .setMessage("Can not connect to remote server")
                    .setPositiveButton("Reconnect", (dialog1, which) -> {
                        SemsApplication.instance.initRPC();
                    })
                    .setNegativeButton("Exit", (dialog12, which) -> {
                        this.getActivity().finish();
                    })
                    .create();
            dialog.show();
        }
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
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.logging));
        emailEditText = rootView.findViewById(R.id.fragment_login_account);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register);
        clauseCheckBox = rootView.findViewById(R.id.fragment_login_clause);
        clauseText = rootView.findViewById(R.id.fragment_login_clause_text);
        clauseText.setOnClickListener(this);
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
                login();
                break;
            case R.id.fragment_login_register:
                LoginActivity loginActivity = getLoginActivity();
                loginActivity.setCurrentFragment(loginActivity.registerFragment);
                break;
            case R.id.fragment_login_clause_text:
                Toast.makeText(getLoginActivity(), "这里该弹出服务条款", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }
}
