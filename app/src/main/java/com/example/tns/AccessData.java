package com.example.tns;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class AccessData  extends SQLiteOpenHelper{

  // Database fields
  private SQLiteDatabase database;
  
  
  //New private class
  

	  //The Android's default system path of your application database.
	    private String DB_PATH = "/data/data/com.example.sqliteappln/databases/";
	 
	    private  final static String DB_NAME = "19feb.sqlite";//30Jan.sqlite has data-31jan is latest
	    //19feb as on black tab 19thtoday,20th feb
	 
	    private SQLiteDatabase myDataBase; 
	  //  vOpratorSiteId
	 
	    public String query_corner_for_layout = "select distinct nCornerNo as _id, nDistance from SITE_CORNER_DETAILS where  vSiteIdCode = ?";
	    //select distinct nCornerNo as _id, nDistance from SITE_CORNER_DETAILS where  vSiteIdCode = ?";
	    public String SELECT_ANTENNA = "SELECT * FROM SITE_ANTEENA_DETAILS";
	    private final Context myContext;
	    private static final String SURVEY_MASTER = "Site_Survey_Master";
	    private static final String IMG_Table = "SITE_IMAGE_DETAILS";
		public static final String TABLE_COMMENTS = "comments";
		  public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_COMMENT = "comment";
		  public static final String COLUMN_vSiteIdCode ="vSiteIdCode";
		  public static final String COLUMN_vOpratorSiteId ="vOpratorSiteId";
		  public static final String COLUMN_vSiteId ="vSiteId";
		  public static final String COLUMN_nLat ="nLat";
		  public static final String COLUMN_nLong ="nLong";
		  public static final String COLUMN_vTowerType ="vTowerType";
		  public static final String COLUMN_nBuildingHeightAGL ="nBuildingHeightAGL";
		  public static final String COLUMN_nAnteenaHeightAGL ="nAnteenaHeightAGL";
		  public static final String COLUMN_nAnteenaNo ="nAnteenaNo";
		  public static final String COLUMN_nBldgNo ="nBldgNo";
		  public static final String COLUMN_nFloors ="nFloors";
		  public static final String COLUMN_nDistance ="nDistance";
		  public static final String COLUMN_nAzimuth ="nAzimuth";
		  public static final String COLUMN_nCornerNo ="nCornerNo";
		  public static final String COLUMN_nCornerHeight ="nCornerHeight";
		  
		  public static final String COLUMN_COMMENT_TITLE = "commentTitle";
		 // public static final String COLUMN_COMMENT_SUBJECT = "commentSubject";

		  //private static final String DATABASE_NAME = "commmentsnew.db";
		  private static final int DATABASE_VERSION = 1;
		  public static final String select_pending_details_query ="select distinct  vSiteIdCode as _id ,vSiteIdCode,vSiteId,vSiteName,vAddress,vCircle from Site_Survey_Master where bComplete =0";
		  public static final String select_autocomplete_pending_details_query ="select distinct  vSiteIdCode as _id ,vSiteIdCode,vSiteId,vSiteName,vAddress,vCircle from Site_Survey_Master where bComplete =0 and vSiteIdCode = ?";
		  public static final String select_autocomplete_complete_details_query ="select distinct  vSiteIdCode as _id ,vSiteIdCode,vSiteId,vSiteName,vAddress,vCircle from Site_Survey_Master where bComplete =1 and vSiteIdCode = ?";
		  public static final String select_completed_details_query ="select distinct  vSiteIdCode as _id ,vSiteIdCode,vSiteId,vSiteName,vAddress,vCircle from Site_Survey_Master where bComplete =1";
		  public static final String select_antenna_details_query = 
				  "select distinct nId as _id , vAddress,vOpratorSiteId,vSiteIdCode,vOpratorName,"+
     "(Select count(*) from SITE_ANTEENA_DETAILS where vSiteIdCode=A.vSiteIdCode and vOpratorSiteId=A.vOpratorSiteId)"+
     "as Antena,nLat,nLong from Site_Survey_Master A where vSiteIdCode= ?";
		  public static final String pending_survey_download="select  distinct vAddress as _id , vAddress,vSiteIdCode,vSiteName,vCircle,bComplete from Site_Survey_Master";
		  //public static final String query_corner_for_draw_layout="select distinct nCornerNo , nDistance from SITE_CORNER_DETAILS where vSiteIdCode = ?";
		  public static final String number_of_corner_details =  "select distinct vSiteIdCode as _id ,vSiteIdCode from Site_Survey_Master where vSiteIdCode = ?";
		  public static final String number_of_bldg_details =  "select distinct vSiteIdCode as _id ,vSiteIdCode from Site_Survey_Master where vSiteIdCode = ?";
		  public static final String update_number_of_corner_details =  "update SITE_ANTEENA_DETAILS set nCornerNo = ? and  nCornerHeight = ? where vSiteIDCode='VODABIHAR0002'";
		  public static final String check_ifComplete = "select nId as _id, bComplete from Site_Survey_Master where vSiteIdCode = ?";
		  public static final String get_buildingNo_autoDraw = "select nBuildingNo as _id, nBuildingNo from SITE_BUILDING_DETAILS where vSiteIdCode = ?";
		  
		  public static final String corner_mapping_details =  "select _id as _id  ,vOpratorSiteId,nAnteenaNo,nCornerNo,nCornerHeight,nDistance from SITE_CORNER_DETAILS  where  vSiteIdCode = ?";
		  public static final String building_mapping_details =  "select ssm.nId as _id, ssm.vOpratorName , nAnteenaNo , nBuildingNo ,  nAntBuildingDistance  from SITE_BUILDING_DETAILS sad , Site_Survey_Master ssm where ssm.vOpratorSiteId=sad.vOpratorSiteId and sad.vSiteIdCode=ssm.vSiteIdCode and sad.vSiteIDCode='VODABIHAR0002'";
		  
		  public static final String bldg_mapping_details =  "select distinct ssm.nId as _id, ssm.vOpratorName ,ssm.vOpratorSiteId, nAnteenaNo , nBldgNo , nDistance from SITE_ANTEENA_BUILDING_DETAILS sad , Site_Survey_Master ssm where  sad.vSiteIdCode=ssm.vSiteIdCode and ssm.vOpratorSiteId=sad.vOpratorSiteId  and sad.vSiteIDCode= ?";
		  
		  public static final String antenna_no_for_bldgs = "select nId as _id  , nAnteenaNo as count from SITE_BUILDING_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vOpratorSiteId = ? and sbd.vSiteIdCode = ?";
		  public static final String antenna_no_for_corners_initial= "select  nId as _id ,vOpratorSiteId  from Site_Survey_Master where  vOpratorName = 'VODA FONE' and vSiteIdCode ='VODABIHAR0002'";
		  public static final String antenna_no_for_corners = "select nId as _id  ,nAnteenaNo as count from SITE_ANTEENA_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vOpratorSiteId = ? and sbd.vSiteIdCode = ?";
		  public static final String antenna_no_for_corners_insert = "select nId as _id  , sbd.vSiteIdCode,sbd.vOpratorSiteId,sbd.nAnteenaNo from SITE_ANTEENA_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vSiteIdCode = ?";
		  
		  public static final String antenna_no_for_bldg_insert = "select distinct nId as _id  , sbd.vSiteIdCode,sbd.vOpratorSiteId,sbd.nAnteenaNo from SITE_BUILDING_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vSiteIdCode = ?";
		  
		  
		  public static final String delete_antenna_no_for_bldgs = "delete from SITE_BUILDING_DETAILS where vSiteIdCode = 'VODABIHAR0001' and vOpratorSiteId ='V001'";
		  public static final String delete_antenna_no_for_corners = "delete from SITE_ANTEENA_DETAILS  where vSiteIdCode = 'VODABIHAR0001' and vOpratorSiteId = 'V001'";
		  public static final String TAB_SITE_BUILDING_DETAILS = "SITE_BUILDING_DETAILS";
		  public static final String TAB_SITE_ANTEENA_DETAILS = "SITE_ANTEENA_DETAILS";
		  //Antenna Details
		  public static final String COLUMN_IPID = "IPID";
		  public static final String COLUMN_AntennaNo = "nAntennaNo";
		  public static final String TABLE_ANTENNA = "SITE_ANTEENA_DETAILS";
		  public static final String TABLE_ANTENNA_BLDG = "SITE_BUILDING_DETAILS";
		  public static final String TABLE_ANTENNA_CORNER = "SITE_CORNER_DETAILS";
		  public static final String COLUMN_CornerNo = "nCornerNo";
		  public static final String COLUMN_height="height";
		  public static final String COLUMN_height_corner="nCornerHeight";
		  public static final String COLUMN_latitude = "latitude";
		  public static final String COLUMN_longitude = "longitude";
		  public static final String COLUMN_latlongDist = "latlongDist";
		  
		  //All complete queries
		  public static final String select_completed_getgpsdata ="select distinct  vSiteIdCode as _id ,nLat,nLong,vSiteSurveyDate from Site_Survey_Master where bComplete =1 and vSiteIdCode = ?";
		  public static final String select_completed_getnoofcornerdetails ="select distinct  nCornerNo as _id ,nCornerNo,nCornerHeight from SITE_ANTEENA_DETAILS where vSiteIdCode = ?";
		  public static final String select_completed_getcornerdetails ="select   vSiteIdCode as _id ,nCornerNo,nLat,nLong,nDistance from SITE_CORNER_DETAILS where vSiteIdCode = ?";
		  public static final String select_completed_getnoonadjbldg ="select   vSiteIdCode as _id ,nBuildingNo from SITE_ANTEENA_BUILDING_DETAILS where vSiteIdCode = ?";
		  public static final String select_completed_getbldgdetails ="select   vSiteIdCode as _id ,nFloors,nAzimuth,nDistance,nLat,nLong from SITE_ANTEENA_BUILDING_DETAILS where vSiteIdCode = ?";
		  
		  
		  
		  // Database creation sql statement
		  private static final String DATABASE_CREATE = "create table "
		      + TABLE_COMMENTS + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_COMMENT
		      + " text not null + COLUMN_COMMENT_TITLE);";

		  
		  public static final String sqlCornerDetailsInsert = 
				  "insert into SITE_ANTEENA_DETAILS(nCornerNo,latitude,longitude,latlongDist)" +
				  "values";
		  		
		  
		  /*CURSOR FACTORY*/
		  public AccessData(Context context) {
		    super(context, DB_NAME, null, DATABASE_VERSION);
		    this.myContext = context;
		    DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
		  }	  
		  
		  
		  
		 
			@Override
			public void onCreate(SQLiteDatabase db) {
		 
			}
		 
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
			}
			
			/**
		     * Creates a empty database on the system and rewrites it with your own database.
		     * */
		    public void createDataBase() throws IOException{
		 
		    	boolean dbExist = checkDataBase();
		 
		    	if(dbExist){
		    		Log.w("dbexists","exists");
		    				    		//do nothing - database already exist
		    	}else{
		 
		    		//By calling this method and empty database will be created into the default system path
		               //of your application so we are gonna be able to overwrite that database with our database.
		        	this.getReadableDatabase();
		 
		        	try {
		 
		    			copyDataBase();
		 
		    		} catch (IOException e) {
		 
		        		throw new Error("Error copying database");
		 
		        	}
		    	}
		 
		    }
		 
		    /**
		     * Check if the database already exist to avoid re-copying the file each time you open the application.
		     * @return true if it exists, false if it doesn't
		     */
		    private boolean checkDataBase(){
		 
		    	SQLiteDatabase checkDB = null;
		 
		    	try{
		    		String myPath = DB_PATH + DB_NAME;
		    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		    		Log.w("dbexists","present");
		 
		    	}catch(SQLiteException e){
		 
		    		//database does't exist yet.
		 
		    	}
		 
		    	if(checkDB != null){
		 
		    		checkDB.close();
		 
		    	}
		 
		    	return checkDB != null ? true : false;
		    }
		 
		    /**
		     * Copies your database from your local assets-folder to the just created empty database in the
		     * system folder, from where it can be accessed and handled.
		     * This is done by transfering bytestream.
		     * */
		    private void copyDataBase() throws IOException{
		 
		    	//Open your local db as the input stream
		    	InputStream myInput = myContext.getAssets().open(DB_NAME);
		 
		    	// Path to the just created empty db
		    	String outFileName = DB_PATH + DB_NAME;
		 
		    	//Open the empty db as the output stream
		    	OutputStream myOutput = new FileOutputStream(outFileName);
		 
		    	//transfer bytes from the inputfile to the outputfile
		    	byte[] buffer = new byte[1024];
		    	int length;
		    	while ((length = myInput.read(buffer))>0){
		    		myOutput.write(buffer, 0, length);
		    	}
		    	Log.w("copied","present");
		    	//Close the streams
		    	myOutput.flush();
		    	myOutput.close();
		    	myInput.close();
		 
		    }
		 
		    public void openDataBase() throws SQLException{
		 
		    	//Open the database
		        String myPath = DB_PATH + DB_NAME;
		    	myDataBase = SQLiteDatabase.openOrCreateDatabase(myPath, null);//(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		    	
		    }
		 
		    @Override
			public synchronized void close() {
		 
		    	    if(myDataBase != null)
		    		    myDataBase.close();
		 
		    	    super.close();
		 
			}
		 
	

   
  private String[] allColumns = { AccessData.COLUMN_ID,
		  AccessData.COLUMN_COMMENT };

  
  
  
  
  /*

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }
 
  public void close() {
    dbHelper.close();
  }*/

  /*insert values
  public long createComment(String comment) {
    ContentValues values = new ContentValues();
    values.put(AccessData.COLUMN_COMMENT, comment);
    Log.w("comment",comment);
    long insertId = myDataBase.insert(AccessData.TABLE_COMMENTS, null,
        values);
    Cursor cursor = myDataBase.query(AccessData.TABLE_COMMENTS,
        allColumns, AccessData.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    //Comment newComment = cursorToComment(cursor);
    cursor.close();
    return newComment;
  }*/
  
  /* raw query returns what*/
  /*versioning in sqlmngr*/
  /*data where it is present*/
