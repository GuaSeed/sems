package cool.zzy.sems.application.fragment;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
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
    private AppCompatImageView scanImageView;
    private AppCompatButton settingButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews(View rootView) {
        nicknameTextView = rootView.findViewById(R.id.fragment_main_username);
        inputEditText = rootView.findViewById(R.id.fragment_main_input);
        scanImageView = rootView.findViewById(R.id.fragment_main_scan);
        settingButton = rootView.findViewById(R.id.fragment_main_setting);
    }

    @Override
    protected void initData() {
        if (user != null) {
            nicknameTextView.setText(user.getNickname());
        } else {
            UserUtils.staticLogin(getActivity());
        }
        scanImageView.setOnClickListener(this);
        settingButton.setOnClickListener(this);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_setting:
                enterSettingFragment();
                break;
            case R.id.fragment_main_scan:
                enterBarcodeFragment();
                break;
            default:
        }
    }
}
