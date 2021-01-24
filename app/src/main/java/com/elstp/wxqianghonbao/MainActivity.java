package com.elstp.wxqianghonbao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.elstp.wxqianghonbao.Tools.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPower();

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(v -> openAccessibilitySettings());




    }

    // 跳转到安卓的辅助功能界面
    private void openAccessibilitySettings (){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            //跳转系统自带界面 辅助功能界面
            Toast.makeText(this,"请打开辅助功能!", Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }


    public void requestPower(){

        for (int i = 0; i< Utils.permissionsREAD.length; i++ ){
            //判断是否已经赋予权限
            if (ContextCompat.checkSelfPermission(this, Utils.permissionsREAD[i]) != PackageManager.PERMISSION_GRANTED) {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Utils.permissionsREAD[i])) {
                    //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
                } else {
                    //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                    ActivityCompat.requestPermissions(this, new String[]{Utils.permissionsREAD[i]}, 1);
                }
            }
        }
    }

}
