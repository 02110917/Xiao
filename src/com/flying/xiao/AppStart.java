package com.flying.xiao;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Ӧ�ó��������ࣺ��ʾ��ӭ���沢��ת��������
 */
public class AppStart extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		final Handler handler=new Handler(){

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				redirectTo();
			}};
        Timer timer=new Timer() ;
        timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(0x01);
			}
		}, 2000);
        
		//����չʾ������
//		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
//		aa.setDuration(2000);
//		view.startAnimation(aa);
//		aa.setAnimationListener(new AnimationListener()
//		{
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				redirectTo();
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {}
//			@Override
//			public void onAnimationStart(Animation animation) {}
//			
//		});
		
    }
    
    /**
     * ��ת��...
     */
    private void redirectTo(){        
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        finish();
    }
}