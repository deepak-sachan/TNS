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
import android.widget.Toast;

public class NoOfCornerDetailsAdapter extends CursorAdapter{
	
		public NoOfCornerDetailsAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor) {
			// TODO Auto-generated method stub
			    TextView textViewIPID = (TextView) view.findViewById(R.id.txtIPIDN);
			    textViewIPID.setText(cursor.getString(cursor.getColumnIndex("vSiteIdCode")));	        
		        
		        EditText ed = (EditText)view.findViewById(R.id.edNoCorners);
		        ed.setText("");
		        
		        EditText edHeight = (EditText)view.findViewById(R.id.edHeight);
		        edHeight.setText("");
		        
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
		        View retView = inflater.inflate(R.layout.numberofcornerdetailsonerow,arg2, false);
		        Log.w("my","view");
		        return retView;
		}
		
		
		
	}

	
	

