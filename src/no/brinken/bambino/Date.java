package no.brinken.bambino;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Denne datoklassen kan brukes direkte som parameter i kall til Oracle da den implementerer interfacene 
 * ORAData og ORADataFactory. Den er basert pŒ java.util.Date og har en GregorianCalendar som medlemsfelt.
 * @author x5k
 */
public class Date extends java.util.Date
{
	static final long serialVersionUID = 0L;
	static Locale locale;
	
	GregorianCalendar gc;
	
	static Locale norge = new Locale("NO", "NO");
	static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd", norge);
	static final SimpleDateFormat NS = new SimpleDateFormat("dd.MM.yyyy", norge);
	static final SimpleDateFormat timeNS = new SimpleDateFormat("HH.mm", norge);
	static final SimpleDateFormat timeISO = new SimpleDateFormat("HH:mm", norge);
	static final SimpleDateFormat datetimeNS = new SimpleDateFormat("dd.MM.yyyyHH.mm", norge);
	static final SimpleDateFormat datetimeISO = new SimpleDateFormat("dd.MM.yyyyHH:mm", norge);

	static final SimpleDateFormat valutaNB = new SimpleDateFormat("dd-MMM-yy", norge);
	static final SimpleDateFormat niborNB = new SimpleDateFormat("dd.MMM.yy", norge);
	static final SimpleDateFormat kortNS = new SimpleDateFormat("dd.mm.yy", norge);
	static final SimpleDateFormat abaGlobal = new SimpleDateFormat("ddMMyyyy", norge);

	static final int[] mDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	static final int[] lDays = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	public Date()
	{
		gc = new GregorianCalendar();
	    this.setTime(gc.getTimeInMillis());
	}

	public Date(Locale locale)
	{
		Date.locale = locale;
		gc = new GregorianCalendar(locale);
	    this.setTime(gc.getTimeInMillis());
	}

	public Date(long millisec)
	{
	    gc = new GregorianCalendar();
		gc.setTimeInMillis(millisec);
		this.setTime(gc.getTimeInMillis());
	}
	
	public Date(java.util.Date d)
	{
		this(d.getTime());
	}
	
	public Date(java.sql.Date d)
	{
		this(d.getTime());
	}
	
	public Date(Date d)
	{
		this(d.getTime());
	}
	
	public Date(int year, int month, int day)
	{
		gc = new GregorianCalendar();
		gc.clear();
		gc.set(year, month-1, day);
		this.setTime(gc.getTimeInMillis());
	}
	
	public Date(int hour, int minute)
	{
		gc = new GregorianCalendar();
		gc.clear();
		this.setTime(hour, minute);
	}
	
	public Date(String datoStr)
	{
	    gc = new GregorianCalendar();
	    SimpleDateFormat sdf;
		if ( datoStr.length() == 5 )
		    sdf = ( datoStr.indexOf('.') > -1 ) ? timeNS : timeISO;
		else if ( datoStr.length() == 8 )
		    sdf = ( datoStr.indexOf('.') > -1 ) ? kortNS : abaGlobal;
		else if ( datoStr.length() == 9 )
		    sdf = ( datoStr.indexOf('-') > -1 ) ? valutaNB : niborNB;
		else if ( datoStr.length() == 10 )
		    sdf = ( datoStr.charAt(4) == '-' ) ? ISO : NS;
		else
		    sdf = ( datoStr.charAt(12) == ':' ) ? datetimeISO :datetimeNS;
		try { gc.setTime(sdf.parse(datoStr)); }
		catch (ParseException e) { throw new RuntimeException("Feil under parsing av datostreng.", e); }
		this.setTime(gc.getTimeInMillis());
	}

