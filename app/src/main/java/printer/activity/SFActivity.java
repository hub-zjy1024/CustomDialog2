package printer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zjy.js.customdialog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import printer.entity.Yundan;

public class SFActivity extends AppCompatActivity {

    private List<Yundan> yundanData;
    private EditText edPid;
    private EditText edPartNo;
    private ArrayAdapter<Yundan> adapter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    Toast.makeText(SFActivity.this, "查询不到数据，请更换查询条件", Toast
                            .LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(SFActivity.this, "连接服务器失败", Toast
                            .LENGTH_SHORT).show();
                    break;
                case 3:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sf);
        yundanData = new ArrayList<>();
        edPid = (EditText) findViewById(R.id.yundan_ed_pid);
        edPartNo = (EditText) findViewById(R.id.yundan_ed_partno);
        ListView lv = (ListView) findViewById(R.id.yundan_lv);
        adapter = new ArrayAdapter<Yundan>(this, android.R.layout.simple_list_item_1,
                yundanData);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(SFActivity.this, SetYundanActivity.class);
                Yundan item = (Yundan) parent.getItemAtPosition(position);
                String goodInfos = "";
                for (int i = 0; i < yundanData.size(); i++) {
                    Yundan good = yundanData.get(i);
                    if (good.getPid().equals(item.getPid())) {
                        goodInfos += good.getPartNo() + "&" + good.getCounts() + ",";
                    }
                }
                goodInfos = goodInfos.substring(0, goodInfos.lastIndexOf(","));
                intent.putExtra("goodInfos", goodInfos);
                intent.putExtra("client", item.getCustomer());
                intent.putExtra("pid", item.getPid());
                intent.putExtra("times", item.getPrint());
                startActivity(intent);
            }
        });

    }
    private static void testCouts(int counts,int timeDuration) {
        int num=0;
        while(num<counts){
            try {
                Thread.sleep(timeDuration);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            new Thread(){
                public void run() {
                    String sUrl="http://192.168.10.65:8080/PrinterServer/WordTestServlet";
                    try {
                        URL url=new URL(sUrl);
                        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                        conn.getResponseCode();
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            }.start();
            num++;
        }
    }
    public void myOnclick(View view) {
        yundanData.clear();
        testCouts(1000, 100);
        new Thread() {
            @Override
            public void run() {
                try {
                    String parno = edPartNo.getText().toString();
                    String pid = edPid.getText().toString();
                    //                    pid = "1154510";
                    getYundanList(pid, parno);
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(2);
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getYundanList(String pid, String xh) throws IOException,
            XmlPullParserException, JSONException {
        SoapObject requestList = new SoapObject("http://tempuri.org/", "GetYunDanList");
        requestList.addProperty("pid", pid); requestList.addProperty("xh", xh);
        /*设置版本号，ver11，和ver12比较常见*/
        SoapSerializationEnvelope envelopeList = new SoapSerializationEnvelope
                (SoapEnvelope.VER11);
        envelopeList.setOutputSoapObject(requestList);
        envelopeList.dotNet = true;
        HttpTransportSE transList = new HttpTransportSE("http://172.16.6" +
                ".160:8006/SF_Server.svc?wsdl", 15 * 1000);
        String actionLIst = "http://tempuri.org/ISF_Server/GetYunDanList";
        transList.call(actionLIst, envelopeList);
        SoapPrimitive soapList = (SoapPrimitive) envelopeList.getResponse();
        Log.e("zjy", "SFActivity->run(): yundanInfoList==" + soapList
                .toString());
        JSONObject object = new JSONObject(soapList.toString());
        ArrayList<String> list = new ArrayList<>();
        JSONArray jArray = object.getJSONArray("表");
        for (int i = 0; i < jArray.length(); i++) {

            JSONObject obj = jArray.getJSONObject(i);
            String sPid = obj.getString("PID");
            String createDate = obj.getString("制单日期");
            String state = obj.getString("状态");
            String deptID = obj.getString("部门ID");
            String saleMan = obj.getString("业务员");
            String storageName = obj.getString("仓库");
            String client = obj.getString("客户");
            String backOrderID = obj.getString("回执单号");
            String print = obj.getString("打印次数");
            String shouHuiDan = obj.getString("收回单");
            String partNo = obj.getString("型号");
            String count = obj.getString("数量");
            String pihao = obj.getString("批号");
            Yundan yundan = new Yundan();
            yundan.setPid(sPid);
            yundan.setCreateDate(createDate);
            yundan.setState(state);
            yundan.setDeptID(deptID);
            yundan.setSaleMan(saleMan);
            yundan.setStorageName(storageName);
            yundan.setCustomer(client);
            yundan.setRecieveBackNo(backOrderID);
            yundan.setPrint(print);
            yundan.setShouHuiDan(shouHuiDan);
            yundan.setPartNo(partNo);
            yundan.setCounts(count);
            yundan.setPihao(pihao);
            yundanData.add(yundan);
        }
        mHandler.sendEmptyMessage(0);
    }

    public String getData() {
        return null;
    }
}
