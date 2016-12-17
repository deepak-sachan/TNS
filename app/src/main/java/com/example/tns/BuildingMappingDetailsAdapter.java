package com.example.tns;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class BuildingMappingDetailsAdapter extends CursorAdapter{
	
	String ipid = "";
	public BuildingMappingDetailsAdapter(Context context, Cursor c,String IPID) {
		super(context, c);
		this.ipid = IPID;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		// TODO Auto-generated method stub
		    TextView textViewOperator = (TextView) view.findViewById(R.id.txtOperator);
		    textViewOperator.setText(cursor.getString(cursor.getColumnIndex("vOpratorName")));	        
	        
		    TextView textViewAntennaNo = (TextView) view.findViewById(R.id.txtAntennaNo);
		    textViewAntennaNo.setText(cursor.getString(cursor.getColumnIndex("nAnteenaNo")));
		    
		    TextView textViewBldgNo = (TextView) view.findViewById(R.id.txtBldgNo);
		    textViewBldgNo.setText(cursor.getString(cursor.getColumnIndex("nBldgNo")));	    
	        

		    TextView textViewCornerNo = (TextView) view.findViewById(R.id.txtIPIDNo);
		    textViewCornerNo.setText(ipid);	
	        
	        EditText ed = (EditText)view.findViewById(R.id.edDistance);
	        ed.setText("0.0");
	        TextView textViewhidden = (TextView) view.findViewById(R.id.hiddenOpratorID);
	        textViewhidden.setText(cursor.getString(cursor.getColumnIndex("vOpratorSiteId")));
	        
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
	        View retView = inflater.inflate(R.layout.buildingmappingdetailsonerow,arg2, false);
	        
	        return retView;
	}
	
	
	
}
