package org.openintents.distribution;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class GetFromMarketDialog extends AlertDialog implements OnClickListener {
	private static final String TAG = "StartSaveActivity";

    Context mContext;
    int mMarketUri;
    
    public GetFromMarketDialog(Context context, int message, int buttontext, int market_uri) {
        super(context);
        mContext = context;
        mMarketUri = market_uri;

        //setTitle(context.getText(R.string.menu_edit_tags));
        setMessage(mContext.getText(message));
    	setButton(mContext.getText(buttontext), this);
        
    }

	public void onClick(DialogInterface dialog, int which) {
    	if (which == BUTTON1) {
    		Uri uri = Uri.parse(mContext.getString(mMarketUri));
    		
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(uri);
			GetFromMarketDialog.startSaveActivity(mContext, intent);
    	}
		
	}
	
	/**
	 * Start an activity but prompt a toast if activity is not found
	 * (instead of crashing).
	 * 
	 * @param context
	 * @param intent
	 */
	public static void startSaveActivity(Context context, Intent intent) {
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context,
					RD.string.update_error,
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Error starting activity.", e);
		}
	}
}
