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
import cn.yg.view.SecondSurfaceView;

public class SecondActivity extends Activity {
	
	
	LinearLayout l2;
	  FrameLayout f2;
	  private Context mContext;
	  private int screen_h;
	  private int screen_w;
	  private MediaPlay me;
	 private  SecondSurfaceView sv2;
	 
	  
	  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
             
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_second);
      Display localDisplay = getWindowManager().getDefaultDisplay();
      this.screen_w = localDisplay.getWidth();
      this.screen_h = localDisplay.getHeight();
      this.mContext = getApplicationContext();       
      Util.init().setContext(mContext);
      init_music();
      findAllViews();
  
     l2.setBackgroundResource(R.drawable.q4);
      this.sv2 = new SecondSurfaceView(this, this.screen_w, this.screen_h,handler);
      f2.removeAllViews();
      f2.addView(this.sv2, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
  			WindowManager.LayoutParams.MATCH_PARENT));
     // this.handler.sendEmptyMessageDelayed(3, 1000L);
      this.handler.sendEmptyMessageDelayed(2, 1100L);
      this.handler.sendEmptyMessageDelayed(1, 1000L);
      this.handler.sendEmptyMessageDelayed(0, 1200L);
      this.handler.sendEmptyMessageDelayed(4, 1300L);
      
  }
  
  @Override
protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
	 this.handler.sendEmptyMessageDelayed(2, 1100L);
     this.handler.sendEmptyMessageDelayed(1, 1000L);
}
  
 
  private void findAllViews()
  {
    f2 = ((FrameLayout)findViewById(R.id.f2));
    l2 = ((LinearLayout)findViewById(R.id.l2));
  }

  
  
  

  //初始化背景音乐
  private void init_music(){	 
      me = MediaPlay.init();
      me.InitMediaPlay(mContext,R.raw.m2);   
      me.play();
  }
  
  public void goThrid(){
		Intent t = new Intent();
		t.setClass(SecondActivity.this, ThirdActivity.class);
		startActivity(t);
		//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
		overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		SecondActivity.this.finish();
		BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
		MediaPlay.init().stop();
	  }
  
  private final int showz= 2;
  private final int showbk=1;
  private final int gothird=3;
  private final int showlightheart=0;
  private final int drawpics=4;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case showlightheart:
    	  sv2.Drawlighthea();
    	  break;
      case showz:
    	  sv2.showW();
    	  break;
      case showbk:
    	  sv2.showBK();
    	  break;
      case gothird:
    	  goThrid();
    	  break;
      case drawpics:
    	  sv2.DrawPics();
    	  break;
      
     /* case NOCONFIG:
    	  isconfig = false;
    	  break;*/
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
