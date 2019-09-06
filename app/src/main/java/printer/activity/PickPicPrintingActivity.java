package printer.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.zjy.js.customdialog.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import printer.adapter.ImageGvAdapter;
import printer.entity.PrinterItem;

public class PickPicPrintingActivity extends AppCompatActivity {
    private List<PrinterItem> selcetItems;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private List<PrinterItem> dataList = new ArrayList<>();
    private ImageGvAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_pic_printing);
        GridView gv = (GridView) findViewById(R.id.activity_pick_pic_printing_gv);
        adapter = new ImageGvAdapter(dataList, this, R.layout.pickpic_printing_gv_items);
        gv.setAdapter(adapter);
        Button btnCommit = (Button) findViewById(R.id
                .activity_pick_pic_printing_btnCommit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PrinterItem> arrayList = adapter.getmSelectedImage();
                Log.e("zjy", "PickPicPrintingActivity->onClick(): arrayList.size==" +
                        arrayList.size());
                if (arrayList.size() == 0) {
                    Toast.makeText(PickPicPrintingActivity.this, "请选择要打印的图片", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                String[] paths = new String[arrayList.size()];
                String[] flags = new String[arrayList.size()];
                for(int i=0;i<arrayList.size();i++) {
                    PrinterItem item = arrayList.get(i);
                    paths[i] = item.getFile().getAbsolutePath();
                    flags[i] = item.getFlag();
                }
                Intent intent = getIntent();
                intent.putExtra("imgPaths", paths);
                intent.putExtra("flags", flags);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PickPicPrintingActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images
                                .Media.DATE_ADDED + " desc");

                Log.e("zjy", "PickPicActivity.java->run():searched pic counts==" +
                        mCursor.getCount());
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore
                                .Images.Media.DATA));
                        PrinterItem item = new PrinterItem();
                        item.setFile(new File(path));
                        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                            item.setFlag("jpg");
                        } else if (path.endsWith(".png")) {
                            item.setFlag("png");
                        }
                        dataList.add(item);
                    }
                    if (mCursor.getCount() > 0) {
                        // 通知Handler扫描图片完成
                        mHandler.sendEmptyMessage(0);
                    }
                    mCursor.close();
                }
            }
        }).start();
    }
}
