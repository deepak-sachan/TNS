package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TowerBuildingLayoutActivity extends Activity implements OnClickListener,OnTouchListener{
	MyCustomView customView;
	String bitmappic ;
	Bitmap mBitmapRed;
	String textoncanvas="";
	ArrayList<Float> linesdraw = new ArrayList<Float>();
	boolean actionMove = false;
	boolean drwline = false,iswrite=false;
	float tempx = 0 , tempy = 0;
	ImageButton onSave,onCaution,onNorth,onRemove,onWarning,onDanger;
	Button onTower;
	ImageButton onLine,onWriteText,onLineRemoveAll,onLineRemoveLast;
	ImageButton onUndoText,onDG,onBldg,onShelter;
	ImageView imgTest;
	String shape , btnClickImage;
	boolean circle=false ,rectangle = false,isLine = false;;
	float runningX=0 , runningY=0;
	float textx = 0,texty =0;
	float[] lines = new float[]{};
	float latestx1 =0, latesty1=0,latestx2 =0, latesty2=0;
	int count =0;
	ArrayList<Image> imgList;
	ArrayList<WriteTextLayout> writeList;
	int shpFocused =-1;
	int imgposID = -1;
	String curLayout = "towerlayout";
	int[] images = new int[]{R.drawable.draw, R.drawable.blue,R.drawable.red,R.drawable.dg};
	String[] layouts = new String[]{"GBT","RTT","RTP"};
	float[] imagesdx = new float[]{5,180,130,130};
	float[] imagesdy = new float[]{5,75,140,75};
	AccessData objAdapter;
	String ipid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tower);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
		//Bundle b = getIntent().getExtras();
        ipid = getIntent().getStringExtra("ipid");
		
		//Toast.makeText(this,"ipid:"+ipid,Toast.LENGTH_SHORT).show();
		customView = (MyCustomView)findViewById(R.id.customView);
		customView.setOnTouchListener(this);								
		customView.setDrawingCacheEnabled(true);
		customView.initialiseImages(images,imagesdx,imagesdy);
		initList();
		onSave = (ImageButton)findViewById(R.id.btnSave);
		onSave.setOnClickListener(this);
		onCaution = (ImageButton)findViewById(R.id.btnCaution);
		onCaution.setOnClickListener(this);
		onNorth = (ImageButton)findViewById(R.id.btnNorth);
		onNorth.setOnClickListener(this);
		onRemove = (ImageButton)findViewById(R.id.btnRemove);		
		onRemove.setOnClickListener(this);
		onWarning = (ImageButton)findViewById(R.id.btnWarning);
		onWarning.setOnClickListener(this);
		onDanger = (ImageButton)findViewById(R.id.btnDanger);
		onDanger.setOnClickListener(this);
		onLine = (ImageButton)findViewById(R.id.btnLine);
		onLine.setOnClickListener(this);
		onWriteText = (ImageButton)findViewById(R.id.btnText);		
		onWriteText.setOnClickListener(this);
		onLineRemoveAll = (ImageButton)findViewById(R.id.btnLineRemoveAll);
		onLineRemoveAll.setOnClickListener(this);
		onLineRemoveLast = (ImageButton)findViewById(R.id.btnLineRemoveLast);
		onLineRemoveLast.setOnClickListener(this);
		onUndoText = (ImageButton)findViewById(R.id.btnUndoText);
		onUndoText.setOnClickListener(this);
		onTower = (Button)findViewById(R.id.btnTower);
		onTower.setOnClickListener(this);
		onDG = (ImageButton)findViewById(R.id.btnDG);
		onDG.setOnClickListener(this);
		onShelter = (ImageButton)findViewById(R.id.btnShelter);
		onShelter.setOnClickListener(this);
		onBldg = (ImageButton)findViewById(R.id.btnBldg);
		onBldg.setOnClickListener(this); 
		objAdapter = new AccessData(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Title
        builder.setTitle("Choose Layout");
        builder.setSingleChoiceItems(layouts, -1, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//curLayout = layouts[which];
				dialog.dismiss();
			}
		});

        builder.create();
        builder.show();
        	
		/*int[] images = new int[1];
		images[0] = R.drawable.draw;
		images[1] = R.drawable.red;i
		customView = (MyCustomView)findViewById(R.id.allimages);
		customView.initialiseImages(images, 0,0,0,0,0, 0);
		*/
		//customView..
		
	}
	
	public void onResume(){
		super.onResume();
		
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void initAll(){
		
	}
	
	//Maintain list of images.
	public void initList(){
		imgList = new ArrayList<Image>();
		Log.w("imglength",String.valueOf(images.length));
		for(int i=0;i< images.length;i++){
			Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),images[i]);			 
			 Image item = new Image(images[i] ,imagesdx[i],imagesdy[i],bitMoved.getWidth(),bitMoved.getHeight(),i+1,"");	 
			imgList.add(item);
		}
		
		
	}

	

