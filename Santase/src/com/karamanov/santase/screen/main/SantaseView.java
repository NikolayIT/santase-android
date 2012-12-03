package com.karamanov.santase.screen.main;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.karamanov.framework.message.Message;
import com.karamanov.santase.Santase;

public final class SantaseView extends SurfaceView implements SurfaceHolder.Callback {

    private boolean active = false;

    private final SantaseActivity activity;

    private final HashMap<Point, Bitmap> bitmaps = new HashMap<Point, Bitmap>();

    private final Paint mSmooth;

    private int oldWidth;

    private int oldHeight;

    public SantaseView(SantaseActivity context) {
        super(context);
        this.activity = context;

        getHolder().addCallback(this);

        mSmooth = new Paint(Paint.FILTER_BITMAP_FLAG);
        mSmooth.setAntiAlias(true);
        mSmooth.setDither(true);
    }

    private Bitmap getBufferBitmap(int width, int height) {
        Bitmap bitmap = null;
        Point point = new Point(width, height);
        bitmap = bitmaps.get(point);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmaps.put(point, bitmap);
        }
        return bitmap;
    }

    public final Canvas getBufferedCanvas() {
        Canvas canvas = null;

        if (oldWidth > 0 && oldHeight > 0) {
            canvas = new Canvas(getBufferBitmap(oldWidth, oldHeight));
        }
        return canvas;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (oldWidth != width || oldHeight != height) {
            oldWidth = width;
            oldHeight = height;

            activity.repaint();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        active = true;
        activity.repaint();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void refresh() {
        if (active) {
            Canvas canvas = null;
            try {
                canvas = getHolder().lockCanvas(null);
                if (canvas != null) {
                    canvas.drawBitmap(getBufferBitmap(canvas.getWidth(), canvas.getHeight()), 0, 0, mSmooth);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (canvas != null) {
                    getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Message tMessage = new Message(Santase.MT_TOUCH_EVENT, new PointF(event.getX(), event.getY()));
            activity.triggerMessage(tMessage);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
