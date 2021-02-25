package cool.zzy.sems.application.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.MainActivity;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.UserUtils;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/2/25 17:49
 * @since 1.0
 */
public class SettingFragment extends BaseFragment {
    private LinearLayout settingBack;
    private AppCompatButton logoutButton;
    private AppCompatButton saveButton;
    private AppCompatEditText emailEdittext;
    private AppCompatEditText usernameEdittext;

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews(View rootView) {
        settingBack = rootView.findViewById(R.id.fragment_setting_back);
        logoutButton = rootView.findViewById(R.id.fragment_setting_logout);
        saveButton = rootView.findViewById(R.id.fragment_setting_save);
        emailEdittext = rootView.findViewById(R.id.fragment_setting_email);
        usernameEdittext = rootView.findViewById(R.id.fragment_setting_username);
    }

    @Override
    protected void initData() {
        settingBack.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        emailEdittext.setText(user.getUkEmail());
        usernameEdittext.setText(user.getNickname());
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_back:
                backMainFragment();
                break;
            case R.id.fragment_setting_logout:
                UserUtils.logout(getActivity());
                break;
            case R.id.fragment_setting_save:
                saveUser();
                break;
            default:
        }
    }

    private void saveUser() {
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            User newUser = this.user;
            newUser.setNickname(usernameEdittext.getText().toString());
            newUser = userService.updateUser(newUser);
            if (newUser == null) {
                Toast.makeText(getActivity(), R.string.modify_user_fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_LONG).show();
                SemsApplication.instance.putUser(newUser);
                backMainFragment();
            }
        } else {
            DialogUtils.showConnectErrorDialog(getActivity());
        }
    }

    private void backMainFragment() {
        getMainActivity(getActivity())
                .setCurrentFragment(getMainActivity(getActivity()).mainFragment);
    }

    private MainActivity getMainActivity(Activity a) {
        return (MainActivity) a;
    }
}
