package com.dsibaja.interviewapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VenueLogic {
	
	public static List<Venue> getListVenue(String result, List<Venue> venueList) {
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
	
	private static Venue fillVenue(JSONObject venueObject){
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
	
	private static ScheduleItem fillSchedule(JSONObject scheduleObject) throws JSONException{
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
