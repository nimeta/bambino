package no.brinken.bambino;

import android.util.Log;
/**
 * Exception-class for Bambino
 */
public class BambinoException extends Exception
{
	static final long serialVersionUID = 1;
	private static final String TAG = "Bambino";
	private String msg;
	
	/**
	 * Metode for Œ gi en feilmelding.
	 * @param msg
	 */
	public BambinoException(String msg) 
	{ 
		this.msg = msg;
		Log.i(TAG, msg);
	}
	
	/**
	 * Metode for å oversette en exception til en PrentException.
	 * @param cause
	 */
	public BambinoException(Throwable cause) { this(null, cause); }

	/**
	 * Metode som skriver en filtrert stack trace i tillegg til meldingen.
	 * @param msg
	 * @param cause
	 */
	public BambinoException(String msg, Throwable cause)
	{
	    if ( msg != null )
	    {
	    	this.msg = msg;
			Log.i(TAG, msg);
			Log.i(TAG, "");
	    }
	    else
	    	this.msg = cause.getMessage();
	    
	    Log.i(TAG, "Programcrash caused by ");
		Log.i(TAG, cause.getClass().getName());
		Log.i(TAG, "");
		Log.i(TAG, cause.getMessage());
		Log.i(TAG, "");
		StackTraceElement[] st = cause.getStackTrace();
		for ( int i = 0; i < st.length; i ++ )
		{   
			if ( st[i].getClassName().substring(0,10).equals("no.brinken") ) 
			{
				String s = st[i].getFileName() + ":" + st[i].getLineNumber() + " - " +
						   st[i].getClassName() + "." + st[i].getMethodName();
				Log.i(TAG, s);
			}	
		}
	}
	
	@Override
	public String getMessage() { return msg; }
}
