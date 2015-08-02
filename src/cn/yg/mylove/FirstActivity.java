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
import cn.yg.view.FirstSurfaceView;


public class FirstActivity extends Activity {
	
	  LinearLayout l1;
	  FrameLayout f1;
	  private Context mContext;
	  private int screen_h;
	  private int screen_w;
	  private MediaPlay me;
	  FirstSurfaceView sv1;
	 
	  
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);
        Display localDisplay = getWindowManager().getDefaultDisplay();
        this.screen_w = localDisplay.getWidth();
        this.screen_h = localDisplay.getHeight();
        this.mContext = getApplicationContext();       
        Util.init().setContext(mContext);
        init_music();
        findAllViews();
    
        this.sv1 = new FirstSurfaceView(this, this.screen_w, this.screen_h,this.handler);
        this.f1.removeAllViews();
        this.f1.addView(this.sv1, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
    			WindowManager.LayoutParams.MATCH_PARENT));
        this.handler.sendEmptyMessageDelayed(0, 1000L);
        
        
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
        this.handler.sendEmptyMessageDelayed(0, 1000L);
    }
    
   
    private void findAllViews()
    {
      f1 = ((FrameLayout)findViewById(R.id.f1));
      l1 = ((LinearLayout)findViewById(R.id.l1));
    }
  
    
   /* public void goToSecond(){
		Intent t = new Intent();
		t.setClass(FirstActivity.this,SecondActivity.class);
		startActivity(t);
		//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
		overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		FirstActivity.this.finish();
		BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
		MediaPlay.init().stop();
	  }*/
    
    
    public void goSecond(){
    	Intent t = new Intent();
    	t.setClass(FirstActivity.this, SecondActivity.class);
    	startActivity(t);
    	//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
    	overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    	FirstActivity.this.finish();
    	BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
    	MediaPlay.init().stop();
      }
  
    //初始化背景音乐
    private void init_music(){	 
        me = MediaPlay.init();
        me.InitMediaPlay(mContext,R.raw.m1);   
        me.play();
    }
    
    
    
    private final int drawpic= 0;
    private final int gotoseconds= 1;
    
    Handler handler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
        case drawpic:
      	  sv1.drawPic();
      	  break;
        case gotoseconds:
        	goSecond();
        	  break;
        /*
        case NOCONFIG:
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
