package cool.zzy.sems.application;

import android.graphics.Bitmap;
import android.view.Surface;
import cool.zzy.sems.application.model.Rect;

public class OpencvJni {
    static {
        System.loadLibrary("native-lib");
    }

    public native String recognitionBarcode(Bitmap bitmap,
                                            int rectX, int rectY, int rectWidth, int rectHeight);

    public native Rect haveBarcode(byte[] data, int width, int height);

    /**
     * 发送Surface到native，用于把数据后的图像数据直接显示到Surface上
     *
     * @param surface
     */
    public native void setSurface(Surface surface);
}
