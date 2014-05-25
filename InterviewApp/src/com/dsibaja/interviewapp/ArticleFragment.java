package com.dsibaja.interviewapp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ArticleFragment extends Fragment {
	
	private static List<Venue> mlistVenue;
	private Bitmap mBitmap;
	final static String ARG_POSITION = "position";
	private int mCurrentPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(savedInstanceState != null){
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		}
		return inflater.inflate(R.layout.article_view, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition);
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		if(args != null){	
			updateArticleView(args.getInt(ARG_POSITION));	
		}else if(mCurrentPosition != -1){
			updateArticleView(mCurrentPosition);
		}
	}
	
	public void updateArticleView(int position){
		TextView dataName = (TextView)getActivity().findViewById(R.id.data_name);
		TextView dataAddress = (TextView)getActivity().findViewById(R.id.data_address);
		ImageView image = (ImageView)getActivity().findViewById(R.id.portrait);
		
		if(!getMlistVenue().get(position).getImageUrl().toString().equals("")){
			mBitmap = getBitmapFromURL(getMlistVenue().get(position).getImageUrl());	
			image.setImageBitmap(mBitmap);
		}
		
		if(getMlistVenue().get(position).getSchedule() != null){
			for (ScheduleItem schedule : getMlistVenue().get(position).getSchedule()) {
				Calendar calStart = new GregorianCalendar();
				Calendar calEnd = new GregorianCalendar();
				calStart.setTime(schedule.getStartDate());
				calEnd.setTime(schedule.getEndDate());
				String date = getFriendlyDate(calStart, calEnd);
				createTextView(date);
			}
		}
		
		dataName.setText(getMlistVenue().get(position).getName().toString());
		dataAddress.setText(getMlistVenue().get(position).getAddress().toString());
		mCurrentPosition = position;
	}
	
	public Bitmap getBitmapFromURL(String src){
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
	
	private String getFriendlyDate(Calendar calStart, Calendar calEnd){
		
		String startHour = "";
		String endHour = "";
		int year = calStart.get(Calendar.YEAR);
		int month = calStart.get(Calendar.MONTH) + 1;
		int dayOfMonth = calStart.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek  = calStart.get(Calendar.DAY_OF_WEEK);
		int startHourOfDay = calStart.get(Calendar.HOUR_OF_DAY);
		int endHourOfDay = calEnd.get(Calendar.HOUR_OF_DAY);
		String friendlyDay = "";
		String result = "";
		
		
		friendlyDay = getFriendlyDay(dayOfWeek);
		startHour = getAMorPMHour(startHourOfDay);
		endHour = getAMorPMHour(endHourOfDay);
		
		
		result = friendlyDay + " " + month + "/" + dayOfMonth + " " + startHour + " to " + endHour;
		
		return result;
	}

	private String getAMorPMHour(int hour){
		String result = "";
		
		if (hour == 0) {
			result =  "12am (Midnight)";  
		} else if (hour < 12) {
			result = hour +"am";
		} else if (hour == 12) {
			result = "12pm (Noon)";
		} else {
			result = hour-12 +"pm";
		}
		
		return result;
		
	}
	
	private String getFriendlyDay(int dayOfWeek){
		
		String result = "";
		
		switch (dayOfWeek)
		{
			case 1:
				result = "Sunday";
				break;
			case 2:
				result = "Monday";
				break;
			case 3:
				result = "Tuesday";
				break;
			case 4:
				result = "Wednesday";
				break;
			case 5:
				result = "Thursday";
				break;
			case 6:
				result = "Friday";
				break;
			case 7:
				result = "Saturday";
				break;
			default:
				result = "BadDayValue";
				break;
		}
		
		return result;
		
	}
	
	private void createTextView(String data){
		TableLayout tableLayout = (TableLayout)getActivity().findViewById(R.id.tableForArticle);
		TableRow tableRow = new TableRow(getActivity());
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		TextView textView = new TextView(getActivity());
		textView.setTextSize(15);
		textView.setPadding(55, 20, 20, 20);
		textView.setText(data);
		tableLayout.addView(tableRow);
		tableRow.addView(textView);
	}
	
	public static List<Venue> getMlistVenue() {
		return mlistVenue;
	}
	
	public static void setMlistVenue(List<Venue> list) {
		ArticleFragment.mlistVenue = list;
		
	}

}
