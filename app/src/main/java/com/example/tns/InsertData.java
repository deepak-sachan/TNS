package com.example.tns;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class InsertData {

	AccessData acData;
	String data = "";
	
	public InsertData(String d){
		this.data = d;
		
	}
	
	
	public void insertData(){	
		try{
			
			acData.createDataBase();
		}catch(Exception ex){
			Log.w("exce","createdb");
		}
		try{	
			acData.openDataBase();			
	    		//objAdapter.openDataBase();
				JSONObject objJson = new JSONObject(data);
				// Toast.makeText(this,"to array",Toast.LENGTH_LONG).show();
	         	JSONArray arr =  objJson.getJSONArray("RetSiteResult");
	        // 	Toast.makeText(this,"to array"+String.valueOf(arr.length()),Toast.LENGTH_LONG).show();
         	for(int i=0;i<arr.length();i++){
     			JSONObject objJ = arr.getJSONObject(i);
     			String value = objJ.getString("vOpratorName");     			
	         	
	         			//value = objJ.getString("VIMEI");
	         			float nATPCFactor = (float) objJ.getDouble("nATPCFactor");
	         			float nAnteenaGain= (float) objJ.getDouble("nAnteenaGain");
	         			float nAnteenaHeightAGL= (float) objJ.getDouble("nAnteenaHeightAGL");
	         			float nBuildingHeightAGL= (float) objJ.getDouble("nBuildingHeightAGL");
	         			int nCarrier_Sector= objJ.getInt("nCarrier_Sector");
	         			float nChannelFreq = (float) objJ.getDouble("nChannelFreq");
	         			float nCominerLoss= (float) objJ.getDouble("nCominerLoss");
	         			float nDTXFactor= (float) objJ.getDouble("nDTXFactor");
	         			float nLat= (float) objJ.getDouble("nLat");
	         			float nLong= (float) objJ.getDouble("nLong");
	         			float nRFCableLength= (float) objJ.getDouble("nRFCableLength");
	         			float nTotalTilt= (float) objJ.getDouble("nTotalTilt");
	         			float nTxPower= (float) objJ.getDouble("nTxPower");
	         			float nUnitLoss= (float) objJ.getDouble("nUnitLoss");
	         			float nVerticalBeamWidth= (float) objJ.getDouble("nVerticalBeamWidth");
	         			float nsideLoabAttenuation= (float) objJ.getDouble("nsideLoabAttenuation");
	         			String vAddress = objJ.getString("vAddress");
	         			String vCircle= objJ.getString("vCircle");
	         			String vIMEI= objJ.getString("vIMEI");
	         			String vMakeModal= objJ.getString("vMakeModal");
	         			String vOpratorName= objJ.getString("vOpratorName");
	         			String vOpratorSiteId= objJ.getString("vOpratorSiteId");
	         			String vSiteId= objJ.getString("vSiteId");
	         			String vSiteIdCode= objJ.getString("vSiteIdCode");
	         			String vSiteName= objJ.getString("vSiteName");
	         			String vSurveyor= objJ.getString("vSurveyor");
	         			String vSysTemType= objJ.getString("vSysTemType");
	         			String vTowerType= objJ.getString("vTowerType");
	         			String vOpratorId = objJ.getString("vOpratorId");
	         			String bcom = objJ.getString("bComplete");
	         			int bcomplete = 0;
	         			if(bcom.equalsIgnoreCase("true")){
	         				bcomplete =1; 
	         			}
	         		long	id =acData.insertSiteSurveyMaster(nATPCFactor, nAnteenaGain, nAnteenaHeightAGL, nBuildingHeightAGL, nCarrier_Sector, nChannelFreq, nCominerLoss, nDTXFactor, nLat, nLong, nRFCableLength, nTotalTilt, nTxPower, nUnitLoss, nVerticalBeamWidth, nsideLoabAttenuation, vAddress, vCircle, vIMEI, vMakeModal, vOpratorName, vOpratorSiteId, vSiteId, vSiteIdCode, vSiteName, vSurveyor, vSysTemType, vTowerType, vOpratorId,bcomplete);
	         		Log.w("insert","made");
     			//Log.w(String.valueOf(id),"id");
     			if(id > 0){
     		//		Toast.makeText(this,"data downloaded",Toast.LENGTH_LONG).show();
     			}
         	}
		}catch(Exception ex){
			//	Toast.makeText(this,"data downloadedex"+ex.getMessage(),Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
	}	
}
