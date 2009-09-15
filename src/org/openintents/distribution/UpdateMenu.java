/* 
 * Copyright (C) 2008 OpenIntents.org
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

package org.openintents.distribution;

// Version Nov 12, 2008

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class UpdateMenu {
	
	private static final String TAG = "UpdateMenu";
	
	public static final String UPDATE_CHECKER = "org.openintents.updatechecker";

	/**
	 * Adds a menu item for update only if update checker is not installed.
	 * 
	 * @param context
	 * @param menu
	 * @param groupId
	 * @param itemId
	 * @param order
	 * @param titleRes
	 * @return
	 */
	public static MenuItem addUpdateMenu(Context context, Menu menu, int groupId,
			int itemId, int order, int titleRes) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(
					UPDATE_CHECKER, 0);
		} catch (NameNotFoundException e) {
			// ignore
		}
		if (pi == null) {
			return menu.add(groupId, itemId, order, titleRes).setIcon(
					android.R.drawable.ic_menu_info_details).setShortcut('9',
					'u');
		} else {
			return null;
		}
	}
	

	/**
	 * Shows dialog box with option to upgrade.
	 * 
	 * @param context
	 */
	public static void showUpdateBox(final Context context) {
		String version = null;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		final Intent intent  = new Intent(Intent.ACTION_VIEW);
		new Builder(context).setMessage(context.getString(RD.string.update_box_text, version))
		.setPositiveButton(RD.string.update_check_now, new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				intent.setData(Uri.parse(context.getString(RD.string.update_app_url)));
				startSaveActivity(context, intent);
			}
			
		}).setNegativeButton(RD.string.update_get_updater, new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				intent.setData(Uri.parse(context.getString(RD.string.update_checker_url)));
				startSaveActivity(context, intent);
			}
			
		}).show();		
	}
	
	/**
	 * Start an activity but prompt a toast if activity is not found
	 * (instead of crashing).
	 * 
	 * @param context
	 * @param intent
	 */
	private static void startSaveActivity(Context context, Intent intent) {
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
