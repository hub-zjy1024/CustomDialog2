package com.zjy.js.customdialog.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjy.js.customdialog.image.OpenCvImageUtils;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class OpenCvImgActivity extends AppCompatActivity {

    private final int reqCode = 400;
    private BaseLoaderCallback mLoader;
    private ImageView imageView;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.zjy.js.customdialog.R.layout.activity_open_cv_img);
        final Button btnGetImg = findViewById(com.zjy.js.customdialog.R.id.opencv_btn_getimg);
        Button btnModify = findViewById(com.zjy.js.customdialog.R.id.opencv_btn_modify);
        imageView = findViewById(com.zjy.js.customdialog.R.id.opencv_iv);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        Bitmap bitmap = drawableToBitmap(imageView.getDrawable());
                        //                        int width = bitmap.getWidth();
                        //                        int height = bitmap.getHeight();
                        //                        int[] pixels = new int[width * height];
                        //                        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                        //                        int[] newPixels = ImageUtils.getModifyOrientation(pixels, width, height);
                        //                        final Bitmap newImg = Bitmap.createBitmap(newPixels, width, height, bitmap
                        //                        .getConfig());
                        OpenCvImageUtils utils = new OpenCvImageUtils();
                        try {
                            final Bitmap newImg = utils.testWarpPerspective(bitmap);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(newImg);
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert("矫正异常");
                                }
                            });
                        }

                    }
                }.start();
            }
        });
        btnGetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromIntent();
            }
        });
        mLoader = new BaseLoaderCallback(this) {

            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        };
    }

    public void showAlert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public Bitmap getModfiyBitmap(Bitmap src) {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == reqCode) {

                if (data == null) {
                    return;
                }
                Uri uri2 = data.getData();
                Log.e("uri2", uri2.toString());
                ContentResolver cr2 = this.getContentResolver();
                try {
                    InputStream input = cr2.openInputStream(uri2);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(input, null, options);
                    if (options.outWidth > 1024 || options.outHeight > 1024) {
                        options.inSampleSize = Math.max(options.outWidth / 1024, options.outHeight / 1024);
                    }
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(cr2.openInputStream(uri2), null, options);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }

            }
        }
    }

    public void getImageFromIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, reqCode);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //通过OpenCV引擎服务加载并初始化OpenCV类库，所谓OpenCV引擎服务即是 OpenCV Manager
//                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoader);
        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoader);
        }
    }

}
