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

package org.openintents.filemanager;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class PreferenceActivity extends android.preference.PreferenceActivity {

	public static final String PREFS_MEDIASCAN = "mediascan";
	
	@Override
	protected void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);

		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	static boolean getMediaScanFromPreference(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean(PREFS_MEDIASCAN, false);
	}
}
