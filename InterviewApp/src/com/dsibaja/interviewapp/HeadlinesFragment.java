package com.dsibaja.interviewapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HeadlinesFragment extends ListFragment {

	final String SITE_URL = "https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";
	private OnHeadlineSelectedListener mCallback;

	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new myAsyncTask().execute();
		
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

	private class myAsyncTask extends AsyncTask<String, String, List<Venue>> {
		List<Venue> venueList = new ArrayList<Venue>();

		@Override
		protected List<Venue> doInBackground(String... params) {
			String result = JSONparser.getJSONString(SITE_URL);
			return VenueLogic.getListVenue(result, venueList);
		}

		@Override
		protected void onPostExecute(final List<Venue> list) {
			int layout = android.R.layout.simple_list_item_2;
			setListAdapter(new ArrayAdapter<Venue>(getActivity(), layout,
					android.R.id.text1, list) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					TextView text1 = (TextView) view
							.findViewById(android.R.id.text1);
					TextView text2 = (TextView) view
							.findViewById(android.R.id.text2);
					text1.setText(list.get(position).getName());
					text2.setText(list.get(position).getAddress());
					return view;
				}
			});
			ArticleFragment.setMlistVenue(list);
		}
		
	}
}