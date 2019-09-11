package com.zjy.js.customdialog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zjy.js.customdialog.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;
import org.xutils.x;

public class AddPrintersActivity extends AppCompatActivity {

    @HttpRequest(builder = DefaultParamsBuilder.class, host = TestXhttp.DEF_HOST, path =
            "/s?wd=微型计算机&tn=98010089_dg&ch=3&ie=utf-8&rsv_cq=-1用2进制表示&rsv_dl=0_right_recommends_merge_28335&euri=5821395")
    public static class TestXhttp extends RequestParams {
        public static final String DEF_HOST = "http://www.baidu.com";
        private String result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_printers);
        try {
            String mUrl = TestXhttp.DEF_HOST + "/s?wd=微型计算机&tn=98010089_dg&ch=3&ie=utf-8&rsv_cq=-1用2进制表示&rsv_dl=0_right_recommends_merge_28335&euri=5821395";
            RequestParams param = new RequestParams(mUrl);
//            param = new TestXhttp();
            x.http().get(param, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("zjy", "AddPrintersActivity->onSuccess(): Resp==" + result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("zjy", "AddPrintersActivity->onSuccess(): Resp==" + ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    Log.e("zjy", "AddPrintersActivity->onFinished(): Resp==finish" );
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
