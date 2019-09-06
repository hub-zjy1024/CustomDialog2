package com.zjy.js.customdialog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjy.js.customdialog.R;

public class FontSizeTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size_test);
        final LinearLayout llDisplay = findViewById(R.id.activity_fontsize_displayll);
        final EditText edInput = findViewById(R.id.activity_fontsize_input);
        final EditText edMin = findViewById(R.id.activity_fontsize_min);
        final EditText edMax = findViewById(R.id.activity_fontsize_max);
        Button btnCreate = findViewById(R.id.activity_fontsize_btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDisplay.removeAllViews();
                String str = edInput.getText().toString();


                int min = Integer.parseInt(edMin.getText().toString());
                int max = Integer.parseInt(edMax.getText().toString());
                for (int size = min; size < max + 1; size++) {
                    //                    TextView tv = new TextView(FontSizeTestActivity.this);
                    //                    TextView tv = new MeasureFontHeightTv(FontSizeTestActivity.this);
                    //                    tv.setText(str);
                    //                    tv.setTextSize(size);
                    View itemView = LayoutInflater.from(FontSizeTestActivity.this).inflate(R.layout.item_fonttest, null);
                    TextView tv1 = itemView.findViewById(R.id.item_fonttest_str);
                    tv1.setTextSize(size);
                    tv1.setText(str);
                    TextView tv2 = itemView.findViewById(R.id.item_fonttest_fontsize);
                    tv2.setText("字号:" + size);
                    llDisplay.addView(itemView);
                }
            }
        });
    }
}
