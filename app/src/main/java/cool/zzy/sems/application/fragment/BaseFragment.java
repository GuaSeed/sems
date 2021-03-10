package cool.zzy.sems.application.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.MainActivity;
import cool.zzy.sems.context.model.User;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 10:55
 * @since 1.0
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        user = SemsApplication.instance.getUser();
        initViews(rootView);
        initData();
        return rootView;
    }

    protected abstract int getLayout();

    protected abstract void initViews(View rootView);

    protected abstract void initData();

    @Override
    public void onClick(View v) {
        viewOnClick(v);
    }

    protected abstract void viewOnClick(View v);

    public void enterSettingFragment() {
        getMainActivity(getActivity())
                .setCurrentFragment(getMainActivity(getActivity()).settingFragment);
    }

    public void enterBarcodeFragment() {
        getMainActivity(getActivity())
                .setCurrentFragment(getMainActivity(getActivity()).barcodeFragment);
    }

    public void enterMainFragment() {
        getMainActivity(getActivity())
                .setCurrentFragment(getMainActivity(getActivity()).mainFragment);
    }

    public MainActivity getMainActivity(Activity a) {
        return (MainActivity) a;
    }
}
