package no.brinken.bambino;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class BambinoToast
{
	public BambinoToast(Context context, String msg)
	{
		CharSequence text = msg;
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
}
