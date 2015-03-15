package com.jacob.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private HandlerThread mHandlerThread = new HandlerThread("Test Handler");
    private MyHandler handler = new MyHandler();
    private TextView mTextView  ;

    private int count = 0;

    private Handler myHandler ;

    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textview);
        handler.sendEmptyMessageDelayed(10,1000);

        mHandlerThread.start();
        myHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    //通过这个demo可以证明，通过HandlerThread开启一个新的线程，
                    //而且这个线程非UI线程，即使在这个线程中sleep，也不会影响到
                    //UI线程，所以比较合适进行异步消息的处理
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTextView.setText("" + (count++));
            sendEmptyMessageDelayed(10,1000);
            if (count == 5){
//                handler2.sendEmptyMessage(11);
                myHandler.sendEmptyMessage(10);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null){
            mHandlerThread.quit();
        }
        handler.removeMessages(10);
        handler2.removeMessages(11);
    }
}