public Cursor deleteC(){
	
	Cursor c = database.rawQuery("delete from  comments", null);
	return c;
}
  
 /* public void  deleteComment(Comment comment) {
    long id = comment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(CommentsData.TABLE_COMMENTS, CommentsData.COLUMN_ID
        + " = " + id, null);
    /*while (!cursor.isAfterLast()) {
        Comment comment = cursorToComment(cursor);
        comments.add(comment);
        cursor.moveToNext();
      }
    
  

  public Cursor getAllComments() {
    List<Comment> comments = new ArrayList<Comment>();
    
    Cursor cursor = myDataBase.query(CommentsData.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);*/

    /*cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Comment comment = cursorToComment(cursor);
      comments.add(comment);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();*/
   

  /*private Comment cursorToComment(Cursor cursor) {
    Comment comment = new Comment();
    comment.setId(cursor.getLong(0));
    comment.setComment(cursor.getString(1));
    return comment;
  }*/
  
	public int deleteData(){
		String where = "vAddress = ?";
		String clause[] = new String[]{"MOTIHARI"};
		return myDataBase.delete(SURVEY_MASTER, where, clause);
		
	}

  public Cursor getAntennaDetails(String ipid){
	  String[] args = new String[]{ipid};
	  Cursor antennaDetails =myDataBase.rawQuery(select_antenna_details_query, args);		
		return antennaDetails;
		  
	  }
  
  public Cursor getNoOfCornerDetails(String ipid){
	  String[] args = new String[]{ipid};
	  Cursor noOfCornerDetails =myDataBase.rawQuery(number_of_corner_details, args);
	  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
		return noOfCornerDetails;
		  
  }
  
  public Cursor getNoOfBldgDetails(String ipid){
	  String[] args = new String[]{ipid};
	  Cursor noOfCornerDetails =myDataBase.rawQuery(number_of_bldg_details, args);
	  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
		return noOfCornerDetails;
		  
	  }
  
