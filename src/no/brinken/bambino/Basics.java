package no.brinken.bambino;

import java.text.NumberFormat;

import no.brinken.orm.DB;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Basics extends Fragment implements Constants
{
	private EditText firstName;
	private EditText lastName;
	private EditText identityNo;
	private Button btnBirthDate;
	private Button btnBirthTime;
	private EditText weigth;
	private EditText length;
	private EditText circumferance;
	private Button btnCancel;
	private Button btnSave;

	private long childID;

	private Child child;
	private Event event;
	private Measurement measurement;
	
	private Date birthDateTime;
	private static final String LOG_TAG = "Basics";

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.i(LOG_TAG, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		childID = ((MainActivity) getActivity()).childID;
		child = new Child();
		event = new Event();
		measurement = new Measurement();
		birthDateTime = new Date();
		Log.i(LOG_TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.basics, container, false);

		firstName = (EditText) view.findViewById(R.id.fornavn);
		firstName.setOnFocusChangeListener(onFocusChangeListener);
		lastName = (EditText) view.findViewById(R.id.etternavn);
		lastName.setOnFocusChangeListener(onFocusChangeListener);
		identityNo = (EditText) view.findViewById(R.id.personnr);
		identityNo.setOnFocusChangeListener(onFocusChangeListener);
		weigth = (EditText) view.findViewById(R.id.vekt);
		weigth.setOnFocusChangeListener(onFocusChangeListener);
		length = (EditText) view.findViewById(R.id.lengde);
		length.setOnFocusChangeListener(onFocusChangeListener);
		circumferance = (EditText) view.findViewById(R.id.hodeomkrets);
		circumferance.setOnFocusChangeListener(onFocusChangeListener);

		btnBirthDate = (Button) view.findViewById(R.id.foedt_dato);
		btnBirthDate.setText(birthDateTime.dateToString());
		btnBirthDate.setOnClickListener(bornOnClickListener);

		btnBirthTime = (Button) view.findViewById(R.id.foedt_tid);
		btnBirthTime.setText(birthDateTime.timeToString());
		btnBirthTime.setOnClickListener(bornOnClickListener);

		btnCancel = (Button) view.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(cancelOnClickListener);
		btnSave = (Button) view.findViewById(R.id.save);
		btnSave.setOnClickListener(saveOnClickListener);
		
		Log.i(LOG_TAG, "onCreateView");
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();

		if (childID > 0)
		{
			try { readBasics(activity); }
			catch (Exception e) { new BambinoToast(activity, e.getMessage()); }
		}
		Log.i(LOG_TAG, "onActivityCreated");
	}
	
	@SuppressLint("NewApi")
	@Override 
	public void onViewStateRestored(Bundle savedInstanceState)
	{
		super.onViewStateRestored(savedInstanceState);
		Log.i(LOG_TAG, "onViewStateRestored");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.i(LOG_TAG, "onResume");
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		// Commit changes that should be persisted if not turning back.
		birthDateTime = new Date(btnBirthDate.getText().toString() + btnBirthTime.getText().toString());
		child.first_name = firstName.getText().toString();
		child.last_name = lastName.getText().toString();
		getFragmentManager().addOnBackStackChangedListener(null);
		Log.i(LOG_TAG, "onPause");
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		Log.i(LOG_TAG, "onStop");
	}
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		Log.i(LOG_TAG, "onDestroyView");
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.i(LOG_TAG, "onDestroy");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		Log.i(LOG_TAG, "onDetatch");
	}

	private final OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (!hasFocus)
			{
				switch (v.getId())
				{
					case R.id.fornavn:
					{
						if (firstName.getText().length() > 0)
							child.first_name = firstName.getText().toString();
						break;
					}
					case R.id.etternavn:
					{
						if (lastName.getText().length() > 0)
							child.last_name = lastName.getText().toString();
						break;
					}
					case R.id.personnr:
					{
						if (identityNo.getText().length() > 0)
							child.identity_no = identityNo.getText().toString();
						break;
					}
					case R.id.vekt:
					{
						if (weigth.getText().length() > 0)
							measurement.weight = Integer.parseInt(weigth.getText().toString());
						break;
					}
					case R.id.lengde:
					{
						if (length.getText().length() > 0)
							measurement.length = Double.parseDouble(length.getText().toString());
						break;
					}
					case R.id.hodeomkrets:
					{
						if (circumferance.getText().length() > 0)
							measurement.circumferance = Double.parseDouble(circumferance.getText().toString());
						break;
					}
				}
			}
		}
	};

	private final OnClickListener bornOnClickListener = new OnClickListener()
	{
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.foedt_dato)
			{
				Date d = new Date(btnBirthDate.getText().toString());
				DialogFragment foedselsDato = Birthdate.getInstance(BASICS_TAG, d.year(), d.month(), d.day());
				((Birthdate) foedselsDato).setTag(DATE_PICKER_TAG);
				FragmentManager fm = getChildFragmentManager();
				foedselsDato.show(fm, DATE_PICKER_TAG);
			} 
			else
			{
				Date d = new Date(btnBirthTime.getText().toString());
				DialogFragment foedselsTid = Birthtime.getInstance(d.hour(), d.minute());
				((Birthtime) foedselsTid).setTag(TIME_PICKER_TAG);
				FragmentManager fm = getChildFragmentManager();
				foedselsTid.show(fm, TIME_PICKER_TAG);
			}
		}
	};

	public void setFoedselsDato(Date foedselsDato)
	{
		btnBirthDate.setText(foedselsDato.dateToString());
		child.birth_date = foedselsDato.dateISO();
		birthDateTime.setDate(foedselsDato.year(), foedselsDato.month(), foedselsDato.day());
	}

	public void setFoedselsTid(Date foedselsTid)
	{
		btnBirthTime.setText(foedselsTid.timeToString());
		child.birth_time = foedselsTid.timeISO();
		birthDateTime.setTime(foedselsTid.hour(), foedselsTid.minute());
	}

	private final OnClickListener cancelOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Context context = v.getContext();
			clearAllWigets();
			if (childID > 0)
			{
				try { readBasics(context); }
				catch (Exception e) { new BambinoToast(context, e.getMessage()); }
			}
		}
	};

	private final OnClickListener saveOnClickListener = new OnClickListener()
	{
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v)
		{
			Context context = v.getContext();
			try
			{
				widgetsToTableobjects(context);
				if (childID == 0)
					saveBasics(context);
				else
					updateBasics(context);
			}
			catch (Exception e) { new BambinoToast(context, e.getMessage()); }
		}	
	};
	
	private void saveBasics(Context context) throws BambinoException
	{
		DB db = ((Bambino) context.getApplicationContext()).getDB();
		try 
		{ 
			db.beginTransaction();
			childID = db.insert(child);
			child.child_id = childID;
			event.child_id = childID;
			measurement.child_id = childID;
			event.event_date = child.birth_date;
			measurement.event_date = event.event_date;
			// TODO: To be rewritten.
			event.event_type_id = db.selectID(new Event_type(), "F¿dsel");
			measurement.event_type_id = event.event_type_id;
			db.insert(event);
			db.insert(measurement);
			((MainActivity) getActivity()).setChildID(childID);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			childID = 0;
			throw new BambinoException(e.getMessage());
		}
		finally { db.endTransaction(); }
	}
	
	private void updateBasics(Context context) throws BambinoException
	{
		DB db = ((Bambino) context.getApplicationContext()).getDB();
		try 
		{ 
			db.beginTransaction();
			child.child_id = childID;
			event.child_id = childID;
			measurement.child_id = childID;
			db.update(child);
			// Keys in theese tables not updated yet
			db.delete(measurement);
			db.delete(event);
			// event_date might have changed, event_type_id cannot change.
			event.event_date = child.birth_date;
			measurement.event_date = child.birth_date;
			db.insert(event);
			db.insert(measurement);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			childID = 0;
			throw new BambinoException(e.getMessage());
		}
		finally { db.endTransaction(); }
	}
	
	private void readBasics(Context context) throws BambinoException
	{
		DB db = ((Bambino) context.getApplicationContext()).getDB();
		db.beginTransaction();
		// TODO: Hvis flere barn, velg.
		try
		{
			child.child_id = childID;
			db.select(child);
			event.child_id = childID;
			event.event_date = child.birth_date;
			event.event_type_id = db.selectID(new Event_type(), "F¿dsel");
			// No select from Event, only primary keys in this table.
			measurement.child_id = childID;
			measurement.event_date = event.event_date;
			measurement.event_type_id = event.event_type_id;
			db.select(measurement);
			db.setTransactionSuccessful();
			tableobjectsToWigets();
		}
		catch (Exception e) { throw new BambinoException(e.getMessage()); }
		finally { db.endTransaction(); }
	}

	private void clearAllWigets()
	{
		firstName.setText(BLANK);
		lastName.setText(BLANK);
		btnBirthDate.setText(new Date().dateISO());
		btnBirthTime.setText(new Date().timeISO());
		identityNo.setText(BLANK);
		weigth.setText(BLANK);
		length.setText(BLANK);
		circumferance.setText(BLANK);
	}

	private void tableobjectsToWigets()
	{
		NumberFormat formatDouble = NumberFormat.getInstance(NORGE);
		formatDouble.setMinimumFractionDigits(1);
		formatDouble.setMaximumFractionDigits(1);

		NumberFormat formatInt = NumberFormat.getInstance(NORGE);
		formatInt.setMinimumFractionDigits(0);
		formatInt.setMaximumFractionDigits(0);

		firstName.setText(child.first_name);
		lastName.setText(child.last_name);
		identityNo.setText(child.identity_no);
		btnBirthDate.setText(new Date(child.birth_date).dateToString());
		btnBirthTime.setText(child.birth_time);
		weigth.setText(formatInt.format(measurement.weight));
		length.setText(formatDouble.format(measurement.length));
		circumferance.setText(formatDouble.format(measurement.circumferance));
	}
	
	private void widgetsToTableobjects(Context context) throws BambinoException
	{
		if (firstName.getText().length() == 0)
			throw new BambinoException("For Œ lagre oppgi fornavn og etternavn.");
		child.first_name = firstName.getText().toString();
		if (lastName.getText().length() == 0)
			throw new BambinoException("For Œ lagre oppgi fornavn og etternavn.");
		child.last_name = lastName.getText().toString();
		// birth_date and birth_time is set on exiting the date and time pickers.
		child.identity_no = identityNo.getText().toString();
		measurement.weight = Integer.parseInt(weigth.getText().toString());
		measurement.length = Double.parseDouble(length.getText().toString());
		measurement.circumferance = Double.parseDouble(circumferance.getText().toString());
	}
}
