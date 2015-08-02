package cn.yg.mylove;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.yg.util.MediaPlay;
import cn.yg.util.Util;
import cn.yg.view.FifthSurfaceView;

public class FifthActivity extends Activity  {
	  LinearLayout l5;
	  FrameLayout f5;
	  private Context mContext;
	  private int screen_h;
	  private int screen_w;
	  private MediaPlay me;
	 private  FifthSurfaceView sv5;
	 
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_fifth);
	    Display localDisplay = getWindowManager().getDefaultDisplay();
	    this.screen_w = localDisplay.getWidth();
	    this.screen_h = localDisplay.getHeight();
	    this.mContext = getApplicationContext();       
	    Util.init().setContext(mContext);
	    init_music();
	    findAllViews();
	
	   l5.setBackgroundResource(R.drawable.q2);
	    this.sv5 = new FifthSurfaceView(this, this.screen_w, this.screen_h,handler);
	    f5.removeAllViews();
	    f5.addView(this.sv5, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT));
	   sv5.showBackground();
	    this.handler.sendEmptyMessageDelayed(SHOWXIN, 2000L);
	    this.handler.sendEmptyMessageDelayed(SHOWHEART, 1000L);
	    this.handler.sendEmptyMessageDelayed(SHOWZI, 1500L);
	    this.handler.sendEmptyMessageDelayed(showb, 1100L);
	}

	 @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		 this.handler.sendEmptyMessageDelayed(1, 1000L);
	}
	 private void findAllViews()
		{
		  f5 = ((FrameLayout)findViewById(R.id.f5));
		  l5 = ((LinearLayout)findViewById(R.id.l5));
		}
	 
	 
	//��ʼ����������
			private void init_music(){	 
			    me = MediaPlay.init();
			    me.InitMediaPlay(mContext,R.raw.m5);   
			    me.play();
			}
			
			private final int SHOWHEART = 2;
			  private final int SHOWXIN = 1;
			  private final int SHOWZI = 3;
			  private final int GOTOSECOND = 4;
			  private final int showb=5;
			  //private final int SOUND = 5;
			//private final int showheart =1;
			Handler handler = new Handler()
				  {
				    public void handleMessage(Message paramMessage)
				    {
				      switch (paramMessage.what)
				      {
				      default:
				      case SHOWXIN:
				    	  sv5.showXin();
				    	  break;
				      case SHOWHEART:
				    	  sv5.showHeart();
				    	  break;
				      case SHOWZI:
				    	  sv5.showWenzi();
				    	  break;
				      case showb:
				    	  sv5.showB();
				    	  break;
				      
				      }
				    }
				  };
}
