package cn.yg.view;



import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.yg.mylove.R;
import cn.yg.util.BitmapCache;

public class FirstSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	
	private volatile boolean isallstop = false;
	
	BitmapCache bitmapcache;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
    private Handler handler;

    public FirstSurfaceView(Context context, int s_w, int s_h, Handler handler) {
		super(context);
		// TODO 鑷姩鐢熸垚鐨勬瀯閫犲嚱鏁板瓨鏍�
		this.setFocusable(true);
		this.setKeepScreenOn(true);
		this.w = s_w;
		this.h = s_h;
		this.mContext = context;
		this.bitmapcache = BitmapCache.getInstance();
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.handler = handler;
		//font32 = new Font32(context);
		//閫忔槑
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		goOn();
		
	}
    
    
    final CountDownLatch begin = new CountDownLatch(1);
	public Thread gotosecond;
	public void goOn() {
		
		gotosecond = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				try {
					begin.await();
					Thread.sleep(3000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
		gotosecond.start();
	}
    
    
    
    
	/*private int[] picId={R.drawable.f1,R.drawable.f2,R.drawable.f3,R.drawable.f4,R.drawable.f5,R.drawable.f6,
			R.drawable.f7,R.drawable.f8,R.drawable.f9,R.drawable.f10,R.drawable.f11,R.drawable.f12,
			R.drawable.f13,R.drawable.f14,R.drawable.f15,R.drawable.f16,R.drawable.f17};*/

	private int[] picId={R.drawable.yi1,R.drawable.yi2,R.drawable.yi3,R.drawable.yi4,
			R.drawable.yi5,R.drawable.yi6,R.drawable.yi7,R.drawable.yi8,
			R.drawable.yi9,R.drawable.yi10,R.drawable.yi11,R.drawable.f3};

	
	//函数能使图片充满整个屏幕
	public static Bitmap FitTheScreenSizeImage(Bitmap m,int ScreenWidth, int ScreenHeight)
	{
	        float width  = (float)ScreenWidth/m.getWidth();
	        float height = (float)ScreenHeight/m.getHeight();
	        Matrix matrix = new Matrix();
	        matrix.postScale(width,height);
	        return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(), matrix, true);
	 }
	
	
	
	private  class ShowPicThread extends Thread{
		private SurfaceHolder holder;
		Paint p=new Paint();
		ShowPicThread(SurfaceHolder holder){
			this.holder=holder;
		}
		
		
		
		public void run() {

			for(int i=0;i<picId.length;i++){
				Bitmap b=null;
				b=bitmapcache.getBitmap(picId[i], mContext);
				b=FitTheScreenSizeImage(b,w, h);
				//b=bitmapcache.getBitmap(R.drawable.f1, mContext);
					Canvas canvas=null;
					canvas=holder.lockCanvas();
					canvas.drawBitmap(b, 0, 0, p);
					if(canvas!=null){
						holder.unlockCanvasAndPost(canvas);
					}
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
			}
			
		begin.countDown();
		}
		
	}
	 
	 private Thread tmp;
	 public void drawPic(){
			tmp=new ShowPicThread(this.holder);
			tmp.start();
		}
	
	
	
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
