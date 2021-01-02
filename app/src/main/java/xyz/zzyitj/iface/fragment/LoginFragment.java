package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.OpencvJni;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.LoginActivity;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.api.ApiUserService;
import xyz.zzyitj.iface.api.BaiduApiConst;
import xyz.zzyitj.iface.api.BaiduFaceService;
import xyz.zzyitj.iface.model.*;
import xyz.zzyitj.iface.model.Rect;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.CameraHelper;
import xyz.zzyitj.iface.util.CameraUtils;
import xyz.zzyitj.iface.util.RegexUtils;
import xyz.zzyitj.iface.util.Utils;

import java.io.*;

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

    private ProgressDialog progressDialog;

    private boolean isLogin = false;

    private void login(byte[] data) {
        progressDialog.show();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.logging));
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }

    private void userLogin() {
        progressDialog.show();
        ApiUserLoginVo apiUserLoginVo = new ApiUserLoginVo();
        // password edittext
        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setError(getString(R.string.password_cannot_empty));
            progressDialog.dismiss();
            return;
        } else {
            apiUserLoginVo.setPassword(passwordEditText.getText().toString());
        }
        ApiUserService.login(apiUserLoginVo)
                .subscribe(this::userLoginSuccess, throwable -> {
                    Log.e(TAG, "userLogin: ", throwable);
                    Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }

    private void userLoginSuccess(ApiUserDto apiUserVo) {
        if (apiUserVo != null) {
            // 登录成功
            IFaceApplication.instance.putUser(apiUserVo);
            progressDialog.dismiss();
            isLogin = false;
            Toast.makeText(getActivity(), apiUserVo.getUsername() + getString(R.string.login_success),
                    Toast.LENGTH_LONG).show();
            // 跳转
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            progressDialog.dismiss();
            isLogin = false;
            Toast.makeText(getActivity(), getString(R.string.login_fail),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_login_button:
                Toast.makeText(getLoginActivity(), "还没写！！！", Toast.LENGTH_LONG).show();
                break;
            case R.id.fragment_login_register:
                LoginActivity loginActivity = getLoginActivity();
                loginActivity.setCurrentFragment(loginActivity.registerFragment);
                break;
            default:
        }
    }
}
