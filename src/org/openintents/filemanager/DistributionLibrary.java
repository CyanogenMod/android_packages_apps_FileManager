package org.openintents.cmfilemanager;

import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;

public class DistributionLibrary {

	public static final int OFFSET_ABOUT = 0;
	public static final int OFFSET_UPDATE = 1;
	
	/** Number of menu IDs that should be reserved
	 * for DistributionLibrary.
	 */
	public static final int MENU_COUNT = 2;
	public static final int DIALOG_COUNT = MENU_COUNT;
	
	
	Activity mActivity;
	int mFirstMenuId = 0;
	int mFirstDialogId = 0;
	
	public DistributionLibrary(Activity activity, int firstMenuId, int firstDialogId) {
		mActivity = activity;
		mFirstMenuId = firstMenuId;
		mFirstDialogId = firstDialogId;
	}
	
	public void setFirst(int firstMenuId, int firstDialogId) {
		mFirstMenuId = firstMenuId;
		mFirstDialogId = firstDialogId;
	}
	
	/**
	 * Typical usage:
	 * Put this code in the beginning of onCreate().
	 * <pre>
	 * if (DistributionLibrary.showEulaOrNewVersion(this)) {
            return;
       }
	 * </pre>
	 * 
	 * If one of the two activities is shown, they make
	 * sure that the calling intent is called again afterwards.
	 * 
	 * @param activity
	 * @return true if one of the dialogs is being shown.
	 *         In this case, onCreate() should be aborted by
	 *         returning.
	 */
	public boolean showEulaOrNewVersion() {
		return EulaOrNewVersion.showEula(mActivity) 
			|| EulaOrNewVersion.showNewVersion(mActivity);
	}
	
	public void onCreateOptionsMenu(Menu menu) {
		// Remove items first so that they don't appear twice:
		menu.removeItem(mFirstMenuId + OFFSET_UPDATE);
		menu.removeItem(mFirstMenuId + OFFSET_ABOUT);
		
		if (UpdateDialog.isUpdateMenuNecessary(mActivity)) {
			menu.add(0, mFirstMenuId + OFFSET_UPDATE, 0, R.string.oi_distribution_menu_update).setIcon(
					android.R.drawable.ic_menu_info_details).setShortcut('9', 'u');
		}
		menu.add(0, mFirstMenuId + OFFSET_ABOUT, 0, R.string.oi_distribution_about).setIcon(
 				android.R.drawable.ic_menu_info_details).setShortcut('0', 'a');
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id - mFirstMenuId) {
		case OFFSET_UPDATE:
			mActivity.showDialog(mFirstDialogId + OFFSET_UPDATE);
			return true;
		case OFFSET_ABOUT:
			AboutDialog.showDialogOrStartActivity(mActivity, 
					mFirstDialogId + OFFSET_ABOUT);
			return true;
		}
		return false;
	}

	public Dialog onCreateDialog(int id) {
		switch (id - mFirstDialogId) {
		case OFFSET_ABOUT:
			return new AboutDialog(mActivity);
		case OFFSET_UPDATE:
			return new UpdateDialog(mActivity);
		}
		return null;
	}
	
	public void onPrepareDialog(int id, Dialog dialog) {
		switch (id - mFirstDialogId) {
		case OFFSET_ABOUT:
			AboutDialog.onPrepareDialog(mActivity, dialog);
			break;
		}
	}
}
