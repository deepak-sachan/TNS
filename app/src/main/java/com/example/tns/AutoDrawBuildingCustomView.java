package com.example.tns;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import java.math.*;
import java.util.ArrayList;
import java.util.Collections;

public class AutoDrawBuildingCustomView extends View{

	Context context;
	Paint mPaint;
	float startlinex ,startliney;
	ArrayList<WriteTextLayout> writeList;
	float initImagesX[] = new float[]{} , initImagesY[]  = new float[]{}, imagesRight[] , imagesBottom[];
	int initImagesID[] = new int[]{};
	Bitmap mBitmap[] ,mBitmapBlue,mBitmapRed;
	char operation;
	ArrayList<Image> movingList;
	ArrayList<Float> linesDraw = new ArrayList<Float>();
	float movingx,movingy;
	int imageMoving,imgRemoved;
	public AutoDrawBuildingCustomView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AutoDrawBuildingCustomView(Context context, AttributeSet attrs) {
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
		  case 'm':
			  caseM(canvas);
			  	break;	  
		  case 'w':		  
			  	casew(canvas);
				  break;
			  
		  }
	}
	
	public void casew(Canvas canvas){
		 
		if(movingList == null || movingList.size() <= 0){		 
		 for(int j =0; j<initImagesID.length;j++){			 
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
			 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);
			 if(j > 1)
			  canvas.drawLine(startlinex, startliney, initImagesX[j],initImagesY[j], mPaint);
		  }
		}
		else{
			for(int j =0; j<movingList.size();j++){
			Image listImg = movingList.get(j);
			 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){
					 mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());					 
					 canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
					 if(j >1)
						 canvas.drawLine(startlinex, startliney, listImg.getXCoordinate(),listImg.getYCoordinate()+20, mPaint);
				}
		    }
		}		
		
		  if(writeList.size() > 0){
			 for(int j =0; j<writeList.size();j++){
					WriteTextLayout wlist = writeList.get(j);
					canvas.drawText(wlist.getName(),wlist.getXCoordinate(),wlist.getYCoordinate(),mPaint);
			 }
		 }
		
	 }
	
	
	public void caseM(Canvas canvas){
		if(movingList!= null){
		 for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
			  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0){
			    canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
			    if(j>1)
			    canvas.drawLine(startlinex, startliney, listImg.getXCoordinate(),listImg.getYCoordinate()+20, mPaint);
			  }
			  else
				  if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 && imageMoving!=2){
					  canvas.drawBitmap(mBitmapBlue,movingx,movingy,mPaint);
					  canvas.drawLine(startlinex, startliney, listImg.getXCoordinate(),listImg.getYCoordinate()+20, mPaint);
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
		
	}
	
	public void caseI(Canvas canvas){
		 for(int j =0; j<initImagesID.length;j++){			 
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);				  
			  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
			 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);
			 if(j==1)
			 {
				 startlinex = initImagesX[j]+20;
				 startliney = initImagesY[j];
			 }
			 if(j >1)
				 canvas.drawLine(startlinex, startliney, initImagesX[j],initImagesY[j], mPaint);
			 
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
	
	public void writeText(ArrayList<WriteTextLayout> maintainList){
		writeList = new ArrayList<WriteTextLayout>(maintainList);
		imageMoving = -1;
		//int num = writeList.size();
		//WriteTextLayout wobj = new WriteTextLayout(num +1, x,y,text);
		//writeList.add(wobj);		
		operation = 'w';
		invalidate();
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
		//Log.w("calling","cornervalues");
		//getCornerValues();
		invalidate();

	}

}
