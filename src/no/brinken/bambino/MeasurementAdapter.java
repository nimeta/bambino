package no.brinken.bambino;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class MeasurementAdapter extends SimpleCursorAdapter
{
	private final Context context;
	private final Cursor cursor;
	
	public MeasurementAdapter(Context context, int layout, Cursor cursor, 
			String[] from, int[] to, int flags)
	{
		super(context, layout, cursor, from, to, flags);
		this.context = context;
		this.cursor = cursor;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
