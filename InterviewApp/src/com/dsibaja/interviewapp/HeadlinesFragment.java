package com.dsibaja.interviewapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HeadlinesFragment extends ListFragment {

	final String SITE_URL = "https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";
	private OnHeadlineSelectedListener mCallback;

	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(haveNetworkConnection() == true){
			callAsyncTask();
		}else{
			Toast.makeText(getActivity(), 
					"You are not connected to the internet please check your connection status and try again!", 
					Toast.LENGTH_LONG).show();
			getActivity().finish();
		}
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener interface");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onArticleSelected(position);
		getListView().setItemChecked(position, true);
	}
	
	private boolean haveNetworkConnection(){
		
		ConnectivityManager mConnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] mNetworkInfo = mConnectivityManager.getAllNetworkInfo();
		for (NetworkInfo netInfo : mNetworkInfo) {
			if(netInfo.getTypeName().equalsIgnoreCase("WIFI")){
				if(netInfo.isConnected())
					return true;
			}else if(netInfo.getTypeName().equalsIgnoreCase("MOBILE")){
				if(netInfo.isConnected())
					return true;
			}
		}
		
		return false;
	}
	
	public void callAsyncTask(){
		new myAsyncTask().execute();
	}
	
	private class myAsyncTask extends AsyncTask<String, String, List<Venue>> {
		List<Venue> mVenueList = new ArrayList<Venue>();

		@Override
		protected List<Venue> doInBackground(String... params) {
			String mResult = JSONparser.getJSONString(SITE_URL);
			return VenueLogic.getListVenue(mResult, mVenueList);
		}

		@Override
		protected void onPostExecute(final List<Venue> mList) {
			int layout = android.R.layout.simple_list_item_2;
			setListAdapter(new ArrayAdapter<Venue>(getActivity(), layout,
					android.R.id.text1, mList) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					TextView text1 = (TextView) view
							.findViewById(android.R.id.text1);
					TextView text2 = (TextView) view
							.findViewById(android.R.id.text2);
					text1.setText(mList.get(position).getName());
					text1.setTextColor(getResources().getColor(R.color.List_Text_Color_2));
					text2.setText(mList.get(position).getAddress());
					text2.setTextColor(getResources().getColor(R.color.List_Text_Color));
					
					return view;
				}
			});
			ArticleFragment.setMlistVenue(mList);
		}
		
	}
}