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

import org.openintents.intents.AboutMiniIntents;
import org.openintents.util.IntentUtils;
import org.openintents.util.VersionUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * About dialog
 *
 * @version 2009-02-04
 * @author Peli
 *
 */
public class AboutDialog extends DownloadAppDialog {
	private static final String TAG = "About";
	private static final boolean DEBUG_NO_OI_ABOUT = false;
	
	public AboutDialog(Context context) {
		super(context,
				R.string.oi_distribution_aboutapp_not_available,
				R.string.oi_distribution_aboutapp,
				R.string.oi_distribution_aboutapp_package,
				R.string.oi_distribution_aboutapp_website);

		String version = VersionUtils.getVersionNumber(mContext);
        String appname = VersionUtils.getApplicationName(mContext);
        String appnameversion = mContext.getString(R.string.oi_distribution_name_and_version, appname, version);
        
        StringBuilder sb = new StringBuilder();
        sb.append(appnameversion);
        sb.append("\n\n");
        sb.append(mMessageText);
        setMessage(sb.toString());
	}
	
	public static void showDialogOrStartActivity(Activity activity, int dialogId) {
		Intent intent = new Intent(AboutMiniIntents.ACTION_SHOW_ABOUT_DIALOG);
		intent.putExtra(AboutMiniIntents.EXTRA_PACKAGE_NAME, activity.getPackageName());
		
		if (IntentUtils.isIntentAvailable(activity, intent)) {
			activity.startActivity(intent);
		} else {
			activity.showDialog(dialogId);
		}
	}

}
