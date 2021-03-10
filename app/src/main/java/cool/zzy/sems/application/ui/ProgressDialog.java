package cool.zzy.sems.application.ui;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import cool.zzy.sems.application.R;

/**
 * xyz.zzyitj.iface.ui
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 15:41
 * @since 1.0
 */
public class ProgressDialog extends Dialog {
    private AppCompatTextView titleTextView;

    public ProgressDialog(@NonNull Context context, String title) {
        super(context);
        setContentView(R.layout.dialog_progress);
        initViews();
        initData(title);
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    public void setTitle(String title) {
        initData(title);
    }

    private void initData(String title) {
        titleTextView.setText(title);
    }

    private void initViews() {
        titleTextView = findViewById(R.id.dialog_progress_title);
    }
}
