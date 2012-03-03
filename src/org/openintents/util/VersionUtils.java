/* 
 * Copyright (C) 2007-2009 OpenIntents.org
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

package org.openintents.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

/**
 * 
 * @version 2011-01-22
 * @author Peli
 *
 */
public class VersionUtils {
	
	private static final String TAG = "VersionUtils";

	/**
	 * Get current version code.
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int version = 0;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return version;
	}
	
	/**
	 * Get current version number.
	 * 
	 * @return
	 */
	public static String getVersionNumber(Context context) {
		String version = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return version;
	}
	
	/**
	 * Get application name.
	 * 
	 * Since API level 4 this routine could be replaced by
	 * appname = getString(getApplicationInfo().labelRes);
	 * 
	 * @return
	 */
	public static String getApplicationName(Context context) {
		String name = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			name = context.getString(pi.applicationInfo.labelRes);
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return name;
	}

	/**
	 * Get application icon.
	 * 
	 * Since API level 4 this routine could be replaced by
	 * icon = getApplicationInfo().icon;
	 * 
	 * @return
	 */
	public static int getApplicationIcon(Context context) {
		int icon = 0;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			icon = pi.applicationInfo.icon;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return icon;
	}

	/**
	 * Indicates whether a specific package with minimum version code is available.
	 */
	public static boolean isPackageAvailable(final Context context, final String packageName,
			final int minVersionCode) {
		boolean result = false;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					packageName, 0);
			if (pi.versionCode >= minVersionCode) {
				result = true;
			}
	    } catch (PackageManager.NameNotFoundException e) {
	    	
	    }
	    return result;
	}
	
	public static int getAndroidSDKLevel() {
      int sdkInt;
	  try {
		  
		  // this is the non-deprecated way to get the version: 
		  //
		  // sdkInt = Build.VERSION.SDK_INT;
		  //
		  // ... but on Cupcake it will cause a VerifyError exception 
		  // the first time any class references anything in VersionUtils. 
		  //
	      // So for now we use the deprecated string version. However we should switch to
		  // just referencing Build.VERSION.SDK if we ever have other reasons to 
	      // stop supporting Android 1.5.
		  //
	      sdkInt = Integer.parseInt(Build.VERSION.SDK);
	    } catch (NumberFormatException nfe) {
	      // Just to be safe
	      sdkInt = 10000;
	    }
	    return sdkInt;
	}
}
