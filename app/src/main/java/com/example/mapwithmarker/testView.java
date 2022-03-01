package com.example.mapwithmarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class testView extends View {
   Paint paint;
   Path path;
   Canvas canvas;
   Bitmap value;
   public testView(Context context, AttributeSet attrs) {
      super(context, attrs);
      paint = new Paint();
      path = new Path();
      paint.setAntiAlias(true);
      paint.setColor(Color.RED);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStyle(Paint.Style.STROKE);
      paint.setFilterBitmap(true);
      paint.setDither(true);
      paint.setStrokeWidth(5f);
   }

   public void setBitmap(Bitmap bitmap) {
      value = bitmap;
   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      canvas.drawColor(Color.BLUE);
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

}