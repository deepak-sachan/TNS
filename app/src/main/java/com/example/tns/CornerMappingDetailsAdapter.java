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

public class CornerMappingDetailsAdapter extends CursorAdapter{
	
	public CornerMappingDetailsAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		// TODO Auto-generated method stub
		    TextView textViewOperator = (TextView) view.findViewById(R.id.txtOperator);
		    textViewOperator.setText(cursor.getString(cursor.getColumnIndex("vOpratorSiteId")));	        
	        
		    TextView textViewAntennaNo = (TextView) view.findViewById(R.id.txtAntennaNo);
		    textViewAntennaNo.setText(cursor.getString(cursor.getColumnIndex("nAnteenaNo")));
		    
		    TextView textViewCornerNo = (TextView) view.findViewById(R.id.txtCornerNo);
		    textViewCornerNo.setText(cursor.getString(cursor.getColumnIndex("nCornerNo")));	    
	        
	        
	        EditText edHeight = (EditText)view.findViewById(R.id.edHeight);
	        
	        edHeight.setText(cursor.getString(cursor.getColumnIndex("nCornerHeight")));
	        
	        EditText ed = (EditText)view.findViewById(R.id.edDistance);
	        ed.setText(cursor.getString(cursor.getColumnIndex("nDistance")));
	        
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
	        View retView = inflater.inflate(R.layout.cornermappingdetailsonerow,arg2, false);
	        
	        return retView;
	}
	
	
	
}
