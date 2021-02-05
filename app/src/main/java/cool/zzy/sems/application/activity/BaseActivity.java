package cool.zzy.sems.application.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.util.IPUtils;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 12:43
 * @since 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        init();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SemsApplication.instance.getIp() == null || "".equals(SemsApplication.instance.getIp())) {
            IPUtils.getMyIp();
        }
    }

    protected abstract void initViews();

    protected abstract void init();

    protected abstract int getContentView();

    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }
        currentFragment = fragment;
        fragmentTransaction.add(getFragmentViewId(), currentFragment).commit();
    }

    protected abstract int getFragmentViewId();
}
