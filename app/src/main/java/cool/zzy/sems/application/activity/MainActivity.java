package cool.zzy.sems.application.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.roughike.bottombar.BottomBar;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.fragment.ClockFragment;

public class MainActivity extends BaseActivity {
    private ClockFragment clockFragment;

    private BottomBar bottomBar;

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
                SemsApplication.instance.removeUser();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViews() {
        bottomBar = findViewById(R.id.main_bottom_bar);
        clockFragment = new ClockFragment(this);
        bottomBar.setItems(R.xml.bottombar_tabs_user);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_clock:
                    setCurrentFragment(clockFragment);
                    break;
                default:
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.main_content;
    }

    public void changeBottomTab(int pos) {
        bottomBar.selectTabAtPosition(pos, true);
    }
}