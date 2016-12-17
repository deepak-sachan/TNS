package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Im;
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

public class AdjBldgLayoutActivity extends Activity implements OnClickListener,OnTouchListener{

	AdjBldgCustomView customView;
	ArrayList<Float> linesdraw = new ArrayList<Float>();
	String bitmappic ;
	boolean drwline = false,iswrite=false;
	Bitmap mBitmapRed;
	float tempx = 0 , tempy = 0;
	String textoncanvas="";
	float textx = 0,texty =0;
	ArrayList<WriteTextLayout> writeList;
	ImageButton onSave,onCaution,onNorth,onRemove,onUndoText;
	ImageButton onTowerR , onTowerT ,onPoleR ,onPoleT,onPoleE;
	ImageButton onLine,onWriteText,onLineRemoveAll,onLineRemoveLast;
	ImageView imgTest;
	String shape , btnClickImage;
	boolean circle=false ,rectangle = false;
	float runningX=0 , runningY=0;
	boolean actionMove = false;
	ArrayList<Image> imgList;
	int shpFocused =-1;
	int imgposID = -1;
	AccessData objAdapter;
	String curLayout = "Adjbuildinglayout";
	int[] images = new int[]{R.drawable.drawad, R.drawable.towert};
	String[] layouts = new String[]{"GBT","RTT","RTP"};
	String ipid;
	float[] imagesdx = new float[]{8,140};
	float[] imagesdy = new float[]{8,80};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_custom_adjbldg_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
        ipid = getIntent().getStringExtra("ipid");
		customView = (AdjBldgCustomView)findViewById(R.id.customView);
		customView.setOnTouchListener(this);								
		customView.setDrawingCacheEnabled(true);
		customView.initialiseImages(images,imagesdx,imagesdy);
		initList();
		objAdapter = new AccessData(this);
		onSave = (ImageButton)findViewById(R.id.btnSave);
		onSave.setOnClickListener(this);		
		onRemove = (ImageButton)findViewById(R.id.btnRemove);
		onRemove.setOnClickListener(this);
		onTowerT = (ImageButton)findViewById(R.id.btntt);
		onTowerT.setOnClickListener(this);
		onTowerR = (ImageButton)findViewById(R.id.btntr);
		onTowerR.setOnClickListener(this);
		onPoleE = (ImageButton)findViewById(R.id.btnpe);
		onPoleE.setOnClickListener(this);
		onPoleR = (ImageButton)findViewById(R.id.btnpr);
		onPoleR.setOnClickListener(this);
		onPoleT = (ImageButton)findViewById(R.id.btnpt);
		onPoleT.setOnClickListener(this);
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
		 
		objAdapter = new AccessData(this);
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Title
        builder.setTitle("Choose Layout");
        builder.setSingleChoiceItems(layouts, -1, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				curLayout = layouts[which];
				dialog.dismiss();
			}
		});

        builder.create();
        builder.show();*/
        	
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
	
	
	public void addToImages(){
		Bitmap newImg=null;
		int newID = 0;
		
		if(btnClickImage.equalsIgnoreCase("polee")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.polee);
			newID = R.drawable.polee;
		}
		else if(btnClickImage.equalsIgnoreCase("poler")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.poler);
			newID = R.drawable.poler;
		}
		else if(btnClickImage.equalsIgnoreCase("polet")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.polet);
			newID = R.drawable.polet;
		}
		else if(btnClickImage.equalsIgnoreCase("towerr")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.tower);
			newID = R.drawable.tower;
		}
		else if(btnClickImage.equalsIgnoreCase("towert")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.towert);
			newID = R.drawable.towert;
		}
		int uniID = imgList.size();
		Image item = new Image(newID ,20,15,newImg.getWidth(),newImg.getHeight(),uniID+1,"");		
		imgList.add(item);
		btnClickImage = "";
		customView.drawNewImage(imgList);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btntt:
			btnClickImage = "towert";
			addToImages();
			
		break;
	case R.id.btnSave:				
			saveBitmapFile();
		break;
	case R.id.btntr:
		btnClickImage = "towerr";
		addToImages();
		
	break;
	case R.id.btnpe:
		btnClickImage = "polee";
		addToImages();
		
	break;
	case R.id.btnpr:
		btnClickImage = "poler";
		addToImages();
		
	break;
	case R.id.btnpt:
		btnClickImage = "polet";
		addToImages();
		
	break;
	
	case R.id.btnRemove:
		if(linesdraw!=null){
			if(linesdraw.size() > 0){
				int listsize = linesdraw.size();		
				texty = linesdraw.get(linesdraw.size() -1);
				linesdraw.remove(linesdraw.size() -1);
				listsize = linesdraw.size();
				textx = linesdraw.get(linesdraw.size() -1);
				linesdraw.remove(linesdraw.size() -1);
			}
		}
		removeImage();			
		break;
		
	case R.id.btnLine:
		//isLine = true;
	//	customView.drawline(linesdraw);
		Toast.makeText(this,"line!",Toast.LENGTH_SHORT).show();
		drwline = true;
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
		File imgFile = new  File(Environment.getExternalStorageDirectory()+"/"+imgName+".png");	 
		 FileOutputStream outStream;
		 customView.buildDrawingCache(true);
	//	 imgTest = (ImageView)findViewById(R.id.test);
		 Bitmap bmp = customView.getDrawingCache();
	        try {
	   //     	outStream = openFileOutput(imgName, MODE_PRIVATE);
	        	outStream = new FileOutputStream(imgFile);
	        bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
	        outStream.flush();
	        outStream.close();
	        //String imgName = ipid +"_"+curLayout;
	       // File imgFile = new  File(Environment.getExternalStorageDirectory()+ "/"+imgName+".png");
	        
	        	objAdapter.openDataBase();
	            String path =imgName;	          
	           long v= objAdapter.saveImagePathToDatabase(imgFile.getAbsolutePath(), ipid, imgName,"ADJ_BL");
	        		   //"ADJ_BL", ipid, imgName,"ADJ_BL");
	        
	        	
	        
	        }catch (Exception e) {	        
	        	Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
	       } 
		 
		 Toast.makeText(this,"Layout stored",Toast.LENGTH_SHORT).show();
//		Intent iAdjBldg = new Intent(this,CameraMainActivity.class);
        Intent iAdjBldg = new Intent(this,CamTestActivity.class);
         iAdjBldg.putExtra("ipid",ipid);
         startActivity(iAdjBldg);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//Toast.makeText(this,"fired",Toast.LENGTH_SHORT).show();
		
		
		runningX = event.getX();
		runningY = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
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
				//9thfeb
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
						Toast.makeText(this,"line end",Toast.LENGTH_SHORT).show();
						customView.drawline(linesdraw);
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
	
	public void checkList()
	{
		//Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
		for(int i=0;i<imgList.size();i++){
			if(i >1){
			 Image img = imgList.get(i);			 
			 Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),img.getID());
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
