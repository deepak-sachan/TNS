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

public class AntennaDetailsCursorAdapter extends CursorAdapter {

	public AntennaDetailsCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		 TextView textViewPersonName = (TextView) view.findViewById(R.id.txtAddress);
	        textViewPersonName.setText(cursor.getString(cursor.getColumnIndex("vAddress")));

	        TextView textViewPersonPIN = (TextView) view.findViewById(R.id.txtIPID);
	        textViewPersonPIN.setText(cursor.getString(cursor.getColumnIndex("vSiteIdCode")));
	        
	        TextView txtOperator = (TextView) view.findViewById(R.id.txtOperator);
	        txtOperator.setText(cursor.getString(cursor.getColumnIndex("vOpratorName")));
	        
	        EditText edAntenna = (EditText)view.findViewById(R.id.edAntennaNo);
	        edAntenna.setText(cursor.getString(cursor.getColumnIndex("Antena")));
	        
	        TextView txtOperatorID = (TextView) view.findViewById(R.id.hiddenOpratorID);
	        txtOperatorID.setText(cursor.getString(2));
	        
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
		 Log.w("new","viewcall");
	        View retView = inflater.inflate(R.layout.antennadetailsonerow,arg2, false);
Log.w("new","view");
	        return retView;
	}


}
