package cn.yg.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.yg.mylove.R;
import cn.yg.util.BitmapCache;
//import com.wbw.view.SecondSurfaceView.ShowText;
//import cn.yg.view.SecondSurfaceView.ShowBianKuan;

public class SecondSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	

	private volatile boolean isallstop = false;
	
	BitmapCache bitmapcache;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
    private Handler handler;

    public SecondSurfaceView(Context context, int s_w, int s_h, Handler handler) {
		super(context);
		
		this.setFocusable(true);
		this.setKeepScreenOn(true);
		this.w = s_w;
		this.h = s_h;
		this.mContext = context;
		this.bitmapcache = BitmapCache.getInstance();
		this.holder = getHolder();
		this.holder.addCallback(this);
		
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT);
		this.handler = handler;
		goOn();
		
	}
	


	
	
	
	
	
	final CountDownLatch begin = new CountDownLatch(7);//bian kuang4 wenzi 1 lightheart1 shangxiapic1
	private Thread gotothird;
	public void goOn() {
		
		gotothird = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					begin.await();
					Thread.sleep(3000);
					handler.sendEmptyMessage(3);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		});
		gotothird.start();
	}
	
	
	private Thread st;
	public void showW(){
		size = 24;  //�����С
		if(w>=540) size = 27;
		if(w>=640) size = 33;
		if(w>=800) size = 35;
		if(w>=1000) size = 36;
		if(w>=1200) size = 37;
		String t=getResources().getString(R.string.second_words);
		st = new ShowText(t, (int)(w*0.42),110, 100);
		st.start();
	}
	
	//�@ʾ����
	private int size;
	private class ShowText extends Thread{
		int startx,starty;
		String text;
		public ShowText(String text,int startx,int starty,int speed){
			this.startx = startx;
			this.starty = starty;
			this.text = text;
		}
		
		@Override
		public void run(){
			drawtext();
			begin.countDown();
		}
		
		  private void drawtext(){	
		
	             System.out.println("createsd");
	             Paint p = new Paint(); //��������
	        	 int C =  mContext.getResources().getColor(R.color.gold);
		     	
	        	 p.setColor(C);	
	             int size = 30;  //�����С
	             p.setTextSize(size);
	             p.setFakeBoldText(true);
	             Xfermode xFermode = new PorterDuffXfermode(Mode.SRC_OVER);
	             p.setXfermode(xFermode);
	             String[] allt = text.split("\n");
	            for(int i=0;i<allt.length && !isallstop;i++){
	             int max = allt[i].length();
		             for(int count = 1;count<max+1 && !isallstop;count++){
		            	 try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
					
							e1.printStackTrace();
						}
		            	 synchronized (holder) {
		     				Canvas c = null;
		     				try {
		     					c = holder.lockCanvas(new Rect(0, starty-size*(i+1)+15*i,
		     							w, starty+size*(i+1)+15*i));
		     					c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY); 
		     					String tm_old = allt[i].substring(0,count-1);
		     					//String tm = allt[i].substring(0,count);	
		     					c.drawText(tm_old, startx,starty+size*i+15*i, p);
		    	                 c.drawText(tm_old, startx,starty+size*i+15*i, p);
		     				} catch (Exception e) {
		     					e.printStackTrace();
		     				} finally {
		     					try{
									if (c != null){
										holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
									}
								}catch(Exception e){
									e.printStackTrace();
								}
		     				}
	
		     			}
		            	 
		            	
		            	
		             }
	            }       
		    }
	}
	
	
	
	
	private Thread sbk_up,sbk_down,sbk_left,sbk_right;
	public void showBK(){
		 sbk_up = new ShowBianKuan(1);
		sbk_up.start();
		sbk_down = new ShowBianKuan(2);
		sbk_down.start();
		sbk_left = new ShowBianKuan(3);
		sbk_left.start();
		sbk_right = new ShowBianKuan(4);
		sbk_right.start();
		//begin.countDown();
	}
	private class ShowBianKuan extends Thread{
		private int type;
		private int startx,endx,starty,endy;
		private int ro_de_begin,ro_de_end;
		private Set<Bitmap> bit_set = new HashSet<Bitmap>();
		private int jiajuli;  //ÿ��Ҫ�ӵľ���
		public ShowBianKuan(int type){
			this.type = type;
		}
		int r_angle = 90;
		private int maxal = 200,minal = 150;  //����
		private int max_little_al = 200,min_little_al = 100;
		
		public void run(){
			
			if(type == 1){
				startx = 0;
				endx = w;
				starty = 0;
				endy = 50;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawUp(type);
				drawUpDown(type);
			}else if(type == 2){
				startx = 0;
				endx = w;
				starty = h-60-50;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawUp(type);
				drawUpDown(type);
			}else if(type == 3){
				startx = 0;
				endx = 40;
				starty = 40;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				drawLeftRight();
			}else if(type == 4){
				startx = w-50;
				endx = w;
				starty = 40;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawLeftRight();
			}

			
			for (Bitmap bit_get: bit_set) {  
			      bit_get.recycle(); 
			}  
			bit_set.clear();
			begin.countDown();
		}
		
		private int[] bit_large = {R.drawable.rightdown1,R.drawable.right1,R.drawable.right4};
		/**
		 * kind 1 up
		 * king 2 down
		 * @param kind
		 */
		private void drawUp(int kind){
			boolean isr = true;		
			Paint p = new Paint();
			int albe ;
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			
			int isup = 0;  //���ΪYλ��			
			int maxjia = 2;  //������
			
			int beginx = startx;
			int beginy;
			jiajuli = 20;			
			beginy = starty;
			albe = minal;
			
			//boolean isup = true;
			while(isr && !isallstop){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}	
				albe = albe+5;
				if(albe>=maxal) albe = minal;
				p.setAlpha(albe);
				//��������
				Bitmap bit_rotate = null;
				
				int position = getRandom(0, bit_large.length-1);
				bit_rotate = rotateBitmap(bit_large[position], 
							ro_de_begin, ro_de_end);	
					
				if(bit_rotate == null) continue;
				int bith = bit_rotate.getHeight();
				int bitw = bit_rotate.getWidth();
				//������ͼ�ڿ����
				int xx = beginx;
				int yy = beginy;
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {
						if(bit_old != null){
							int xx_r,yy_r,w_r,h_r;
							xx_r= xx>oldx?oldx:xx;
							yy_r = yy>oldy?oldy:yy;
							w_r = bit_old.getWidth();
							h_r = bit_old.getHeight();
							rt = new Rect(xx_r-10,yy_r-10,
									xx_r+w_r+bitw,yy_r+h_r+bith-jiajuli);
						
						}else
							rt = new Rect(xx-10,yy-10,xx+bitw+20,yy+bith+20);
						
						c = holder.lockCanvas(rt);
						if(bit_old != null){
							c.drawBitmap(bit_old, oldx, oldy, p);
						}
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}			
				
				if(beginx >endx) isr= false;
				beginx = beginx+bitw-10;	
				
				isup++;
				if(isup >= maxjia) isup =0;
				
				if(isup == 0){
					beginy = starty;
				}else if(isup == 1){
					beginy = starty + jiajuli;
				}else if(isup == 2){
					beginy = starty + jiajuli +jiajuli;
				}
				
			}
		}
		
		private void drawLeftRight(){
			boolean isr = true;		
			Paint p = new Paint();
			int albe ;
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			int beginx = startx;
			int beginy;
			jiajuli = 20;			
			beginy = starty;
			albe = minal;
			
			//boolean isup = true;
			while(isr  && !isallstop){
				try {
					//if(type == 3)
					//	Thread.sleep(1500);
					 Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}	
				albe = albe+5;
				if(albe>=maxal) albe = minal;
				p.setAlpha(albe);
				//��������
				Bitmap bit_rotate = null;
				
				int position = getRandom(0, bit_large.length-1);
				bit_rotate = rotateBitmap(bit_large[position], 
							ro_de_begin, ro_de_end);	
					
				if(bit_rotate == null) continue;
				int bith = bit_rotate.getHeight();
				int bitw = bit_rotate.getWidth();
				//������ͼ�ڿ����
				int xx = beginx;
				int yy = beginy;
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {

						rt = new Rect(xx-10,yy-10,xx+bitw+20,yy+bith+20);						
						c = holder.lockCanvas(rt);
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}			
				
				beginy = beginy+bith-5;			
				if(beginy >= endy-40) isr= false;
			}
		}
		
		private int[] dianzui = {R.drawable.love,R.drawable.love_middle_down,R.drawable.ulr1};
		//Сͼ�������׺
		private void drawUpDown(int type){
			boolean isr = true;
			int num = 0;  //������ͼƬ�±�
			Paint p = new Paint();			
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			
			while(isr && !isallstop){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}	
				int pa = getRandom(min_little_al,max_little_al);
				p.setAlpha(pa);
				
				//��������
				int position = getRandom(0, dianzui.length-1);
				Bitmap bit_rotate = rotateBitmap(dianzui[position], 
						ro_de_begin, ro_de_end);
				if(bit_rotate == null) continue;
				int bitw = bit_rotate.getWidth();
				int bith = bit_rotate.getHeight();
				//������ͼ�ڿ����
				int xx;
				
				xx = getRandom(startx,endx);
				int yy ;
				yy = getRandom(starty,endy);
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {
						
						rt = new Rect(xx,yy,xx+bitw,yy+bith);						
						c = holder.lockCanvas(rt);
						if(bit_old != null){
							c.drawBitmap(bit_old, oldx, oldy, p);						
						}
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}	
				num++;
				if(num>30) isr = false;
			}
		}
		
		//�õ���ת��ͼƬ���ǵû���
		private Bitmap rotateBitmap(int resourceid , int ro_begin,int ro_end){
			int de = getRandom(ro_begin, ro_end);
			Bitmap b = bitmapcache.getBitmap(resourceid, mContext);
			Matrix m = new Matrix();
			m.setRotate(de,
					(float) b.getWidth() / 2, (float) b.getHeight() / 2);
			Bitmap b2 = null;
			if(!b.isRecycled())
				b2 = Bitmap.createBitmap(
						b, 0, 0, b.getWidth(), b.getHeight(), m, true);
			
			return b2;
		}

	}
	
	
	
	private Thread drawlh;
	public void Drawlighthea(){
		drawlh=new DrawLightHeart();
		drawlh.start();
	}
	
	
	private class DrawLightHeart extends Thread{
		public DrawLightHeart(){
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			drawLightH();
			begin.countDown();
		}
		
		private int miCount=0;
		private void  drawLightH(){
            while(miCount<80){
            	try {
					Thread.sleep(800);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            Canvas canvas=null;
            Rect rt=new Rect(w/3, (int)(0.55*h), w, h);
            synchronized (holder){
            canvas = holder.lockCanvas(rt);
            miCount++;
            try {
                    if (holder == null || canvas == null) {
                            return;
                    }
                    
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                   // paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(2);
                   // canvas.drawRect(0, 0, w, h, paint);
                    switch (miCount % 6) {
                    case 0:
                            paint.setColor(Color.BLUE);
                            break;
                    case 1:
                            paint.setColor(Color.GREEN);
                            break;
                    case 2:
                            paint.setColor(Color.RED);
                            break;
                    case 3:
                            paint.setColor(Color.YELLOW);
                            break;
                    case 4:
                            paint.setColor(Color.argb(255, 255, 181, 216));
                            break;
                    case 5:
                            paint.setColor(Color.argb(255, 0, 255, 255));
                            break;
                    default:
                            paint.setColor(Color.WHITE);
                            break;
                    }
                    int i, j;
                    double x, y, r;

                    for (i = 0; i <= 90; i++) {
                            for (j = 0; j <= 90; j++) {
                                    r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 30;
                                    x = r * Math.cos(Math.PI / 45 * j)
                                                    * Math.sin(Math.PI / 45 * i) + w *0.63;
                                    y = -r * Math.sin(Math.PI / 45 * j) + h*0.6;
                                    canvas.drawPoint((float) x, (float) y, paint);
                                   // canvas.drawPoint((float) x, (float) y, paint);
                            }
                    }

                    paint.setTextSize(33);
                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);
                    holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
            }
            }
    }
	}
	}
	
	
	
	private Thread tm;
	public void DrawPics(){
		tm=new DrawPic1();
		tm.start();
	}
	
	
	private class DrawPic1 extends Thread{
		
		public  DrawPic1(){
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			drawp1();
			begin.countDown();
		}
		
		
		private int miCount=0;
		private int speed=5;
		private int[] pics={R.drawable.f1,R.drawable.f2,R.drawable.f3,R.drawable.f4,R.drawable.f5,R.drawable.f6,
				R.drawable.f7,R.drawable.f8,R.drawable.f9,R.drawable.f10,R.drawable.f11,R.drawable.f12,
				R.drawable.f13,R.drawable.f14};
		 
		
		
		private void drawp1(){
			int ww=w/3-90;
			int hh=ww*5/4;
			
			Rect rtup=new Rect(70, 100, w/3, h/2);
		Drawable up=getResources().getDrawable(R.drawable.f1);
		 Bitmap bitup= drawableToBitmap(up);
		 bitup=zoomImage(bitup, ww, hh);
		 
		 
		 Rect rtdown=new Rect(70, h/2, w/3, h-100);
		 Drawable down=getResources().getDrawable(R.drawable.f14);				
		Bitmap bitdown= drawableToBitmap(down);
		bitdown=zoomImage(bitdown, ww, hh);
		int startxdown=75,startydown=h-100-hh;
		int startxup=75,startyup=100;
		int i=0;
		
			while(miCount<600){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 miCount++;
				 
				 
		  
		
		
				
				 synchronized (holder){
					 
					 Canvas canvas =null;
					 canvas=holder.lockCanvas(rtdown);//�²�
					 canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
					 if (holder == null || canvas == null) {
                         return;
                      }
					 Paint paint=new Paint();
					 canvas.drawBitmap(bitdown, startxdown, startydown, paint);
					 startydown=startydown-speed;
					 if(startydown<h/2-hh)
					 {
						 startydown=h-100-hh;
					 }
					 holder.unlockCanvasAndPost(canvas);
					 canvas=holder.lockCanvas(rtup);//�ϲ�
					 canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
					 canvas.drawBitmap(bitup, startxup, startyup, paint);
					 startyup=startyup+speed;
					 if((startyup>=h/2)&&(startyup-speed<h/2)){
						 startyup=100;
						 bitup = bitmapcache
									.getBitmap(pics[i], mContext);
						
						 bitup=zoomImage(bitup, ww, hh);
						  i++;
						 if(i>=pics.length-1)
							 i=0;
					 }
					 
					 
						holder.unlockCanvasAndPost(canvas); 
					 
					 
				 }
			}
			
		}
		
	}
	
	//�õ������
	private int getRandom(int begin ,int end){
		return (int)Math.round(Math.random()*(end-begin)+begin);
	}
	
	
	
	private static Bitmap drawableToBitmap(Drawable drawable) {  
 	   int width = drawable.getIntrinsicWidth();  
 	   int height = drawable.getIntrinsicHeight();  
 	   Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
 	   Canvas canvas = new Canvas(bitmap);  
 	   drawable.setBounds(0, 0, width, height);  
 	   drawable.draw(canvas);  
 	   return bitmap;  
 	  
 	}  
	
	
	
	private Bitmap zoomImage(Bitmap bgimage, double newWidth,
            double newHeight) {
	         // ��ȡ���ͼƬ�Ŀ�͸�
	         float width = bgimage.getWidth();
	         float height = bgimage.getHeight();
	         // ��������ͼƬ�õ�matrix����
	         Matrix matrix = new Matrix();
	         // ������������
	         float scaleWidth = ((float) newWidth) / width;
	         float scaleHeight = ((float) newHeight) / height;
	         // ����ͼƬ����
	         matrix.postScale(scaleWidth, scaleHeight);
	         Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
	                         (int) height, matrix, true);
	         return bitmap;
      }

	
	
	
	public SecondSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
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
