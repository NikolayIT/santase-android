package com.karamanov.santase.screen.logo;

import game.beans.pack.Pack;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.karamanov.framework.graphics.ImageUtil;
import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
import com.karamanov.santase.screen.base.SantasePainter;

class LogoView extends View {

    /**
     * Desk image.
     */
    private final Bitmap desk;

    /**
     * Card width.
     */
    private final int CardWidth;

    /**
     * Card height.
     */
    private final int CardHeight;

    public LogoView(Context context) {
        super(context);

        Drawable d = getResources().getDrawable(R.drawable.card_back);
        desk = ((BitmapDrawable) d).getBitmap();

        CardWidth = desk.getWidth();
        CardHeight = desk.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBufferedImage(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
    }

    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL);

        GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.score_bkg);
        gradientDrawable.setBounds(0, 0, getWidth(), getHeight());
        gradientDrawable.draw(canvas);
    }

    private void drawPack(Canvas canvas) {
        final Pack pack = Pack.createFullPack();
        pack.arrange();

        int visiblePart = (getWidth() - 2 - CardWidth) / (pack.getSize() - 1);
        int x = (getWidth() - 2 - (pack.getSize() - 1) * visiblePart - CardWidth) / 2;
        int y = (getHeight() - CardHeight) / 2;

        SantasePainter bp = new SantasePainter(getContext());
        // draw all cards
        for (int i = 0; i < pack.getSize(); i++) {
            final Card card = pack.getCard(i);
            bp.drawCard(card, canvas, x + i * visiblePart + 1, y);
        }
    }

    private void drawAutor(Canvas canvas) {
        int dip14 = Santase.fromPixelToDip(getContext(), 14);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.rgb(253, 241, 213));
        paint.setTextSize(dip14);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SERIF);
        Rect bounds = new Rect();
        String autor = getResources().getString(R.string.Autor);
        paint.getTextBounds(autor, 0, autor.length(), bounds);

        int x = (getWidth() - bounds.width()) / 2;
        int y = 2 * getHeight() / 3 + (getHeight() / 3 - bounds.height() * 2 - bounds.height()) / 2;
        canvas.drawText(autor, x, y, paint);
        y += bounds.bottom;

        // draw email;
        bounds = new Rect();
        String email = getResources().getString(R.string.Email);
        paint.getTextBounds(email, 0, email.length(), bounds);
        x = (getWidth() - bounds.width()) / 2;
        y += bounds.height();
        canvas.drawText(email, x, y, paint);
        y += bounds.bottom;
        // draw Copyright
        bounds = new Rect();
        String copyRight = getResources().getString(R.string.Copyright);
        paint.getTextBounds(copyRight, 0, copyRight.length(), bounds);
        x = (getWidth() - bounds.width()) / 2;
        y += bounds.height();
        canvas.drawText(copyRight, x, y, paint);
        y += bounds.bottom;
        // draw Version
        bounds = new Rect();

        String systemVersion;
        try {
            systemVersion = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            systemVersion = getResources().getString(R.string.NotAvailableShort);
        }
        String version = getResources().getString(R.string.Version) + " " + systemVersion;

        paint.getTextBounds(version, 0, version.length(), bounds);
        x = (getWidth() - bounds.width()) / 2;
        y += bounds.height();
        canvas.drawText(version, x, y, paint);
    }

    private void drawSuitsCorners(Canvas canvas) {
        SantasePainter santasePainter = new SantasePainter(getContext());
        Paint paint = new Paint();

        int dip5 = Santase.fromPixelToDip(getContext(), 5);

        Bitmap b = santasePainter.getSuitImage(Suit.Spade);
        canvas.drawBitmap(b, dip5, dip5, paint);
        b = santasePainter.getSuitImage(Suit.Heart);
        canvas.drawBitmap(b, getWidth() - dip5 - b.getWidth(), dip5, paint);
        b = santasePainter.getSuitImage(Suit.Diamond);
        canvas.drawBitmap(b, dip5, getHeight() - dip5 - b.getHeight(), paint);
        b = santasePainter.getSuitImage(Suit.Club);
        b = ImageUtil.transformToDisabledImage(b);
        canvas.drawBitmap(b, getWidth() - dip5 - b.getWidth(), getHeight() - dip5 - b.getHeight(), paint);
    }

    private float getTextWidth(Paint paint, String text) {
        float result = 0;
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);

        for (float f : widths) {
            result += f;
        }

        return result;
    }

    private void drawSantaseText(Canvas canvas) {
        int dip5 = Santase.fromPixelToDip(getContext(), 5);
        int dip64 = Santase.fromPixelToDip(getContext(), 64);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(dip64);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setTypeface(Typeface.SERIF);
        paint.setTextAlign(Paint.Align.LEFT);

        Rect rect = new Rect();
        String santase = getContext().getString(R.string.santase);
        paint.getTextBounds(santase, 0, santase.length(), rect);

        int santaseWidth = Math.round(getTextWidth(paint, santase));

        Bitmap gradientBitmap = Bitmap.createBitmap(santaseWidth, rect.height(), Bitmap.Config.ARGB_8888);
        Canvas gradientCanvas = new Canvas(gradientBitmap);
        int colors[] = { Color.YELLOW, Color.RED };
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawable.setBounds(0, 0, santaseWidth, rect.height());
        gradientDrawable.draw(gradientCanvas);

        Bitmap textBitmap = Bitmap.createBitmap(santaseWidth, rect.height(), Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawText(santase, 0, rect.height() - rect.bottom, paint);

        Bitmap bitmapXfermode = Bitmap.createBitmap(santaseWidth, rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvasXfermode = new Canvas(bitmapXfermode);

        canvasXfermode.drawBitmap(gradientBitmap, 0, 0, paint);
        Paint paintXfermode = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintXfermode.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvasXfermode.drawBitmap(textBitmap, 0, 0, paintXfermode);

        paint.setColor(Color.BLACK);
        int pictureTop = ((getHeight() - CardHeight) / 2 - bitmapXfermode.getHeight()) / 2;
        int pictureLeft = (getWidth() - bitmapXfermode.getWidth()) / 2;
        canvas.drawText(santase, pictureLeft + dip5, pictureTop + dip5 + rect.height() - rect.bottom, paint);
        canvas.drawBitmap(bitmapXfermode, pictureLeft, pictureTop, paint);
    }

    private void drawBufferedImage(Canvas canvas) {
        drawBackground(canvas);
        drawPack(canvas);
        drawAutor(canvas);
        drawSuitsCorners(canvas);
        drawSantaseText(canvas);
    }
}