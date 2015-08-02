package cn.yg.mylove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.yg.util.BitmapCache;
import cn.yg.util.MediaPlay;
import cn.yg.util.Util;
import cn.yg.view.ThirdSurfaceView;

public class ThirdActivity extends Activity {
	
		LinearLayout l3;
		  FrameLayout f3;
		  private Context mContext;
		  private int screen_h;
		  private int screen_w;
		  private MediaPlay me;
		 private  ThirdSurfaceView sv3;
		 
		  
		  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	           
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_third);
	    Display localDisplay = getWindowManager().getDefaultDisplay();
	    this.screen_w = localDisplay.getWidth();
	    this.screen_h = localDisplay.getHeight();
	    this.mContext = getApplicationContext();       
	    Util.init().setContext(mContext);
	    init_music();
	    findAllViews();
	
	   l3.setBackgroundResource(R.drawable.f15);
	    this.sv3 = new ThirdSurfaceView(this, this.screen_w, this.screen_h,handler);
	    f3.removeAllViews();
	    f3.addView(this.sv3, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT));
	   // this.handler.sendEmptyMessageDelayed(3, 1000L);
	    this.handler.sendEmptyMessageDelayed(4, 1100L);
	    this.handler.sendEmptyMessageDelayed(2, 1000L);
	    this.handler.sendEmptyMessageDelayed(0, 1500L);
	    this.handler.sendEmptyMessageDelayed(5, 1200L);
	    this.handler.sendEmptyMessageDelayed(6, 1400L);
	    this.handler.sendEmptyMessageDelayed(7, 2000L);
	    
	}
	
	@Override
	protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
	this.handler.sendEmptyMessageDelayed(2, 1000L);
    this.handler.sendEmptyMessageDelayed(3, 1500L);
	}
	
	
	
	
	private void findAllViews()
	{
	  f3 = ((FrameLayout)findViewById(R.id.f3));
	  l3 = ((LinearLayout)findViewById(R.id.l3));
	}
	
	
	
	
	
	//初始化背景音乐
	private void init_music(){	 
	    me = MediaPlay.init();
	    me.InitMediaPlay(mContext,R.raw.m3);   
	    me.play();
	}
	
	public void goFourth(){
			Intent t = new Intent();
			t.setClass(ThirdActivity.this, FourthActivity.class);
			startActivity(t);
			//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
			overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
			ThirdActivity.this.finish();
			BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
			MediaPlay.init().stop();
		  }
	
	
	private final int showbiankuang3=3;
	private final int showheart=2;
	private final int gofourth=1;
	private final int drawdikaer=4;
	private final int drawxiangrikui=0;
	private final int drawbutterf=5;
	private final int drawflower=6;
	private final int drawpeple=7;
	private Handler handler = new Handler()
	{
	  public void handleMessage(Message paramMessage)
	  {
	    switch (paramMessage.what)
	    {
	    default:
	    case showheart:
	  	  sv3.runHeart();
	  	  break;
	    /*case showbiankuang3:
		  	  sv3.showBK3();
		  	  break;*/
	    case gofourth:
	  	 goFourth();
	  	  break;
	    case drawdikaer:
		  	 sv3.drawDikaer();
		  	  break;
	    case drawxiangrikui:
		  	 sv3.drawxiangri();
		  	  break;
	    
	    case drawbutterf:
	  	  sv3.drawbutterfly();
	  	  break;
	    case drawflower:
		  	  sv3.drawflower();
		  	  break;
	    case drawpeple:
	    	sv3.Drawpepole();
	    	break;
	    }
	   
	  }
	};
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle action bar item clicks here. The action bar will
	    // automatically handle clicks on the Home/Up button, so long
	    // as you specify a parent activity in AndroidManifest.xml.
	    int id = item.getItemId();
	    if (id == R.id.action_settings) {
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}


}
