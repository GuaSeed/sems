package cool.zzy.sems.application.activity;

import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.fragment.MainFragment;
import cool.zzy.sems.application.fragment.SettingFragment;
import cool.zzy.sems.application.util.UserUtils;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    public MainFragment mainFragment;
    public SettingFragment settingFragment;

//    private BottomBar bottomBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void initViews() {
//        bottomBar = findViewById(R.id.main_bottom_bar);
    }

    @Override
    protected void initData() {
        mainFragment = new MainFragment();
        settingFragment = new SettingFragment();
        setCurrentFragment(mainFragment);
//        bottomBar.setItems(R.xml.bottombar_tabs_user);
//        for (int i = 0; i < bottomBar.getTabCount(); i++) {
//            BottomBarTab tab = bottomBar.getTabAtPosition(i);
//            tab.setGravity(Gravity.CENTER);
//        }
//        bottomBar.setOnTabSelectListener(tabId -> {
//            switch (tabId) {
//                case R.id.tab_clock:
//                    setCurrentFragment(mainFragment);
//                    break;
//                default:
//            }
//        });
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.main_content;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login_out:
                UserUtils.logout(this);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeBottomTab(int pos) {
//        bottomBar.selectTabAtPosition(pos, true);
    }
}