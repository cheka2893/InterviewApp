package com.dsibaja.interviewapp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

public class MediaController {
	
	public static Bitmap getBitmapFromURL(String src){
		try{
			if (android.os.Build.VERSION.SDK_INT > 9) 
			{
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
			}
			
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap imageBitmap = BitmapFactory.decodeStream(input);			
			return imageBitmap;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
