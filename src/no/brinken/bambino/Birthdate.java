package no.brinken.bambino;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class Birthdate extends DialogFragment implements OnDateSetListener
{
	private static String tabTag;
	private int year;
	private int month;
	private int day;
	
	public String tag;
	OnDateSetListener activityCallback;
	
	public static Birthdate getInstance(String tabTag, int year, int month, int day)
	{
		Birthdate.tabTag = tabTag;
		Birthdate birthdate = new Birthdate();
		Bundle args = new Bundle();
		args.putInt("year", year);
		args.putInt("month", month);
		args.putInt("day", day);
		birthdate.setArguments(args);
		return birthdate;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		super.onCreateDialog(savedInstanceState);
		year = getArguments().getInt("year");
		month = getArguments().getInt("month");
		day = getArguments().getInt("day");
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			activityCallback = (OnDateSetListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement OnDateSetListener.");
		}
	}

	public void setTag(String tag) { this.tag = tag; }

	public interface OnDateSetListener
	{
		public void onDateSet(String tabTag, Date foedselsDato);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day)
	{
		Date foedselsDato = new Date(year, month, day);
		activityCallback.onDateSet(tabTag, foedselsDato);
	}
}