	public Date(double doubleDate)
	{
		GregorianCalendar gc = new GregorianCalendar();
		double year = Math.floor(doubleDate);
		int[] mlDays = gc.isLeapYear((int)year) ? Date.lDays : Date.mDays;
		double yDays = gc.isLeapYear((int)year) ? 366 : 365;
		int days = (int)Math.round((doubleDate - year) * yDays);
		
		// Etter kl. 1200 (middag) pŒ nyttŒrsaften vil dette inntreffe
		// hvis vi opererer med kontinuerlig tid, som i Prognose.
		// Da mŒ vi gj¿re dette hacket for ikke Œ havne en dag framover.

		if ( days == yDays )
			days = days -1; 

		int i = 0;
		for ( ; days >= mlDays[i]; i++ )
			days -= mlDays[i];
		
		gc.clear();
		gc.set((int)year, i, days+1);
		this.setTime(gc.getTimeInMillis());
	}
	
	public Date setDate(int year, int month, int day)
	{
		gc.set(Calendar.YEAR, year);
		gc.set(Calendar.MONTH, month-1);
		gc.set(Calendar.DAY_OF_MONTH, day);
		this.setTime(gc.getTimeInMillis());
		return this;
	}
	
	public Date setTime(int hour, int minute)
	{
		gc.set(Calendar.HOUR_OF_DAY, hour);
		gc.set(Calendar.MINUTE, minute);
		this.setTime(gc.getTimeInMillis());
		return this;
	}
	
	@Override
	public Object clone()
	{
		return new Date(this.getTime());
	}

	public java.sql.Date toSqlDate()
	{
		return new java.sql.Date(getTime());
	}
	
	public Date theDayBefore()
	{
		Date before = (Date)clone();
		before.addDays(-1);
		return before;
	}

	public Date theDayAfter()
	{
		Date before = (Date)clone();
		before.addDays(1);
		return before;
	}

	public boolean beforeOrEquals(Date when) { return before(when) || equals(when); }
	
	public boolean afterOrEquals(Date when) { return after(when) || equals(when); }
	
	public void addDays(int amount)
	{
		gc.add(Calendar.DATE, amount);
	    setTime(gc.getTimeInMillis());
	}
	
	public Date firstInMonth()
	{
		Date first = (Date)clone();
		first.gc.set(Calendar.DAY_OF_MONTH, 1);
		first.setTime(first.gc.getTimeInMillis());
		return first;
	}
	
	public Date lastInMonth()
	{
		Date last = (Date)clone();
		last.gc.set(Calendar.DAY_OF_MONTH, last.gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		setTime(last.gc.getTimeInMillis());
		return last;
	}

	public Date firstInYear()
	{
		Date first = (Date)clone();
	    first.gc.set(Calendar.DAY_OF_MONTH, 1);
	    first.gc.set(Calendar.MONTH, 0);
		first.setTime(first.gc.getTimeInMillis());
		return first;
	}
	
	public boolean sameMonth(Date d)
	{
		return ( gc.get(Calendar.MONTH) == d.gc.get(Calendar.MONTH) );
	}
	
	public boolean newYear(Date d)
	{
		return ( gc.get(Calendar.YEAR) != d.gc.get(Calendar.YEAR) );
	}
	
	public String dateToString()
	{
		return NS.format(this);
	}
	
	public String timeToString()
	{
		return timeNS.format(this);
	}
	
	public String dateISO()
	{
		return ISO.format(this);
	}
	
	public String timeISO()
	{
		return timeISO.format(this);
	}

	public void clearHours()
	{
		gc.clear(Calendar.HOUR_OF_DAY);
		gc.clear(Calendar.HOUR);
		gc.clear(Calendar.MINUTE);
		gc.clear(Calendar.SECOND);
		gc.clear(Calendar.MILLISECOND);
	}

	public double toDouble()
	{
		return gc.get(Calendar.YEAR) + ((gc.get(Calendar.DAY_OF_YEAR)-1.0)/gc.getActualMaximum(Calendar.DAY_OF_YEAR));     
	}
	
	public int year() { return gc.get(Calendar.YEAR); } 
	public int day() { return gc.get(Calendar.DAY_OF_MONTH); } 
	public int month() { return gc.get(Calendar.MONTH) + 1; } 
	public int hour() { return gc.get(Calendar.HOUR_OF_DAY); } 
	public int minute() { return gc.get(Calendar.MINUTE); } 
	
	public GregorianCalendar getCalendar() { return gc; }
	
	public void update() { this.setTime(gc.getTimeInMillis()); }
}