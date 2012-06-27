package org.openintents.cmfilemanager;

/* $Id: BulletedTextView.java 57 2007-11-21 18:31:52Z steven $ 
 * 
 * Copyright 2007 Steven Osborn 
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

/**
 * Dec 7, 2008: Peli: Use inflated layout.
 */

import android.content.Context; 
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView; 
import android.widget.LinearLayout; 
import android.widget.TextView; 

public class IconifiedTextView extends LinearLayout { 
      
    private TextView mText; 
    private TextView mInfo; 
     private ImageView mIcon; 
     private ImageView mCheckIcon;
      
     public IconifiedTextView(Context context, final IconifiedText aIconifiedText) { 
          super(context); 
		
		// inflate rating
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		inflater.inflate(
				R.layout.filelist_item, this, true);
		
		mIcon = (ImageView) findViewById(R.id.icon);
		mText = (TextView) findViewById(R.id.text);
		mInfo = (TextView) findViewById(R.id.info);
		mCheckIcon = (ImageView) findViewById(R.id.select_icon);
     } 

     public ImageView getImageView() {
    	 return mIcon;
     }
     
     public void setText(String words) { 
         mText.setText(words); 

         int height = getHeight();
         
         if (height > 0) {
        	 ThumbnailLoader.setThumbnailHeight(height);
         }
    } 
     
     public void setInfo(String info) { 
         mInfo.setText(info);
    } 
     
     public void setIcon(Drawable bullet) { 
          mIcon.setImageDrawable(bullet); 
     }
     
    public void setIcon(Bitmap bitmap) {
    	mIcon.setImageBitmap(bitmap);
    }
	
	public void setCheckVisible(boolean visible) {
		mCheckIcon.setVisibility((visible) ? View.VISIBLE : View.GONE);
	}
	
	public void setCheckDrawable(Drawable icon) {
		mCheckIcon.setImageDrawable(icon);
	}
}
