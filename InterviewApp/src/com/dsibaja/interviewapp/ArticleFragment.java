package com.dsibaja.interviewapp;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
			mBitmap = MediaController.getBitmapFromURL(getMlistVenue().get(position).getImageUrl());	
			image.setImageBitmap(mBitmap);
		}
		
		if(getMlistVenue().get(position).getSchedule() != null){
			for (ScheduleItem schedule : getMlistVenue().get(position).getSchedule()) {
				Calendar calStart = new GregorianCalendar();
				Calendar calEnd = new GregorianCalendar();
				calStart.setTime(schedule.getStartDate());
				calEnd.setTime(schedule.getEndDate());
				String date = DateController.getFriendlyDate(calStart, calEnd);
				GUIController.createTextView(date, getActivity());
			}
		}
		dataName.setText(getMlistVenue().get(position).getName().toString());
		dataAddress.setText(getMlistVenue().get(position).getAddress().toString());
		mCurrentPosition = position;
	}
	
	public static List<Venue> getMlistVenue() {
		return mlistVenue;
	}
	
	public static void setMlistVenue(List<Venue> list) {
		ArticleFragment.mlistVenue = list;
		
	}

}
