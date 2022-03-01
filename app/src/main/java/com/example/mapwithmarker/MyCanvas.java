package com.example.mapwithmarker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MyCanvas extends SurfaceView implements SurfaceHolder.Callback{
   Paint paint;
   Path path;
   Bitmap value;
   Bitmap bmpBase;
   Canvas canvas;
   ContentValues cv = new ContentValues();
   FileOutputStream outputStream;
   Uri imageUri;

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
      setDrawingCacheEnabled(true);
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
   public String finishEdit(Context context) throws FileNotFoundException {
      ContentResolver resolver = context.getContentResolver();
      try {
         cv.put(MediaStore.MediaColumns.DISPLAY_NAME, SystemClock.currentThreadTimeMillis() + ".jpeg");
         cv.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
         cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
         imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

         outputStream = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));
         Bitmap temp = this.getDrawingCache();
         temp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
         Objects.requireNonNull(outputStream);
         Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();

      } catch (IOException e) {
         Toast.makeText(context, "Image not saved " + e.getMessage(), Toast.LENGTH_SHORT).show();
      }
      return imageUri.toString();
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
