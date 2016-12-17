package com.example.tns;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AutoDrawCustomView extends View {
	
	Paint mPaint;
	AccessData objAdapter;
	ArrayList<Image> movingList;
	String ipid="AD-1116";
	ArrayList<WriteTextLayout> writeList;
	Bitmap mBitmap[] ,mBitmapBlue,mBitmapRed;
	ArrayList<Float> linesDraw = new ArrayList<Float>();
	int initImagesID[] = new int[]{};
	float[] lines = null;
	float initImagesX[] = new float[]{} , initImagesY[]  = new float[]{}, imagesRight[] , imagesBottom[];
	float lastCircleX , lastCircleY , lastRectX , lastRectY;
	int mainWidth , mainHeight,linemovingwidth,linemovingheight;
	float movingx,movingy,linemovingx=130 , linemovingy=80;
	int imageMoving,imgRemoved;
	String shape;
	ArrayList<String> cornerList;
	char operation;
	String writeT ="";
	Bitmap newBitmap;
	Boolean drawCircle=false,drawRect=false,drawmovingcircle=false;
	String itsCircle="";

	Context context;
	public AutoDrawCustomView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AutoDrawCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mPaint = new Paint();
		
	}
	
	protected void onDraw(Canvas canvas) {
		  super.onDraw(canvas);
		  
		  switch(operation){
		  case 'i':
			  Log.w("i","inside case i");
			    caseI(canvas);
			 
			  break;
		  case 'c':
			  //canvas.drawBitmap(mBitmapBlue, 0,0, mPaint);
			  break;
		  case 's':
		  		caseI(canvas);
		  		canvas.drawRect(0,0,30,10,mPaint);
		  		break;
		  case 'm':
			  caseM(canvas);
			  	break;
		  case 'n':
		        caseN(canvas);
		        break;
		  case 'r':
			  
		  	   caseR(canvas);
			  break;
		  case 'l':
			  	casel(canvas);
				  break;
			  case 'w':
			  	casew(canvas);
				  break;
			  case 'a':
				  	casea(canvas);
					  break;		  
		  
		  }
		  
		  
		 }
	
	public void casea(Canvas canvas){
		 
		 if(movingList.size() <= 0 && movingList == null){		 
			 for(int j =0; j<initImagesID.length;j++){			 
				  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
				  
				  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
				 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);				  
			  }
			}
			else{
				for(int j =0; j<movingList.size();j++){
				Image listImg = movingList.get(j);
				 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){
					 if(listImg.getuniqueID()!= listImg.getID()){
						 mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());
					 }
					 else{
						 mBitmapBlue = BitmapFactory.decodeFile(listImg.getName());
					 }
					 if(imageMoving != listImg.getuniqueID()){
						 canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
					 }
					 else{
						 canvas.drawBitmap(mBitmapBlue,movingx,movingy,mPaint);
					 }
				 }
			  }
			 }
		 
		 if(writeList!=null){
			 if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			 }
		//1st
		 canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
		 //2nd
		 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
		 //3rd
		 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
		 //4th
		 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
	 }
	 
	 @SuppressWarnings("static-access")
		public void casew(Canvas canvas){
			 Log.w("write","text");		 
			 /*newBitmap = drawTextToBitmap(context, R.drawable.writetxt,writeT);		 
			 try {
				FileOutputStream ous = context.openFileOutput(writeT+".png",context.MODE_WORLD_READABLE);			
				newBitmap.compress(CompressFormat.PNG, 90, ous);
				ous.flush();
				ous.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			if(movingList == null || movingList.size() <= 0){		 
			 for(int j =0; j<initImagesID.length;j++){			 
				  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
				  
				  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
				 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);				  
			  }
			}
			else{
				for(int j =0; j<movingList.size();j++){
				Image listImg = movingList.get(j);
				 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){
						 mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());					 
						 canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
					 	}
			    }
			}
			// canvas.drawBitmap(newBitmap, 20,15,mPaint);
			if(linesDraw!=null){
				 float[] floatArray = new float[linesDraw.size()];
				 Log.w("linesdraw",String.valueOf(linesDraw.size()));
				 for (int i = 0; i < linesDraw.size(); i++) {
				     Float f = linesDraw.get(i);
				     floatArray[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
				 }			 
				 canvas.drawLines(floatArray, 0,floatArray.length,mPaint);
				 }	
			  if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			//1st
			  canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
				 //2nd
				 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
				 //3rd
				 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
				 //4th
				 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
		 }
		 
		 public void casel(Canvas canvas){
			 
			 if(movingList == null || movingList.size() <= 0){		 
				 for(int j =0; j<initImagesID.length;j++){			 
					  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
					  
					  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
					 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);				  
				  }
				}
				else{
					for(int j =0; j<movingList.size();j++){
					Image listImg = movingList.get(j);
					 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){						 
						 mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());
						 canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
						 
					 }
				  }
				}
			 
			 if(writeList!=null){
			 if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			 }
			 if(linesDraw!=null){
				 float[] floatArray = new float[linesDraw.size()];
				 Log.w("linesdraw",String.valueOf(linesDraw.size()));
				 for (int i = 0; i < linesDraw.size(); i++) {
				     Float f = linesDraw.get(i);
				     floatArray[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
				 }			 
				 canvas.drawLines(floatArray, 0,floatArray.length,mPaint);
				 }
			//1st
			 canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
			 //2nd
			 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
			 //3rd
			 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
			 //4th
			 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
			 
	}
		 
		 
		
	 
	 public void caseM(Canvas canvas){
		 for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
			  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0)
			    canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
			  else
				  if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 && imageMoving!=2){
					  canvas.drawBitmap(mBitmapBlue,movingx,movingy,mPaint);
					  
					  //1st
					  canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
						 //2nd
						 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
						 //3rd
						 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
						 //4th
						 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
					  
				  }
				  else{
					  //If main images is moving
					  canvas.drawBitmap(mBitmapBlue,movingx,movingy,mPaint);
					  /* //1st
		 canvas.drawLine(24,21,130,80, mPaint);
		 //2nd
		 canvas.drawLine(171,81,283,22, mPaint);
		 //3rd
		 canvas.drawLine(131,102,24,195, mPaint);
		 //4th
		 canvas.drawLine(171,102,283,195, mPaint);*/
					  
					  linemovingx = movingx;
					  linemovingy = movingy;
					  linemovingwidth = listImg.getW();
					  linemovingheight = listImg.getH();
					  //1st
					  canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
						 //2nd
						 canvas.drawLine(linemovingx +linemovingwidth,linemovingy,283,22, mPaint);
						 //3rd
						 canvas.drawLine(linemovingx,linemovingy + linemovingheight,24,195, mPaint);
						 //4th
						 canvas.drawLine(linemovingx+linemovingwidth,linemovingy+linemovingheight,283,195, mPaint);
				  }	
		  }
		 
		 if(writeList!=null){
			 if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			 }
		 if(linesDraw!=null){
			 float[] floatArray = new float[linesDraw.size()];
			 Log.w("linesdraw",String.valueOf(linesDraw.size()));
			 for (int i = 0; i < linesDraw.size(); i++) {
			     Float f = linesDraw.get(i);
			     floatArray[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
			 }			 
			 canvas.drawLines(floatArray, 0,floatArray.length,mPaint);
			 }
		
	 }
	 
	 public void caseR(Canvas canvas){
		 
		 for(int j =0; j<movingList.size();j++){			 
			 Image Img = movingList.get(j);
			 if(Img.getuniqueID()!= 0 && Img.getuniqueID()!= -1 ){
				 mBitmapBlue = BitmapFactory.decodeResource(getResources(),Img.getID());
		         canvas.drawBitmap(mBitmapBlue,Img.getXCoordinate(),Img.getYCoordinate(),mPaint);
			 } 
			 //caseI(canvas); 
		  }			
		 if(linesDraw!=null){
			 float[] floatArray = new float[linesDraw.size()];
			 Log.w("linesdraw",String.valueOf(linesDraw.size()));
			 for (int i = 0; i < linesDraw.size(); i++) {
			     Float f = linesDraw.get(i);
			     floatArray[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
			 }			 
			 canvas.drawLines(floatArray, 0,floatArray.length,mPaint);
			 }	
		 if(writeList!=null){
			 if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			 }
		//1st
		 canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
		 //2nd
		 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
		 //3rd
		 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
		 //4th
		 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
	 }
	 
public void caseN(Canvas canvas){
		 
		 for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 ){
				 mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());			  
				 canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
			 }	 
		  }			
		 if(linesDraw!=null){
			 float[] floatArray = new float[linesDraw.size()];
			 Log.w("linesdraw",String.valueOf(linesDraw.size()));
			 for (int i = 0; i < linesDraw.size(); i++) {
			     Float f = linesDraw.get(i);
			     floatArray[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
			 }			 
			 canvas.drawLines(floatArray, 0,floatArray.length,mPaint);
			 }	
		 if(writeList!=null){
			 if(writeList.size() > 0){
				 for(int j =0; j<writeList.size();j++){
						WriteTextLayout wlist = writeList.get(j);
						canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
				 }
			 }
			 }
		 
		//1st
		 canvas.drawLine(24,21,linemovingx,linemovingy, mPaint);
		 //2nd
		 canvas.drawLine(linemovingx +41,linemovingy,283,22, mPaint);
		 //3rd
		 canvas.drawLine(linemovingx,linemovingy + 21,24,195, mPaint);
		 //4th
		 canvas.drawLine(linemovingx+41,linemovingy+21,283,195, mPaint);
	 }

	
	
	public void caseI(Canvas canvas){
		 for(int j =0; j<initImagesID.length;j++){			 
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);				  
			  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
			 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);
			 
		  }			
		// canvas.drawText("3m",75,47, mPaint);
		 //1st
		 canvas.drawLine(24,21,130,80, mPaint);
		 //2nd
		 canvas.drawLine(171,81,283,22, mPaint);
		 //3rd
		 canvas.drawLine(131,102,24,195, mPaint);
		 //4th
		 canvas.drawLine(171,102,283,195, mPaint);
		 int j=0,i=0;
		 if(cornerList!=null){
			 for(int k=0;k<cornerList.size();k++){
				 //canvas.drawText(cornerList.get(k),, y, mPaint);
				canvas.drawText(cornerList.get(k),75+i,47+j, mPaint);
				i = i+10;
				j = j+10;
				Log.w(String.valueOf(cornerList.get(k)),String.valueOf(k));
			 }
		 }
		 
	 }
	
	
	public void getCornerValues(){
	//	objAdapter = new AccessData(context);
		try{
			objAdapter = new AccessData(context);
		objAdapter.openDataBase();
		Cursor objCorner =objAdapter.getCornerValuesForLayoutDraw(ipid);
		Toast.makeText(context, "fe vals",Toast.LENGTH_SHORT).show();
		if(objCorner!=null){
			cornerList = new ArrayList<String>();
			while(objCorner.moveToNext()){
				cornerList.add(objCorner.getString(objCorner.getColumnIndex("nDistance")));
				Toast.makeText(context,objCorner.getString(objCorner.getColumnIndex("nDistance")), Toast.LENGTH_SHORT).show();
			//	Log.w("ncornerno",objCorner.getString(1));
			}
			
		}
		else{
			//Log.w("no","cornervalues");
			Toast.makeText(context, "corner values cursor null",Toast.LENGTH_SHORT).show();
		}
		}catch(Exception ex){
			Toast.makeText(context, "er"+ex.getMessage(),Toast.LENGTH_SHORT).show();
		}
	}
	
	public void drawImage(float x ,float y ,int imgPosID,ArrayList<Image>maintainList){
		movingList = new ArrayList<Image>(maintainList);
		movingx = x;
		movingy = y;
		imageMoving = imgPosID;
		operation = 'm';
		//this.movingList = maintainList;
		invalidate();
	}

	public void drawNewImage(ArrayList<Image>maintainList){
		movingList = new ArrayList<Image>(maintainList);
		//movingList = maintainList;
		operation = 'n';
		invalidate();
	}
	
	public void removeImage(ArrayList<Image>maintainList){
		movingList = new ArrayList<Image>(maintainList);
		operation = 'r';
		invalidate();
	}
	
	public void drawline(ArrayList<Float> l){
		linesDraw = new ArrayList<Float>(l);
		imageMoving = -1;
		//System.arraycopy(l, 0,lines,0,l.length);
		operation = 'l';
		//fpoints = new ArrayList<Float>(pnts);
		invalidate();
	}
	
	public void removeAllLines(){
		//System.arraycopy(l, 0,lines,0,l.length);
		linesDraw.clear();
		imageMoving = -1;
		operation = 'a';
		//fpoints = new ArrayList<Float>(pnts);
		invalidate();
	}
	
	
	public void writeText(ArrayList<WriteTextLayout> maintainList){
		writeList = new ArrayList<WriteTextLayout>(maintainList);
		imageMoving = -1;
		//int num = writeList.size();
		//WriteTextLayout wobj = new WriteTextLayout(num +1, x,y,text);
		//writeList.add(wobj);		
		operation = 'w';
		invalidate();
	}
	
	public void copyList(ArrayList<Image> maintainList){
		this.movingList = new ArrayList<Image>(maintainList);
	}
	
	
	public void initialiseImages(int imageID[] , float x[] , float y[]){
		//Toast.makeText(context, "inside initialiseImages",Toast.LENGTH_LONG).show();
		int count = imageID.length;
		initImagesID = new int[count];
		initImagesX = new float[count];
		initImagesY = new float[count];
		mBitmap = new Bitmap[count];
		operation = 'i';
		System.arraycopy(imageID, 0,initImagesID,0,imageID.length);
		System.arraycopy(x, 0,initImagesX,0,x.length);
		System.arraycopy(y, 0,initImagesY,0,y.length);
		Log.w("calling","cornervalues");
		//getCornerValues();
		invalidate();

	}

	
	

}
