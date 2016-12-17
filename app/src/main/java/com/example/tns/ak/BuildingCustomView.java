package com.example.tns.ak;

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
import java.util.List;

public class BuildingCustomView extends View{

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
	Bitmap bitmap;
	int curImgPos;
	
	public BuildingCustomView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public BuildingCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(20);
		mPaint.setStrokeWidth(1);		
		
	}
	
	protected void onDraw(Canvas canvas) {
		  super.onDraw(canvas);
		  
		  switch(operation){
		  case 'i':
			  Log.w("i","inside case i");
			    caseI(canvas);			 
			  break;
		  case 'm':
			  caseMoveImage(canvas);
			  	break;	  
		  case 'w':		  
			  	casew(canvas);
				  break;
		  case 'l':		  
			  	caseDrawLine(canvas);
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
		invalidate();

	}
	
	public void mooveImg(float x,float y,int curpos,ArrayList<Image>maintainList)
	{
		movingList = new ArrayList<Image>(maintainList);
		movingx = x;
		movingy = y;
		imageMoving = curpos;
		//curImgPos=curpos;
		operation='m';
		invalidate();
	}
	public void caseMoveImage(Canvas canvas)
	{
		
		for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			  bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
			  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0)
			  {
			    canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
			    if(listImg.getXCoordinate()<startlinex)
			    {
					 canvas.drawLine(startlinex,startliney, listImg.getXCoordinate()+listImg.getW(), listImg.getYCoordinate()+listImg.getH(), mPaint);
			    }
				  else
				  {
				  canvas.drawLine(startlinex,startliney, listImg.getXCoordinate(),listImg.getYCoordinate()+listImg.getH(), mPaint);
				  }
			  }
			    
			  else
				  if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1)
				  {
				  canvas.drawBitmap(bitmap,movingx,movingy,mPaint);
				  if(listImg.getXCoordinate()<startlinex)
				    {
						 canvas.drawLine(startlinex,startliney, movingx+listImg.getW(), movingy+listImg.getH(), mPaint);
				    }
				  else
				  {
				  canvas.drawLine(startlinex,startliney, movingx,movingy+listImg.getH(), mPaint);
				  }
				  listImg.setXCoordinate(movingx);
				  listImg.setYCoordinate(movingy);
				  }
				  // canvas.drawLine(startlinex,startliney, listImg.getXCoordinate()+listImg.getW()-10, listImg.getYCoordinate()+listImg.getH()-10, mPaint);

		  }
		
/*		for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 ){
				 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());			  
				 canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
				 canvas.drawLine(startlinex,startliney, listImg.getXCoordinate()+listImg.getW()-10, listImg.getYCoordinate()+listImg.getH()-10, mPaint);
//				 LineData linedata= new LineData(j, startlinex, startliney, listImg.getXCoordinate()+listImg.getW()-5, listImg.getYCoordinate()+listImg.getH()-5);
//				 lineList.add(linedata);
				 // canvas.drawLine(listImg.getXCoordinate()+listImg.getW(), listImg.getYCoordinate()+listImg.getH(), startlinex, startliney, mPaint);
			 }	 
		  }*/
	}
	
	
	public void caseDrawLine(Canvas canvas)
	{
		 for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 ){
				 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());			  
				 canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
				 canvas.drawLine(startlinex,startliney, listImg.getXCoordinate()+listImg.getW()-5, listImg.getYCoordinate()+listImg.getH()-5, mPaint);
				 // canvas.drawLine(listImg.getXCoordinate()+listImg.getW(), listImg.getYCoordinate()+listImg.getH(), startlinex, startliney, mPaint);
			 }	 
		  }
		 
	}
	
	public void initializeLine(float x1,float y1,ArrayList<Image>maintainList)
	{
		movingList = new ArrayList<Image>(maintainList);
		startlinex=x1;
		startliney=y1;
		operation='l';
		invalidate();
	}

}
