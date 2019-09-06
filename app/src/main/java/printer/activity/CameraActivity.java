package printer.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.zjy.js.customdialog.R;

import java.io.IOException;


public class CameraActivity extends AppCompatActivity {
    public Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);

        Button btnStopPreview = (Button) findViewById(R.id.stop_yulan);
        Button btn_commit = (Button) findViewById(R.id.yulan);
        btnStopPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.startPreview();
            }
        });
        //获取surfaceholder
        SurfaceHolder mHolder = surfaceView.getHolder();
        mHolder.setKeepScreenOn(true);
        //添加SurfaceHolder回调
        if (mHolder != null) {
            mHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    int counts = Camera.getNumberOfCameras();
                    if (counts == 0) {
                        return;
                    }
                    camera = Camera.open(0); // 打开摄像头
                    if (camera == null) {
                        return;
                    }
                    //设置旋转角度
                    camera.setDisplayOrientation(90);
                    //设置parameter注意要检查相机是否支持，通过parameters.getSupportXXX()
                    try {
                        // 设置用于显示拍照影像的SurfaceHolder对象
                        camera.setPreviewDisplay(holder);
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFocusMode(Camera.Parameters
                                .FOCUS_MODE_CONTINUOUS_PICTURE);
                        camera.setParameters(parameters);
                        //初始化操作在开始预览之前完成
                        camera.startPreview();
                        CameraFocusCallback mFoucs = new CameraFocusCallback(camera);
                        camera.autoFocus(mFoucs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                           int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if (camera != null) {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                }
            });
        }
    }
}
