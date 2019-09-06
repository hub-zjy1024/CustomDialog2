package printer.activity;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by 张建宇 on 2017/9/15.
 */

public class CameraFocusCallback implements Camera.AutoFocusCallback{
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.e("zjy", "CameraFocusCallback->handleMessage(): ==");
                if (mCamera == null) {
                    return;
                }
                mCamera.autoFocus(CameraFocusCallback.this);
            }
        }
    };
    private Camera mCamera;
    private int dur=2000;

    public CameraFocusCallback(Camera mCamera) {
        this.mCamera = mCamera;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        handler.sendEmptyMessageDelayed(0, dur);
    }
}
