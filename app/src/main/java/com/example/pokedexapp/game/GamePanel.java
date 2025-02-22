package com.example.pokedexapp.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint pain = new Paint();
    private SurfaceHolder holder = getHolder();
    private List<Square> point = new ArrayList<>();

    public GamePanel(Context context) {
        super(context);
        holder.addCallback(this);
        pain.setColor(Color.RED);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        PointF pos = new PointF(400, 600);
        point.add(new Square(50, pos, Color.RED));
        render();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
       if(event.getAction() == MotionEvent.ACTION_DOWN){
           PointF pos = new PointF(event.getX(), event.getY());
           point.add(new Square(50, pos, Color.RED));
           render();
       }
        return true;
    }

    public void render(){
        Canvas canvas = holder.lockCanvas();
        for(Square s: point){
            s.addNewNode(canvas);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private class Square{
        int size;
        PointF pos;
        int color;
        private Square(int size, PointF pos, int color){
            this.color = color;
            this.size = size;
            this.pos = pos;
        }
        public void addNewNode(Canvas canvas){
            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawRect(pos.x, pos.y, pos.x + size, pos.y + size, paint);
        }
    }
}
