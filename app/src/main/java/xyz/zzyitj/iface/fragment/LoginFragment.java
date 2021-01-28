package xyz.zzyitj.iface.fragment;

import android.annotation.SuppressLint;
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
import cool.zzy.rpc.client.RpcClient;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.LoginActivity;
import xyz.zzyitj.iface.constant.Const;
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

    private AppCompatEditText passwordEditText;
    private AppCompatButton loginButton;
    private AppCompatButton registerButton;
    private AppCompatCheckBox clauseCheckBox;
    private AppCompatTextView clauseText;

    private ProgressDialog progressDialog;

    private boolean isLogin = false;

    private void login() {
//        progressDialog.show();
        @SuppressLint("DefaultLocale") RpcClient rpcClient = new RpcClient(String.format("%s:%d", Const.RPC_IP, Const.RPC_PORT));
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
                Toast.makeText(getLoginActivity(), "还没写！！！", Toast.LENGTH_LONG).show();
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
