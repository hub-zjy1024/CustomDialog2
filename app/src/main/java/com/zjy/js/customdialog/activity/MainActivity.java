package com.zjy.js.customdialog.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zjy.js.customdialog.R;
import com.zjy.js.customdialog.printer.MyPrintService;
import com.zjy.js.customdialog.printer.NetPrinter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    pullToRefreshListView.onRefreshComplete();

                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                    break;
            }
        }
    };
    ArrayAdapter<String> adapter;
    SwipeRefreshLayout swipeRefresh;
    PullToRefreshListView pullToRefreshListView;
    ListView lv;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private NetPrinter printer;
    private int wlname = 18;
    private int wlnum = 6;
    private int wlunit = 7;
    private int wlprice = 9;
    boolean isclick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_lv);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        lv = (ListView) findViewById(R.id.main_lv);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_lv);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_lv);
        final ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            data.add("今天我吃苹果" + i);
        }
        Intent intent = new Intent(MainActivity.this, MyPrintService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String ipAddress = "192.168.13.249";
                    int port = 515;
                    printer = new NetPrinter(ipAddress, port);
                    printer.Open();
                    if (printer.isOpened) {
                        Log.e("zjy", "MainActivity->run(): start print==");
                        printer.PrintText("这是一个测试的", 1, 0, 1);
                        //                    printer.CutPage(0);
                        printer.Close();
                        isclick = false;
                        Log.e("zjy", "MainActivity->run(): print success==");
                    } else {
                        Log.e("zjy", "MainActivity->run(): print fail==");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                pullToRefreshListView.setRefreshing();
                adapter.notifyDataSetChanged();
                for (int i = 0; i < 10; i++) {
                    data.add("你在搞笑么" + Math.random());
                }
                mHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });

            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout
                    .simple_list_item_1, data);
            lv.setAdapter(adapter);
            ListView actualListView = pullToRefreshListView.getRefreshableView();
            actualListView.setAdapter(adapter);
            pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            pullToRefreshListView.setOnItemClickListener(new AdapterView
                    .OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Log.e("zjy", "MainActivity->onItemClick(): par==" + parent
                            .getItemAtPosition(position));
                }
            });
            pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase
                    .OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    data.clear();
                    pullToRefreshListView.setRefreshing();
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < 10; i++) {
                        data.add("你在搞笑么" + Math.random());
                    }
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    Log.e("zjy", "MainActivity->onPullUpToRefresh(): ==" + System
                            .currentTimeMillis());
                    pullToRefreshListView.setRefreshing();
                    for (int i = 0; i < 10; i++) {
                        data.add("你在搞笑么" + Math.random());
                    }
                    mHandler.sendEmptyMessageDelayed(1, 2000);
                }
            });
            pullToRefreshListView.getLoadingLayoutProxy().setRefreshingLabel("刷刷刷...");
            pullToRefreshListView.getLoadingLayoutProxy().setPullLabel("继续下拉开始刷新...");
            pullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel("刷刷刷...");

            //        createLoadingDialog(this, "正在加载中").show();
        }

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.mdialog_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        ProgressDialog loadingDialog;
        loadingDialog = new ProgressDialog(context);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.loading_back);
        loadingDialog.setIndeterminateDrawable(drawable);
        //        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
        //                LinearLayout.LayoutParams.MATCH_PARENT,
        //                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }
}
