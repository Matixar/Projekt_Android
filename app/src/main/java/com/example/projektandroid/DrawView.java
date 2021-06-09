package com.example.projektandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

public class DrawView extends View {

    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paint;
    protected boolean isTurned = false;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, Bitmap mBitmap) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, Bitmap mBitmap) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, Bitmap mBitmap) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Bitmap mBitmap) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // jeżeli bitmapa nie jest właśnie przywracana po obrocie - utwórz nową bitmapę o wymiarach dopasowanych do nowego widoku
        if (!isTurned) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            // w innym wypadku ustaw bitmapę na podstawie przeskalowanej do nowego wymiaru bitmapy pomocniczej
            Bitmap bmp = getResizedBitmap(bitmap, w, h);
            bitmap = bmp;
            isTurned = false;
        }
        // utwórz kanwę na podstawie bitmapy
        canvas = new Canvas(bitmap);
    }

    public Bitmap getResizedBitmap(Bitmap _bitmap, int newWidth, int newHeight) {

        int width = _bitmap.getWidth();
        int height = _bitmap.getHeight();

        // oblicz współczynniki skalowania x,y
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // utwórz macierz
        Matrix matrix = new Matrix();

        // wprowadź do macierzy współczynniki skalowania x,y
        matrix.postScale(scaleWidth, scaleHeight);

        // utwórz nową, przeskalowaną na podstawie macierzy, bitmapę
        Bitmap resizedBitmap = Bitmap.createBitmap(
                _bitmap, 0, 0, width, height, matrix, false);
        _bitmap.recycle();
        return resizedBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paint);
    }

    private void resetPathAndDrawCircle(float x, float y) {
        path.reset();
        canvas.drawCircle(x, y, 10, paint);
    }

    private void drawLine(float x, float y) {
        path.lineTo(x, y);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // w zależności od rodzaju eventu (początek/koniec dotknięcia lub przesuwanie palca po ekranie)
        switch (event.getAction()) {
            // początek dotknięcia
            case MotionEvent.ACTION_DOWN:
                resetPathAndDrawCircle(event.getX(), event.getY());
                path.moveTo(event.getX(), event.getY()); //ustal współrzędne początkowe ściężki na miejsce dotknięcia
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                // rysuj linię po ścieżce ruchu palca
                drawLine(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // po oderwaniu palca od ekranu - zresetuj współrzędne ścieżki oraz narysuj okrąg
                resetPathAndDrawCircle(event.getX(), event.getY());
                invalidate();
                break;
        }
        return true;
    }

    public void setPaintColor(String color) {
        switch (color) {
            case "red":
                paint.setColor(Color.RED);
                break;
            case "blue":
                paint.setColor(Color.BLUE);
                break;
            case "black":
                paint.setColor(Color.BLACK);
                break;
            case "green":
                paint.setColor(Color.GREEN);
                break;
        }

    }

    public void clearScreen() {
        setDrawingCacheEnabled(false);
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        invalidate();
        setDrawingCacheEnabled(true);
    }

    public Bitmap getmBitmap() {
        return bitmap;
    }

    public void setmBitmap(Bitmap _Bitmap) {
        this.bitmap = _Bitmap;
    }
}
