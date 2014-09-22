/**
 * 
 */
package no.brinken.bambino;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements Constants,
	Birthdate.OnDateSetListener, Birthtime.OnTimeSetListener
{
	private static final String LOG_TAG = "MainActivity";
	
	public long childID;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    // Notice that setContentView() is not used, because we use the root
	    // android.R.id.content as the container for each fragment
	    
	    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	    childID = prefs.getLong(CHILD_ID, 0);

	    // setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(true);

	    Tab tab;
	    tab = actionBar.newTab().setText(R.string.basics_tab)
	    		.setTabListener(new TabListener<Basics>(this, BASICS_TAG, Basics.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab().setText(R.string.health_tab)
	            .setTabListener(new TabListener<Health>(this, HEALTH_TAG, Health.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab().setText(R.string.measurements_tab)
	            .setTabListener(new TabListener<Growth>(this, GROWTH_TAG, Growth.class));
	    actionBar.addTab(tab);
	    
	    Log.i(LOG_TAG, "onCreate");
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.i(LOG_TAG, "onStart");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.i(LOG_TAG, "onResume");
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.i(LOG_TAG, "onPause");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.i(LOG_TAG, "onStop");
	}
	
	@Override 
	protected void onRestart()
	{
		super.onRestart();
		Log.i(LOG_TAG, "onRestart");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.i(LOG_TAG, "onDestroy");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
		Log.i(LOG_TAG, "onSaveInstanceState");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
		{
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
		Log.i(LOG_TAG, "onRestoreInstanceState");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	
	/**
	 * CALLBACKS
	 */
	
	public void setChildID(long childID)
	{
		this.childID = childID;
	    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(CHILD_ID, childID);
		editor.apply();
	}
	
	@Override
	public void onDateSet(String tabTag, Date foedselsDato)
	{
		if (tabTag.equals(BASICS_TAG))
		{
			Basics basics = (Basics) getFragmentManager().findFragmentByTag(BASICS_TAG);
			basics.setFoedselsDato(foedselsDato);
		}
		else
		{
			Growth growth = (Growth) getFragmentManager().findFragmentByTag(GROWTH_TAG);
			growth.setDate(foedselsDato);
		}
	}
	
	@Override
	public void onTimeSet(Date foedselsTid)
	{
		Basics basics = (Basics) getFragmentManager().findFragmentByTag(BASICS_TAG);
		basics.setFoedselsTid(foedselsTid);
	}
}