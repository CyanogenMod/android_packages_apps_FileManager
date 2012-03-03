/* 
 * Copyright (C) 2008-2011 OpenIntents.org
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

import org.openintents.util.VersionUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * @version 2009-10-23: support Market and aTrackDog
 * @version 2009-02-04
 * @author Peli
 *
 */
public class UpdateDialog extends DownloadAppDialog {
	
	private static final String TAG = "UpdateMenu";
	private static final boolean DEBUG_NO_MARKET = false;
	
	/**
	 * If any of the following applications is installed,
	 * there is no need for a manual "Update" menu entry.
	 */
	public static final String[] UPDATE_CHECKER = new String[]
	    {
			"org.openintents.updatechecker", // OI Update
			"com.android.vending", // Google's Android Market
			"com.a0soft.gphone.aTrackDog" // aTrackDog
	    };
    
    public UpdateDialog(Context context) {
        super(context, 
        		R.string.oi_distribution_update_box_text, 
        		R.string.oi_distribution_update_app, 
        		R.string.oi_distribution_update_checker_package, 
        		R.string.oi_distribution_update_checker_website);
        mContext = context;

        String version = VersionUtils.getVersionNumber(mContext);
        String appname = VersionUtils.getApplicationName(mContext);
        String appnameversion = mContext.getString(R.string.oi_distribution_name_and_version, appname, version);
        
        StringBuilder sb = new StringBuilder();
        sb.append(appnameversion);
        sb.append("\n\n");
        sb.append(mMessageText);
        setMessage(sb.toString());
        
        setButton(mContext.getText(R.string.oi_distribution_update_check_now), this);
    }

	public void onClick(DialogInterface dialog, int which) {
		final Intent intent  = new Intent(Intent.ACTION_VIEW);
		
    	if (which == BUTTON1) {
    		
    		// TODO: Obtain this resId properly from
    		// Manifest or about.xml
    		int resId = R.string.about_website_url;
    		
			intent.setData(Uri.parse(mContext.getString(resId)));
			startSaveActivity(intent);
    	} else {
    		// BUTTON2 is handled by parent.
    		super.onClick(dialog, which);
    	}
		
	}
	
	/**
	 * Check if no updater application is installed.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isUpdateMenuNecessary(Context context) {
		PackageInfo pi = null;
		
		// Test for existence of all known update checker applications.
		for (int i = 0; i < UPDATE_CHECKER.length; i++) {
			try {
				pi = context.getPackageManager().getPackageInfo(
						UPDATE_CHECKER[i], 0);
			} catch (NameNotFoundException e) {
				// ignore
			}
			if (pi != null && !DEBUG_NO_MARKET) {
				// At least one kind of update checker exists,
				// so there is no need to add a menu item.
				return false;
			}
		}
		
		// If we reach this point, we add a menu item for manual update.
		return true; 
	}

}
