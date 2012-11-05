package com.karamanov.santase;

import game.beans.Game;
import game.logic.SantaseFacade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.karamanov.framework.MessageApplication;
import com.karamanov.framework.message.MessageType;

public class Santase extends MessageApplication {
    
    public final static MessageType MT_KEY_PRESSED = new MessageType("MT_KEY_PRESSED");

    public final static MessageType MT_TOUCH_EVENT = new MessageType("MT_TOUCH_EVENT");

    public final static MessageType MT_EXIT_EVENT = new MessageType("MT_EXIT_EVENT");

    public final static MessageType MT_PAINT_EVENT = new MessageType("MT_PAINT_EVENT");

    private static final String SANTASE_DAT = "santase.dat";
    
    private final SantaseFacade santaseFacade;

    public Santase() {
        super();
        santaseFacade = new SantaseFacade();
    }

    public static synchronized SantaseFacade getSantaseFacade(Activity context) {
        if (context.getApplication() instanceof Santase) {
            return ((Santase) context.getApplication()).santaseFacade;
        }
        return new SantaseFacade();
    }
    
    public static synchronized void initSantaseFacade(Activity context) {
        if (!Santase.loadGame(context)) {
            getSantaseFacade(context).getGame().newGame();
        }
    }

    public static void resetGame(Activity context) {
        getSantaseFacade(context).setGame(new Game());
        getSantaseFacade(context).getGame().newGame();
        context.deleteFile(SANTASE_DAT);
    }

    public static boolean loadGame(Activity context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.prefStoreGame);
        boolean storeGame = preferences.getBoolean(key, Boolean.FALSE);

        if (storeGame) {
            try {
                FileInputStream fis = context.openFileInput(SANTASE_DAT);
                try {
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    try {
                        Object object = ois.readObject();
                        if (object instanceof Game) {
                            getSantaseFacade(context).setGame((Game) object);
                            return true;
                        }
                    } finally {
                        ois.close();
                    }
                } finally {
                    fis.close();
                }
            } catch (FileNotFoundException e) {

            } catch (StreamCorruptedException e) {

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            }
        }
        return false;
    }

    public static void terminate(Activity context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.prefStoreGame);
        boolean storeGame = preferences.getBoolean(key, Boolean.FALSE);

        if (storeGame) {
            try {
                FileOutputStream fos = context.openFileOutput(SANTASE_DAT, Context.MODE_PRIVATE);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    try {
                        oos.writeObject(getSantaseFacade(context).getGame());
                    } finally {
                        oos.close();
                    }
                } finally {
                    fos.close();
                }

            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    public static void _saveLog(ArrayList<String> log) {
        try {
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                File file = new File(root, "santaseLog.txt");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(fileWriter);
                for (String text : log) {
                    out.write(text + "\n");
                }
                out.close();
            }
        } catch (IOException e) {
            // D.N.
        }
    }
}