/*	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		boolean eventConsumed = true;
		int x = (int)event.getX();
		int y = (int)event.getY();		
		int action = event.getAction();		
		if (action == MotionEvent.ACTION_DOWN) {
			//bitmappic = customView.getBitmapTouched(x,y);
			
			
		} else if (action == MotionEvent.ACTION_UP) {				

		} else if (action == MotionEvent.ACTION_MOVE) {
			if(bitmappic.equalsIgnoreCase("blue")){
				
			}
			
			
		//Toast.makeText(getBaseContext(),"inside blue case",Toast.LENGTH_LONG).show();
			
					
		}
		return false;
	}*/

	public void addToImages(){
		Bitmap newImg=null;
		int newID = 0;
		
		if(btnClickImage.equalsIgnoreCase("caution")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.caution);
			newID = R.drawable.caution;
		}
		else if(btnClickImage.equalsIgnoreCase("north")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.north);
			newID = R.drawable.north;
		}
		else if(btnClickImage.equalsIgnoreCase("warning")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.warning);
			newID = R.drawable.warning;
		}
		else if(btnClickImage.equalsIgnoreCase("danger")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.danger);
			newID = R.drawable.danger;
		}
		
		else if(btnClickImage.equalsIgnoreCase("dg")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.dg);
			newID = R.drawable.dg;
		}
		
		else if(btnClickImage.equalsIgnoreCase("bldg")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.building);
			newID = R.drawable.building;
		}
		
		else if(btnClickImage.equalsIgnoreCase("shelter")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.red);
			newID = R.drawable.red;
		}
		else if(btnClickImage.equalsIgnoreCase("tower")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.blue);
			newID = R.drawable.blue;
		}
		
		
		/*else if(btnClickImage.equalsIgnoreCase("writetext")){
			customView.writeText(textoncanvas);
			newImg = BitmapFactory.decodeFile(textoncanvas+".png");
		}*/		
		int uniID = imgList.size();
		Image item;
		if(btnClickImage.equalsIgnoreCase("writetext"))
			 item = new Image(uniID +1 ,20,15,newImg.getWidth(),newImg.getHeight(),uniID+1,textoncanvas);
		else
			 item = new Image(newID ,20,15,newImg.getWidth(),newImg.getHeight(),uniID+1,"");		
		imgList.add(item);		
		
		if(!btnClickImage.equalsIgnoreCase("writetext")){
			customView.drawNewImage(imgList);
		}
		else{
			customView.copyList(imgList);
		}
		
		btnClickImage = "";
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnCaution:
				btnClickImage = "caution";
				addToImages();
				
			break;
		case R.id.btnSave:				
				saveBitmapFile();
			break;
		case R.id.btnNorth:				
			btnClickImage = "north";
			addToImages();
			break;		
		case R.id.btnRemove:			
			/*if(linesdraw!=null){
				if(linesdraw.size() > 0){
					int listsize = linesdraw.size();		
					texty = linesdraw.get(linesdraw.size() -1);
					linesdraw.remove(linesdraw.size() -1);
					listsize = linesdraw.size();
					textx = linesdraw.get(linesdraw.size() -1);
					linesdraw.remove(linesdraw.size() -1);
				}
			}*/
			removeImage();			
			break;
		case R.id.btnDanger:
			btnClickImage = "danger";
			addToImages();			
		break;
		case R.id.btnWarning:
			btnClickImage = "warning";
			addToImages();			
		break;		
		case R.id.btnTower:
			btnClickImage = "tower";
			addToImages();			
		break;
		
		case R.id.btnBldg:
			btnClickImage = "bldg";
			addToImages();			
		break;
		
		case R.id.btnShelter:
			btnClickImage = "shelter";
			addToImages();			
		break;
		
		case R.id.btnDG:
			btnClickImage = "dg";
			addToImages();			
		break;
		
		case R.id.btnLine:
			//isLine = true;
			Toast.makeText(this,"line!",Toast.LENGTH_SHORT).show();
			drwline = true;
			//customView.drawline(linesdraw);
			break;
			
		case R.id.btnText:
			/*if(linesdraw!=null){
				if(linesdraw.size() > 0){						
					texty = linesdraw.get(linesdraw.size() -1);
					linesdraw.remove(linesdraw.size() -1);					
					textx = linesdraw.get(linesdraw.size() -1);
					linesdraw.remove(linesdraw.size() -1);
				}
			}
			else{
				textx = runningX;
				texty = runningY;
			}*/
		//	btnClickImage = "writetext";
			//Toast.makeText(this,"inside textclick",Toast.LENGTH_SHORT).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        //Title
	        builder.setTitle("Please enter text:");
	        LayoutInflater inflater = this.getLayoutInflater();
	        final View mychildview = inflater.inflate(R.layout.writetextdialog,null);	        
	        builder.setView(mychildview);
	        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
					iswrite = true;
				EditText ed	 = (EditText)mychildview.findViewById(R.id.textlayout);				
					textoncanvas = ed.getText().toString();
								
					//lines = null;	
					if(writeList == null){
						writeList = new ArrayList<WriteTextLayout>();
						
					}
					arg0.dismiss();		
					
					//WriteTextLayout wobj = new WriteTextLayout(num +1, textx,texty,textoncanvas);
					//writeList.add(wobj);
				//	customView.writeText(writeList);					
					//addToImages();			    
					
				}
			});
	        //To show negative button
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
					
				}
			});
	        builder.create();
	        builder.show();
	        Log.w("outof","switchmain");
			
			break;
		case R.id.btnLineRemoveAll:			
			linesdraw.clear();
			customView.removeAllLines();
			break;
		
		case R.id.btnLineRemoveLast:
			if(linesdraw!=null){
				int cnt = 4;
				if(linesdraw.size() > 0){
					while(cnt > 0){
						linesdraw.remove(linesdraw.size() - 1);
						cnt--;
					}
					
				}
				customView.drawline(linesdraw);
			}			
			break;
		case R.id.btnUndoText:
			if(writeList!=null){				
				if(writeList.size() > 0){					
					writeList.remove(writeList.size() - 1);					
					}					
				}
				customView.writeText(writeList);		
			
			break;
		}
	}
	
	public void removeImage(){
		if(imgposID!=1){
		for(int i = 0;i<imgList.size();i++){		
			Image imgR = imgList.get(i);			
			if(imgposID == imgR.getuniqueID())
				imgR.setuniqueID(0);
		  }
		customView.removeImage(imgList);
	   }
		else{
			Toast.makeText(this,"Main image cannot be removed", Toast.LENGTH_LONG).show();
		}
		imgposID = -1;
	}
	
	@SuppressLint("NewApi")
	public void saveBitmapFile(){
	    String imgName = ipid +"_"+curLayout;
	    Toast.makeText(this,imgName,Toast.LENGTH_SHORT).show();
		File imgFile = new  File(Environment.getExternalStorageDirectory()+"/"+imgName+".png");		 
		 FileOutputStream outStream;
		 customView.buildDrawingCache(true);
		// imgTest = (ImageView)findViewById(R.id.test);
		 Bitmap bmp = customView.getDrawingCache();
	        try {
	        //outStream = openFileOutput(imgName, MODE_PRIVATE);
	        	outStream = new FileOutputStream(imgFile);
	        bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
	        outStream.flush();
	        outStream.close();
	    
	        //File imgFile = new  File(Environment.getExternalStorageDirectory()+ "/"+imgName+".png");
	        
	        	objAdapter.openDataBase();
	            String path = imgName;	          
	          //  long v=objAdapter.saveImagePathToDatabase("TOWER_BL", ipid, imgName,"TOWER_BL");
	            long v=objAdapter.saveImagePathToDatabase(imgFile.getAbsolutePath(), ipid, imgName,"TOWER_BL");
	          //  Toast.makeText(this,"L:"+String.valueOf(v),Toast.LENGTH_LONG).show();
	        
	        }catch (Exception e) {	        
	        	Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
	       } 
	        Toast.makeText(this,"Layout stored",Toast.LENGTH_SHORT).show();
            Intent iAdjBldg = new Intent(this,AdjBldgLayoutActivity.class);
            iAdjBldg.putExtra("ipid",ipid);
            startActivity(iAdjBldg);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//Toast.makeText(this,"fired",Toast.LENGTH_SHORT).show();
		
		
		runningX = event.getX();
		runningY = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{	
			checkList();
			Toast.makeText(this,"line start",Toast.LENGTH_SHORT).show();
			actionMove = false;
			
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(runningX != 0 && runningY!=0 )			
					customView.drawImage(runningX,runningY,imgposID,imgList);
				actionMove = true;				
				
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			if(runningX != 0 && runningY!=0 ){			
				if(actionMove){
				for(int i = 0 ; i <imgList.size() ; i++){
					Image imgUP = imgList.get(i);
					if(imgUP.getuniqueID() == imgposID){
						imgUP.setXCoordinate(runningX);
						imgUP.setYCoordinate(runningY);
					}
				}
				imgposID = -1;
			  }
				
				if(!actionMove && drwline){
					//Toast.makeText(this,"in list",Toast.LENGTH_SHORT).show();
							
					if(tempx ==0 && tempy == 0){
						tempx = runningX;
						tempy = runningY;	
					}
					else{
						linesdraw.add(runningX);
						linesdraw.add(runningY);
						linesdraw.add(tempx);
						linesdraw.add(tempy);
						customView.drawline(linesdraw);
						Toast.makeText(this,"line end",Toast.LENGTH_SHORT).show();
						tempx =0;
						tempy =0;
						drwline = false;
					}
					
				}
				if(!actionMove && iswrite){
					WriteTextLayout wobj = new WriteTextLayout(writeList.size() +1, runningX,runningY,textoncanvas);
					writeList.add(wobj);
					customView.writeText(writeList);
					iswrite = false;
				}
				
				actionMove = false;				
			}
	}
		
		return true;
		 
	}
	
	@SuppressLint("NewApi")
	public void checkList()
	{
		//Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
		for(int i=0;i<imgList.size();i++){
			if(i!=0){
			 Image img = imgList.get(i);
			 if((img.getuniqueID()!= img.getID()) && (img.getName().isEmpty())){
			 Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),img.getID());
			 }
			 float x = img.getXCoordinate();
			 float y = img.getYCoordinate();
			 float dx = img.getW() + x;
			 float dy = img.getH() + y;
			 if(runningX < dx && runningX > x){
				 if(runningY < dy && runningY > y){
					 shpFocused = img.getID();
					 imgposID = img.getuniqueID();
					 img.setXCoordinate(runningX);
					 img.setYCoordinate(runningY);
					 
				 }
			 }
			} 
			 
		}
		
		
	}

}
