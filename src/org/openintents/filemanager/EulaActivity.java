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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openintents.util.VersionUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Displays the Eula for the first time, reading it from a raw resource.
 * 
 * @author Peli
 *
 */
public class EulaActivity extends Activity {
	
	Button mAgree;
	Button mDisagree;
	
	String mLaunchPackage;
	String mLaunchClass;
	Intent mLaunchIntent;
	
	String mAppName;

	TextView mText1;
	TextView mText2;
	TextView mText;
	ImageView mImage;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.oi_distribution_eula);
		
		// Extras are provided by checkEula() below.
		Intent i = getIntent();
		Bundle b = i.getExtras();
		mLaunchPackage = b.getString(EulaOrNewVersion.EXTRA_LAUNCH_ACTIVITY_PACKAGE);
		mLaunchClass = b.getString(EulaOrNewVersion.EXTRA_LAUNCH_ACTIVITY_CLASS);
		//mLaunchIntent 
		mLaunchIntent = b.getParcelable(EulaOrNewVersion.EXTRA_LAUNCH_ACTIVITY_INTENT);
		
		//mIntroContinue = (Button) findViewById(R.id.intro_continue);
		mAgree = (Button) findViewById(R.id.button1);
		mAgree.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				accept();
			}
		});
		
		mDisagree = (Button) findViewById(R.id.button2);
		mDisagree.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				refuse();
			}
		});

		mText1 = (TextView) findViewById(R.id.text1);
		mText2 = (TextView) findViewById(R.id.text2);
		mText = (TextView) findViewById(R.id.text);
		mImage = (ImageView) findViewById(R.id.imageview);
		
		mAppName = VersionUtils.getApplicationName(this);
		int iconRes = VersionUtils.getApplicationIcon(this);
		
		setTitle(mAppName);
		mImage.setImageResource(iconRes);

		String title = getString(R.string.oi_distribution_eula_title, 
				mAppName);
		String message = getString(R.string.oi_distribution_eula_message, 
				mAppName);
		
		mText1.setText(title);
		mText2.setText(message);
		mText.setText(readTextFromRawResource(R.raw.license_short, false));
	}
	
	
	/**
	 * Accept EULA and proceed with main application.
	 */
	void accept() {
		EulaOrNewVersion.storeEulaAccepted(this);
		
		startOriginalActivity();
	}




	void startOriginalActivity() {
		// Call the activity that originally called checkEula()
		// or checkNewVersion()
		Intent i;
		if (mLaunchIntent != null) {
			i = mLaunchIntent;
			
			// Android 2.3: category LAUNCHER needs to be removed,
			// otherwise main activity is not called.
			i.removeCategory(Intent.CATEGORY_LAUNCHER);
		} else {
			i = new Intent();
			i.setClassName(mLaunchPackage, mLaunchClass);
		}
		i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
		startActivity(i);
		finish();
	}
	
	/**
	 * Refuse EULA.
	 */
	void refuse() {
		finish();
	}

	
	/**
	 * Read license from raw resource.
	 * @param resourceid ID of the raw resource.
	 * @return
	 */
	String readTextFromRawResource(int resourceid, boolean preserveLineBreaks) {

		// Retrieve license from resource:
		String license = "";
		Resources resources = getResources();
    		
		//Read in the license file as a big String
		BufferedReader in
		   = new BufferedReader(new InputStreamReader(
				resources.openRawResource(resourceid)));
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = in.readLine()) != null) { // Read line per line.
				if (TextUtils.isEmpty(line)) {
					// Empty line: Leave line break
					sb.append("\n\n");
				} else {
					sb.append(line);
					if (preserveLineBreaks) {
						sb.append("\n");
					} else {
						sb.append(" ");
					}
				}
			}
			license = sb.toString();
		} catch (IOException e) {
			//Should not happen.
			e.printStackTrace();
		}
		
    	
    	return license;
	}
}
