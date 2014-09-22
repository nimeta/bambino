package no.brinken.bambino;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.widget.TimePicker;

public class Birthtime extends DialogFragment implements OnTimeSetListener
{
	private int hour;
	private int minute;
	
	public String tag;
	OnTimeSetListener activityCallback;

	public static Birthtime getInstance(int hour, int minute)
	{
		Birthtime birthtime = new Birthtime();
		Bundle args = new Bundle();
		args.putInt("hour", hour);
		args.putInt("minute", minute);
		birthtime.setArguments(args);
		return birthtime;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		super.onCreateDialog(savedInstanceState);
		hour = getArguments().getInt("hour");
		minute = getArguments().getInt("minute");
		return new TimePickerDialog(getActivity(), this, hour, minute, true);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			activityCallback = (OnTimeSetListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + "must implement OnTimeSetListener.");
		}
	}

	public void setTag(String tag) { this.tag = tag; }

	public interface OnTimeSetListener
	{
		public void onTimeSet(Date foedselsTid);
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hour, int minute)
	{
		Date foedselsTid = new Date().setTime(hour, minute);
		activityCallback.onTimeSet(foedselsTid);
	}
}