public Cursor getCornerMappingDetails(String ipid){
	  String[] args = new String[]{ipid};
	  Cursor mappingdetails =myDataBase.rawQuery(corner_mapping_details, args);
	  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
		return mappingdetails;
		  
	  }


public int updateCornerMappingDetails(String IPID,String operator,int antena,int cNo,float dist,float heightCorner){
	String where = "vSiteIdCode = ?"
		    + " AND vOpratorSiteId = ?"
		    + " AND nAnteenaNo = ?"
		    + " AND nCornerNo = ?";
	String[] args = new String[]{IPID,operator,String.valueOf(antena),String.valueOf(cNo)};
	ContentValues cv = new ContentValues();
	cv.put("nDistance", dist);
	cv.put("nCornerHeight", heightCorner);
	return myDataBase.update("SITE_CORNER_DETAILS", cv,where, args);
}

public int updateBldgMappingDetails(String IPID,String operator,int antena,int cNo,float dist,Context cnt){
	try{
	String where = "vSiteIdCode = ?"
		    + " AND vOpratorSiteId = ?"
		    + " AND nAnteenaNo = ?"
		    + " AND nBldgNo = ?";
	String[] args = new String[]{IPID,operator,String.valueOf(antena),String.valueOf(cNo)};
	ContentValues cv = new ContentValues();
	cv.put("nDistance", dist);
	Toast.makeText(cnt,"bmap"+String.valueOf(dist), Toast.LENGTH_SHORT).show();
	return myDataBase.update("SITE_ANTEENA_BUILDING_DETAILS", cv,where, args);
	}catch(Exception ex){
		Toast.makeText(cnt,"ees"+ex.getMessage(),Toast.LENGTH_SHORT).show();
	}
	return 0;
}
  
