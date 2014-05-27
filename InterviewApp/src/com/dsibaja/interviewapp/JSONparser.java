package com.dsibaja.interviewapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;


public class JSONparser {
	
	private static String mResult = "";
	
	public static String getJSONString(String SITE_URL) {
		if(!mResult.equals("")){
			return mResult;
		}else{
			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpGet httpGet = new HttpGet(SITE_URL);
			httpGet.setHeader("Content-type", "application/json");
			InputStream inputStream = null;
			try {
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder theStringBuilder = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					theStringBuilder.append(line + "\n");
				}
				mResult = theStringBuilder.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return mResult;
		}
	}

}
