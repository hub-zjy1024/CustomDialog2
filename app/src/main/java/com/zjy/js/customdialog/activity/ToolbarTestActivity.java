package com.zjy.js.customdialog.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zjy.js.customdialog.JniTest;
import com.zjy.js.customdialog.R;
import com.zjy.js.customdialog.opencvutils.ImageUtils;

import org.opencv.samples.imagemanipulations.ImageManipulationsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import printer.activity.PickPicPrintingActivity;
import printer.entity.PrinterItem;

public class ToolbarTestActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private EditText edIP;
    private Spinner spiPrintes;
    private ListView searchLv;
    private List<String> spiItems;
    private ArrayAdapter<String> pinterAdapter;
    private List<PrinterItem> lvItems;
    private ArrayAdapter<PrinterItem> lvAdapter;
    private SharedPreferences sp;
    private final Object lock = new Object();
    private Handler mHandler = new Handler();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_test);
        edIP = (EditText) findViewById(R.id.ed_IP);
        spiPrintes = (Spinner) findViewById(R.id.spi_printers);
        searchLv = (ListView) findViewById(R.id.print_lv);
        lvItems = new ArrayList<>();
        spiItems = new ArrayList<>();
        JniTest test = new JniTest();
        String a = test.getString("北京", "我是傻子");
        Log.e("zjy", "MainActivity->onCreate(): jniTest==" + a);
        String nativeString = ImageUtils.getNativeString();
        Log.e("zjy", getClass() + "->onCreate(): nativeString==" +nativeString);
        String nate = ImageUtils.getNativeString2();
        Log.e("zjy", getClass() + "->onCreate(): nativeString2==" +nate);
        try {
            ImageUtils.testVoid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            int width=200;
            int height = 200;
            int size = width * height;
            int pixs[] = new int[size];
            for(int i=0;i<size;i++){
                pixs[i] = (255 << 24) | (255 << 16) | (255 << 8) | (255 & 0xFF);
            }
            int[] modifyOrientation = ImageUtils.getModifyOrientation(pixs, width, height);
            if (modifyOrientation != null) {
                Log.e("zjy", getClass() + "->onCreate(): modifyOrientation==" + Arrays.toString(modifyOrientation));
            }
            int[] modifyOrientation2 = ImageUtils.getModifyOrientation2(pixs, 12, 12);
            if (modifyOrientation2 != null) {
                Log.e("zjy", getClass() + "->onCreate(): modifyOrientation2==" + Arrays.toString(modifyOrientation2));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences("printerServer", Context.MODE_PRIVATE);
        String serverAddress = sp.getString("ip", "");
        edIP.setText(serverAddress);
        pinterAdapter = new ArrayAdapter<String>(this, R.layout.spinner_simple_item, R
                .id.spinner_item_tv, spiItems);
        lvAdapter = new ArrayAdapter<>(this, R.layout.printing_lv_item, R
                .id.spinner_item_tv, lvItems);
        searchLv.setAdapter(lvAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    333);
        }
//        Intent service = new Intent(this, ServiceMQTT.class);
//        startService(service);
        spiPrintes.setAdapter(pinterAdapter);
        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                final PrinterItem clickItem = (PrinterItem) parent.getItemAtPosition
                        (position);
                String ip = edIP.getText()
                        .toString().trim();
                if (ip.equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入IP地址", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                final String strUrl = "http://" + ip + "/PrinterServer/PrintServlet?";
                Object selectItem = spiPrintes.getSelectedItem();
                String printer = "";
                if (selectItem != null) {
                    printer = selectItem.toString();
                }
                final String temPrinter = printer;
                new Thread() {
                    @Override
                    public void run() {
                        print(clickItem.getFile(), strUrl,
                                clickItem.getFlag()
                                , temPrinter);
                    }
                }.start();

            }
        });
        pd = ProgressDialog.show(this, "提示", "正在搜索。。", true, false);
        pd.cancel();
        final File file = Environment.getExternalStorageDirectory();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String string = uri.toString();
            File file;
            String a[] = new String[2];
            String schema = "";
            Log.e("zjy", "ToolbarTestActivity->onActivityResult(): uri==" + string);
            //判断文件是否在sd卡中
            if (string.contains(String.valueOf(Environment.getExternalStorageDirectory()
            ))) {
                schema = String.valueOf(Environment.getExternalStorageDirectory
                        ());
                //对Uri进行切割
                a = string.split(schema);
                Log.e("zjy", "ToolbarTestActivity->onActivityResult(): sd==" + schema);
                //获取到file
                file = new File(Environment.getExternalStorageDirectory(), a[1]);
            } else if (string.contains(String.valueOf(Environment.getDataDirectory()))
                    ) { //判断文件是否在手机内存中
                //对Uri进行切割
                schema = String.valueOf(Environment.getDataDirectory
                        ());
                //对Uri进行切割
                a = string.split(schema);
                //获取到file
                file = new File(Environment.getDataDirectory(), a[1]);
            } else {
                //出现其他没有考虑到的情况
            }
            //
        } else if (resultCode == Activity.RESULT_OK && requestCode == 300) {
            String[] paths = data.getStringArrayExtra("imgPaths");
            String[] flags = data.getStringArrayExtra("flags");
            String ip = edIP.getText()
                    .toString().trim();
            if (ip.equals("")) {
                Toast.makeText(getApplicationContext(), "请输入IP地址", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            String strUrl = "http://" + ip + "/PrinterServer/PrintServlet?";
            Object selectItem = spiPrintes.getSelectedItem();
            String printer = "";
            if (selectItem != null) {
                printer = selectItem.toString();
            }
            Log.e("zjy", "ToolbarTestActivity->onActivityResult(): startPrinterImg==");
            for (int i = 0; i < paths.length; i++) {
                print(new File(paths[i]), strUrl, flags[i], printer);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        if (requestCode == 333) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File root = Environment.getExternalStorageDirectory();
                File tencent = new File(root, "tencent");
                getFileList(tencent, "");
                //用户同意使用write
            } else {
                //用户不同意，自行处理即可
                Toast.makeText(this, "无权限、、、", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadandPrint(File file, String strUrl) {
        if (file.exists()) {
            Log.e("zjy", "ToolbarTestActivity->run(): file found==");
            try {
                URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15 * 1000);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "multipart/form-data");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                FileInputStream fis = new FileInputStream(file);
                OutputStream outputStream = conn.getOutputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader
                        (inputStream, "UTF-8"));
                Log.e("zjy", "ToolbarTestActivity->run(): ==write over:" + reader
                        .readLine());
                reader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ToolbarTestActivity.this, "网络错误", Toast
                                .LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        }
    }

    public void getFileList(File dir, String type) {
        LinkedList<File> dirs = new LinkedList<>();
        dirs.add(dir);
        File temp_file;
        while (!dirs.isEmpty()) {
            temp_file = dirs.removeFirst();
            File[] files = temp_file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        dirs.addLast(f);
                    } else {
                        boolean isNeed = checkFile(f, type);
                        if (isNeed) {
                            lvItems.add(new PrinterItem(f, type));
                        }
                    }
                }
            }
        }
    }

    public boolean checkFile(File f, String type) {
        //
        if (type.equals("word")) {
            if (f.getName().endsWith(".doc")) {
                return true;
            } else if (f.getName().endsWith(".docx")) {
                return true;
            }
        } else if (type.equals("excel")) {
            if (f.getName().endsWith(".xls")) {
                return true;
            } else if (f.getName().endsWith(".xlsx")) {
                return true;
            }
        } else if (type.equals("pdf")) {
            if (f.getName().endsWith(".pdf")) {
                return true;
            }
        }
        return false;
    }

    /**
     @param view
     */
    public void btnOnClick(final View view) {
        final File file = Environment.getExternalStorageDirectory();
        final File file2 = new File(file, "tencent/qqfile_recv/");
        final File file3 = new File(file, "tencent/MicroMsg/Download");
        Log.e("zjy", "ToolbarTestActivity->btnOnClick(): file==" + file2.getAbsolutePath());
        Intent fontTestIntent = new Intent();
        switch (view.getId()) {
            case R.id.btn_sizeTest:
                 fontTestIntent.setClass(this, FontSizeTestActivity.class);
                startActivity(fontTestIntent);
                break;
            case R.id.btn_open_addprinter:
                fontTestIntent.setClass(this, AddPrintersActivity.class);
                startActivity(fontTestIntent);
                break;
            case R.id.btn_open_sample:
                fontTestIntent.setClass(this, ImageManipulationsActivity.class);
                startActivity(fontTestIntent);
                break;
            case R.id.btn_opencv:
                Intent openCv = new Intent(this, OpenCvImgActivity.class);
                startActivity(openCv);
                break;
            case R.id.btn_open_Vp_title:
                Intent qqTest = new Intent(this, QQBrowserTabActivity.class);
                startActivity(qqTest);
                break;

            case R.id.btn1:
                if (!lvAdapter.isEmpty()) {
                    lvItems.clear();
                }
                pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getFileList(file2, "word");
                        getFileList(file3, "word");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "搜索完成", Toast
                                        .LENGTH_SHORT).show();
                                lvAdapter.notifyDataSetChanged();
                                pd.cancel();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.btn3:
                if (!lvAdapter.isEmpty()) {
                    lvItems.clear();
                }
                pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getFileList(file2, "excel");
                        getFileList(file3, "excel");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "搜索完成", Toast
                                        .LENGTH_SHORT).show();
                                lvAdapter.notifyDataSetChanged();
                                pd.cancel();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.btn8:
                if (!lvAdapter.isEmpty()) {
                    lvItems.clear();
                }
                pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getFileList(file2, "pdf");
                        getFileList(file3, "pdf");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "搜索完成", Toast
                                        .LENGTH_SHORT).show();
                                lvAdapter.notifyDataSetChanged();
                                pd.cancel();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.btn6:
                Intent intent = new Intent(ToolbarTestActivity.this,
                        PickPicPrintingActivity.class);
                startActivityForResult(intent, 300);
                break;
            case R.id.btn7:
                spiItems.clear();
                final String ip = edIP.getText()
                        .toString().trim();
                if (ip.equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入IP地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                sp.edit().putString("ip", ip).commit();
                new Thread() {
                    @Override
                    public void run() {
                        String urlPrinter = "http://" + ip +
                                "/PrinterServer/GetPrinterInfoServlet";
                        try {
                            URL url = new URL(urlPrinter);
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setConnectTimeout(15 * 1000);
                            InputStream in = conn.getInputStream();
                            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
                            BufferedReader bis = new BufferedReader(reader);
                            String s = "";
                            String result = "";
                            while ((s = bis.readLine()) != null) {
                                result += s;
                            }
                            Log.e("zjy", "ToolbarTestActivity->run():printer: reuslt=="
                                    + result);
                            if (!result.equals("")) {
                                String[] printers = result.split(",");
                                for (String p : printers) {
                                    spiItems.add(p);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pinterAdapter.notifyDataSetChanged();

                                    }
                                });
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                break;
            case R.id.btn9:
                //                Intent intent1 = new Intent(ToolbarTestActivity.this, SFActivity.class);
                //                Intent intent1 = new Intent(ToolbarTestActivity.this, CaptureActivity
                //                        .class);
                //                startActivity(intent1);
                Log.e("zjy", "ToolbarTestActivity->btnOnClick(): SD state==" +
                        Environment.getExternalStorageState());

                break;
        }
    }

    private boolean print(final File file2, String strUrl, String flag, String printer) {
        try {
            strUrl += "flag=" + flag;
            strUrl += "&filename=" + URLEncoder
                    .encode(file2.getName(), "UTF-8");
            //            strUrl += "&printer=" + printer;
            strUrl += "&printer=" + URLEncoder
                    .encode(printer, "UTF-8");
            final String finalStrUrl = strUrl;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    uploadandPrint(file2, finalStrUrl);
                }
            }.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

}