public Cursor getBldgMappingDetails(String IPID){	
	String[] args = new String[]{IPID};
	Cursor bldgMappingDetails =myDataBase.rawQuery(bldg_mapping_details, args);
	  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
		return bldgMappingDetails;
}

public Cursor getBoundaryLayoutDetails(String IPID){	
	final String[] whereArgs = {IPID};
	  String where = "vSiteIdCode = ?";	
	//Cursor boundaryLayoutDetails =myDataBase.query("SITE_BOUNDARY_LAYOUT_DETAILS", args);
	  return myDataBase.query("SITE_BOUNDARY_LAYOUT_DETAILS", null, where, whereArgs, null, null, null);
		//return boundaryLayoutDetails;
}

public Cursor getPendingSurveyDetails(){
	  Cursor pendingsurveyDetails =myDataBase.rawQuery(pending_survey_download, null);
	  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
		return pendingsurveyDetails;
}

public long insertSiteSurveyMaster(float nATPCFactor,float nAnteenaGain,float nAnteenaHeightAGL,float nBuildingHeightAGL,int nCarrier_Sector,float nChannelFreq,float nCominerLoss,float nDTXFactor,float nLat,float nLong,float nRFCableLength,float nTotalTilt,float nTxPower,float nUnitLoss,float nVerticalBeamWidth,float nsideLoabAttenuation,String vAddress,String vCircle,String vIMEI,String vMakeModal,String vOpratorName,String vOpratorSiteId,String vSiteId,String vSiteIdCode,String vSiteName,String vSurveyor,String vSysTemType,String vTowerType,String vOpratorId,int bcom){
	Log.w("in","accessdata");
	ContentValues cValues = new ContentValues();
	cValues.put("nATPCFactor",nATPCFactor);
	cValues.put("nAnteenaGain",nATPCFactor);
	cValues.put("nAnteenaHeightAGL",nAnteenaHeightAGL);
	cValues.put("nBuildingHeightAGL",nBuildingHeightAGL);
	cValues.put("nCarrier_Sector",nCarrier_Sector);
	cValues.put("nChannelFreq",nChannelFreq);
	cValues.put("nCominerLoss",nCominerLoss);
	cValues.put("nDTXFactor",nDTXFactor);
	cValues.put("nLat",nLat);
	cValues.put("nLong",nLong);
	cValues.put("nRFCableLength",nRFCableLength);
	cValues.put("nTotalTilt",nTotalTilt);
	cValues.put("nTxPower",nTxPower);
	cValues.put("nUnitLoss",nUnitLoss);
	cValues.put("nVerticalBeamWidth",nVerticalBeamWidth);
	cValues.put("nsideLoabAttenuation",nsideLoabAttenuation);
	cValues.put("vAddress",vAddress);
	cValues.put("vCircle",vCircle);
	cValues.put("vIMEI",vIMEI);
	cValues.put("vMakeModal",vMakeModal);
	cValues.put("vOpratorName",vOpratorName);
	cValues.put("vOpratorSiteId",vOpratorSiteId);
	cValues.put("vSiteId",vSiteId);
	cValues.put("vSiteIdCode",vSiteIdCode);
	cValues.put("vSiteName",vSiteName);
	cValues.put("vSurveyor",vSurveyor);
	cValues.put("vSysTemType",vSysTemType);
	cValues.put("vTowerType",vTowerType);
	cValues.put("vOpratorId",vOpratorId);
	cValues.put("bComplete", bcom);
	Log.w("dataentered","accessdata");
	return myDataBase.insert(SURVEY_MASTER, null, cValues);
	
}


