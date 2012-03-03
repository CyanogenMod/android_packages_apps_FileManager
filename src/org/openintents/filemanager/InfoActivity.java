package org.openintents.cmfilemanager;

import org.openintents.util.VersionUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends DistributionLibraryListActivity implements OnItemClickListener {

	// Override the following variables in init()
    protected int[] mApplications = {};
    protected String[] mPackageNames = {};
    protected int[] mMinVersionCodes = {};
    protected String[] mMinVersionName = {};
    protected int[] mInfoText = {};
    protected String[] mDeveloperUris = {};
    protected String[] mIntentAction = {};
    protected String[] mIntentData = {};
    
    //
	
	private static final int MENU_DISTRIBUTION_START = Menu.FIRST + 100; // MUST BE LAST

	public static final int DIALOG_INFO = 0;
	public static final int DIALOG_GET_FROM_MARKET = 100;
	private static final int DIALOG_DISTRIBUTION_START = 200; // MUST BE LAST

	
    private String[] mApplicationStrings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDistribution.setFirst(MENU_DISTRIBUTION_START, DIALOG_DISTRIBUTION_START);
        
        // Check whether EULA has been accepted
        // or information about new version can be presented.
        if (mDistribution.showEulaOrNewVersion()) {
            return;
        }
        
        setContentView(R.layout.oi_distribution_infoactivity);

        init();
        
        mApplicationStrings = new String[mApplications.length];
        for (int i = 0; i < mApplications.length; i++) {
        	mApplicationStrings[i] = getString(mApplications[i]);
        }
        setListAdapter(new FontArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mApplicationStrings));
        ListView listview = getListView();
        
        listview.setOnItemClickListener(this);
        
        // Set message of activity
        String appname = VersionUtils.getApplicationName(this);
		String message = getString(R.string.oi_distribution_info_activity_text, 
				appname);
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(message);
        
        /*
        TypedArray a = obtainStyledAttributes(mTheme, R.styleable.ShoppingList);
		String typefaceName = a.getString(R.styleable.ShoppingList_textTypeface);
	    mTextSizeMedium = a.getDimensionPixelOffset(R.styleable.ShoppingList_textSizeMedium, 23);
	    mTextSizeLarge = a.getDimensionPixelOffset(R.styleable.ShoppingList_textSizeLarge, 28);
	    mTextColor = a.getColor(R.styleable.ShoppingList_textColor, Color.BLACK);
	    Drawable background = a.getDrawable(R.styleable.ShoppingList_background);

	    
	    View v = findViewById(R.id.background);
	    v.setBackgroundDrawable(background);
	    
		mTypeface = Typeface.createFromAsset(getResources().getAssets(), typefaceName);
        
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setTypeface(mTypeface);
        tv.setTextSize(mTextSizeMedium);
        tv.setTextColor(mTextColor);
	    */
    }

    public void init() {
    	
    }

	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (VersionUtils.isPackageAvailable(this, mPackageNames[pos], mMinVersionCodes[pos])) {
			showDialog(DIALOG_INFO + pos);
		} else {
			showDialog(DIALOG_GET_FROM_MARKET + pos);
		}
	}
	
	private class FontArrayAdapter<T> extends ArrayAdapter<T> {

		public FontArrayAdapter(Context context, int textViewResourceId,
				T[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = (TextView) super.getView(position, convertView, parent);
			/*
			tv.setTypeface(mTypeface);
			tv.setTextSize(mTextSizeLarge);
	        tv.setTextColor(mTextColor);
			*/
			return tv;
		}
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = super.onCreateDialog(id);
		
		if (dialog != null) {
			return dialog;
		}
		
		if (id >= DIALOG_INFO && id < DIALOG_GET_FROM_MARKET) {
			dialog = buildInfoDialog(id - DIALOG_INFO);
		} else if (id >= DIALOG_GET_FROM_MARKET && id < DIALOG_DISTRIBUTION_START){
			dialog = buildGetFromMarketDialog(id - DIALOG_GET_FROM_MARKET);
		}
		/*if (dialog == null) {
			dialog = super.onCreateDialog(id);
		}*/
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		if (id >= DIALOG_INFO && id < DIALOG_GET_FROM_MARKET) {
			dialog.findViewById(android.R.id.button1).setVisibility(View.GONE);
		} else if (id >= DIALOG_GET_FROM_MARKET && id < DIALOG_DISTRIBUTION_START){
			DownloadAppDialog.onPrepareDialog(this, dialog);
		}
		
	}

	private AlertDialog buildInfoDialog(final int pos) {
		String infotext = getString(mInfoText[pos], mApplicationStrings[pos]);
		String infolaunch = getString(R.string.oi_distribution_info_launch, mApplicationStrings[pos]);
		
		// Trick for Android 2.3:
		// To achieve the visual trick of extending the button over the whole width,
		// we first set a positive button, and then set its visibility to GONE in 
		// onPrepareDialog().
		
		return new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.oi_distribution_info_instructions)
			.setMessage(infotext)
			.setPositiveButton("", null)
			.setNegativeButton(infolaunch,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// click Ok
							launchApplication(pos);
						}
					})
			.create();
	}

	void launchApplication(int pos) {
		Intent intent = new Intent();
		intent.setAction(mIntentAction[pos]);
		if (mIntentAction[pos].equals(Intent.ACTION_MAIN)) {
			// Exception for ACTION_MAIN:
			// Use data as class name.
			if (mPackageNames[pos] != null && mIntentData[pos] != null) {
				intent.setClassName(mPackageNames[pos], mIntentData[pos]);
			}
		} else {
			if (mIntentData[pos] != null) {
				intent.setData(Uri.parse(mIntentData[pos]));
			}
		}
		
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {

			Toast.makeText(this,
					R.string.oi_distribution_launch_error,
					Toast.LENGTH_SHORT).show();
		}
	}

	private AlertDialog buildGetFromMarketDialog(int pos) {
		String this_appname = VersionUtils.getApplicationName(this);
		String info_not_available = getString(R.string.oi_distribution_info_not_available, 
				this_appname, mApplicationStrings[pos], mMinVersionName[pos]);
		String download_appname = getString(mApplications[pos]);
		
		return new DownloadAppDialog(this, 
				info_not_available, 
				download_appname, 
				mPackageNames[pos], 
				mDeveloperUris[pos]);
	}
}
