/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.base;

import game.beans.Game;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.view.View;

import com.karamanov.framework.graphics.Color;
import com.karamanov.framework.graphics.ImageUtil;
import com.karamanov.framework.graphics.Rectangle;
import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
import com.karamanov.santase.screen.main.SantaseView;

/**
 * BelotPainter class.
 * @author Dimitar Karamanov
 */
public final class SantasePainter extends BasePainter {

    private int SPACE = Santase.fromPixelToDip(getContext(), 4);

    private static final int PLAYER_CARD_COUNT = 6;

    private int PlayerCardsOverlapped = Santase.fromPixelToDip(getContext(), 7);

    /**
     * Constructor.
     * @param width canvas width.
     * @param height canvas height.
     */
    public SantasePainter(Context context) {
        super(context);
    }

    /**
     * Returns card width.
     * @return int.
     */
    protected int getCardWidth() {
        return cardWidth;
    }

    /**
     * Returns card height.
     * @return int.
     */
    public int getCardHeight() {
        return cardHeight;
    }

    /**
     * Draw card to the canvas.
     * @param card which image is retrieve.
     * @param g - graphics object.
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    public void drawCard(final Card card, final Canvas canvas, final int x, final int y) {
        Bitmap b = pictureDecorator.getCardImage(card);
        canvas.drawBitmap(b, x, y, null);
    }

    /**
     * Draw card darkened to the canvas.
     * @param card which image is retrieve.
     * @param g - graphics object.
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    private void drawDarkenedCard(final Card card, final Canvas canvas, final int x, final int y) {
        Bitmap picture = pictureDecorator.getCardImage(card);
        Bitmap b = ImageUtil.transformToDarkenedImage(picture);
        canvas.drawBitmap(b, x, y, new Paint());
    }

    /**
     * Draw card mixed with color to the canvas.
     * @param card which image is retrieve.
     * @param g - graphics object.
     * @param x - x coordinate.
     * @param y - y coordinate.
     * @param mixedColor used to transform the image.
     */
    public void drawMixedColorCard(final Card card, final Canvas canvas, final int x, final int y, final Color mixedColor) {
        Bitmap picture = pictureDecorator.getCardImage(card);
        final Rectangle rec = new Rectangle(0, 0, picture.getWidth(), picture.getHeight());
        Bitmap b = ImageUtil.transformToMixedColorImage(picture, mixedColor, rec);
        canvas.drawBitmap(b, x, y, new Paint());
    }

    /**
     * Returns suit's image.
     * @param suit which image is retrieved.
     * @return Image suit's image.
     */
    public final Bitmap getSuitImage(final Suit suit) {
        return pictureDecorator.getSuitImage(suit);
    }

    private int getFirstCardPosX(Canvas canvas) {
        return (canvas.getWidth() - PLAYER_CARD_COUNT * cardBackWidth - (PLAYER_CARD_COUNT - 1) * SPACE) / 2;
    }

    private int getFirstCardPosXX(View view) {
        if (PLAYER_CARD_COUNT * cardWidth > (view.getWidth() - 2)) {
            double d = (double) (PLAYER_CARD_COUNT * cardWidth - view.getWidth() + 2) / (PLAYER_CARD_COUNT - 1);
            PlayerCardsOverlapped = (int) Math.ceil(d);
        } else {
            PlayerCardsOverlapped = 0;
        }

        return (view.getWidth() - PLAYER_CARD_COUNT * cardWidth + (PLAYER_CARD_COUNT - 1) * PlayerCardsOverlapped) / 2 + 1;
    }

    private int getMiddleCardY(View view) {
        return (view.getHeight() - cardHeight) / 2;
    }

    public Rectangle getTrumpCardRectangle(View view) {
        final int x = getFirstCardPosXX(view);
        final int y = getMiddleCardY(view);
        return new Rectangle(x, y, cardWidth, cardHeight);
    }

    public Rectangle getCloseCardRectangle(View view) {
        final int x = getFirstCardPosXX(view) + cardWidth - PlayerCardsOverlapped;
        final int y = getMiddleCardY(view);
        return new Rectangle(x, y, cardWidth, cardHeight);
    }

    public Rectangle getPlayerCardRectangle(Game game, final int index, View view) {
        int x = getFirstCardPosXX(view) + index * (cardWidth - PlayerCardsOverlapped);
        int y = view.getHeight() - (int) new TextPaint().getTextSize() - cardHeight;
        if (index == PLAYER_CARD_COUNT - 1 || index == game.getHuman().getCards().getSize() - 1) {
            return new Rectangle(x, y, cardWidth, cardHeight);
        }
        return new Rectangle(x, y, cardWidth - PlayerCardsOverlapped, cardHeight);
    }

    private void drawPlayedCards(Canvas g, View view, Game game) {
        final int x1 = getFirstCardPosXX(view) + 3 * (cardWidth - PlayerCardsOverlapped);
        final int x2 = x1 + cardWidth + SPACE;
        final int y = getMiddleCardY(view);

        drawCardOrEmpty(game.getHuman().getPlayedCard(), g, x1, y);
        drawCardOrEmpty(game.getComputer().getPlayedCard(), g, x2, y);
    }

