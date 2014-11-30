package com.screen.shots;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovingSurface extends SurfaceView implements SurfaceHolder.Callback {
    GameThread thread;
    int screenW; //Device's screen width.
    int screenH; //Devices's screen height.
    int initialY ;
    int bgrW;
    int bgrH;
    int bgrScroll;
    int dBgrY; //Background scroll speed.
    float acc;
    Bitmap  bgr, bgrReverse;
    boolean reverseBackroundFirst;
    

    //Measure frames per second.
    long now;
    int framesCount=0;
    int framesCountAvg=0;
    long framesTimer=0;
    Paint fpsPaint=new Paint();

    //Frame speed
    long timeNow;
    long timePrev = 0;
    long timePrevFrame = 0;
    long timeDelta;


    public MovingSurface(Context context) {
        super(context);
        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.comman_bg); //Load a background.
         //Create a flag for the onDraw method to alternate background with its mirror image.
        reverseBackroundFirst = true;

        //Initialise animation variables.
        acc = 0.2f; //Acceleration
        initialY = 100; //Initial vertical position
        bgrScroll = 0;  //Background scroll position
        dBgrY = 1; //Scrolling background speed

        fpsPaint.setTextSize(30);

        //Set thread
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.
        screenW = w;
        screenH = h;

        bgr = Bitmap.createScaledBitmap(bgr, w, h, true); //Scale background to fit the screen.
        bgrW = bgr.getWidth();
        bgrH = bgr.getHeight();

        //Create a mirror image of the background (horizontal flip) - for a more circular background.
        Matrix matrix = new Matrix();  //Like a frame or mould for an image.
        matrix.setScale(-1, 1); //Horizontal mirror effect.
        bgrReverse = Bitmap.createBitmap(bgr, 0, 0, bgrW, bgrH, matrix, true); //Create a new mirrored bitmap by applying the matrix.

    }

    //***************************************
    //*************  TOUCH  *****************
    //***************************************
    
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw scrolling background.
        Rect  toRect1 = new Rect(0, 0, bgrW - bgrScroll, bgrH);
        Rect fromRect1= new Rect(bgrScroll, 0, bgrW, bgrH);

        Rect   toRect2  = new Rect(bgrW - bgrScroll, 0, bgrW, bgrH);
        Rect fromRect2= new Rect(0, 0, bgrScroll, bgrH);

        if (!reverseBackroundFirst) {
            canvas.drawBitmap(bgr, fromRect1, toRect1, null);
            canvas.drawBitmap(bgrReverse, fromRect2, toRect2, null);
        }
        else{
            canvas.drawBitmap(bgr, fromRect2, toRect2, null);
            canvas.drawBitmap(bgrReverse, fromRect1, toRect1, null);
        }

        //Next value for the background's position.
        if ( (bgrScroll += dBgrY) >= bgrW) {
            bgrScroll = 0;
            reverseBackroundFirst = !reverseBackroundFirst;
        }

        

        //Measure frame rate (unit: frames per second).
         now=System.currentTimeMillis();
         canvas.drawText(framesCountAvg+" fps", 40, 70, fpsPaint);
         framesCount++;
         if(now-framesTimer>1000) {
                 framesTimer=now;
                 framesCountAvg=framesCount;
                 framesCount=0;
         }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }


    class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private MovingSurface gameView;
        private boolean run = false;

        public GameThread(SurfaceHolder surfaceHolder, MovingSurface gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setRunning(boolean run) {
            this.run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return surfaceHolder;
        }

        @SuppressLint("WrongCall")
		@Override
        public void run() {
            Canvas c;
            while (run) {
                c = null;

                //limit frame rate to max 60fps
                timeNow = System.currentTimeMillis();
                timeDelta = timeNow - timePrevFrame;
                if ( timeDelta < 16) {
                    try {
                        Thread.sleep(16 - timeDelta);
                    }
                    catch(InterruptedException e) {

                    }
                }
                timePrevFrame = System.currentTimeMillis();

                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                       //call methods to draw and process next fame
                        try {
							gameView.onDraw(c);
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}