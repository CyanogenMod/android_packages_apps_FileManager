/* 
 * Copyright (C) 2007-2011 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openintents.cmfilemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @version 2009-02-04
 * @version 2011-02-07: Allow for string arguments.
 * @author Peli
 */
public class DownloadAppDialog extends AlertDialog implements OnClickListener {
	private static final String TAG = "StartSaveActivity";

    Context mContext;
    String mDownloadAppName;
    String mDownloadPackageName;
    String mDownloadWebsite;
    String mMessageText;
    
    boolean mMarketAvailable;
    
    public DownloadAppDialog(Context context) {
        super(context);
        mContext = context;
    }
    
    public DownloadAppDialog(Context context, int message_id, int download_name_id, int download_package_id, int download_website_id) {
        super(context);
        mContext = context;
        set(message_id, download_name_id, download_package_id, download_website_id);
    }
    
    public DownloadAppDialog(Context context, String message, String download_name, String download_package, String download_website) {
        super(context);
        mContext = context;
        set(message, download_name, download_package, download_website);
    }

	protected void set(int message_id, int download_name_id,
			int download_package_id, int download_website_id) {
		String message = mContext.getString(message_id);
		String download_name = mContext.getString(download_name_id);
		String download_package = mContext.getString(download_package_id);
		String download_website = mContext.getString(download_website_id);
		set(message, download_name, download_package, download_website);
	}
	
	protected void set(String message, String download_name,
			String download_package, String download_website) {
        mDownloadAppName = download_name;
        mDownloadPackageName = download_package;
        mDownloadWebsite = download_website;
        
        mMarketAvailable = MarketUtils.isMarketAvailable(mContext, mDownloadPackageName);
        
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append(" ");
        if (mMarketAvailable) {
        	sb.append(mContext.getString(R.string.oi_distribution_download_market_message, 
        			mDownloadAppName));
        } else {
        	sb.append(mContext.getString(R.string.oi_distribution_download_message, 
        			mDownloadAppName));
        }
        mMessageText = sb.toString();
        setMessage(mMessageText);

        setTitle(mContext.getString(R.string.oi_distribution_download_title,
        		mDownloadAppName));
        
        setButton(mContext.getText(R.string.oi_distribution_download_market), this);
    	setButton2(mContext.getText(R.string.oi_distribution_download_web), this);
	}
    
	public void onClick(DialogInterface dialog, int which) {
		Intent intent;
		
		if (which == BUTTON1) {
			intent = MarketUtils.getMarketDownloadIntent(mDownloadPackageName);
			startSaveActivity(intent);
    	} else if (which == BUTTON2) {
    		intent  = new Intent(Intent.ACTION_VIEW);
    		Uri uri= Uri.parse(mDownloadWebsite);
			intent.setData(uri);
			startSaveActivity(intent);
    	}
	}

	public static void onPrepareDialog(Context context, Dialog dialog) {
		DownloadAppDialog d = (DownloadAppDialog) dialog;
		
		boolean has_android_market = MarketUtils.isMarketAvailable(context, d.mDownloadPackageName);

		dialog.findViewById(android.R.id.button1).setVisibility(
				has_android_market ? View.VISIBLE : View.GONE);
	}
	
	/**
	 * Start an activity but prompt a toast if activity is not found
	 * (instead of crashing).
	 * 
	 * @param context
	 * @param intent
	 */
	public void startSaveActivity(Intent intent) {
		try {
			mContext.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mContext,
					R.string.oi_distribution_update_error,
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Error starting second activity.", e);
		}
	}
}
