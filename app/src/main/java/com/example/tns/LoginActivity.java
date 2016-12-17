package com.example.tns;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnClickListener{
	
	Button btnLogin;
	EditText edUsername , edPassword;
	AccessData accessServerDataAdapter;
	String user,pass;
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		edUsername = (EditText)findViewById(R.id.edUsername);
		edPassword = (EditText)findViewById(R.id.edPassword);
		accessServerDataAdapter = new AccessData(this);
		accessServerDataAdapter.openDataBase();
		Cursor value =accessServerDataAdapter.getLoginDetails();
		while(value!=null && value.moveToNext()){
			//Toast.makeText(this,"Cursor has values",Toast.LENGTH_SHORT).show();
			 user = value.getString(value.getColumnIndex("username"));
			 pass = value.getString(value.getColumnIndex("password"));
		} 
		edUsername.setText(user);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//
		String password = edPassword.getText().toString();
		
		if(password.equalsIgnoreCase(pass)){
			prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor prefEditor = prefs.edit();
			prefEditor.putString("LoginSts", "on");
		    prefEditor.commit();
			
		//	Toast.makeText(this,"Credentials Valid!",Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
			String vAndroidDeviceId = Secure.getString(this.getContentResolver(),
	                Secure.ANDROID_ID);
			b.putString("imei",vAndroidDeviceId);
			Intent i = new Intent(this,AllOptionsActivity.class);
			i.putExtras(b);
			startActivity(i);
			finish();
			
		}
		else{
			//Toast.makeText(this,"Credentials Invalid!",Toast.LENGTH_SHORT).show();
		}		
		
	}
	
	
	public void onDestroy(){
		super.onDestroy();
		accessServerDataAdapter.close();
	}
}	