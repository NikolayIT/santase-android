package com.karamanov.santase.screen.main;

import javax.microedition.khronos.opengles.GL;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

public final class SantaseCanvas extends Canvas {

    private final Object _lock;

    private final int width;

    private final int height;

    public SantaseCanvas(Bitmap bitmap, int width, int height, Object _lock) {
        super(bitmap);
        this._lock = _lock;
        this.width = width;
        this.height = height;
    }

    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
    }

    public void setViewport(int width, int height) {
        super.setViewport(width, height);
    }

    public boolean isOpaque() {
        return super.isOpaque();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int save() {
        return super.save();
    }

    public int save(int saveFlags) {
        return super.save(saveFlags);
    }

    public int saveLayer(RectF bounds, Paint paint, int saveFlags) {
        return super.saveLayer(bounds, paint, saveFlags);
    }

    public int saveLayer(float left, float top, float right, float bottom, Paint paint, int saveFlags) {
        return super.saveLayer(left, top, right, bottom, paint, saveFlags);
    }

    public int saveLayerAlpha(RectF bounds, int alpha, int saveFlags) {
        return super.saveLayerAlpha(bounds, alpha, saveFlags);
    }

    public int saveLayerAlpha(float left, float top, float right, float bottom, int alpha, int saveFlags) {
        return super.saveLayerAlpha(left, top, right, bottom, alpha, saveFlags);
    }

    public void restore() {
        super.restore();
    }

    public int getSaveCount() {
        return super.getSaveCount();
    }

    public void restoreToCount(int saveCount) {
        super.restoreToCount(saveCount);
    }

    public void translate(float dx, float dy) {
        super.translate(dx, dy);
    }

    public void scale(float sx, float sy) {
        super.scale(sx, sy);
    }

    public void rotate(float degrees) {
        super.rotate(degrees);
    }

    public void skew(float sx, float sy) {
        super.skew(sx, sy);
    }

    public void concat(Matrix matrix) {
        super.concat(matrix);
    }

    public void setMatrix(Matrix matrix) {
        super.setMatrix(matrix);
    }

    public void getMatrix(Matrix ctm) {
        super.getMatrix(ctm);
    }

    public boolean clipRect(RectF rect, Region.Op op) {
        return super.clipRect(rect, op);
    }

    public boolean clipRect(Rect rect, Region.Op op) {
        return super.clipRect(rect, op);
    }

    public boolean clipRect(RectF rect) {
        return super.clipRect(rect);
    }

    public boolean clipRect(Rect rect) {
        return super.clipRect(rect);
    }

    public boolean clipRect(float left, float top, float right, float bottom, Region.Op op) {
        return super.clipRect(left, top, right, bottom, op);
    }

    public boolean clipRect(float left, float top, float right, float bottom) {
        return super.clipRect(left, top, right, bottom);
    }

    public boolean clipRect(int left, int top, int right, int bottom) {
        return super.clipRect(left, top, right, bottom);
    }

    public boolean clipPath(Path path, Region.Op op) {
        return super.clipPath(path, op);
    }

    public boolean clipPath(Path path) {
        return super.clipPath(path);
    }

    public boolean clipRegion(Region region, Region.Op op) {
        return super.clipRegion(region, op);
    }

    public boolean clipRegion(Region region) {
        return super.clipRegion(region);
    }

    public DrawFilter getDrawFilter() {
        return super.getDrawFilter();
    }

    public void setDrawFilter(DrawFilter filter) {
        super.setDrawFilter(filter);
    }

    public GL getGL() {
        return super.getGL();
    }

    public boolean quickReject(RectF rect, EdgeType type) {
        return super.quickReject(rect, type);
    }

    public boolean quickReject(Path path, EdgeType type) {
        return super.quickReject(path, type);
    }

    public boolean quickReject(float left, float top, float right, float bottom, EdgeType type) {
        return super.quickReject(left, top, right, bottom, type);
    }

    public boolean getClipBounds(Rect bounds) {
        return super.getClipBounds(bounds);
    }

    public void drawRGB(int r, int g, int b) {
        synchronized (_lock) {
            super.drawRGB(r, g, b);
        }
    }

    public void drawARGB(int a, int r, int g, int b) {
        synchronized (_lock) {
            super.drawARGB(a, r, g, b);
        }
    }

    public void drawColor(int color) {
        synchronized (_lock) {
            super.drawColor(color);
        }
    }

    public void drawColor(int color, PorterDuff.Mode mode) {
        synchronized (_lock) {
            super.drawColor(color, mode);
        }
    }

    public void drawPaint(Paint paint) {
        synchronized (_lock) {
            super.drawPaint(paint);
        }
    }

    public void drawPoints(float[] pts, int offset, int count, Paint paint) {
        synchronized (_lock) {
            super.drawPoints(pts, offset, count, paint);
        }
    }

    public void drawPoints(float[] pts, Paint paint) {
        synchronized (_lock) {
            super.drawPoints(pts, paint);
        }
    }

    public void drawPoint(float x, float y, Paint paint) {
        synchronized (_lock) {
            super.drawPoint(x, y, paint);
        }
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        synchronized (_lock) {

            super.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    public void drawLines(float[] pts, int offset, int count, Paint paint) {
        synchronized (_lock) {

            super.drawLines(pts, offset, count, paint);
        }
    }

    public void drawLines(float[] pts, Paint paint) {

        synchronized (_lock) {
            super.drawLines(pts, paint);
        }
    }

    public void drawRect(RectF rect, Paint paint) {
        synchronized (_lock) {
            super.drawRect(rect, paint);
        }
    }

    public void drawRect(Rect r, Paint paint) {
        synchronized (_lock) {
            super.drawRect(r, paint);
        }
    }

    public void drawRect(float left, float top, float right, float bottom, Paint paint) {
        synchronized (_lock) {
            super.drawRect(left, top, right, bottom, paint);
        }
    }

    public void drawOval(RectF oval, Paint paint) {
        synchronized (_lock) {
            super.drawOval(oval, paint);
        }
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        synchronized (_lock) {
            super.drawCircle(cx, cy, radius, paint);
        }
    }

    public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        synchronized (_lock) {
            super.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
        }
    }

    public void drawRoundRect(RectF rect, float rx, float ry, Paint paint) {
        synchronized (_lock) {
            super.drawRoundRect(rect, rx, ry, paint);
        }
    }

    public void drawPath(Path path, Paint paint) {
        synchronized (_lock) {
            super.drawPath(path, paint);
        }
    }

    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        synchronized (_lock) {
            super.drawBitmap(bitmap, left, top, paint);
        }
    }

    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
        synchronized (_lock) {
            super.drawBitmap(bitmap, src, dst, paint);
        }
    }

    public void drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        synchronized (_lock) {
            super.drawBitmap(bitmap, src, dst, paint);
        }
    }

    public void drawBitmap(int[] colors, int offset, int stride, int x, int y, int width, int height, boolean hasAlpha, Paint paint) {
        synchronized (_lock) {
            super.drawBitmap(colors, offset, stride, x, y, width, height, hasAlpha, paint);
        }
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        synchronized (_lock) {
            super.drawBitmap(bitmap, matrix, paint);
        }
    }

    public void drawBitmapMesh(Bitmap bitmap, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, Paint paint) {
        synchronized (_lock) {
            super.drawBitmapMesh(bitmap, meshWidth, meshHeight, verts, vertOffset, colors, colorOffset, paint);
        }
    }

    public void drawVertices(VertexMode mode, int vertexCount, float[] verts, int vertOffset, float[] texs, int texOffset, int[] colors, int colorOffset,
            short[] indices, int indexOffset, int indexCount, Paint paint) {
        synchronized (_lock) {
            super.drawVertices(mode, vertexCount, verts, vertOffset, texs, texOffset, colors, colorOffset, indices, indexOffset, indexCount, paint);
        }
    }

    public void drawText(char[] text, int index, int count, float x, float y, Paint paint) {
        synchronized (_lock) {
            super.drawText(text, index, count, x, y, paint);
        }
    }

    public void drawText(String text, float x, float y, Paint paint) {
        synchronized (_lock) {
            super.drawText(text, x, y, paint);
        }
    }

    public void drawText(String text, int start, int end, float x, float y, Paint paint) {
        synchronized (_lock) {
            super.drawText(text, start, end, x, y, paint);
        }
    }

    public void drawText(CharSequence text, int start, int end, float x, float y, Paint paint) {
        synchronized (_lock) {
            super.drawText(text, start, end, x, y, paint);
        }
    }

    public void drawPosText(char[] text, int index, int count, float[] pos, Paint paint) {
        synchronized (_lock) {
            super.drawPosText(text, index, count, pos, paint);
        }
    }

    public void drawPosText(String text, float[] pos, Paint paint) {
        synchronized (_lock) {
            super.drawPosText(text, pos, paint);
        }
    }

    public void drawTextOnPath(char[] text, int index, int count, Path path, float hOffset, float vOffset, Paint paint) {
        synchronized (_lock) {
            super.drawTextOnPath(text, index, count, path, hOffset, vOffset, paint);
        }
    }

    public void drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint) {
        synchronized (_lock) {
            super.drawTextOnPath(text, path, hOffset, vOffset, paint);
        }
    }

    public void drawPicture(Picture picture) {
        synchronized (_lock) {
            super.drawPicture(picture);
        }
    }

    public void drawPicture(Picture picture, RectF dst) {
        synchronized (_lock) {
            super.drawPicture(picture, dst);
        }
    }

    public void drawPicture(Picture picture, Rect dst) {
        synchronized (_lock) {
            super.drawPicture(picture, dst);
        }
    }
}
