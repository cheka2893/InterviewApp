package com.dsibaja.interviewapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HeadlinesFragment extends ListFragment {

	final String SITE_URL = "https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";
	final String SCHEDULE_ITEM = "schedule";
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
			String result = getJSONString();
			return getListVenue(result);
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

		public String getJSONString() {
			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpGet httpGet = new HttpGet(SITE_URL);
			httpGet.setHeader("Content-type", "application/json");
			InputStream inputStream = null;
			String result = null;
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
				result = theStringBuilder.toString();
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
			return result;
		}

		public List<Venue> getListVenue(String result) {
			try {
				JSONArray venueJSON = new JSONArray(result);
				for (int i = 0; i < venueJSON.length(); i++) {
					JSONObject jsonVenueObject = venueJSON.getJSONObject(i);
					Venue ven = fillVenue(jsonVenueObject);
					venueList.add(ven);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return venueList;
		}
		
		private Venue fillVenue(JSONObject venueObject){
			Venue ven = new Venue();
			List<ScheduleItem> listSchedule = new ArrayList<ScheduleItem>();
			try {
				JSONArray scheduleArray = venueObject.getJSONArray("schedule");
				if(scheduleArray.toString().equalsIgnoreCase("[]")){
					listSchedule = null;
				}else{
					for (int i = 0; i < scheduleArray.length(); i++) {
						JSONObject obj = scheduleArray.getJSONObject(i);
						ScheduleItem scheduleItem = fillSchedule(obj);
						listSchedule.add(scheduleItem);
					}
				}
				
			}catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			try {
				ven = new Venue(venueObject.getInt("id"), venueObject.getInt("pcode"), venueObject.getDouble("latitude"), venueObject.getDouble("longitude"),
						venueObject.getString("name"), venueObject.getString("address"), venueObject.getString("city"), venueObject.getString("state"),
						venueObject.getString("zip"), venueObject.getString("phone"), venueObject.getString("tollfreephone"), venueObject.getString("description"),
						venueObject.getString("ticket_link"), venueObject.getString("image_url"), listSchedule);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			return ven;
		}
		
		private ScheduleItem fillSchedule(JSONObject scheduleObject) throws JSONException{
			ScheduleItem schedule = new ScheduleItem();
			try {
				schedule.setStartDate(scheduleObject.getString("start_date"));
				schedule.setEndDate(scheduleObject.getString("end_date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return schedule;
		}
	}
}