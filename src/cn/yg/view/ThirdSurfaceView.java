package cn.yg.view;

import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.yg.mylove.R;
import cn.yg.util.BitmapCache;
import cn.yg.util.Util;

public class ThirdSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	
	private volatile boolean isallstop = false;	
	BitmapCache bitmapcache;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
    private Handler handler;

	public ThirdSurfaceView(Context context, int s_w, int s_h, Handler handler) {
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

	
	final CountDownLatch begin = new CountDownLatch(6);
	private Thread gotofourth;
	public void goOn() {
		
		gotofourth = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					begin.await();
					Thread.sleep(3000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		});
		gotofourth.start();
	}
	
	private Thread sh;
	public void runHeart(){
		sh=new ShowBigHeart();
		sh.start();
	}
	private int ymax;
	private class ShowBigHeart extends Thread {
		public ShowBigHeart() {
		}

		public void run() {
			run_heart() ;
			begin.countDown();
			//handler.sendEmptyMessage(4);
		}

		public void run_heart() {
			int i, j;
			double x, y, r;
			int max = 180;
			//�ȼ�������е�λ�ã���ȥ��ͼ
			float[][] x_ff = new float[max][max];
			float[][] y_ff = new float[max][max];
			for (i = 0; i < max; i++) {
				for (j = 0; j < max; j++) {
					double pi = Math.PI;
					r = (pi / 45 * i * (1 - (Math.sin(pi / 45 * j))) * 18);
					x = ((r * (Math.cos(pi / 45 * j)) * (Math.sin(pi / 45 * i)) + w / 2) * 1.01);
					y = ((-r * (Math.sin(pi / 45 * j)) + h / 4) * 1.01);
					x_ff[i][j] = (float) x;
					y_ff[i][j] = (float) y;
				}
			}

			i = 0;
			j = 0;
			for (i = 0; i < max && !isallstop; i++) {

//					//sleep,��Ļ
					try {
						Thread.sleep(10);
//						clearAll();
					} catch (InterruptedException e) {
//						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					Canvas c = null;
					int numm = 10;
					
					for (j = 0; j < max && !isallstop; j=j+numm) {
						
						synchronized (holder) {
						try {
							Paint p = new Paint(); // ��������
							p.setColor(Color.RED);
							//p.setFakeBoldText(true);
							p.setStrokeWidth(2);
							//�ҳ������С
							float xx_min=x_ff[i][j],
									xx_max=x_ff[i][j],
									yy_min=y_ff[i][j],
									yy_max=y_ff[i][j];
							for(int k =0;k<numm;k++){
								float xx_n = x_ff[i][j+k];
								float yy_n = y_ff[i][j+k];
								if(xx_n >= xx_max) xx_max = xx_n;
								if(xx_n <= xx_min) xx_min = xx_n;
								if(yy_n >= yy_max) yy_max = yy_n;
								if(yy_n <= yy_min) yy_min = yy_n;
										
							}
							int xmin,xmax,ymin;
							if(xx_min == 0) xmin = 0;
							else xmin = (int) (xx_min-5>0?xx_min-5:0);
							if(yy_min == 0) ymin = 0;
							else ymin = (int) (yy_min-5>0?yy_min-5:0);
							xmax = (int) (xx_max+5);
							ymax = (int) (yy_max+5);
							
						
							//c = holder.lockCanvas(new Rect(xi,yi,xa,ya));
							c = holder.lockCanvas(new Rect(xmin,ymin,xmax,ymax));
							
							if(j!=0){
								int m = j-numm;
								for(int k =0;k<numm;k++){
									float xx_n = x_ff[i][m+k];
									float yy_n = y_ff[i][m+k];
									c.drawPoint(xx_n, yy_n, p);
								}
							}
							/*for(int k =0;k<numm;k++){
								float xx_n = x_ff[i][j+k];
								float yy_n = y_ff[i][j+k];
								c.drawPoint(xx_n, yy_n, p);
							}*/

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
	
	
	
	
	private Thread drdika;
	public void drawDikaer(){
		drdika=new DikaerCurve();
		drdika.start();
	}
	public class DikaerCurve extends Thread{
		public DikaerCurve(){
			
		}
		
		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			super.run();
				drawD();
			begin.countDown();	
	
		}
		
		
		
		private int  miCount=0;
		private int timesflag=0;
		private void drawD(){

            // TODO Auto-generated method stub
			
			
				Rect rt=new Rect(0, 0, w/3+20, w/3+20);
				 while(miCount < 70){
					 try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 miCount++;
					 if(timesflag<1){
						 timesflag++;
						 synchronized (holder){

								Canvas canvas=null;
				            canvas = holder.lockCanvas();
				            
				            try {
				                    if (holder == null || canvas == null) {
				                            return;
				                    }
				                    
				                    Paint paint = new Paint();
				                    paint.setAntiAlias(true);
				                    paint.setColor(Color.BLACK);
				                    paint.setStrokeWidth(3);
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
				                    
				                    
				                    for (i = 0; i <= 200*Math.PI; i++) {
				                        
				        				double t=0.01*i;
				                        r =2*(1+Math.sin(t))*w/18;//Ĭ����x=[-3,3]y=[-4,0.5]
				                        x = r*Math.cos(t)+3*w/18+10;
				                        y = r*Math.sin(t)+0.5*w/18+10;
				                        canvas.drawPoint((float) x, (float) y, paint);
				                        
				                        
				                
				        }

				                    /*for (i = 0; i <= 200*Math.PI; i++) {
				                            
				                    				double t=0.01*i;
				                                    r =2*(1+Math.sin(t))*w/18;
				                                    x = r*Math.cos(t)+3*w/18+10;
				                                    y = r*Math.sin(t)+0.5*w/18+10;
				                                    canvas.drawPoint((float) x, (float) y, paint);
				                                    
				                                    for(j=0;j<r;j=j+5){
				                                    	x = j*Math.cos(t)+3*w/18+10;
				                                        y = j*Math.sin(t)+0.5*w/18+10;
				                                        canvas.drawPoint((float) x, (float) y, paint);
				                                    }
				                                    
				                                   // canvas.drawPoint((float) x, (float) y, paint);
				                            
				                    }*/

				                    /*paint.setTextSize(33);
				                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

				                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
				                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
				                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
				                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
				                    holder.unlockCanvasAndPost(canvas);
				            } catch (Exception e) {
				            }
				    
						
						 }
					 }
					 synchronized (holder){
				Canvas canvas=null;
            canvas = holder.lockCanvas();
            
            try {
                    if (holder == null || canvas == null) {
                            return;
                    }
                    
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(3);
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
                    
                    
                    for (i = 0; i <= 200*Math.PI; i++) {
                        
        				double t=0.01*i;
                        r =2*(1+Math.sin(t))*w/18;//Ĭ����x=[-3,3]y=[-4,0.5]
                        x = r*Math.cos(t)+3*w/18+10;
                        y = r*Math.sin(t)+0.5*w/18+10;
                        canvas.drawPoint((float) x, (float) y, paint);
                        
                        
                
        }

                    for (i = 0; i <= 200*Math.PI; i++) {
                            
                    				double t=0.01*i;
                                    r =2*(1+Math.sin(t))*w/18;
                                    x = r*Math.cos(t)+3*w/18+10;
                                    y = r*Math.sin(t)+0.5*w/18+10;
                                    canvas.drawPoint((float) x, (float) y, paint);
                                    
                                    for(j=0;j<r;j=j+5){
                                    	x = j*Math.cos(t)+3*w/18+10;
                                        y = j*Math.sin(t)+0.5*w/18+10;
                                        canvas.drawPoint((float) x, (float) y, paint);
                                    }
                                    
                                   // canvas.drawPoint((float) x, (float) y, paint);
                            
                    }

                    /*paint.setTextSize(33);
                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
                    holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
            }
    
		}
		
			}
			}
	}
	
	
	
	private Thread Xiangri;
	public void drawxiangri(){
		Xiangri=new Xiangrikui();
		Xiangri.start();
	}
	
	private  class Xiangrikui extends Thread{
		public void Xiangrikui(){
			
		}
		
		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			super.run();
				drawB();
			begin.countDown();	
	
		}
		
		
		
		private int  miCount=0;
		private int timesflag=0;
		private void drawB(){

            // TODO Auto-generated method stub
			
			//sleep
			 Rect rt=new Rect(2*w/3-10, 0, w, w/3+10);
				 while(miCount < 71){
					 miCount++;
					 try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
					 if(timesflag<1){
						 timesflag++;
						 synchronized (holder){

								Canvas canvas=null;
				            canvas = holder.lockCanvas(rt);
				            
				            try {
				                    if (holder == null || canvas == null) {
				                            return;
				                    }
				                    
				                    Paint paint = new Paint();
				                    paint.setAntiAlias(true);
				                    paint.setColor(Color.BLACK);
				                    paint.setStrokeWidth(3);
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
				                    
				                    
				                    for (i = 0; i <= 200*Math.PI; i++) {
				                        
				        				double t=3.6*i;
				        				r=(30+10*Math.sin(t*30))*w/240;//x=[-40,40]y=[-40,40]
				                        x = r*Math.cos(t)+w-120-10;
				                        y = r*Math.sin(t)+120+10;
				                        canvas.drawPoint((float) x, (float) y, paint);
				                        
				                        
				                
				        }

				                    /*for (i = 0; i <= 200*Math.PI; i++) {
				                            
				                    				double t=3.6*i;
				                    				r=(30+10*Math.sin(t*30))*w/240;
				                                    x = r*Math.cos(t)+w-120-10;
				                                    y = r*Math.sin(t)+120+10;
				                                    canvas.drawPoint((float) x, (float) y, paint);
				                                    
				                                    for(j=0;j<r;j=j+20){
				                                    	x = j*Math.cos(t)+w-120-10;
				                                        y = j*Math.sin(t)+120+10;
				                                        canvas.drawPoint((float) x, (float) y, paint);
				                                    }
				                                    
				                                   // canvas.drawPoint((float) x, (float) y, paint);
				                            
				                    }*/

				                    /*paint.setTextSize(33);
				                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

				                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
				                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
				                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
				                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
				                    holder.unlockCanvasAndPost(canvas);
				            } catch (Exception e) {
				            }
				    
						
						 }
					 }
					 synchronized (holder){
				Canvas canvas=null;
            canvas = holder.lockCanvas(rt);
            
            try {
                    if (holder == null || canvas == null) {
                            return;
                    }
                    
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(3);
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
                    
                    
                    for (i = 0; i <= 200*Math.PI; i++) {
                        
        				double t=3.6*i;
        				r=(30+10*Math.sin(t*30))*w/240;//x=[-40,40]y=[-40,40]
                        x = r*Math.cos(t)+w-120-10;
                        y = r*Math.sin(t)+120+10;
                        canvas.drawPoint((float) x, (float) y, paint);
                        
                        
                
        }

                    for (i = 0; i <= 200*Math.PI; i++) {
                            
                    				double t=3.6*i;
                    				r=(30+10*Math.sin(t*30))*w/240;
                                    x = r*Math.cos(t)+w-120-10;
                                    y = r*Math.sin(t)+120+10;
                                    canvas.drawPoint((float) x, (float) y, paint);
                                    
                                    for(j=0;j<r;j=j+20){
                                    	x = j*Math.cos(t)+w-120-10;
                                        y = j*Math.sin(t)+120+10;
                                        canvas.drawPoint((float) x, (float) y, paint);
                                    }
                                    
                                   // canvas.drawPoint((float) x, (float) y, paint);
                            
                    }

                    /*paint.setTextSize(33);
                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
                    holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
            }
    
		}
		
			}
			}
		
	}
	
	
	
	
	
	private Thread butter;
	public void drawbutterfly(){
		butter=new Butterfly();
		butter.start();
	}
	private class Butterfly extends Thread{
		public Butterfly(){
			
		}

		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			super.run();
				drawB();
			begin.countDown();	
	
		}
		
		
		
		private int  miCount=0;
		private int timesflag=0;
		private void drawB(){

            // TODO Auto-generated method stub
			
			
				Rect rt=new Rect(0, h-w/5-70, w/3+20, h);
				 while(miCount < 72){
					 try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 miCount++;
					 if(timesflag<1){
						 timesflag++;
						 synchronized (holder) {

								Canvas canvas=null;
				            canvas = holder.lockCanvas();
				            
				            try {
				                    if (holder == null || canvas == null) {
				                            return;
				                    }
				                    
				                    Paint paint = new Paint();
				                    paint.setAntiAlias(true);
				                    paint.setColor(Color.BLACK);
				                    paint.setStrokeWidth(3);
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
				                    
				                    
				                    for (i = 0; i <= 200*Math.PI; i++) {
				                        
				        				double t=0.01*i;
				                        r =(0.2*Math.sin(3*t)+Math.sin(4*t)+2*Math.sin(5*t)+1.9*Math.sin(7*t)-0.2*Math.sin(9*t)+Math.sin(11*t))*w/30;//Ĭ����x=[-5,5]y=[-3,3]
				                        x = r*Math.cos(t)+w/5+10;
				                        y = r*Math.sin(t)+h-w/10-60;
				                        canvas.drawPoint((float) x, (float) y, paint);
				                        
				                        
				                
				        }

				                   /* for (i = 0; i <= 200*Math.PI; i++) {
				                            
				                    				double t=0.01*i;
				                    				r =(0.2*Math.sin(3*t)+Math.sin(4*t)+2*Math.sin(5*t)+1.9*Math.sin(7*t)-0.2*Math.sin(9*t)+Math.sin(11*t))*w/30;//Ĭ����x=[-5,5]y=[-3,3]
				                                    x = r*Math.cos(t)+w/5+10;
				                                    y = r*Math.sin(t)+h-w/10-60;
				                                    canvas.drawPoint((float) x, (float) y, paint);
				                                    
				                                    for(j=0;j<r;j=j+20){
				                                    	x = j*Math.cos(t)+w/5+10;
				                                        y = j*Math.sin(t)+h-w/10-60;
				                                        canvas.drawPoint((float) x, (float) y, paint);
				                                    }
				                                    
				                                   // canvas.drawPoint((float) x, (float) y, paint);
				                            
				                    }*/

				                    /*paint.setTextSize(33);
				                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

				                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
				                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
				                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
				                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
				                    holder.unlockCanvasAndPost(canvas);
				            } catch (Exception e) {
				            }
				    
						
						}
					 }
					 synchronized (holder){
				Canvas canvas=null;
            canvas = holder.lockCanvas();
            
            try {
                    if (holder == null || canvas == null) {
                            return;
                    }
                    
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(3);
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
                    
                    
                    for (i = 0; i <= 200*Math.PI; i++) {
                        
        				double t=0.01*i;
                        r =(0.2*Math.sin(3*t)+Math.sin(4*t)+2*Math.sin(5*t)+1.9*Math.sin(7*t)-0.2*Math.sin(9*t)+Math.sin(11*t))*w/30;//Ĭ����x=[-5,5]y=[-3,3]
                        x = r*Math.cos(t)+w/5+10;
                        y = r*Math.sin(t)+h-w/10-60;
                        canvas.drawPoint((float) x, (float) y, paint);
                        
                        
                
        }

                    for (i = 0; i <= 200*Math.PI; i++) {
                            
                    				double t=0.01*i;
                    				r =(0.2*Math.sin(3*t)+Math.sin(4*t)+2*Math.sin(5*t)+1.9*Math.sin(7*t)-0.2*Math.sin(9*t)+Math.sin(11*t))*w/30;//Ĭ����x=[-5,5]y=[-3,3]
                                    x = r*Math.cos(t)+w/5+10;
                                    y = r*Math.sin(t)+h-w/10-60;
                                    canvas.drawPoint((float) x, (float) y, paint);
                                    
                                    for(j=0;j<r;j=j+20){
                                    	x = j*Math.cos(t)+w/5+10;
                                        y = j*Math.sin(t)+h-w/10-60;
                                        canvas.drawPoint((float) x, (float) y, paint);
                                    }
                                    
                                   // canvas.drawPoint((float) x, (float) y, paint);
                            
                    }

                    /*paint.setTextSize(33);
                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
                    holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
            }
    
		}
		
			}
			}
	
	}
	
	
	private Thread drfl;
	public void drawflower(){
		drfl=new Flower();
		drfl.start();
	}
	
	private class Flower extends Thread{
		public Flower(){
			
		}
		

		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			super.run();
				drawF();
			begin.countDown();	
	
		}
		
		
		
		private int  miCount=0;
		int timesflag=0;
		private void drawF(){

            // TODO Auto-generated method stub
			
			
				Rect rt=new Rect(2*w/3-20, h-w*5/18-70, w, h);
				 while(miCount < 73){
					 try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 miCount++;
					 
					 if(timesflag<1){
						 timesflag++;
					 
					 synchronized (holder) {

							Canvas canvas=null;
			            canvas = holder.lockCanvas();
			            
			            try {
			                    if (holder == null || canvas == null) {
			                            return;
			                    }
			                    
			                    Paint paint = new Paint();
			                    paint.setAntiAlias(true);
			                    paint.setColor(Color.BLACK);
			                    paint.setStrokeWidth(3);
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
			                    
			                    
			                    for (i = 0; i <= 200*Math.PI; i++) {
			                        
			        				double t=0.01*i;
			                        r =(3*Math.sin(3*t)+3.5*Math.cos(10*t)*Math.cos(8*t))*w/36;//Ĭ����x=[-6,6]y=[-7,3]
			                        x = r*Math.cos(t)+5*w/6-10;
			                        y = r*Math.sin(t)+h-w/12-60;
			                        canvas.drawPoint((float) x, (float) y, paint);
			                        
			                        
			                
			        }

			                   /* for (i = 0; i <= 200*Math.PI; i++) {
			                            
			                    				double t=0.01*i;
			                    				r =3*Math.sin(3*t)+3.5*Math.cos(10*t)*Math.cos(8*t);//Ĭ����x=[-5,5]y=[-3,3]
			                                    x = r*Math.cos(t)+w/5+10;
			                                    y = r*Math.sin(t)+h-w/10-60;
			                                    canvas.drawPoint((float) x, (float) y, paint);
			                                    
			                                    for(j=0;j<r;j=j+20){
			                                    	x = j*Math.cos(t)+w/5+10;
			                                        y = j*Math.sin(t)+h-w/10-60;
			                                        canvas.drawPoint((float) x, (float) y, paint);
			                                    }
			                                    
			                                   // canvas.drawPoint((float) x, (float) y, paint);
			                            
			                    }*/

			                    /*paint.setTextSize(33);
			                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

			                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
			                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
			                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
			                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
			                    holder.unlockCanvasAndPost(canvas);
			            } catch (Exception e) {
			            }
			    
					
						
					}
					 }
					 synchronized (holder){
				Canvas canvas=null;
            canvas = holder.lockCanvas();
            
            try {
                    if (holder == null || canvas == null) {
                            return;
                    }
                    
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(3);
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
                    
                    
                    for (i = 0; i <= 200*Math.PI; i++) {
                        
        				double t=0.01*i;
                        r =(3*Math.sin(3*t)+3.5*Math.cos(10*t)*Math.cos(8*t))*w/36;//Ĭ����x=[-6,6]y=[-7,3]
                        x = r*Math.cos(t)+5*w/6-10;
                        y = r*Math.sin(t)+h-w/12-60;
                        canvas.drawPoint((float) x, (float) y, paint);
                        
                        
                
        }

                    for (i = 0; i <= 200*Math.PI; i++) {
                            
                    				double t=0.01*i;
                    				r =3*Math.sin(3*t)+3.5*Math.cos(10*t)*Math.cos(8*t);//Ĭ����x=[-5,5]y=[-3,3]
                                    x = r*Math.cos(t)+w/5+10;
                                    y = r*Math.sin(t)+h-w/10-60;
                                    canvas.drawPoint((float) x, (float) y, paint);
                                    
                                    for(j=0;j<r;j=j+20){
                                    	x = j*Math.cos(t)+w/5+10;
                                        y = j*Math.sin(t)+h-w/10-60;
                                        canvas.drawPoint((float) x, (float) y, paint);
                                    }
                                    
                                   // canvas.drawPoint((float) x, (float) y, paint);
                            
                    }

                    /*paint.setTextSize(33);
                    paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

                    RectF rect = new RectF(w/2-100, h/2+20, w/2+100, h/2+30);
                    canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
                    canvas.drawText("Loving You", w/2-80, h/2, paint);*/
                    //canvas.drawText("Loving You", w/2-80, h/2, paint);
                    holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
            }
    
		}
		
			}
			}
	
	
	}
	
	
	private Thread drawp;
	public void Drawpepole(){
		drawp=new DrawPepole();
		drawp.start();
	}
	private class DrawPepole extends Thread{
		public DrawPepole(){
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			DrawP(2,(int)(0.61*h),(int)(0.85*w));
			//DrawP(2,ymax,(int)(0.7*w));
			begin.countDown();
		}
		
		
		private int[] bit_large = {R.drawable.f1,R.drawable.f2,R.drawable.f3,R.drawable.f4,R.drawable.f5,R.drawable.f6,
				R.drawable.f7,R.drawable.f8,R.drawable.f9,R.drawable.f10,R.drawable.f11,R.drawable.f12,
				R.drawable.f13};
		//private int[] bit_large={R.drawable.hudei};
		private int ro_de_begin=-5;
		private int ro_de_end=5;
		private int minal=150;
		private int maxal=200;
		private void DrawP(int startx,int starty,int endx){
			

			boolean isr = true;		
			Paint p = new Paint();
			int albe ;
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			
			int isup = 0;  //���ΪYλ��			
			int maxjia = 1;  //������
			
			int beginx = startx;
			int beginy;
			int jiajuli = 20;			
			beginy = starty;
			albe = minal;
			int timesflag=0;
			//boolean isup = true;
			while(isr && !isallstop){
				try {
					Thread.sleep(4000);
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
				//bit_rotate = rotateBitmap(bit_large[position], 
							//ro_de_begin, ro_de_end);	
					
				
				bit_rotate = rotateBitmap(bit_large[position], 
						ro_de_begin, ro_de_end);
				bit_rotate=zoomImage(bit_rotate, 140, 180);
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
									xx_r+w_r+bitw,yy_r+h_r+bith-jiajuli+100);
						
						}else
							rt = new Rect(xx-10,yy-10,xx+bitw+20,yy+bith+110);
						
						c = holder.lockCanvas(rt);
						if(bit_old != null){
							c.drawBitmap(bit_old, oldx, oldy, p);
						}
						
						if(timesflag<1)
						{
							c.drawBitmap(bit_rotate, xx, yy, p);
							timesflag++;
						}
						
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						//bit_set.add(bit_rotate);
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
				
				if(beginx >w-bitw) isr= false;
				beginx = beginx+bitw;	
				
				isup++;
				if(isup > maxjia) isup =0;
				
				if(isup == 0){
					beginy = starty;
				}else if(isup == 1){
					beginy = starty + jiajuli;
				}
				
			}
		
			
		}
	}
	
	
	/*
private Thread sbk_up,sbk_down,sbk_left,sbk_right;
	public void showBK3(){
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
				//drawUpDown(type);
			}else if(type == 2){
				startx = 0;
				endx = w;
				starty = h-60-50;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawUp(type);
				//drawUpDown(type);
			}else if(type == 3){
				startx = 0;
				endx = 40;
				starty = 50;
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
				starty = 50;
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
		
		private int[] bit_large = {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6,
				R.drawable.a7,R.drawable.a8,R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12,
				R.drawable.a13,R.drawable.a14,R.drawable.a15,R.drawable.a16,R.drawable.a17};
		*//**
		 * kind 1 up
		 * king 2 down
		 * @param kind
		 *//*
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
		}}
		*/
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

	
		 public  Bitmap zoomImage(Bitmap bgimage, double newWidth,
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

		
	
	private int getRandom(int begin ,int end){
		return (int)Math.round(Math.random()*(end-begin)+begin);
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
