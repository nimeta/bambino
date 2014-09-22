package no.brinken.bambino;

import java.util.Locale;

import android.app.Application;

public class Bambino extends Application implements Constants
{
	private static DB DB_INSTANCE;
	
	public void onCreate()
	{
		super.onCreate();
		Locale.setDefault(NORGE);
		DB_INSTANCE = DB.getInstance(this);
	}
	
	public DB getDB() { return DB_INSTANCE; }
}
