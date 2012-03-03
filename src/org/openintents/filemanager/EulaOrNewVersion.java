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

import org.openintents.util.VersionUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;



/**
 * Displays the Eula for the first time, reading it from a raw resource.
 * 
 * @author Peli
 *
 */
public class EulaOrNewVersion {
	/** TAG for log messages. */
	private static final String TAG = "EulaOrNewVersion";
	private static final boolean debug = !false;
	
	public static final String PREFERENCES_EULA_ACCEPTED = "eula_accepted";

	public static final String PREFERENCES_VERSION_NUMBER = "org.openintents.cmfilemanager.version_number_check";
	
	/**
	 * Extra for main intent.
	 * Specifies activity that should be launched after Eula has been accepted.
	 */
	static final String EXTRA_LAUNCH_ACTIVITY_PACKAGE = "org.openintents.extra.launch_activity_package";
	static final String EXTRA_LAUNCH_ACTIVITY_CLASS = "org.openintents.extra.launch_activity_class";
	static final String EXTRA_LAUNCH_ACTIVITY_INTENT = "org.openintents.extra.launch_activity_intent";
	
	/**
	 * Test whether EULA has been accepted. Otherwise display EULA.
	 * 
	 * @return True if Eula needs to be shown.
	 */
	static boolean showEula(Activity activity) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean accepted = sp.getBoolean(PREFERENCES_EULA_ACCEPTED, false);
		
		if (accepted) {
			if (debug) Log.i(TAG, "Eula has been accepted.");
			return false;
		} else {
			if (debug) Log.i(TAG, "Eula has not been accepted yet.");
			
			startForwardActivity(activity, EulaActivity.class);
			return true;
		}
	}
	
	static void storeEulaAccepted(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor e = sp.edit();
		e.putBoolean(PREFERENCES_EULA_ACCEPTED, true);
		e.commit();
	}
	
	/**
	 * Test whether version code changed.
	 * 
	 * @return True if version code changed and recent changes are being shown.
	 */
	static boolean showNewVersion(Activity activity) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		int lastVersion = sp.getInt(PREFERENCES_VERSION_NUMBER, 0);
		int thisVersion = VersionUtils.getVersionCode(activity);
		
		if (lastVersion == thisVersion) {
			if (debug) Log.i(TAG, "Same version " + lastVersion + " as last launch.");
			return false;
		} else {
			if (debug) Log.i(TAG, "Newer version " + thisVersion + " since last launch " + lastVersion + ". Show recent changes.");
			
			startForwardActivity(activity, NewVersionActivity.class);
			return true;
		}
	}
	
	static void storeCurrentVersionCode(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int thisVersion = VersionUtils.getVersionCode(context);
		SharedPreferences.Editor e = sp.edit();
		e.putInt(PREFERENCES_VERSION_NUMBER, thisVersion);
		e.commit();
	}

	private static void startForwardActivity(Activity activity, Class launchClass) {
		// Launch Eula activity
		Intent forwardIntent = activity.getIntent();
		
		Intent i = new Intent(activity, launchClass);
		ComponentName ci = activity.getComponentName();
		
		// Specify in intent extras which activity should be called
		// after Eula has been accepted.
		if (debug) Log.d(TAG, "Local package name: " + ci.getPackageName());
		if (debug) Log.d(TAG, "Local class name: " + ci.getClassName());
		i.putExtra(EXTRA_LAUNCH_ACTIVITY_PACKAGE, ci.getPackageName());
		i.putExtra(EXTRA_LAUNCH_ACTIVITY_CLASS, ci.getClassName());
		if (forwardIntent != null) {
			i.putExtra(EXTRA_LAUNCH_ACTIVITY_INTENT, forwardIntent);
		}
		i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
		activity.startActivity(i);
		activity.finish();
	}
	
}
