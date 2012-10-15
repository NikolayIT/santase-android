package com.karamanov.santase;

import game.beans.Game;
import game.logic.SantaseGame;

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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.TypedValue;

public class Santase extends Application {

	private static final String SANTASE_DAT = "santase.dat";

	private static SantaseGame game;

	public Santase() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static SantaseGame getGame() {
		if (game != null) {
			return game;
		}

		game = new SantaseGame();
		return game;
	}
	
	public static void resetGame(Context context) {
		game = new SantaseGame();
		game.newGame();
		context.deleteFile(SANTASE_DAT);
	}

	public static boolean loadGame(Context context) {
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
							getGame().setGame((Game) object);
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

	public static void terminate(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.prefStoreGame);
		boolean storeGame = preferences.getBoolean(key, Boolean.FALSE);

		if (storeGame) {
			try {
				FileOutputStream fos = context.openFileOutput(SANTASE_DAT, Context.MODE_PRIVATE);
				try {
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					try {
						oos.writeObject(game.getGame());
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
		game = null;
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

	public static int fromPixelToDip(Context context, int pixels) {
		Resources resources = context.getResources();
		if (pixels == 1) {
			return Math.max(1, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics())));
		} else {
			return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics()));
		}
	}

	public static float fromPixelToDipF(Context context, float pixels) {
		Resources resources = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics());
	}
}