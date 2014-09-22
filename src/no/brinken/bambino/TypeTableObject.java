package no.brinken.bambino;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TypeTableObject extends TableObject
{
	public String selectId(String s)
	{
		String table = tableName();
		String key = getIdField();
		String value = getFieldnameEndingIn("_name");
		
		return "select " + key + " from " + table + " where " + value + " = \"" + s + "\"";
	}
	
	private String getIdField()
	{
		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			Annotation a = f.getAnnotation(IdField.class);
			if ( a instanceof IdField ) 
					return f.getName();
		}
		throw new RuntimeException("No _id field in table " + tableName());
	}
	
	
	private String getFieldnameEndingIn(String s)
	{
		Field[] fields = c.getFields();
		for (Field f : fields)
		{
			if ( f.getName().endsWith(s) )
				return f.getName();
		}
		throw new RuntimeException("No columns ending in " + s + " in table " + tableName());
	}
}
