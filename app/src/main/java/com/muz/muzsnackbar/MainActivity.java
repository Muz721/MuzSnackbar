package com.muz.muzsnackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muz.muzsnackbarlibrary.MuzSnackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.main_txt);
        MuzSnackbar snackbar = MuzSnackbar.make(textView,"幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀幕栀",MuzSnackbar.LENGTH_INDEFINITE)
                .setAction("你好！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "你好", Toast.LENGTH_SHORT).show();
                    }
                }).setActionTextColor(0xFF3F51B5)
                .setTextColor(0xFF3F51B8)
                .addCallback(new MuzSnackbar.Callback() {
                    @Override
                    public void onShown(MuzSnackbar sb) {
                        super.onShown(sb);
                        Log.e("MainActivity", "onShown");
                    }

                    @Override
                    public void onDismissed(MuzSnackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        switch (event) {
                            case MuzSnackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                            case MuzSnackbar.Callback.DISMISS_EVENT_MANUAL:
                            case MuzSnackbar.Callback.DISMISS_EVENT_SWIPE:
                            case MuzSnackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                break;
                            case MuzSnackbar.Callback.DISMISS_EVENT_ACTION:
                                Toast.makeText(MainActivity.this, "撤销了删除操作", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        Log.e("MainActivity", "onDismissed");
                    }
                })
                .setDuration(5000)
                .isTop(true)
                .setMaxLines(3)
                .setIcon(R.drawable.ic_launcher_background)
                .setTextSize(R.dimen.muz_snackbar_txt_size)
                .setTextTypeface("ffa.ttf");
                snackbar.show();

    }
}
