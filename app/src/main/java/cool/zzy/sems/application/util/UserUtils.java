package cool.zzy.sems.application.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.LoginActivity;
import cool.zzy.sems.application.activity.MainActivity;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 11:16
 * @since 1.0
 */
public class UserUtils {

    public static void staticLogin(Activity activity) {
        User user = SemsApplication.instance.getUser();
        if (user != null) {
            UserService userService = SemsApplication.instance.getUserService();
            if (userService != null) {
                if (userService.signIn(user.getUkEmail(), user.getPasswordHash()) != null) {
                    if (activity.getClass() != MainActivity.class) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }
                } else {
                    Toast.makeText(activity, R.string.login_fail, Toast.LENGTH_LONG).show();
                }
            } else {
                DialogUtils.showConnectErrorDialog(activity);
            }
        }
    }

    public static void logout(Activity activity) {
        SemsApplication.instance.removeUser();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }
}
