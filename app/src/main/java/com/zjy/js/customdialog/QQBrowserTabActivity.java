package com.zjy.js.customdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zjy.js.customdialog.view.ScrollableTabView;

import java.util.ArrayList;
import java.util.List;

public class QQBrowserTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbrowser_tab);
        ScrollableTabView viewById = findViewById(R.id.mView);
        List<String> titles = new ArrayList<>();
        titles.add("平道1");
        titles.add("平道2");
        titles.add("平道3");
        titles.add("平道4");
        viewById.setTitles(titles);
    }
}
