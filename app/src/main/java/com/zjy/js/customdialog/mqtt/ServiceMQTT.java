package com.zjy.js.customdialog.mqtt;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zjy.js.customdialog.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.testability.ApolloClient;

import java.io.File;

public class ServiceMQTT extends Service {
    public static boolean isOk = false;
    public ServiceMQTT() {
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread() {
            @Override
            public void run() {
                final Context mContext = getApplicationContext();
                final NotificationManager systemService = (NotificationManager)
                        mContext.getSystemService(NOTIFICATION_SERVICE);
                ApolloClient client = new ApolloClient();
                try {
                    client.start("1234");
                } catch (org.eclipse.paho.client.mqttv3.MqttException e) {
                    e.printStackTrace();
                }
                client.setCallBack(new MqttCallback() {

                    @Override
                    public void messageArrived(String topic, MqttMessage message)
                            throws Exception {
                        final NotificationCompat.Builder builder = new
                                NotificationCompat.Builder(mContext);
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher);
                        builder.setContentTitle("收到消息")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentText("messag" + message.toString())
                                .setProgress(100, 0, false)
                                .setLargeIcon(largeIcon).setAutoCancel(true);
                        Uri defRingstons = RingtoneManager
                                .getActualDefaultRingtoneUri(ServiceMQTT.this,
                                        RingtoneManager.TYPE_NOTIFICATION);
                        File f = new File("/system/media/audio/ringtones/");
                        Uri validRingtoneUri = RingtoneManager.getValidRingtoneUri
                                (ServiceMQTT.this);
//                        RingtoneManager manager = new RingtoneManager(ServiceMQTT.this);
//                        Cursor cursor = manager.getCursor();
//                        int count = cursor.getColumnCount();
//                        StringBuilder sbc = new StringBuilder();
//
//                        for(int i=0;i<count;i++) {
//                            sbc.append(cursor.getColumnName(i) + "\t");
//                        }
//                        Log.e("zjy", "ServiceMQTT->messageArrived(): colum==" + sbc
//                                .toString());
//                        StringBuilder buid = new StringBuilder();
//                        while (cursor.moveToNext()) {
//                            buid.append(cursor.getString(0)+"\t"+cursor.getString(1)+"\t"+cursor.getString(2)+"\n");
//                        }
//                        Log.e("zjy", "ServiceMQTT->messageArrived(): totoal==" + buid
//                                .toString());
//                        Log.e("zjy", "ServiceMQTT->messageArrived(): validRingtone==" +
//                                validRingtoneUri.toString());
//                        //                        for(int i=0;f)
//                        String columnName = cursor.getColumnName(2);
//                        defRingstons = Uri.parse(columnName +"/"+ cursor.getString(0));
                        Cursor result = getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null,
                                null, null, null);

                        StringBuilder sbui = new StringBuilder();
                        int count1 = result.getCount();
                        for(int i=0;i<count1;i++) {
                            sbui.append(result.getColumnName(i) + "\t");
                        }
                        while ((result.moveToNext())) {
                            int m = 0;
                            while (m < count1) {
                                sbui.append(result.getString(m) + "\t");
                                m++;
                            }
                        }
                        Log.e("zjy", "ServiceMQTT->messageArrived(): sbui==" + sbui
                                .toString());
                        builder.setSound(defRingstons);
                        systemService.notify((int) (Math.random()*1000), builder.build());
                        Log.e("zjy", "ServiceMQTT->messageArrived(): message==" + topic
                                + "---" + message.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // TODO Auto-generated method stub
                        System.out.println("deliveryComplete---------" + token
                                .isComplete());
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        // //连接丢失后，一般在这里面进行重连
                        cause.printStackTrace();
                        System.out.println("connectionLost----------");
                        Log.e("zjy", "ServiceMQTT->connectionLost(): ==");
                    }
                });
                try {

                    client.subscribe("101", 2);
                    isOk = true;
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);
    }
}
