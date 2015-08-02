package cn.yg.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.yg.mylove.R;
import cn.yg.util.BitmapCache;



/**
 * @author yg
 *@version 1.0
 *
 *过程描述：1.画五角行的心，设置透明度并旋转
 *2.画周围的小心点，设置透明度（画了两边所以才能保持不闪烁）
 *3.画有限个五角星，并随机设置透明度，由于透明度的不同，当重新画第二遍的时候会有闪烁效果
 *4.那变化大小的心是每次绘制的时候都进行放大，然后放大到一定次数时开启下坠的心
 */

public class FourthSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	
	private volatile boolean isallstop = false;	
	BitmapCache bitmapcache;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
    private Handler handler;

   
	public FourthSurfaceView(Context context, int s_w, int s_h, Handler handler) {
		
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
	
	
	final CountDownLatch begin = new CountDownLatch(4);
	private Thread gotofifth;
	public void goOn() {
		
		gotofifth = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					begin.await();
					Thread.sleep(3000);
					//当前surfaceview全部绘制完整后发送handler消息跳转到第5个界面
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		gotofifth.start();
	}
	
	
	
	
	private Thread ldthread,hsthread,abthread,cdfgthread;

	//定义一个hashmap存放下落的 心，每个心是一个线程
	private HashMap<Integer,Thread> dropthread = new HashMap<Integer, Thread>();
	private int dropnum = 0;
	
	private void dropthread_Add(int dn,Thread dr){
		synchronized (dropthread) {
			
			dropthread.put(dn,dr);
		}
	}
	
	public void dropthread_Remove(int dn){
		synchronized (dropthread) {
			dropthread.remove(dn);
		}
	}
	
	public void dropthread_Iterator(){
		synchronized (dropthread) {
			Iterator<Integer> iterator = dropthread.keySet().iterator();        
		    while (iterator.hasNext()) {    
		    	Thread t = dropthread.get(iterator.next());
		    	if(t != null && t.isAlive())
		    		t.interrupt();   
		    }   
		}
	}
	
	

	public Bitmap love_l;
	public int love_l_w,love_l_h;
	private int dest_x,dest_y;  
	private int beau_w,beau_h;
	int dnum =20;
	
	int xadd = 0;
	
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		int ii = 0;
		Bitmap beau = bitmapcache.getBitmap(R.drawable.beau1, mContext);  
		Bitmap stick = bitmapcache.getBitmap(R.drawable.stick, mContext); 
		Bitmap big = bitmapcache.getBitmap(R.drawable.big1, mContext);
	
		
		if(w>=500) xadd = xadd+10;
		if(w>=600) xadd = xadd+20;
		if(w>=800) xadd = xadd+20;
		
		if(w>=1000) xadd = xadd+30;
		if(w>=1500) xadd = xadd+30;
 		
		Paint p = new Paint();
		dest_x = 163+big.getWidth()/2 +xadd; 
		dest_y = 190;
		beau_w = beau.getWidth();
		beau_h = beau.getHeight();
		
		dropx = 160 +xadd; 
		dropy = dest_y;
		Bitmap llove = bitmapcache.getBitmap(R.drawable.big_h, mContext);
		int bw = llove.getWidth();
		int bh = llove.getHeight();
		dropw = bw+1;
		droph = bh+1;
		
		
		love_l = bitmapcache.getBitmap(R.drawable.love, mContext);
		love_l_h = love_l.getHeight();
		love_l_w = love_l.getWidth();
	
		
		drawText();//写字
		makeLoveDot(dnum);
		//开启画小心得线程
		 ldthread = new LoveDotThread();
		ldthread.start();
		
		 hsthread = new HeartShowThread();
		hsthread.start();
		
		
	
		
	}

	
	

		private int cx,dx,fx,gx,ay,by;
		private int maxziy;
		//写字
		private void drawText(){

			
			
			String aa=getResources().getString(R.string.thrid_f_et_1);
			String bb=getResources().getString(R.string.thrid_f_et_2);
			String cc=getResources().getString(R.string.thrid_s_et_1);
			String dd=getResources().getString(R.string.thrid_s_et_2);
			String ff=getResources().getString(R.string.thrid_s_et_3);
			String gg=getResources().getString(R.string.thrid_s_et_4);
			String a =  aa;
			String b =  bb;
			String c =  cc;
			String d =  dd;
			String f = ff;
			String g =  gg;
			if(a.length()>4) a = a.substring(0, 4);
			else if(a.length()<4) a = aa;
			if(b.length()>4) b = b.substring(0, 4);
			else if(b.length()<4) b = bb;
			if(c.length()>4) c = c.substring(0, 4);
			else if(c.length()<4) c = cc;
			if(d.length()>4) d = d.substring(0, 4);
			else if(d.length()<4) d = dd;
			if(f.length()>4) f = f.substring(0, 4);
			else if(f.length()<4) f = ff;
			if(g.length()>4) g = g.substring(0, 4);
			else if(g.length()<4) g = gg;
			
			StringKind[] ab = new StringKind[2];
			ay = 100;
			by = 200;
			StringKind a_kind = new StringKind(a,0,beau_w+20+xadd,ay,10);
			StringKind b_kind = new StringKind(b,0,beau_w+20+xadd,by,10);
			ab[0] = a_kind;
			ab[1] = b_kind;
			abthread = new drawTextThread(ab);
			abthread.start();
			
			int w5 = w/4-20;
			
			 cx = w-w5+10;
			 dx = w-2*w5+20;
			//int fx = w-3*w5;
			//int gx = w-4*w5;
			 fx = dropx+dropw-5+xadd;
			 gx = dropx+5-textsize-xadd;
			
			cx = checkTextXInDrop(cx);
			dx = checkTextXInDrop(dx);
			fx = checkTextXInDrop(fx);
			gx = checkTextXInDrop(gx);
			
			int cy = 300,dy = 350,fy=400,gy=450;
			int cg_space = 20;
			
			if(h<=700) {
				textsize = 40;
				cg_space = 10;
				dy = 330;
				fy = 360;
				gy = 390;
			}else if(h<=750){			
					textsize = 40;
					cg_space = 15;
					dy = 335;
					fy = 370;
					gy = 405;			
			}else if(h<=850){
				textsize = 45;
				cg_space = 15;
				dy = 340;
				fy = 380;
				gy = 420;
			}else if(h>=1000 && h<=1200){
				textsize = 60;
				cg_space = 25;
				cy = 370;
				dy = 430;
				fy = 490;
				gy = 550;
			}else if(h>1200){
				textsize = 65;
				cg_space = 25;
				cy = 400;
				dy = 460;
				fy = 520;
				gy = 580;
			}
			
			StringKind[] cdfg = new StringKind[4];
			StringKind c_kind = new StringKind(c,1,cx,cy,cg_space);
			StringKind d_kind = new StringKind(d,1,dx,dy,cg_space);
			StringKind f_kind = new StringKind(f,1,fx,fy,cg_space);
			StringKind g_kind = new StringKind(g,1,gx,gy,cg_space);
			
			maxziy = gy+4*(textsize+20);
			cdfg[0] = c_kind;
			cdfg[1] = d_kind;
			cdfg[2] = f_kind;
			cdfg[3] = g_kind;
			cdfgthread = new drawTextThread(cdfg);
			cdfgthread.start();
		}
		
		//����Ƿ���drop��
		private int checkTextXInDrop(int x){
			System.out.println("x:"+x+" dropx:"+dropx+" dropw:"+dropw);
			if(x>=dropx && x<= dropx+dropw){
				if(x< dropx+dropw/2) x = dropx-dropw/2;
				else x = dropx+dropw+10;
			}
			return x;
			
		}
		
		
		
		
		int textsize = 55;
		class drawTextThread extends Thread{
			//String text;
			//int or,drawx,drawy,space;
			StringKind[] sk;
			/**
			 * orΪ0�ᣬΪ1����
			 * @param text
			 * @param orentation
			 * @param drawx
			 * @param drawy
			 * @param space
			 */
			public drawTextThread(StringKind[] sk){
				this.sk = sk;
			}
			
			public void run(){
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				for(int i =0;i<sk.length && !isallstop;i++){
					drawT(sk[i]);
					
				}
				begin.countDown();
			}
			
			private void drawT(StringKind s){
				String text = s.text;
				int or = s.or;
				int drawx = s.drawx;
				int drawy = s.drawy;
				int space = s.space;			
				Paint paint = new Paint();
				paint.setStyle(Style.STROKE);//���÷����
				//paint.setStrokeWidth(5);//�ʿ�5����
				paint.setColor(Color.RED);//����Ϊ���
				paint.setAntiAlias(true);//��ݲ���ʾ
				paint.setTextSize(textsize);
				paint.setStrokeCap(Paint.Cap.ROUND);  //���ñ�ˢ����ʽ
				//measureText
				//serif �������壨��ĩ�˼�ǿ���� ����������
				//Italic ����б�� Italic ����˼�壬���������
				paint.setTypeface(Typeface.create(Typeface.SERIF,Typeface.ITALIC));
				paint.setTextAlign(Align.LEFT);
				char[] c =text.toCharArray();
				for(int i = 0; i<c.length && !isallstop;i++){  //��								
					Rect r = new Rect();
					paint.getTextBounds(c, i, 1, r);
					int tw = r.right-r.left;
					int th = r.bottom - r.top;			
					for(int j =1;j<=15 && !isallstop;j++){  //������� 					
						if(j<10){
							paint.setAlpha(j*10);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO �Զ����ɵ� catch ��
								e.printStackTrace();
							}
						}else {
							paint.setAlpha(j*15);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO �Զ����ɵ� catch ��
								e.printStackTrace();
							}
						}
						synchronized (holder) {
							Canvas ca = null;
							try {
								Rect rtt = new Rect(drawx-textsize,drawy-textsize,drawx+textsize,drawy+textsize);
								ca = holder.lockCanvas(rtt);		
								Xfermode xFermode = new PorterDuffXfermode(Mode.SRC_OVER);
								paint.setXfermode(xFermode);
								ca.drawText(String.valueOf(c[i]), drawx, drawy, paint);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try{
									if (c != null){
										holder.unlockCanvasAndPost(ca);// ����������ͼ�����ύ�ı䡣
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}

						}
						
					}//for����
					if(or == 0){  //x++
						drawx = drawx+textsize+space;
					}else drawy = drawy +textsize+space;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}//for��
				
			}
		}
		
		
		
		
		
		
		
		private int dropx ,dropy,dropw,droph;
		class HeartShowThread extends Thread{
			public void run(){
				show();
				begin.countDown();
			}
			
			public void show(){
				int ii = 30;
				boolean run = true;
				Paint p = new Paint();
				
				//��ת�Ļ�
				Bitmap hua = bitmapcache.getBitmap(R.drawable.hua, mContext);
				int huax = dest_x-40+xadd/2;
				int huay = 30;
				int huaw = hua.getWidth();
				int huah = hua.getHeight();
				int huamax = 180;
				int huamin = 0;
				int hua_add_plus = 2;
				int huar=0;	
				int re_num = 0;
				
				//��
				Bitmap xin1 = bitmapcache.getBitmapByLM(R.drawable.xin1, mContext,2);
				Bitmap xin2 = bitmapcache.getBitmapByLM(R.drawable.xin2, mContext,2);
				int xin1w = xin1.getWidth();
				int xin1h = xin1.getHeight();
				int xin2w = xin2.getWidth();
				int xin2h = xin2.getHeight();
				//������1��������2
				ArrayList<LoveDot> xinall = new ArrayList<LoveDot>();
				xinall.add(new LoveDot(1+xadd,10,1));
				xinall.add(new LoveDot(48+xadd,18,1));
				xinall.add(new LoveDot(110+xadd,40,1));
				xinall.add(new LoveDot(20+xadd,150,2));
				xinall.add(new LoveDot(150+xadd,160,2));
				xinall.add(new LoveDot(130+xadd,190,2));
				
				boolean xinboolean = true;  //Ϊ���ʱ����,Ϊ�ٵ�ʱ�����
				int oldx = 0;
				while (true  && !isallstop) {
					try {
						Thread.sleep(150);
					} catch (InterruptedException e2) {
						// TODO �Զ����ɵ� catch ��
						e2.printStackTrace();
					}
					
					//��ת��͸��
					synchronized (holder) {
						Canvas c = null;
						Bitmap b2 = null;	
						try {
							//c.drawColor(co);					
							Matrix m = new Matrix();
							m.setRotate(huar);
							p.setAlpha(255-Math.abs(huar));
							b2 = Bitmap.createBitmap(
										hua, 0, 0, huaw,huah, m, true);	
							c = holder.lockCanvas(new Rect(huax,huay,huax+b2.getWidth(),
									huay+b2.getHeight()));
							c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
							c.drawBitmap(b2, huax,huay, p);
							//c.drawBitmap(big, dest_x, dest_y, p);
							
							huar = huar+hua_add_plus;
							if(huar==huamax) hua_add_plus = -2;
							if(huar == huamin) hua_add_plus = 2;
							
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
							//if(b2 != null)
							//	b2.recycle();
						}
					}
					
					
					//������˸
					//Ϊ���ʱ����,Ϊ�ٵ�ʱ�����
					if(xinboolean){
						
						LoveDot d = xinall.get(oldx);
						Bitmap xinb = null;
						int xw,xh;
						int xx = d.x;
						int yy = d.y;
						if(d.num == 2){
							xinb = xin2;
							xw = xin2w;
							xh = xin2h;
						}
						else {
							xinb = xin1;
							xw = xin1w;
							xh = xin1h;
						}
						
						synchronized (holder) {
							Canvas c = null;
							try {
								c = holder.lockCanvas(new Rect(xx,yy,xx+xw,yy+xh));							
								p.setAlpha(255);
								//c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
								c.drawBitmap(xinb, xx,yy, p);
							
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
						//oldx = thisone;
						xinboolean = !xinboolean;
					}else{
						int thisone = getRandom(0, xinall.size()-1);
						LoveDot d = xinall.get(thisone);
						int xw,xh;
						int xx = d.x;
						int yy = d.y;
						if(d.num == 2){
							//xinb = xin2;
							xw = xin2w;
							xh = xin2h;
						}
						else {
							//xinb = xin1;
							xw = xin1w;
							xh = xin1h;
						}
						synchronized (holder) {
							Canvas c = null;
							try {
								c = holder.lockCanvas(new Rect(xx,yy,xx+xw,yy+xh));							
								p.setAlpha(255);
								c.drawColor(Color.TRANSPARENT,Mode.CLEAR);						
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
						oldx = thisone;
						xinboolean = !xinboolean;
					}
					
					
					
					re_num++;
					if(re_num>3){
						re_num = 0;
					}else continue;
					
					Bitmap big15 = bitmapcache.getBitmap(R.drawable.big99, mContext);
					//YΪdest ,x Ϊdest - w/2
					int bw = big15.getWidth();
					int bh = big15.getHeight();
					Bitmap mBitmap = Bitmap.createScaledBitmap(big15, bw-ii, bh-ii, true);  
					bw = mBitmap.getWidth();
					bh = mBitmap.getHeight();
					int x = dest_x-bw/2;
					int y = dest_y;
					//
					dropx = x;
					dropy = y;
					//dropw = ,droph
					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(x-1,y-1,x+1+bw,y+1+bh));					
							p.setAlpha(255);
							c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
							c.drawBitmap(mBitmap, x,y, p);
							//c.drawBitmap(big, dest_x, dest_y, p);
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
					ii--;
					if(ii <=0) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO �Զ����ɵ� catch ��
							e1.printStackTrace();
						}
						ii = 30;

						synchronized (holder) {
							Canvas c = null;
							try {
								c = holder.lockCanvas(new Rect(x-1,y-1,x+1+bw,y+1+bh));
								//c.drawColor(co);
								c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
								//c.drawBitmap(big15, x,y, p);
								//c.drawBitmap(big, dest_x, dest_y, p);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try{
									if (c != null){
										holder.unlockCanvasAndPost(c);
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}

						}
						if(!isallstop){
							dropnum++;
							Thread drop = new LoveDrop(dropnum,x,y);
							drop.start();
							dropthread_Add(dropnum, drop);
							//new LoveDrop(x,y).start();
						}
								
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		
		
		
		
		
		
		
		class LoveDrop extends Thread{
			int numkey;
			private LoveDrop(int numkey,int x,int y){
				startx = x;
				starty = y+10;
				this.numkey = numkey;
			}
			public void run(){
				drop();
				dropthread_Remove(numkey);
				begin.countDown();
			}
			int startx,starty;
			int endy,endx;
			private void drop(){
				endy = h-100;  
				endx = startx;
				Bitmap llove = bitmapcache.getBitmap(R.drawable.big_h, mContext);
				int bw = llove.getWidth();
				int bh = llove.getHeight();
				Paint p = new Paint();
				boolean isr = true;
				long de = 30;
				dropw = bw+1;
				droph = bh+1;
				while(isr && !isallstop){
					synchronized (holder) {
						Canvas c = null;
						try {
							
							c = holder.lockCanvas(new Rect(startx,starty-2,startx+bw,starty+bh+1));						
							c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
							c.drawBitmap(llove, startx,starty, p);	
							starty=starty+2;
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try{
								if (c != null){
									holder.unlockCanvasAndPost(c);
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}

					}
					try {
						Thread.sleep(de);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					de=(long) (de-0.05);
					if(de <=40) de=(long) (de-0.01);
					if(de <=20) de = 20;
					if(starty >= endy && !isallstop){//׼��������
						isr = false; 				
						int centerY = endy;
						//int[] Y_axis = new int[w-startx];
						int left_right = getRandom(0, 10);
						boolean isright = true;  //Ϊ��Ϊ�ң�Ϊ��Ϊ��Ϊ�ҵĻ����Щ
						if(left_right<4){  //��
							isright = false;
						}
						int le,top;
						
						int maxhh = endy - maxziy;
						int rmin = 80,rmax = 100;
						int lmin = 40,lmax = 50;
						if(maxhh <100 && maxhh>50 ) {
							rmin = 50;
							rmax = 60;
						}
						if(maxhh < 50){
							rmin = 30;
							rmax = 40;
							lmin = 20;
							lmax = 30;
						}
						
						if(isright){
							le = w-startx;
							top = getRandom(rmin, rmax);
						}
						else {
							le = startx+bw;
							top = getRandom(lmin,lmax);
						}
						for (int i = 1; i < le && !isallstop; i++) {// �������Ҳ�
							int x;
							if(isright)
								x = startx+i;
							else x = startx-i;
							//y=Asin����x+�� �գ�����λ��������������X��λ�ù�ϵ������ƶ����루����Ҽ���
							//�أ��������ڣ���С������T=2��/|��|��
							//		A��������ֵ������������ѹ���ı����� 	
							int y = centerY-Math.abs( (int) (top * Math.sin(i * 2 * Math.PI/ 180)));
							synchronized (holder) {
								Canvas c = null;
								try {
									c = holder.lockCanvas(new Rect(x-2,
											y-15, x + bw+1, y + bh
													+ 15));								
									c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
									c.drawBitmap(llove, x, y, p);								
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

							}// sy
							int delay = endy - y;
							try {
								//��������delay ��Խ����ʱ�䳤
								Thread.sleep(30+delay*2);
								//System.out.println("y:"+y);
							} catch (InterruptedException e) {
								// TODO �Զ����ɵ� catch ��
								e.printStackTrace();
							}
						}	
					}
				}
			}
		}
		
		
		
		
		
		
		private Vector<LoveDot> lovelist = new Vector<LoveDot>();
		private void makeLoveDot(int num){
			lovelist.clear();
			for(int i=0;i<num;i++){
				LoveDot ld = new LoveDot();
				int[] get = getRandom(0, w,0,h);
				ld.x = get[0];
				ld.y = get[1];
				lovelist.add(ld);
			}
		}
		
		
		
		
		
		
		
		class LoveDotThread extends Thread{
			
			public void run(){
				showAlldot();
				begin.countDown();
			}
			
			private void showAlldot(){
				boolean isrun = true;
				Paint paint = new Paint();
				Xfermode xFermode = new PorterDuffXfermode(Mode.DST_OVER);
				paint.setXfermode(xFermode);
				while(isrun && !isallstop){
					//��һ��С��
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					//Xfermode xFermode = new PorterDuffXfermode(Mode.DST_OVER);
					//paint.setXfermode(xFermode);
					synchronized (lovelist) {
						//С�ھ����
						if(lovelist.size()<dnum){						
								LoveDot ld = new LoveDot();
								int[] get = getRandom(0, w,0,h);
								ld.x = get[0];
								ld.y = get[1];
								
								lovelist.add(ld);					
						}
							int i = getRandom(0, lovelist.size()-1);
							LoveDot ld = lovelist.get(i);
							synchronized (holder) {
								Canvas c = null;
								Rect rt = null;							
								try {
									rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
									c = holder.lockCanvas(rt);															
									c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
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
								try {
									Thread.sleep(200);
								} catch (InterruptedException e1) {
									// TODO �Զ����ɵ� catch ��
									e1.printStackTrace();
								}
							synchronized (holder) {
								Canvas c = null;
								Rect rt = null;	
								try {
									paint.setAlpha((10-ld.num)*18);
									//����д������Ͳ㣬����Ӱ������
									//���ᣬ���ǻᱻ����
									//Xfermode xFermode = new PorterDuffXfermode(Mode.DST_ATOP);
									//paint.setXfermode(xFermode);
									rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
									c = holder.lockCanvas(rt);								
									c.drawBitmap(love_l,ld.x,ld.y,paint);
									
									
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
								ld.num++;
								if(ld.num>=10 && !isallstop){
									synchronized (holder) {
										Canvas c = null;
										Rect rt = null;	
									try {
										rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
										c = holder.lockCanvas(rt);								
										//c.drawColor(co);
										c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
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
									lovelist.remove(i);
								}
								
							}//sy
						//}//for
					}//sy
				}//while
			}
		
		
		
		
		
		
		private int getRandom(int begin,int end){
			return (int)Math.round(Math.random()*(end-begin)+begin);
		}
		
		
		
		
		
		//�õ������,����õ����������λ������
		//�õ�x��y;
			private int[] getRandom(int beginx ,int endx,int beginy, int endy){
				int getx = (int)Math.round(Math.random()*(endx-beginx)+beginx);
				int gety = (int)Math.round(Math.random()*(endy-beginy)+beginy);
				//��������Ů��Χ��
				if(getx <= beau_w  && gety <= beau_h){
					if(Math.random()>0.5)
						getx = getx+beau_w;
					else gety = gety+beau_h;
				}
				//���������������
				if(getx>=cx && getx <=cx+textsize)
					getx = cx+textsize+5;
				if(getx>=fx && getx <=fx+textsize)
					getx = fx+textsize+5;
				if(getx>=gx && getx <=gx+textsize)
					getx = gx+textsize+5;
				if(getx>=dx && getx <=dx+textsize)
					getx = dx+textsize+5;
				
				//�������
				if(gety>=ay-textsize && gety<=ay)
					gety = ay+5;
				if(gety>=by-textsize && gety<=by)
					gety = by+5;
				
				int[] get = new int[2];
				get[0] = getx;
				get[1] = gety;
				return get;
			}
		
		
		
	
			private int[] nextXY(int beginx,int beginy,int endx,int endy){
				int gotox,gotoy;
				int[] go = new int[2];
				if(beginx >= endx) gotox = beginx-1;
				else gotox = beginx+1;
				if(beginy >= endy) gotoy = beginy-1;
				else gotoy = beginy+1;
				go[0] = gotox;
				go[1] = gotoy;
				return go;
			}
			
			
			
			
			
			class LoveDot {
				private int x;
				private int y;
				private int num = 0;
				public LoveDot(){
					
				}
				public LoveDot(int x,int y,int num){
					this.x = x;
					this.y = y;
					this.num = num;
				}
			}
			
			
			//color
			
			class StringKind{
				String text;
				int or;
				int drawx;
				int drawy;
				int space;
				private StringKind(String text,int or,int drawx,int drawy,int space){
					this.text =text;
					this.or = or;
					this.drawx = drawx;
					this.drawy = drawy;
					this.space = space;
				}
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