public void checkForAntennaDetails(int numAnt , String ipid , String operatorID,Context cont){
	
	int countAntennaCorner = -1 , countAntennaBldg =-1;
	String[] whereClau = new String[]{operatorID,ipid}; 
	Cursor c1 = myDataBase.rawQuery(antenna_no_for_corners,whereClau);
	Cursor c2 = myDataBase.rawQuery(antenna_no_for_bldgs,whereClau);
	String tab = "a";
	ContentValues cvInsert = null;
	 
	if(c1!=null && c1.moveToNext()){
		//c1.moveToNext();
		countAntennaCorner = Integer.valueOf(c1.getString(c1.getColumnIndex("count")));
	}
//	Toast.makeText(cont,"ant:"+String.valueOf(countAntennaCorner),Toast.LENGTH_LONG).show();
	if(c2!=null && c2.moveToNext()){
		//c2.moveToNext();
		countAntennaBldg = Integer.valueOf(c2.getString(c2.getColumnIndex("count")));
	}
	Log.w("countAntennaBldg",String.valueOf(countAntennaBldg));
	//Toast.makeText(cont,"bldg:"+String.valueOf(countAntennaCorner),Toast.LENGTH_LONG).show();
	
	if(countAntennaCorner == -1 && numAnt!= 0){
		//insert row in anteena_details
		cvInsert = new ContentValues();
		cvInsert.put("vSiteIdCode", ipid);
		cvInsert.put("vOpratorSiteId", operatorID);
		cvInsert.put("nAnteenaNo", numAnt);
		myDataBase.insert(TABLE_ANTENNA, null, cvInsert);
	
		//Toast.makeText(cont, "insert corner",Toast.LENGTH_SHORT).show();
	}
	if(countAntennaBldg == -1 && numAnt!=0)
	{
		//insert row in bldg_details
		cvInsert = new ContentValues();
		cvInsert.put("vSiteIdCode", ipid);
		cvInsert.put("vOpratorSiteId", operatorID);
		cvInsert.put("nAnteenaNo", numAnt);
		long i = myDataBase.insert(TABLE_ANTENNA_BLDG, null, cvInsert);
		Log.w("insert",String.valueOf(i));
	
		//Toast.makeText(cont, "insert bldg",Toast.LENGTH_SHORT).show();
	}
	String table_name = "";
	String where = "";
	ContentValues cvUpdate = null;
	String[] whereArgs = new String[]{ipid,operatorID};
	where = "vSiteIdCode = ?"
		    + " AND vOpratorSiteId = ?";
	cvUpdate = new ContentValues();
	cvUpdate.put("nAnteenaNo",numAnt);
	if(numAnt != countAntennaCorner && countAntennaCorner >= 0){		 
		
		if(countAntennaCorner != 0){
			table_name = "SITE_CORNER_DETAILS";
			myDataBase.delete(table_name, where, whereArgs);
			Toast.makeText(cont, "delete corner",Toast.LENGTH_SHORT).show();
		}

		table_name = "SITE_ANTEENA_DETAILS";
		//myDataBase.insert(table_name, null, cvUpdate);
		myDataBase.update(TABLE_ANTENNA,cvUpdate,where, whereArgs);
		Toast.makeText(cont, "update corner",Toast.LENGTH_SHORT).show();

		
	}	
	
	if(numAnt != countAntennaBldg && countAntennaBldg >= 0){
		if(countAntennaBldg != 0){
			table_name = "SITE_ANTEENA_BUILDING_DETAILS";		
			int y = myDataBase.delete(table_name, where, whereArgs);
			Log.w("del ant Bldg",String.valueOf(y));
		}
		table_name = "SITE_BUILDING_DETAILS";
		int r = myDataBase.update(table_name,cvUpdate,where, whereArgs);
		Log.w("update site Bldg",String.valueOf(r));
		//Toast.makeText(cont, "update bldg",Toast.LENGTH_SHORT).show();
	}	
  
		c1.close();
		c2.close();
		
}

public long insertAntCorner(int numAnt,String ipid,String operatorID){
	long result = -1;
	Log.w("inside",String.valueOf(numAnt));
	ContentValues cv = new ContentValues();
	cv.put("nAnteenaNo",numAnt);
	cv.put("vSiteIdCode",ipid);
	cv.put("vOpratorSiteId",operatorID);
	result = myDataBase.insert(TAB_SITE_ANTEENA_DETAILS, null,cv);
	Log.w("TAB_SITE_ANTEENA_DETAILS",String.valueOf(result));
	return result;
}

