package com.example.user.game1;

/**
 * Created by User on 19.01.2018
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class PlatformView extends SurfaceView
        implements Runnable {
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;
    // For drawing
    private Paint paint;
    // Canvas could initially be local.
// But later we will use it outside of draw()
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;
    Timer timer;
    boolean cd_ok,cd_start;
    long cd_time;
    static int s_x,pixelx;
    static int s_y,pixely;
    private levelmanager levelmanager;
    private ArrayList<Terrain> tobjes;
    private ArrayList< GameCha> gos;
    private ArrayList< GameCha> engos;
    boolean playerturn;
    int cha_control;
    // Our new engine classes

    PlatformView(Context context, int screenWidth,
                 int screenHeight) {
        super(context);
        this.context = context;
// Initialize our drawing objects
        ourHolder = getHolder();
        cha_control=0;
        paint = new Paint();
        timer=new Timer();
        this.s_x=screenWidth;
        this.s_y=screenHeight;
        pixelx=screenWidth/20;
        pixely=screenHeight/10;
        levelmanager=new levelmanager();
        tobjes=new ArrayList<>();
        field_training map=new field_training();
        levelmanager.loadlevel(tobjes,map.map,context);
        gos=new ArrayList<>();
        engos=new ArrayList<>();
        gos.add(new cha_Warrior(context,9*PlatformView.pixelx,6*PlatformView.pixelx,10001,0));
        gos.get(cha_control).setStoping(true);
        playerturn=true;





    }
    @Override
    public void run() {
        while (running) {
            startFrameTime = System.currentTimeMillis();
            update();
            draw();
// Calculate the fps this frame
// We can then use the result to
// time animations and movement.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }
    private void update() {
// Our new update() code will go here
        for(GameCha g: gos){
            g.update();
        }

    }
    private void draw() {
        if (ourHolder.getSurface().isValid()){
//First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
// Rub out the last frame with arbitrary color
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 255, 255, 255));
            paint.setColor(Color.CYAN);
            canvas.drawText("pixelx:"+pixelx+"Xpixely"+pixely,s_x-5*pixelx,pixely,paint);
            paint.setColor(Color.BLACK);
            //draw terrains
            for( Terrain t:tobjes ){
                canvas.drawBitmap(t.getBitmap(),t.getP().x,t.getP().y,paint);
                if(t.getType()=="turf"){
                    paint.setColor(Color.BLUE);
                //üstline
                canvas.drawLine(t.getCollider().left,t.getCollider().top,t.getCollider().right,t.getCollider().top,paint);
                //altline
               // canvas.drawLine(t.getCollider().left,t.getCollider().bottom,t.getCollider().right,t.getCollider().bottom,paint);
                //sagline
                canvas.drawLine(t.getCollider().right,t.getCollider().top,t.getCollider().right,t.getCollider().bottom,paint);
                //solline
                canvas.drawLine(t.getCollider().left,t.getCollider().top,t.getCollider().left,t.getCollider().bottom,paint);
               // canvas.drawRect(t.getCollider(),paint);
            }//end if
            }
            for(GameCha g: gos){
                canvas.drawBitmap(g.getB(),g.getP().x,g.getP().y,paint);
            }
            if(!timer.cd_start)
                timer.set_timer(1000);

            if(timer.finished())
            canvas.drawText("finished",200,400,paint);
            else{
                canvas.drawText(""+timer.get_rem_time(),200,400,paint);
            }

// New drawing code will go here
// Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    // Clean up our thread if the game is interrupted
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }
    // Make a new thread and start it
// Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        int X = (int)event.getX();
        int Y = (int)event.getY();
        switch (eventaction ) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}// End of PlatformView

