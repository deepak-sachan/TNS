package com.example.tns;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class UploadImageDataToServerActivity extends Activity{
	
	AccessData objAdapter;
	Cursor dataUpload;
	JSONObject jObj;
	String postString, postData;
	int countImages=0;
	String[] urls = new String[]{};
	JSONArray jArray;
	JSONObject jMain;
	ProgressDialog progressDialog;
	ArrayList<String> imgName , imgType , imgPath,imgSiteID;
	String ipid="";
	int total = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.uploaddata);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
        ipid = getIntent().getStringExtra("ipid");
       //Toast.makeText(this,ipid,Toast.LENGTH_SHORT).show();
        fetchImagesFromServer();
        
//        progressDialog = new ProgressDialog(UploadImageDataToServerActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//       
//        progressDialog.setMax(countImages);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setMessage("Uploading image data....");
//        progressDialog.setCancelable(false);
        
    	new SendJSONFeedTask().execute("http://tnssofts.com/Ashwani/ReqService.svc/FileUpload/");
        
	
	}	
	
	public void fetchImagesFromServer(){
		objAdapter = new AccessData(this);
		objAdapter.openDataBase();
		Cursor images = objAdapter.fetchImagePath();
		imgName = new ArrayList<String>();
		imgType = new ArrayList<String>();
		imgPath = new ArrayList<String>();
		imgSiteID = new ArrayList<String>();
		if(images!=null){
			while(images.moveToNext()){	
				 if((images.getString(1)).equalsIgnoreCase(ipid)){	
				imgPath.add(images.getString(2));				
				imgName.add(images.getString(3));				
				imgType.add(images.getString(4));	
				imgSiteID.add(images.getString(1));
				countImages++;
				 }
			}
		}
		else{
			Toast.makeText(this,"Not Any Images to Upload", Toast.LENGTH_SHORT).show();
			this.finish();
		}
			 
	}
		
	//Fetch Building details.
	public String getBuildingDetails(){
	 Cursor bldgDetails = objAdapter.getAllBuildingDetails();
	 try {
		 while(bldgDetails.moveToNext() && bldgDetails!=null){
			 jObj = new JSONObject();
				jObj.put("vSiteIDCode",dataUpload.getString(0));
				jObj.put("vOpratorSiteID", dataUpload.getString(1));
	        	jObj.put("nAnteenaNo", dataUpload.getString(2));
	        	jObj.put("nBuildingNo", dataUpload.getString(3));
	        	jObj.put("nNoofFloor", dataUpload.getString(4));
	        	jObj.put("nAntBuildingDistance", dataUpload.getString(5));
	        	jObj.put("nAziAngle", dataUpload.getString(6));
	        	jObj.put("nLat", dataUpload.getString(7));
	        	jObj.put("nLong", dataUpload.getString(8));
	        	jArray.put(jObj);
			}
		  	jMain.put("obj",jArray);
	    }
        catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return jMain.toString();
	}
	
	public String getCornerDetails(){
		 Cursor bldgDetails = objAdapter.getAllAntenaDetailsUpload();
		 try {
			 while(bldgDetails.moveToNext() && bldgDetails!=null){
				 jObj = new JSONObject();
				 jObj.put("vSiteIDCode",dataUpload.getString(0));
				 jObj.put("vOpratorSiteID", dataUpload.getString(1));
		         jObj.put("nAnteenaNo", dataUpload.getString(2));
		         jObj.put("nCornerNo",dataUpload.getString(3));
		         jObj.put("nDistance",dataUpload.getString(4));
				 jObj.put("nCornerHeight",dataUpload.getString(5));
		         jObj.put("nLat", dataUpload.getString(6));
				 jObj.put("nLong", dataUpload.getString(7));				 		           
		        jArray.put(jObj);
			 }
			  	jMain.put("obj",jArray);
		   }
	        catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			return jMain.toString();
		}
	
	private void obtainedData(String data){	
		
			//Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
			this.finish();
//			Intent uploaddata = new Intent(this,Upload.class);
//			startActivity(uploaddata);
		
	}
	
	private class SendJSONFeedTask extends AsyncTask<String ,Integer,String>{
		  
		String urlServer =""; 
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(UploadImageDataToServerActivity.this);
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        progressDialog.setMessage("Loading..."+"0/"+countImages);
		        progressDialog.setMax(100);
		        progressDialog.setIndeterminate(false);
		        progressDialog.setTitle("Uploading image data....");
		        progressDialog.setCancelable(false);
		        progressDialog.show();
		    }

		
		@Override
		protected String doInBackground(String... params) {

//		       Log.d("Savepoint 1", "Done");

		       //String urlServer = "http://tnssofts.com/Ashwani/ReqService.svc/FileUpload1/xyz.png";
		       int bytesRead, bytesAvailable, bufferSize;
		           byte[] buffer;
		           int maxBufferSize = 1*1024*1024;
		           FileInputStream fileInputStream;

		        //DataInputStream DinputStream = null;
		        DataOutputStream DoutputStream = null ;		        
		        
		        
		        HttpURLConnection connection;
		        String serverResponseString="", response="";
		        total = 0;
		        for(int i=0;i<countImages;i++){
		        	
		         try
		           {
		        	 //FileUPload/filename~siteidcode~type~angle
		        	 String urlsent = params[0];
		        	 //imei + "~" +vDevicename
		        	 urlServer = urlsent + imgName.get(i)+".png"+"~"+imgSiteID.get(i)+"~"+imgType.get(i)+"~"+imgName.get(i);
		        	// urlServer = urlsent + "test9feb.jpeg";
		        	 //fileInputStream =  openFileInput(imgName.get(i));
		           fileInputStream = new FileInputStream(imgPath.get(i));

		           URL url = new URL(urlServer);
		           
		           
		           connection = (HttpURLConnection) url.openConnection();

		           // Allow Inputs & Outputs
		           connection.setDoInput(true);
		           connection.setDoOutput(true);
		           connection.setChunkedStreamingMode(0);
		           connection.setUseCaches(false);
		           
		           // Enable POST method
		           connection.setRequestMethod("POST");
		           
		           connection.setRequestProperty("Connection", "Keep-Alive");
		           connection.setRequestProperty("Content-Type",  "multipart/form-data");
		           //connection.setRequestProperty("SD-FileName",imgName.get(i)+".png");//This will be the file name
		           connection.setRequestProperty("SD-FileName",imgName.get(i)+".png");//This will be the file name
		           Log.d("Savepoint 2", "Done");

		           OutputStream out = new BufferedOutputStream( connection.getOutputStream());
		           DoutputStream = new DataOutputStream(out);

		           bytesAvailable = fileInputStream.available();
		           bufferSize = Math.min(bytesAvailable, maxBufferSize);
		           buffer = new byte[bufferSize];

		           // Read file
		           bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		           while (bytesRead > 0)
		           {
		               DoutputStream.write(buffer, 0, bufferSize);
		               bytesAvailable = fileInputStream.available();
		               bufferSize = Math.min(bytesAvailable, maxBufferSize);
		               bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		           }

		            int serverResponseCode = connection.getResponseCode();
		            String serverResponseMessage = connection.getResponseMessage();		            
		            Log.w("ServerCode",""+serverResponseCode);
		            Log.w("serverResponseMessage",""+serverResponseMessage);
		            serverResponseString =serverResponseMessage.toString();

		           fileInputStream.close(); 
		           DoutputStream.flush();
		           DoutputStream.close();


		           //reading response from server.........
		           String line = "";           
		           InputStreamReader isr = new InputStreamReader(connection.getInputStream());
		           BufferedReader reader = new BufferedReader(isr);
		           StringBuilder sb = new StringBuilder();
		           
		           while ((line = reader.readLine()) != null)
		           {
		        	  // total ++;
		               sb.append(line + "\n");
		              // publishProgress((int)(total * 100)/countImages);
		           }

		           

		           // Response from server after login process will be stored in response variable.                
		           response = sb.toString();

		           Log.d("Response", response);
		           serverResponseString = serverResponseString + " ,and the response is: " + response;
		           isr.close();
		           reader.close();


		           }
		           catch (Exception ex)
		           {
		               //ex.printStackTrace();
		               Log.e("Error: ", ex.toString());

		           }
		           total ++;
	               publishProgress((int)(total * 100)/countImages);
		           
		        }
			return serverResponseString;
		}
		
		 @Override
		    protected void onProgressUpdate(Integer... progress) {
		        super.onProgressUpdate(progress);
		   /*     if (progress[0] < 20) {            
		        	progressDialog.setProgress(0);
		        } else if (progress[0] < 40) {            
		        	progressDialog.setProgress(20);
		        }
		        else if (progress[0] < 60) {            
		        	progressDialog.setProgress(40);
		        }
		        else if (progress[0] < 80) {            
		        	progressDialog.setProgress(60);
		        }
		        else if (progress[0] < 100) {            
		        	progressDialog.setProgress(80);
		        }
		        else if (progress[0] == 100) {            
		        	progressDialog.setProgress(100);
		        }*/
		        progressDialog.setProgress((int) (progress[0]));
		        progressDialog.setMessage("Loading " + total+"/"+countImages); 
		    }
		
		 @Override
		protected void onPostExecute(String data){
			 
			 progressDialog.dismiss();
			 total = 0;
			UploadImageDataToServerActivity.this.obtainedData(data);
			
		}	
	}
}

