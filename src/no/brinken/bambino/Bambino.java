package no.brinken.bambino;

import java.util.Locale;

import no.brinken.orm.DB;
import android.app.Application;

public class Bambino extends Application implements Constants
{
	private static DB DB_INSTANCE;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Locale.setDefault(NORGE);
		DB_INSTANCE = DB.getInstance(this, getString(R.string.db_name), 
										   getResources().getInteger(R.integer.db_version), 
										   getString(R.string.db_create));
	}
	
	public DB getDB() { return DB_INSTANCE; }
}
