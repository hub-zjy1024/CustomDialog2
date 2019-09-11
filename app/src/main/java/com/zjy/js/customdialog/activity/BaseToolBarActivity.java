package com.zjy.js.customdialog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.zjy.js.customdialog.R;

public abstract class BaseToolBarActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tool_bar);
        ViewGroup root = (ViewGroup) findViewById(R.id.activity_base_tool_bar);
        root.addView(setLayoutView());
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
    }

    public abstract View setLayoutView();

    public Toolbar getToolBar() {
        return toolbar;
    }

}
