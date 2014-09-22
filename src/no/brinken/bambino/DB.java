package no.brinken.bambino;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DB extends SQLiteOpenHelper
{
	private final Context context;
	private SQLiteDatabase sqliteDb;
	
	public static DB getInstance(Context context)
	{
		String dbName = context.getString(R.string.db_name);
		int dbVersion = context.getResources().getInteger(R.integer.db_version);
		return new DB(context, dbName, null, dbVersion);
	}


	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	private DB(Context context, String dbName, CursorFactory factory, int dbVersion)
	{
		super(context, dbName, factory, dbVersion);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try 
		{ 
			String zip = context.getString(R.string.db_create);;
			SQLiteDBDeploy.deploy(db, context, zip);
			populateTypeTables(db);
		}
		catch (Exception e) { throw new RuntimeException("Database creation failed.", e); }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO: This has not been an issue yet.
	}

	@Override
	public void onConfigure(SQLiteDatabase db)
	{
		db.execSQL("PRAGMA foreign_keys=ON;");
	}
	
	private void populateTypeTables(SQLiteDatabase db) throws SQLException
	{
		Resources res = context.getResources();

		String[] events = res.getStringArray(R.array.event_type);
		for (String event : events)
		{
			Event_type et = new Event_type();
			et.event_name = event;
			et.measure_ind = event.equals("Barselgruppe") ? 0 : 1;
			db.insertOrThrow(et.tableName(), null, et.contentValues());
		}
		
		String[] vaccines = res.getStringArray(R.array.vaccine_type);
		for (String vaccine : vaccines)
		{
			Vaccine_type vt = new Vaccine_type();
			vt.vaccine_name = vaccine;
			db.insertOrThrow(vt.tableName(), null, vt.contentValues());
		}
		
		String[] roles = res.getStringArray(R.array.role_type);
		for (String role : roles)
		{
			Role_type rt = new Role_type();
			rt.role_name = role;
			db.insertOrThrow(rt.tableName(), null, rt.contentValues());
		}
	}
	
	public void begin() { sqliteDb = this.getWritableDatabase(); }
	
	public void end() { sqliteDb.close(); }
	
	public void beginTransaction() 
	{ 
		sqliteDb = this.getWritableDatabase();
		sqliteDb.beginTransaction(); 
	}
	
	public void setTransactionSuccessful() { sqliteDb.setTransactionSuccessful(); }
	
	public void endTransaction() 
	{ 
		sqliteDb.endTransaction();
		sqliteDb.close();
	}
	
	public long selectID(TypeTableObject dbo, String type)
	{
		String s = dbo.selectId(type);
		SQLiteStatement stmt = sqliteDb.compileStatement(s);
		return stmt.simpleQueryForLong();
	}
	
	public long selectCount(TableObject dbo)
	{
		String s = dbo.selectCount();
		SQLiteStatement stmt = sqliteDb.compileStatement(s);
		return stmt.simpleQueryForLong();
	}
	
	public Cursor select(TableObject dbo)
	{
		Cursor cursor = sqliteDb.rawQuery(dbo.select(), null);
		cursor.moveToFirst();
		dbo.populate(cursor);
		return cursor;
	}
	
	public long insert(TableObject dbo)
	{
		return sqliteDb.insertOrThrow(dbo.tableName(), null, dbo.contentValues());
	}
	
	public void update(TableObject dbo)
	{
		sqliteDb.execSQL(dbo.update());
	}
	
	public void delete(TableObject dbo)
	{
		sqliteDb.execSQL(dbo.delete());
	}
}