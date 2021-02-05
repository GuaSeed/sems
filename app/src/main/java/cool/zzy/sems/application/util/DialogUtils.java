package cool.zzy.sems.application.util;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 11:32
 * @since 1.0
 */
public class DialogUtils {
    public static void showConnectErrorDialog(Activity activity) {
        showConnectErrorDialog(activity, (dialog1, which) -> {
            SemsApplication.instance.initRPC();
        });
    }

    public static void showConnectErrorDialog(Activity activity,
                                              DialogInterface.OnClickListener reconnectListener) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.error)
                .setMessage(R.string.connect_rpc_error)
                .setPositiveButton(R.string.reconnect, reconnectListener)
                .setNegativeButton(R.string.exit, (dialog12, which) -> activity.finish())
                .create();
        dialog.show();
    }
}
