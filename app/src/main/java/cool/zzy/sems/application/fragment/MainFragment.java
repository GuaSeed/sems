package cool.zzy.sems.application.fragment;

import android.view.View;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.util.UserUtils;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 10:54
 * @since 1.0
 */
public class MainFragment extends BaseFragment {
    private AppCompatTextView nicknameTextView;
    private AppCompatEditText inputEditText;
    private AppCompatImageView scanTextView;
    private AppCompatTextView settingTextView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews(View rootView) {
        nicknameTextView = rootView.findViewById(R.id.fragment_main_username);
        inputEditText = rootView.findViewById(R.id.fragment_main_input);
        scanTextView = rootView.findViewById(R.id.fragment_main_scan);
        settingTextView = rootView.findViewById(R.id.fragment_main_setting);
    }

    @Override
    protected void initData() {
        if (user != null) {
            nicknameTextView.setText(user.getNickname());
        } else {
            UserUtils.staticLogin(getActivity());
        }
        scanTextView.setOnClickListener(this);
        settingTextView.setOnClickListener(this);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_setting:
                UserUtils.logout(getActivity());
                break;
            default:
        }
    }
}
