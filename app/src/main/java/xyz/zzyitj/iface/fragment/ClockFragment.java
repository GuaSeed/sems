package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import xyz.zzyitj.iface.OpencvJni;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.CameraHelper;

import java.io.File;


/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 14:44
 * @since 1.0
 */
public class ClockFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = ClockFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private View rootView;

    private final MainActivity mainActivity;

    private OpencvJni openCvJni;
    private CameraHelper cameraHelper;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private SurfaceView surfaceView;

    private ProgressDialog progressDialog;

    public ClockFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clock, container, false);
        initOpenCV(rootView);
        initViews(rootView);
        return rootView;
    }

    private void initOpenCV(View rootView) {
//        openCvJni = new OpencvJni();
        surfaceView = rootView.findViewById(R.id.fragment_clock_surface_view);
        surfaceView.getHolder().addCallback(this);
        cameraHelper = new CameraHelper(cameraId);
        cameraHelper.setPreviewCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        startOpenCV();
    }

    private void startOpenCV() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            String path = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml")
                    .getAbsolutePath();
            cameraHelper.startPreview();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        stopCamera();
    }

    private void initViews(View rootView) {
        progressDialog = new ProgressDialog(mainActivity, getString(R.string.clocking));
    }

    private void stopCamera() {
        cameraHelper.stopPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
//        boolean haveFace = openCvJni.postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
//        if (haveFace && !isClock) {
//            isClock = true;
//            byte[] faceData = CameraUtils.runInPreviewFrame(data, camera);
//            clock(faceData);
//        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        openCvJni.setSurface(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }
}