    private void drawCardOrEmpty(Card card, Canvas g, final int x, final int y) {
        if (card == null) {
            drawEmptyImage(g, x, y);
        } else {
            drawCard(card, g, x, y);
        }
    }

    private void drawEmptyImage(Canvas g, final int x, final int y) {
        // g.drawBitmap(pictureDecorator.getEmpty(), x, y, null);
    }

    public void drawGame(Canvas graphics, Game game, SantaseView view, long delay) {
        drawTable(graphics);
        drawScore(graphics, game);
        drawCompCards(graphics, game, view, delay);
        drawTrumpCard(graphics, view, game);
        drawPlayedCards(graphics, view, game);
        drawPlayerCards(graphics, game, view, game.getHuman().getSelectedCard(), delay);
    }

    private void drawPlayerCards(Canvas g, Game game, SantaseView view, Card PlayerSelectedCard, long delay) {
        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            Rectangle rec = getPlayerCardRectangle(game, i, view);

            if (i < game.getHuman().getCards().getSize()) {
                Card card = game.getHuman().getCards().getCard(i);
                drawCard(card, g, rec.x, rec.y);
                if (card.equals(PlayerSelectedCard)) {
                    drawDarkenedCard(card, g, rec.x, rec.y);
                }
            } else {
                drawEmptyImage(g, rec.x, rec.y);
            }

            if (delay > 0) {
                view.refresh();
                sleep(delay);
            }
        }
    }

    private void drawCompCards(Canvas graphics, Game game, SantaseView view, long delay) {
        final int fx = getFirstCardPosX(graphics);

        Paint p = new Paint();
        Rect bounds = new Rect();
        p.getTextBounds("|", 0, 1, bounds);
        final int y = bounds.height();

        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            final int x = fx + i * (cardBackWidth + SPACE);
            if (i < game.getComputer().getCards().getSize()) {
                drawDeskImage(graphics, x, y);
            } else {
                // drawEmptyImageSmall(g, x, y);
            }

            if (delay > 0) {
                view.refresh();
                sleep(delay);
            }
        }
    }

    /**
     * Sleeps for provided millisecond.
     * @param ms provided millisecond.
     */
    public final void sleep(final long ms) {
        if (ms > 0) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ex) {
                // ex.printStackTrace();
            }
        }
    }

    private void drawTrumpCard(Canvas graphics, View view, Game game) {
        Rectangle rec = getTrumpCardRectangle(view);

        if (!game.getGameCards().isEmpty() && game.isNotClosedGame()) {
            Card card = game.getGameCards().getCard(game.getGameCards().getSize() - 1);
            drawCard(card, graphics, rec.x, rec.y);
            drawDeskImageLarge(graphics, rec.x + cardWidth - PlayerCardsOverlapped, rec.y);
        } else {
            if (game.getTrumpSuit() != null) {
                Paint p = new Paint();
                p.setColor(Color.clDarkGreen.getRGB());

                int dip2 = Santase.fromPixelToDip(getContext(), 2);

                int xo;
                int yo;
                if (game.isClosedGame()) {
                    xo = rec.x + dip2;
                    yo = rec.y + dip2;
                } else {
                    xo = rec.x + (cardWidth - getSuitImage(game.getTrumpSuit()).getWidth()) / 2;
                    yo = rec.y + (cardHeight - getSuitImage(game.getTrumpSuit()).getHeight()) / 2;
                }

                graphics.drawBitmap(pictureDecorator.getMask(), rec.x, rec.y, null);
                graphics.drawBitmap(getSuitImage(game.getTrumpSuit()), xo, yo, null);

                drawCloseString(graphics, rec.x, rec.y, game);
            }
        }
    }

    private void drawCloseString(Canvas canvas, int x, int y, Game game) {
        if (game.isNotClosedGame()) {
            return;
        }

        Context context = getContext();

        Drawable player;
        if (game.isPlayerClosed(game.getHuman())) {
            player = context.getResources().getDrawable(R.drawable.human);
        } else {
            player = context.getResources().getDrawable(R.drawable.android);
        }

        int px = x + (cardWidth - player.getMinimumWidth()) / 2;
        int py = y + (cardHeight - player.getMinimumHeight()) / 2;

        player.setBounds(px, py, player.getMinimumWidth() + px, player.getMinimumHeight() + py);
        player.draw(canvas);

        Drawable closed = context.getResources().getDrawable(R.drawable.closed);

        int cx = x + (cardWidth - closed.getMinimumWidth()) / 2;
        int cy = y + (cardHeight - closed.getMinimumHeight());

        closed.setBounds(cx, cy, closed.getMinimumWidth() + cx, closed.getMinimumHeight() + cy);
        closed.draw(canvas);
    }

    private void drawTable(Canvas graphics) {
        GradientDrawable bkg = pictureDecorator.getMainBKG();
        bkg.setBounds(new Rect(0, 0, graphics.getWidth(), graphics.getHeight()));
        bkg.draw(graphics);
    }

    private int getPointsColor(int points) {
        if (points == 0) {
            return Color.clGreen.getRGB();
        }
        if (points < Game.POINTS_ZONE) {
            return Color.clLightGreen.getRGB();
        }
        if (points < Game.END_GAME_POINTS) {
            return Color.clLightRed.getRGB();
        }
        return Color.clRed.getRGB();
    }

    private void drawScore(Canvas graphics, Game game) {
        int x = 1;
        int y = 0;
        Paint p = new Paint();
        p.setColor(Color.clLightYellow.getRGB());
        Context context = getContext();
        String str = context.getString(R.string.Points);
        graphics.drawText(str, x, y + p.getTextSize(), p);

        int dip2 = Santase.fromPixelToDip(getContext(), 2);

        y = y + Math.round(p.getTextSize()) + dip2;

        Drawable andro = context.getResources().getDrawable(R.drawable.android_small);
        Drawable human = context.getResources().getDrawable(R.drawable.human_small);

        int maxWidth = Math.max(andro.getMinimumWidth(), human.getMinimumWidth());

        int d = (maxWidth - andro.getMinimumWidth()) / 2;
        andro.setBounds(x + d, y, x + d + andro.getMinimumWidth(), y + andro.getMinimumHeight());
        andro.draw(graphics);

        String scoreMask = "000";
        Rect bounds = new Rect();
        p.getTextBounds(scoreMask, 0, scoreMask.length(), bounds);
        Rect pointsBounds = new Rect();
        String compPoints = String.valueOf(game.getComputer().getPoints(game.getTrumpSuit()));
        p.getTextBounds(compPoints, 0, compPoints.length(), pointsBounds);
        p.setColor(getPointsColor(game.getComputer().getPoints(game.getTrumpSuit())));
        graphics.drawText(compPoints, x + maxWidth + (bounds.width() - pointsBounds.width()), y + pointsBounds.height()
                + (andro.getMinimumHeight() - pointsBounds.height()) / 2, p);

        y = y + andro.getMinimumHeight() + dip2;

        d = (maxWidth - human.getMinimumWidth()) / 2;
        human.setBounds(x + d, y, x + d + human.getMinimumWidth(), y + human.getMinimumHeight());
        human.draw(graphics);

        String humanPoints = String.valueOf(game.getHuman().getPoints(game.getTrumpSuit())); // Refactored !!!
        p.getTextBounds(humanPoints, 0, humanPoints.length(), pointsBounds);
        p.setColor(getPointsColor(game.getHuman().getPoints(game.getTrumpSuit())));
        graphics.drawText(humanPoints, x + maxWidth + (bounds.width() - pointsBounds.width()), y + pointsBounds.height()
                + (human.getMinimumHeight() - pointsBounds.height()) / 2, p);

        // SCORE
        y = y + human.getMinimumHeight() + dip2;
        p.setColor(Color.clLightYellow.getRGB());
        str = context.getString(R.string.Score);
        graphics.drawText(str, x, y + p.getTextSize(), p);

        y = y + Math.round(p.getTextSize()) + dip2;
        d = (maxWidth - andro.getMinimumWidth()) / 2;
        andro.setBounds(x + d, y, x + d + andro.getMinimumWidth(), y + andro.getMinimumHeight());
        andro.draw(graphics);

        pointsBounds = new Rect();
        compPoints = String.valueOf(game.getComputer().getLittleGames());
        p.getTextBounds(compPoints, 0, compPoints.length(), pointsBounds);
        p.setColor(Color.clLightGreen.getRGB());
        graphics.drawText(compPoints, x + maxWidth + (bounds.width() - pointsBounds.width()), y + pointsBounds.height()
                + (andro.getMinimumHeight() - pointsBounds.height()) / 2, p);

        y = y + andro.getMinimumHeight() + dip2;

        d = (maxWidth - human.getMinimumWidth()) / 2;
        human.setBounds(x + d, y, x + d + human.getMinimumWidth(), y + human.getMinimumHeight());
        human.draw(graphics);

        humanPoints = String.valueOf(game.getHuman().getLittleGames());
        p.getTextBounds(humanPoints, 0, humanPoints.length(), pointsBounds);
        p.setColor(Color.clLightGreen.getRGB());
        graphics.drawText(humanPoints, x + maxWidth + (bounds.width() - pointsBounds.width()), y + pointsBounds.height()
                + (human.getMinimumHeight() - pointsBounds.height()) / 2, p);
    }

    /**
     * Returns suit's image.
     * @param suit which image is retrieved.
     * @return Image suit's image.
     */
    public final Bitmap getHappy() {
        return pictureDecorator.getHappy();
    }

    /**
     * Returns suit's image.
     * @param suit which image is retrieved.
     * @return Image suit's image.
     */
    public final Bitmap getUnhappy() {
        return pictureDecorator.getUnhappy();
    }
}
