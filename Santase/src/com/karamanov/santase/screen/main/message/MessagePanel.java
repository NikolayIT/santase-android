/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.main.message;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.karamanov.santase.R;
import com.karamanov.santase.Santase;

/**
 * MessagePanel class.
 * @author Dimitar Karamanov
 */
public class MessagePanel extends LinearLayout {

    private final TableLayout table;

    /**
     * Constructor.
     * @param parent component.
     */
    public MessagePanel(Context context, ArrayList<MessageData> messages) {
        super(context);
        setOrientation(VERTICAL);

        setBackgroundResource(R.drawable.message_shape);

        table = new TableLayout(context);

        TextView caption = new TextView(context);
        caption.setText(context.getString(R.string.santase));
        caption.setTextColor(Color.WHITE);
        caption.setTypeface(Typeface.DEFAULT_BOLD);
        caption.setGravity(Gravity.CENTER_HORIZONTAL);
        caption.setBackgroundResource(R.drawable.message_title);

        addView(caption);

        for (MessageData data : messages) {
            addMessage(data.getImage(), data.getMessage());
        }

        addView(table);
    }

    /**
     * Clears messages.
     */
    public void clear() {
        removeAllViews();
    }

    /**
     * Adds a message to list.
     * @param image of message.
     * @param text of message.
     */
    private void addMessage(final Bitmap image, final String text) {
        int dip3 = Santase.fromPixelToDip(getContext(), 3);
        int dip5 = Santase.fromPixelToDip(getContext(), 5);
        TableRow row = new TableRow(getContext());

        if (image == null) {
            row.addView(new TextView(getContext()));
        } else {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(image);
            TableRow.LayoutParams trp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            trp.rightMargin = dip5;
            trp.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(trp);
            row.addView(imageView);
        }

        TextView message = new TextView(getContext());
        message.setText(text);
        message.setTextColor(Color.DKGRAY);
        message.setTypeface(Typeface.DEFAULT_BOLD);
        TableRow.LayoutParams trp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        trp.weight = 1;
        trp.gravity = Gravity.CENTER_VERTICAL;
        message.setLayoutParams(trp);
        row.addView(message);

        row.setPadding(dip3, dip3, dip3, dip3);
        table.addView(row);
    }
}
