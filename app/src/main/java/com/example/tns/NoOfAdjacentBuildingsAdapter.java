package com.example.tns;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NoOfAdjacentBuildingsAdapter extends CursorAdapter{
	
	public NoOfAdjacentBuildingsAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		// TODO Auto-generated method stub
		    TextView textViewIPID = (TextView) view.findViewById(R.id.txtIPIDN);
		    textViewIPID.setText(cursor.getString(cursor.getColumnIndex("vSiteIdCode")));        
	        EditText edHeight = (EditText)view.findViewById(R.id.edNoAdjBuildings);
	        edHeight.setText("");
	        
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
	        View retView = inflater.inflate(R.layout.nofadjacentbuildingsonerow,arg2, false);
	        
	        return retView;
	}
	
	
	
}




