package xyz.zzyitj.iface.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.LoginActivity;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 15:58
 * @since 1.0
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private View rootView;

    private LinearLayout backLienarLayout;
    private AppCompatEditText emailEditText;
    private AppCompatEditText userNameEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatButton registerButton;
    private AppCompatTextView loginTextView;
    private AppCompatCheckBox clauseCheckBox;

    private final Activity activity;

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

    public RegisterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearViewsData();
    }

    private void clearViewsData() {
        Objects.requireNonNull(emailEditText.getText()).clear();
        Objects.requireNonNull(userNameEditText.getText()).clear();
        Objects.requireNonNull(passwordEditText.getText()).clear();
    }

    private void initViews(View rootView) {
        backLienarLayout = rootView.findViewById(R.id.fragment_register_back);
        emailEditText = rootView.findViewById(R.id.fragment_register_email);
        userNameEditText = rootView.findViewById(R.id.fragment_register_user_name);
        passwordEditText = rootView.findViewById(R.id.fragment_register_password);
        registerButton = rootView.findViewById(R.id.fragment_register_button);
        loginTextView = rootView.findViewById(R.id.fragment_register_login);
        clauseCheckBox = rootView.findViewById(R.id.fragment_register_clause);
        backLienarLayout.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LoginActivity loginActivity = getLoginActivity();
        switch (v.getId()) {
            case R.id.fragment_register_back:
            case R.id.fragment_register_login:
                loginActivity.setCurrentFragment(loginActivity.loginFragment);
                break;
            case R.id.fragment_register_button:
                if (!isRegistering) {
                    // 注册
                }
                break;
            default:
        }
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }
}