public long insertAntBldg(int numAnt,String ipid,String operatorID){
	long result = -1;
	Log.w("inside",String.valueOf(numAnt));
	ContentValues cv = new ContentValues();
	cv.put("nAnteenaNo",numAnt);
	cv.put("vSiteIdCode",ipid);
	cv.put("vOpratorSiteId",operatorID);
	result = myDataBase.insert(TAB_SITE_BUILDING_DETAILS, null,cv);
	Log.w("TAB_SITE_BUILDING_DETAILS",String.valueOf(result));
	return result;
}

  public int updateAntennaDetails(int number , String antID){
	  int result = -1;
	  final String[] whereArgs = {antID};
	  //String insert = "Insert into SITE_ANTEENA_DETAILS(nAntennaNo) WHERE IPID='"+ antID+"'";
	  //result = myDataBase.rawQuery(insert, null);
	  ContentValues values = new ContentValues();
	  values.put(AccessData.COLUMN_AntennaNo,number);
	  result =myDataBase.update(AccessData.TABLE_ANTENNA,values, "Operator = ?",whereArgs);
	  
	  return result;
  }
  
  public int updateBoundaryDetails(String IPID,int cornerNo,float dist){
	  int result = -1;
	  String where = "vSiteIdCode = ? and nCornerNo = ?";				
	  final String[] whereArgs = {IPID ,String.valueOf(cornerNo)};	  
	  ContentValues values = new ContentValues();
	  values.put("nDistance",dist);
	  result =myDataBase.update("SITE_BOUNDARY_LAYOUT_DETAILS",values, where,whereArgs);
	  
	  return result;
  }
  
  
  public int updateNumberOfCorners(int numberCorner, float height, String IPID,Context cnt){
	  int result = -1;
	  final String[] whereArgs = {IPID};
	  myDataBase.delete("SITE_CORNER_DETAILS", "vSiteIdCode = ?", whereArgs);
	  myDataBase.delete("SITE_BOUNDARY_LAYOUT_DETAILS", "vSiteIdCode = ?", whereArgs);
	  ContentValues values = new ContentValues();
	  values.put(AccessData.COLUMN_CornerNo,numberCorner);
	  values.put(AccessData.COLUMN_height_corner,height);
	  result =myDataBase.update(AccessData.TABLE_ANTENNA,values, "vSiteIdCode = ?",whereArgs);
	 // Toast.makeText(cnt, "After update:"+String.valueOf(result),Toast.LENGTH_SHORT).show();
	  long re =insertToSiteCorner(numberCorner,height,IPID,cnt);
	//  Toast.makeText(cnt,"callin bound",Toast.LENGTH_SHORT).show();	
	  
	  insertToBoundaryLayout(numberCorner, IPID,cnt);
	//  Toast.makeText(cnt, "CORNER INSERT"+String.valueOf(re),Toast.LENGTH_SHORT).show();
	  return result;
  }
  
  public void getNumberOfCornersEntered(String ipid){
	  String[] cols = {"nCornerNo"};
	  final String[] whereArgs = {ipid};
	  String where = "vSiteIdCode = ?";				
	  myDataBase.query(AccessData.TABLE_ANTENNA, cols, where, whereArgs, null, null, null);
  }
  
  public int updateNumberOfBldg(int numberBldg,String IPID,Context cnt){
	  int result = -1;
	  final String[] whereArgs = {IPID};
	  myDataBase.delete("SITE_ANTEENA_BUILDING_DETAILS", "vSiteIdCode = ?", whereArgs);
	  ContentValues values = new ContentValues();
	  values.put("nBuildingNo",numberBldg);	  
	  result =myDataBase.update(AccessData.TABLE_ANTENNA_BLDG,values, "vSiteIdCode = ?",whereArgs);	  
	  insertToSiteBldg(numberBldg,IPID,cnt);
	  return result;
  }
  
  
  public int updateBldgDetails(int i,double distance,double lati,double longi,int azimuth,int floors,String IPID,Double height,Context cnt){
	  int result = -1;
	  try{
	  final String[] whereArgs = {IPID,String.valueOf(i)};	  
	  ContentValues values = new ContentValues();	  
	  values.put("nDistance",distance);
	  values.put("nLat",lati);
	  values.put("nLong",longi);
	  values.put("nAzimuth",azimuth);
	  values.put("nFloors",floors);
	  values.put("nBldgHeight",height);
	  result =myDataBase.update("SITE_ANTEENA_BUILDING_DETAILS",values, "vSiteIdCode = ? and nBldgNo = ?",whereArgs);
	  }catch(Exception ex){
		  Toast.makeText(cnt,"ex:"+ex.getMessage(),Toast.LENGTH_SHORT).show();
	  }
	  Toast.makeText(cnt,String.valueOf(result),Toast.LENGTH_SHORT).show();
	  return result;
  }
  
  
  public long insertToSiteCorner(int numberCorner,float height,String IPID,Context cnt){
	  String[] whereClau = new String[]{IPID};	  
	  Cursor countAntenna = myDataBase.rawQuery(antenna_no_for_corners_insert, whereClau);
	  
	  int countAntennaValue = -1;
	  long id=-1;	  
	  String operID="";
	  if(numberCorner != 0){
		  while( countAntenna.moveToNext()){
			  countAntennaValue = Integer.valueOf(countAntenna.getString(countAntenna.getColumnIndex("nAnteenaNo")));
			  Toast.makeText(cnt, String.valueOf(countAntennaValue),Toast.LENGTH_SHORT).show();
			  operID = countAntenna.getString(countAntenna.getColumnIndex("vOpratorSiteId"));
			  Toast.makeText(cnt,"opp"+ operID,Toast.LENGTH_SHORT).show();
		  }	  
			  if(countAntennaValue!=0){
				  for(int i = 0; i<countAntennaValue;i++){
					  for(int j=0;j<numberCorner;j++){
	//select nId as _id  , sbd.vSiteIdCode,sbd.vOpratorSiteId,sbd.nAnteenaNo from SITE_ANTEENA_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vSiteIdCode = ?					  
						  ContentValues cvCorner = new ContentValues();
						  cvCorner.put("vSiteIdCode", IPID);
						  cvCorner.put("vOpratorSiteId", operID);
						  cvCorner.put("nAnteenaNo",i+1);
						  cvCorner.put("nCornerNo", j+1);
						  cvCorner.put("nCornerHeight",height);
						  id = myDataBase.insert("SITE_CORNER_DETAILS", null, cvCorner);
						  Toast.makeText(cnt, IPID+operID+String.valueOf(i+1)+String.valueOf(j+1), Toast.LENGTH_SHORT).show();
					  }
				  }				  
			  }
			 
		  
	  }
	  
	  return id;
  }
  
  
  public long insertToBoundaryLayout(int numberCorner,String IPID,Context cnt){
	  
	  ContentValues cvCorner = new ContentValues();
	  long res = 0;
	  for(int cornNum = 1; cornNum <= numberCorner;cornNum ++){
		  int corn1 = cornNum ;
		  int corn2=1;
		  if(cornNum != numberCorner)			  
			  corn2 = cornNum +1;
		  cvCorner.put("vSiteIdCode", IPID);
		  cvCorner.put("nCornerNo",cornNum );
		  cvCorner.put("vDescription","C"+corn1+"-"+"C"+corn2);
		  res = myDataBase.insert("SITE_BOUNDARY_LAYOUT_DETAILS",null,cvCorner);
		  Toast.makeText(cnt, "boundary",Toast.LENGTH_SHORT).show();
	  }
	  
	  return res;
  }
  public long insertToSiteBldg(int numberCorner,String IPID,Context cnt){
	  String[] whereClau = new String[]{IPID};	  
	  Cursor countAntenna = myDataBase.rawQuery(antenna_no_for_bldg_insert, whereClau);
	  int countAntennaValue = -1;
	  long id = -1;
	  String operID ="";
	  if(numberCorner != 0){
		  while(countAntenna.moveToNext()){
			  countAntennaValue = Integer.valueOf(countAntenna.getString(countAntenna.getColumnIndex("nAnteenaNo")));
			  operID = countAntenna.getString(countAntenna.getColumnIndex("vOpratorSiteId"));
			 
		  }
			  if(countAntennaValue!=0){
				  for(int i = 0; i<countAntennaValue;i++){
					  for(int j=0;j<numberCorner;j++){
	//select nId as _id  , sbd.vSiteIdCode,sbd.vOpratorSiteId,sbd.nAnteenaNo from SITE_ANTEENA_DETAILS sbd , Site_Survey_Master ssm  where   sbd.vSiteIdCode = ssm.vSiteIdCode and sbd.vOpratorSiteId = ssm.vOpratorSiteId and sbd.vSiteIdCode = ?					  
						  ContentValues cvCorner = new ContentValues();
						  cvCorner.put("vSiteIdCode", IPID);
						  cvCorner.put("vOpratorSiteId", operID);
						  cvCorner.put("nAnteenaNo",i+1);
						  cvCorner.put("nBldgNo", j+1);
						 id = myDataBase.insert("SITE_ANTEENA_BUILDING_DETAILS", null, cvCorner);
					  }
				  }				  
			  }			 
		  
	  }
	  
	  return id;
  }
  
  
  
  	public int updateCornerDetailsLatLong(int i,double distance,double lati,double longi,String IPID,Context cnt){
  		Log.w("i",String.valueOf(i));
  		final String[] whereArgs = {IPID,String.valueOf(i)};	  
  		String where = "vSiteIdCode = ?" +
  				"and nCornerNo = ?";
  		ContentValues values = new ContentValues();
  	    values.put("nLat",lati);
  	    values.put("nLong",longi);
  	    values.put("nDistance",distance);
  	    int r =myDataBase.update("SITE_CORNER_DETAILS", values,where, whereArgs);
  	    Toast.makeText(cnt,"site corner update"+String.valueOf(r),Toast.LENGTH_SHORT).show();
  	    ContentValues values1 = new ContentValues();
  	  	values1.put("nDistance",distance);
  	  return myDataBase.update(TAB_SITE_ANTEENA_DETAILS, values1,where, whereArgs);
//  	    myDataBase.insert("", nullColumnHack, values)
  	}
  	
  	public Cursor getCornerValues(String ipid){
  		String selection = COLUMN_vSiteIdCode;
  		String[] args = new String[]{ipid};
  		return myDataBase.query("SITE_CORNER_DETAILS",null,selection,args,null,null,null);
  	}
  	
  	//TNS - Get list of pending sites
  	public Cursor getPendingDetails(){
  	  return myDataBase.rawQuery(select_pending_details_query,null);
  		  
  	}
  	public Cursor getPendingDetailsSearch(String s)
  	{
		 String search_pending_details_survey ="select distinct  vSiteIdCode as _id ,vSiteIdCode,vSiteId,vSiteName,vAddress,vCircle from Site_Survey_Master where bComplete =0 and vSiteIdCode like '"+s+"%' or vSiteName like '"+s+"%'";
	
	     return myDataBase.rawQuery(search_pending_details_survey,null);
  	}
  	
  	
  	public Cursor getCompletedDetails(){
    	  return myDataBase.rawQuery(select_completed_details_query,null);
    		  
    	}
  	
  	public Cursor getLoginDetails(){		
  		return myDataBase.query("SITE_LOGIN_DETAILS", null, null, null, null, null, null, null);  
	  }
  	
  	public long insertLoginDetails(String username , String password){
  		ContentValues cvLogin = new ContentValues();
  		cvLogin.put("password",password);
  		cvLogin.put("username",username);  		
  		return myDataBase.insert("SITE_LOGIN_DETAILS",null, cvLogin);  
	  }
  	
  	public long saveImagePathToDatabase(String vAngle , String ipid ,String imgName,String imgType){
  		ContentValues values = new ContentValues();
  	    values.put("vSiteIdCode",ipid);
  	    values.put("vPath",vAngle);
  	    values.put("vImgName",imgName);  
  	  values.put("vImgType",imgType);  	  
  	   return myDataBase.insert("SITE_IMAGE_DETAILS", null,values);
  	}
  	
  	public Cursor getAllAntenaDetailsUpload(){	
  		//String[] columns = new String[]{COLUMN_vSiteIdCode,COLUMN_vOpratorSiteId,COLUMN_AntennaNo,COLUMN_nCornerNo,COLUMN_nDistance,COLUMN_nCornerHeight,COLUMN_nLat,COLUMN_nLong};
  		return myDataBase.query(TABLE_ANTENNA_CORNER, null, null, null, null, null, null, null);  
	  }
  	
  	public Cursor getAllBuildingDetails(){
  		//String[] columns = new String[]{COLUMN_vSiteIdCode,COLUMN_vOpratorSiteId,COLUMN_AntennaNo,COLUMN_nBldgNo,COLUMN_nFloors,COLUMN_nDistance,COLUMN_nAzimuth,COLUMN_nLat,COLUMN_nLong};  		
  		return myDataBase.query("SITE_ANTEENA_BUILDING_DETAILS", null, null, null, null, null, null, null);
	}
  	
  	public int insertGPSData(double lat , double longi,String ipid,String dateSurvey,String twrType,double twrHt){
  		final String[] whereArgs = {ipid};
  		ContentValues cvGPS = new ContentValues();
  		cvGPS.put("nLat",lat);
  		cvGPS.put("nLong",longi);
  		cvGPS.put("vSiteSurveyDate",dateSurvey); 
  		cvGPS.put("vTowerType",twrType); 
  		cvGPS.put("nTowerHeight",twrHt); 
  		return myDataBase.update(SURVEY_MASTER,cvGPS,"vSiteIdCode = ?",whereArgs);
  	}
  	
  	public Cursor getSiteSurveyMasterDetails(String ipid){
  		String[] columns = new String[]{COLUMN_vSiteIdCode,COLUMN_vOpratorSiteId,COLUMN_vSiteId,COLUMN_nLat,COLUMN_nLong,COLUMN_vTowerType,"vSiteSurveyDate","nTowerHeight"};
  		Cursor allDetails = myDataBase.query(SURVEY_MASTER, columns,  "vSiteIdCode=?", new String[]{ipid}, null, null, null, null);
  		return allDetails; 
  	}
  	
  	public Cursor getImageDetails(){  		
  		Cursor allDetails = myDataBase.query(IMG_Table, null,  null, null, null, null, null, null);
  		return allDetails; 
  	}
  	
  	public Cursor getCornerValuesForLayoutDraw(String ipid){  	
  		String[] cols = new  String[]{"nDistance"};
  		String groupby = "nCornerNo";
  		String where = "vSiteIdCode = ?"; 
  		String[] args = new String[]{ipid};
  		return myDataBase.query("SITE_CORNER_DETAILS", cols,  where, args, groupby, null, null, null);
  		 
  	}
  	
  	public Cursor getAllBoundaryLayoutDetails(){	
  		
  		return myDataBase.query("SITE_BOUNDARY_LAYOUT_DETAILS", null,  null, null, null, null, null, null);
  		  //myDataBase.query(TABLE_ANTENNA, columns, selection, selectionArgs, groupBy, having, orderBy)
  			
  	}
  	
  	public Cursor fetchImagePath(){
  		return myDataBase.query(IMG_Table, null,  null, null, null, null, null, null);
  	}
  	
  	public int markSiteAsComplete(String ipid){
  		ContentValues cvSiteUpdate = new ContentValues();
  		cvSiteUpdate.put("bComplete", String.valueOf(1));
  		String where = "vSiteIdCode = ?" ;
  		String[] args = new String[]{ipid};
  		return myDataBase.update("SITE_SURVEY_MASTER", cvSiteUpdate, where, args);
  	}
  
  	public Cursor getIfSiteComplete(String ipid){
  		String[] args = new String[]{ipid};
  		 return myDataBase.rawQuery(check_ifComplete, args);
  	}
  	
  	public Cursor getSuggestions(String vSiteIDCode){
  		String[] args = new String[]{vSiteIDCode};
  		return myDataBase.rawQuery(select_autocomplete_pending_details_query,args);
  	}
  	
  	public Cursor getCompletedSuggestions(String vSiteIDCode){
  		String[] args = new String[]{vSiteIDCode};
  		return myDataBase.rawQuery(select_autocomplete_complete_details_query,args);
  	}
  	
  	public Cursor getBldgNumber(String ipid){
  		String[] args = new String[]{ipid};
  		return myDataBase.rawQuery(get_buildingNo_autoDraw,args);
  	}
  	
  	public Cursor getCompletedGPSdata(String ipid){
  		String[] args = new String[]{ipid};
  		return myDataBase.rawQuery(select_completed_getgpsdata,args);
  	}
} 