package com.example.mapwithmarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class MyCanvas extends SurfaceView implements SurfaceHolder.Callback{
   Paint paint;
   Path path;
   Bitmap value;
   public MyCanvas(Context context) {
      super(context);
      init();
   }
   public MyCanvas(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
   }
   public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      init();
   }
   public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      init();
   }
   private void init() {
      paint = new Paint();
      path = new Path();
      paint.setAntiAlias(true);
      paint.setColor(Color.RED);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStyle(Paint.Style.STROKE);
      paint.setFilterBitmap(true);
      paint.setDither(true);
      paint.setStrokeWidth(5f);
      setBackgroundColor(Color.TRANSPARENT);
      setZOrderOnTop(true);
      getHolder().setFormat(PixelFormat.TRANSPARENT);
      getHolder().addCallback(this);
   }

   public void setBitmap(Bitmap bitmap) {
      value = bitmap;
      invalidate();
   }
   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      canvas.drawBitmap(Bitmap.createScaledBitmap(value, this.getWidth(), value.getHeight(),true), 0, 0,paint);
      canvas.drawPath(path, paint);
   }

   public boolean onTouchEvent(MotionEvent event) {
      float xPos = event.getX();
      float yPos = event.getY();
      switch (event.getAction()) {
         case MotionEvent.ACTION_DOWN:
            path.moveTo(xPos, yPos);
            return true;
         case MotionEvent.ACTION_MOVE:
            path.lineTo(xPos, yPos);
            break;
         case MotionEvent.ACTION_UP:
            break;
         default:
            return false;
      }
      invalidate();
      return true;
   }

   @Override
   public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

   }

   @Override
   public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

   }

   @Override
   public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

   }
}
