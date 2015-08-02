package cn.yg.mylove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.yg.util.BitmapCache;
import cn.yg.util.MediaPlay;
import cn.yg.util.Util;
import cn.yg.view.FourthSurfaceView;

public class FourthActivity extends Activity {
	
	  LinearLayout l4;
	  FrameLayout f4;
	  private Context mContext;
	  private int screen_h;
	  private int screen_w;
	  private MediaPlay me;
	  private  FourthSurfaceView sv4;
	 
	 
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.activity_fourth);
		    Display localDisplay = getWindowManager().getDefaultDisplay();
		    this.screen_w = localDisplay.getWidth();
		    this.screen_h = localDisplay.getHeight();
		    this.mContext = getApplicationContext();       
		    Util.init().setContext(mContext);
		    init_music();
		    findAllViews();
		
		   l4.setBackgroundResource(R.drawable.l4);
		   this.sv4 = new FourthSurfaceView(this, this.screen_w, this.screen_h,handler);
		   f4.removeAllViews();
		   f4.addView(this.sv4, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT));
	}
	 
	 
	 @Override
	protected void onRestart() {
		super.onRestart();
	}
	private void findAllViews()
	{
	   f4 = ((FrameLayout)findViewById(R.id.f4));
	   l4 = ((LinearLayout)findViewById(R.id.l4));
	}

		//初始化音乐播放器
		private void init_music(){	 
		    me = MediaPlay.init();
		    me.InitMediaPlay(mContext,R.raw.m4);   
		    me.play();
		}
		
		
		//跳转到FifthActivity并清空缓存
		public void goFifth(){
			Intent t = new Intent();
			t.setClass(FourthActivity.this, FifthActivity.class);
			startActivity(t);
			overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);
			FourthActivity.this.finish();
			BitmapCache.getInstance().clearCache(); 
		  }
		
		
		final int gofive = 0;
		Handler handler = new Handler()
		  {
		    public void handleMessage(Message paramMessage)
		    {
		      switch (paramMessage.what)
		      {
		      default:
		      case gofive:
		    	  goFifth();
		    	  break;
		      }
		    }
		  };

}
