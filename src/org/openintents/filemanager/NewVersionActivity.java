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

import android.os.Bundle;
import android.view.View;

/**
 * Displays the recent changes, reading them from a raw resource.
 * 
 * @author Peli
 *
 */
public class NewVersionActivity extends EulaActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Modifications to convert EulaActivity into
		// NewVersionActivity:
		String version = VersionUtils.getVersionNumber(this);
		String title = getString(R.string.oi_distribution_name_and_version, 
				mAppName, version);
		String message = getString(R.string.oi_distribution_newversion_message, 
				mAppName);
		message += "\n\n" + getString(R.string.oi_distribution_newversion_recent_changes);
		
		mText1.setText(title);
		mText2.setText(message);
		mText.setText(readTextFromRawResource(R.raw.recent_changes, true));
		
		mAgree.setText(R.string.oi_distribution_newversion_continue);
		mDisagree.setVisibility(View.GONE);
		View v = findViewById(R.id.space);
		v.setVisibility(View.GONE);
	}
	

	/**
	 * Accept EULA and proceed with main application.
	 */
	void accept() {
		EulaOrNewVersion.storeCurrentVersionCode(this);
		
		startOriginalActivity();
	}
}
