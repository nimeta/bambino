package no.brinken.bambino;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class TableObject
{
	@SuppressWarnings("rawtypes")
	protected final Class c;
	private static final String EQ = " = ";	// equals
	private static final String DL = "\"";	// delimiter

	public TableObject() { c = this.getClass(); }

	public String tableName() { return c.getSimpleName(); }

	public ContentValues contentValues()
	{
		ContentValues cv = new ContentValues();
		Field[] fields = c.getFields();

		try
		{
			for (Field f : fields)
			{
				Annotation a = f.getAnnotation(IdField.class);
				if (a instanceof IdField)
					continue;

				if (f.getType().equals(String.class))
				{
					String s = (String) f.get(this);
					cv.put(f.getName(), s);
				} 
				else if (f.getType().equals(int.class))
				{
					int i = f.getInt(this);
					cv.put(f.getName(), i);
				} 
				else if (f.getType().equals(long.class))
				{
					long l = f.getLong(this);
					cv.put(f.getName(), l);
				} 
				else if (f.getType().equals(double.class))
				{
					double d = f.getDouble(this);
					cv.put(f.getName(), d);
				} 
				else throw new RuntimeException("Not implemented yet: " + f.getType());
			}
		} 
		catch (IllegalAccessException e) { throw new RuntimeException("Illegal access exception"); }

		return cv;
	}

	public String select()
	{
		String table = tableName();
		String columns = columnsString();
		String where = whereString();

		return "select " + columns + " from " + table + " where " + where;
	}

	public String update()
	{
		String table = tableName();
		String set = setString();
		String where = whereString();

		return "update " + table + set + " where " + where;
	}
	
	public String delete()
	{
		String table = tableName();
		String where = whereString();
		return "delete from " + table + " where " + where;
	}
	
	public String selectCount()
	{
		String table = tableName();
		return "select count(*) from " + table;
	}

	private String columnsString()
	{
		StringBuffer s = new StringBuffer();
		boolean first = true;

		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			Annotation a = f.getAnnotation(PrimaryKey.class);
			if ( a instanceof PrimaryKey )
				continue;
			if ( !first )
				s.append(" ,");
			else
				first = false;
			s.append(f.getName());
		}
		return s.toString();
	}

	private String whereString()
	{
		StringBuffer sb = new StringBuffer();
		boolean first = true;

		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			Annotation a = f.getAnnotation(PrimaryKey.class);
			if (a instanceof PrimaryKey)
			{
				if ( !first )
					sb.append(" and ");
				else
					first = false;

				try
				{
					if (f.getType().equals(String.class))
						sb.append(f.getName() + EQ + DL + f.get(this) + DL);
					else if (f.getType().equals(int.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else if (f.getType().equals(long.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else if (f.getType().equals(double.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else
						throw new RuntimeException("Not implemented yet in where: " + f.getType());
				} 
				catch (Exception e) { throw new RuntimeException("Where-string error."); }
			}
		}
		return sb.toString();
	}

	private String setString()
	{
		StringBuffer sb = new StringBuffer(" set ");
		boolean first = true;

		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			Annotation a = f.getAnnotation(PrimaryKey.class);
			if ( !(a instanceof PrimaryKey) )
			{
				if ( !first ) 
					sb.append(", ");
				else 
					first = false;

				try
				{
					if (f.getType().equals(String.class))
						sb.append(f.getName() + EQ + DL + f.get(this) + DL);
					else if (f.getType().equals(int.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else if (f.getType().equals(long.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else if (f.getType().equals(double.class))
						sb.append(f.getName() + EQ + f.get(this).toString());
					else
						throw new RuntimeException("Not implemented yet in set: " + f.getType());
				} 
				catch (Exception e) { throw new RuntimeException("Set-string error."); }
			}
		}
		return sb.toString();
	}

	public void populate(Cursor cursor)
	{
		
		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			Annotation a = f.getAnnotation(PrimaryKey.class);
			if ( a instanceof PrimaryKey ) 
				continue;
			
			int i = cursor.getColumnIndex(f.getName());
			try
			{
				if ( f.getType().equals(String.class) )
					f.set(this, cursor.getString(i));
				else if ( f.getType().equals(int.class) )
					f.setInt(this, cursor.getInt(i));
				else if ( f.getType().equals(long.class) )
					f.setLong(this, cursor.getLong(i));
				else if ( f.getType().equals(double.class) )
					f.set(this, cursor.getDouble(i));
				else throw new RuntimeException("Not implemented yet in populate: " + f.getType());
			} 
			catch (Exception e) { throw new RuntimeException("populate error."); }
		}
	}
}
