package com.dsibaja.interviewapp;

import java.util.Calendar;

public class DateController {
	
	public static String getFriendlyDate(Calendar calStart, Calendar calEnd){
		
		String startHour = "";
		String endHour = "";
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
	
	private static String getAMorPMHour(int hour){
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
	
	private static String getFriendlyDay(int dayOfWeek){
		
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

}
