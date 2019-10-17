package com.example.multithreadingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_dx,btn_yb,btn_hld,btn_ald,btn_other;
    private ProgressBar progress_bar;
    private TextView tvmsg;
    private MyHandler myHandler = new MyHandler(this);
    private CalculateThread calculateThread;

    private static final int START_NUM = 1;
    private static final int ADDING_NUM = 2;
    private static final int ENDING_NUM = 3;
    private static final int CANCEL_NUM = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress_bar=findViewById(R.id.progress_bar);
        tvmsg=findViewById(R.id.tvmsg);
        btn_dx=findViewById(R.id.btn_dx);
        btn_yb=findViewById(R.id.btn_yb);
        btn_hld=findViewById(R.id.btn_hld);
        btn_ald=findViewById(R.id.btn_ald);
        btn_other=findViewById(R.id.btn_other);
        btn_dx.setOnClickListener(this);
        btn_yb.setOnClickListener(this);
        btn_hld.setOnClickListener(this);
        btn_ald.setOnClickListener(this);
        btn_other.setOnClickListener(this);

    }



    class CalculateThread extends Thread{

        @Override
        public void run(){
            int result =0;//存放结果的变量
            boolean isCancel = false;
            //刚开始发送一个空消息
            myHandler.sendEmptyMessage(START_NUM);
            for(int i = 0;i<=100;i++){
                try {
                    Thread.sleep(100);
                    result+=i;
                }catch (InterruptedException e){
                    e.printStackTrace();
                    isCancel=true;
                    break;
                }
                if (i%5==0){
                    Message msg = Message.obtain();
                    msg.what=ADDING_NUM;
                    msg.arg1=i;
                    myHandler.sendMessage(msg);

                }
            }if(!isCancel){
                Message msg=Message.obtain();
                msg.what = ENDING_NUM;
                msg.arg1=result;
                myHandler.sendMessage(msg);
        }
    }


}



    //自定义Handler静态类
    static class MyHandler extends Handler{
        //定义弱引用对象
        private WeakReference<Activity>ref;
        //在构造方法中创建此对象
        public MyHandler(Activity activity){
            this.ref = new WeakReference<>(activity);
        }
        //重写handler方法
        @Override
        public  void handleMessage(Message msg){
            super.handleMessage(msg);
            //获取弱引用指向的Activity对象
            MainActivity activity = (MainActivity) ref.get();
            if(activity == null){
                return;
            }
            //根据Message的what属性值处理消息
            switch (msg.what){
                case START_NUM:
                activity.progress_bar.setVisibility(View.VISIBLE);
                break;
                case ADDING_NUM:
                    activity.progress_bar.setProgress(msg.arg1);
                    activity.tvmsg.setText("计算已完成"+msg.arg1+"%");
                    break;
                case ENDING_NUM:
                    activity.progress_bar.setVisibility(View.GONE);
                    activity.tvmsg.setText("计算已完成，结果为："+msg.arg1);
                    activity.myHandler.removeCallbacks(activity.calculateThread);
                    break;
                case CANCEL_NUM:
                    activity.progress_bar.setProgress(0);
                    activity.progress_bar.setVisibility(View.GONE);
                    activity.tvmsg.setText("计算已取消");
                    break;
            }
        }


    }public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dx:
                calculateThread = new CalculateThread();
                calculateThread.start();
                break;
            case R.id.btn_yb:

                break;
            case R.id.btn_hld:
                calculateThread = new CalculateThread();
                calculateThread.start();
                break;
            case R.id.btn_ald:

                break;
            case R.id.btn_other:

                break;
        }


    }
}
