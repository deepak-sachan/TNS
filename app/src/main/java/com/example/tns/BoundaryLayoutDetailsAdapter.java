package com.example.tns;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class BoundaryLayoutDetailsAdapter extends CursorAdapter {
	
	public BoundaryLayoutDetailsAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		// TODO Auto-generated method stub
		   TextView textViewOperator = (TextView) view.findViewById(R.id.txtBoundaryDescription);
		    textViewOperator.setText(cursor.getString(cursor.getColumnIndex("vDescription")));
		    
		    EditText edDistance = (EditText)view.findViewById(R.id.edDistance);
		    edDistance.setText("0");//
	     
		    TextView textCorner = (TextView) view.findViewById(R.id.txtBoundaryCornerNo);
		    textCorner.setText(cursor.getString(cursor.getColumnIndex("nCornerNo")));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
	        View retView = inflater.inflate(R.layout.boundarylayoutonerow,arg2, false);	        
	        return retView;
	}


}
