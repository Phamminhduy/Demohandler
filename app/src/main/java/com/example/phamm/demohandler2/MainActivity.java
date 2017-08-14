package com.example.phamm.demohandler2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MESSAGE_COUNT_DOWN = 1001;
    private static final int MESSAGE_DONE = 10002;
    TextView txtHienThi;
    Button btnCount;
    Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        UpdateView();
    }
    private void UpdateView(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mhandler = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what){
                                    case MESSAGE_COUNT_DOWN :
                                        txtHienThi.setText(String.valueOf(msg.arg1));
                                        break;
                                    case MESSAGE_DONE:
                                        Toast.makeText(MainActivity.this, "DONE!", Toast.LENGTH_SHORT).show();
                                    default: break;
                                }
                            }
                        };
                    }
                });
            }
        });
               thread1.start();
    }
    private void initView() {
        txtHienThi = (TextView) findViewById(R.id.textView);
        btnCount   = (Button) findViewById(R.id.button);
        btnCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                doCountDown();
                break;
            default: break;
        }

    }

    private void doCountDown() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 10;
                do{
                    time--;
                    Message msg = mhandler.obtainMessage();
                    msg.what=MESSAGE_COUNT_DOWN;
                    msg.arg1=time;
                    mhandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (time>0);
                mhandler.sendEmptyMessage(MESSAGE_DONE);
            }
        });
        thread.start();
    }
}
