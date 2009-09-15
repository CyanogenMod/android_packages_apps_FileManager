package org.openintents.filemanager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openintents.filemanager.util.MimeTypes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class DirectoryScanner extends Thread {

	private static final String TAG = "OIFM_DirScanner";
	
	private File currentDirectory;
	boolean cancel;

	private String mSdCardPath;
	private Context context;
    private MimeTypes mMimeTypes;
	private Handler handler;
	private long operationStartTime;
	
	// Update progress bar every n files
	static final private int PROGRESS_STEPS = 50;

	// Cupcake-specific methods
    static Method formatter_formatFileSize;

    static {
    	initializeCupcakeInterface();
    }
    


	DirectoryScanner(File directory, Context context, Handler handler, MimeTypes mimeTypes) {
		super("Directory Scanner");
		currentDirectory = directory;
		this.context = context;
		this.handler = handler;
		this.mMimeTypes = mimeTypes;
	}
	
	private void clearData() {
		// Remove all references so we don't delay the garbage collection.
		context = null;
		mMimeTypes = null;
		handler = null;
	}

	public void run() {
		Log.v(TAG, "Scanning directory " + currentDirectory);
		
		File[] files = currentDirectory.listFiles();

		int fileCount = 0;
		int dirCount = 0;
		int sdCount = 0;
		int totalCount = 0;
		
		if (cancel) {
			Log.v(TAG, "Scan aborted");
			clearData();
			return;
		}
		
		operationStartTime = SystemClock.uptimeMillis();
		
		totalCount = files.length;
		Log.v(TAG, "Counting files... (total count=" + totalCount + ")");

		int progress = 0;
		
		/** Dir separate for sorting */
		List<IconifiedText> listDir = new ArrayList<IconifiedText>(totalCount);

		/** Files separate for sorting */
		List<IconifiedText> listFile = new ArrayList<IconifiedText>(totalCount);

		/** SD card separate for sorting */
		List<IconifiedText> listSdCard = new ArrayList<IconifiedText>(3);

		// Cache some commonly used icons.
		Drawable sdIcon = context.getResources().getDrawable(R.drawable.icon_sdcard);
		Drawable folderIcon = context.getResources().getDrawable(R.drawable.ic_launcher_folder);
		Drawable genericFileIcon = context.getResources().getDrawable(R.drawable.icon_file);

		Drawable currentIcon = null; 
		for (File currentFile : files){ 
			if (cancel) {
				// Abort!
				Log.v(TAG, "Scan aborted while checking files");
				clearData();
				return;
			}
			
			progress++;
			updateProgress(progress, totalCount);
			
			/*
        	  if (currentFile.isHidden()) {
        		  continue;
        	  }
			 */
			if (currentFile.isDirectory()) { 
				if (currentFile.getAbsolutePath().equals(mSdCardPath)) {
					currentIcon = sdIcon;

					listSdCard.add(new IconifiedText( 
							currentFile.getName(), "", currentIcon)); 
				} else {
					currentIcon = folderIcon;

					listDir.add(new IconifiedText( 
							currentFile.getName(), "", currentIcon)); 
				}
			}else{ 
				String fileName = currentFile.getName(); 

				String mimetype = mMimeTypes.getMimeType(fileName);

				currentIcon = getDrawableForMimetype(mimetype);
				if (currentIcon == null) {
					currentIcon = genericFileIcon;
				}

				String size = "";

				try {
					size = (String) formatter_formatFileSize.invoke(null, context, currentFile.length());
				} catch (Exception e) {
					// The file size method is probably null (this is most
					// likely not a Cupcake phone), or something else went wrong.
					// Let's fall back to something primitive, like just the number
					// of KB.
					size = Long.toString(currentFile.length() / 1024);
					size +=" KB";

					// Technically "KB" should come from a string resource,
					// but this is just a Cupcake 1.1 fallback, and KB is universal
					// enough.
				}

				listFile.add(new IconifiedText( 
						currentFile.getName(), size, currentIcon)); 
			} 
		}
		
		Log.v(TAG, "Sorting results...");
		
		//Collections.sort(mListSdCard); 
		Collections.sort(listDir); 
		Collections.sort(listFile); 

		if (!cancel) {
			Log.v(TAG, "Sending data back to main thread");
			
			DirectoryContents contents = new DirectoryContents();

			contents.listDir = listDir;
			contents.listFile = listFile;
			contents.listSdCard = listSdCard;

			Message msg = handler.obtainMessage(FileManagerActivity.MESSAGE_SHOW_DIRECTORY_CONTENTS);
			msg.obj = contents;
			msg.sendToTarget();
		}

		clearData();
	}
	
	private void updateProgress(int progress, int maxProgress) {
		// Only update the progress bar every n steps...
		if ((progress % PROGRESS_STEPS) == 0) {
			// Also don't update for the first second.
			long curTime = SystemClock.uptimeMillis();
			
			if (curTime - operationStartTime < 1000L) {
				return;
			}
			
			// Okay, send an update.
			Message msg = handler.obtainMessage(FileManagerActivity.MESSAGE_SET_PROGRESS);
			msg.arg1 = progress;
			msg.arg2 = maxProgress;
			msg.sendToTarget();
		}
	}

	/**
     * Return the Drawable that is associated with a specific mime type
     * for the VIEW action.
     * 
     * @param mimetype
     * @return
     */
    Drawable getDrawableForMimetype(String mimetype) {
   	 PackageManager pm = context.getPackageManager();
   	 
   	 Intent intent = new Intent(Intent.ACTION_VIEW);
   	 intent.setType(mimetype);
   	 
   	 final List<ResolveInfo> lri = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
   	 
   	 if (lri != null && lri.size() > 0) {
   		 // return first element
   		 final ResolveInfo ri = lri.get(0);
   		 return ri.loadIcon(pm);
   	 }
   	 
   	 return null;
    }

    private static void initializeCupcakeInterface() {
        try {
            formatter_formatFileSize = Class.forName("android.text.format.Formatter").getMethod("formatFileSize", Context.class, long.class);
        } catch (Exception ex) {
       	 // This is not cupcake.
       	 return;
        }
    }
}
	
