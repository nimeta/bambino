package no.brinken.bambino;

import java.text.NumberFormat;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Growth extends Fragment implements Constants,
		OnItemSelectedListener
{
	private Button btnDateEdit;
	private EditText weigthEdit;
	private EditText lengthEdit;
	private EditText circumferanceEdit;
	private Button btnCancel;
	private Button btnSave;

	private Spinner spinner;
	public ArrayAdapter<CharSequence> adapter;

	private long childID;

	private Event event;
	private Measurement measurement;

	private long eventTypeID;
	private Date date;
	private static final String LOG_TAG = "Growth";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		childID = ((MainActivity) getActivity()).childID;
		event = new Event();
		measurement = new Measurement();
		date = new Date();

		Log.i(LOG_TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.growth, container, false);

		spinner = (Spinner) view.findViewById(R.id.typeSpinner);
		adapter = ArrayAdapter.createFromResource(container.getContext(),
				R.array.event_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		btnDateEdit = (Button) view.findViewById(R.id.dateEdit);
		btnDateEdit.setText(date.dateToString());
		btnDateEdit.setOnClickListener(dateOnClickListener);
		weigthEdit = (EditText) view.findViewById(R.id.weightEdit);
		lengthEdit = (EditText) view.findViewById(R.id.lengthEdit);
		circumferanceEdit = (EditText) view
				.findViewById(R.id.circumferanceEdit);

		btnCancel = (Button) view.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(cancelOnClickListener);
		btnSave = (Button) view.findViewById(R.id.save);
		btnSave.setOnClickListener(saveOnClickListener);
		
//		startManagingCursor(mCursor);
//		
//		DB db = ((Bambino)view.getContext().getApplicationContext()).getDB();
//		Cursor cursor = getMeasurements(db);
//
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(view, android.R.layout.measurement_row,
//				cursor, new String[] { ContactsContract.Contacts._ID,
//						ContactsContract.Contacts.DISPLAY_NAME },
//				new int[] { android.R.id.text1, android.R.id.text2 });
//
//		// Bind to our new adapter.
//		setListAdapter(adapter);		
		
		Log.i(LOG_TAG, "onCreateView");
		return view;
	}	
	
//	private Cursor getMeasurements(DB db)
//	{
//		Cursor cursor = ((Object) db).rawQuery(measurement.select(), null);
//		return db.select(measurement);
//	}

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
		// getFragmentManager().popBackStack();
		Log.i(LOG_TAG, "onResume");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		date = new Date(btnDateEdit.getText().toString());
		// getFragmentManager().addOnBackStackChangedListener(null);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id)
	{
		String type = parent.getItemAtPosition(pos).toString();
		DB db = ((Bambino) parent.getContext().getApplicationContext()).getDB();
		db.beginTransaction();
		try
		{
			eventTypeID = db.selectID(new Event_type(), type);
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			new BambinoToast(view.getContext(), e.getMessage());
		} finally
		{
			db.endTransaction();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{ /* Keep present eventTypeID */
	}

	private final OnClickListener dateOnClickListener = new OnClickListener()
	{
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v)
		{
			Date d = new Date(btnDateEdit.getText().toString());
			DialogFragment foedselsDato = Birthdate.getInstance(GROWTH_TAG,
					d.year(), d.month(), d.day());
			((Birthdate) foedselsDato).setTag(DATE_PICKER_TAG);
			FragmentManager fm = getChildFragmentManager();
			foedselsDato.show(fm, DATE_PICKER_TAG);
		}
	};

	public void setDate(Date date)
	{
		btnDateEdit.setText(date.dateToString());
		event.event_date = date.dateISO();
		measurement.event_date = date.dateISO();
		this.date.setDate(date.year(), date.month(), date.day());
	}

	private final OnClickListener cancelOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			clearAllWigets();
		}
	};

	private final OnClickListener saveOnClickListener = new OnClickListener()
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v)
		{
			Context context = v.getContext();
			try
			{
				widgetsToTableobjects(context);
				saveMeasurements(context);
			} catch (Exception e)
			{
				new BambinoToast(context, e.getMessage());
			}
		}
	};

	private void saveMeasurements(Context context) throws BambinoException
	{
		DB db = ((Bambino) context.getApplicationContext()).getDB();
		try
		{
			db.beginTransaction();
			event.child_id = childID;
			// event.event_date set in date picker
			event.event_type_id = eventTypeID;
			db.insert(event);
			measurement.child_id = childID;
			// measurement.event_date set in date_picker
			measurement.event_type_id = eventTypeID;
			db.insert(measurement);
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			throw new BambinoException(e.getMessage());
		} finally
		{
			db.endTransaction();
		}
	}

	private void readMeasurements(Context context) throws BambinoException
	{
		DB db = ((Bambino) context.getApplicationContext()).getDB();
		db.beginTransaction();
		try
		{
			event.event_type_id = db.selectID(new Event_type(), "F¿dsel");
			// No select from Event, only primary keys in this table.
			measurement.event_date = event.event_date;
			measurement.event_type_id = event.event_type_id;
			db.select(measurement);
			db.setTransactionSuccessful();
			tableobjectsToWigets();
		} catch (Exception e)
		{
			throw new BambinoException(e.getMessage());
		} finally
		{
			db.endTransaction();
		}
	}

	private void clearAllWigets()
	{
		btnDateEdit.setText(new Date().dateToString());
		weigthEdit.setText(BLANK);
		lengthEdit.setText(BLANK);
		circumferanceEdit.setText(BLANK);
	}

	private void tableobjectsToWigets()
	{
		NumberFormat formatDouble = NumberFormat.getInstance(NORGE);
		formatDouble.setMinimumFractionDigits(1);
		formatDouble.setMaximumFractionDigits(1);

		NumberFormat formatInt = NumberFormat.getInstance(NORGE);
		formatInt.setMaximumFractionDigits(0);

		btnDateEdit.setText(new Date(measurement.event_date).dateToString());
		weigthEdit.setText(formatInt.format(measurement.weight));
		lengthEdit.setText(formatDouble.format(measurement.length));
		circumferanceEdit.setText(formatDouble
				.format(measurement.circumferance));
	}

	private void widgetsToTableobjects(Context context) throws BambinoException
	{
		// TODO: Date must be set on exiting the picker.
		measurement.weight = Integer.parseInt(weigthEdit.getText().toString());
		measurement.length = Double
				.parseDouble(lengthEdit.getText().toString());
		measurement.circumferance = Double.parseDouble(circumferanceEdit
				.getText().toString());
	}
}
