package com.dsibaja.interviewapp;

import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GUIController {
	
	public static void createTextView(String data, FragmentActivity context){
		TableLayout tableLayout = (TableLayout)context.findViewById(R.id.tableForArticle);
		TableRow tableRow = new TableRow(context);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		TextView textView = new TextView(context);
		textView.setTextSize(15);
		textView.setPadding(55, 20, 20, 20);
		textView.setText(data);
		tableLayout.addView(tableRow);
		tableRow.addView(textView);
	}

}
