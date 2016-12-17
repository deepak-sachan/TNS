package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.*;
import java.util.ArrayList;
import java.util.Collections;

public class MyCustomView extends View  {

	Paint mPaint;
	ArrayList<Image> movingList;
	ArrayList<WriteTextLayout> writeList;
	Bitmap mBitmap[] ,mBitmapBlue,mBitmapRed;
	ArrayList<Float> linesDraw = new ArrayList<Float>();
	int initImagesID[] = new int[]{};
	float[] lines = null;
	float initImagesX[] = new float[]{} , initImagesY[]  = new float[]{}, imagesRight[] , imagesBottom[];
	float lastCircleX , lastCircleY , lastRectX , lastRectY;
	int mainWidth , mainHeight;
	float movingx,movingy;
	int imageMoving,imgRemoved;
	String shape;
	Context context;
	char operation;
	String writeT ="";
	Bitmap newBitmap;
	Boolean drawCircle=false,drawRect=false,drawmovingcircle=false;
	String itsCircle="";
	public MyCustomView(Context context){
		super(context);
		this.context = context;
		
	}
	
	public MyCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mPaint = new Paint();
		operation = 'c';
		//Toast.makeText(context, "inside init",Toast.LENGTH_LONG).show();
		// TODO Auto-generated constructor stub
		//mBitmapBlue = BitmapFactory.decodeResource(getResources(), R.drawable.draw);
	//	mBitmapBlue = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
		
		
		  //paint.setStrokeWidth(20);
		 // paint.setColor(Color.RED);
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
			 /*android.graphics.Bitmap.Config bitmapConfig =
			            text.getConfig();
			 Bitmap.createBitmap(width, height, config)*/
			 //Log.w("ismuta",String.valueOf(text.isMutable()));
		 }
		 
		 public void casel(Canvas canvas){
			 
			 if(movingList == null || movingList.size() <= 0 ){		 
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
			 
			 }
		 
		 
		
	 
	 public void caseM(Canvas canvas){
		 for(int j =0; j<movingList.size();j++){			 
			 Image listImg = movingList.get(j);
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
			  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0)
			    canvas.drawBitmap(mBitmapBlue,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
			  else
				  if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1)
				  canvas.drawBitmap(mBitmapBlue,movingx,movingy,mPaint);
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
		 
	 }
	 
		public Bitmap drawTextToBitmap(Context gContext,  int gResId,  String gText) {
	        Resources resources = gContext.getResources();
	        float scale = resources.getDisplayMetrics().density;
	        Bitmap bitmap = 
	            BitmapFactory.decodeResource(resources, gResId);
	       
	        android.graphics.Bitmap.Config bitmapConfig =
	            bitmap.getConfig();
	        // set default bitmap config if none
	        if(bitmapConfig == null) {
	          bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
	        }
	        // resource bitmaps are imutable, 
	        // so we need to convert it to mutable one
	        bitmap = bitmap.copy(bitmapConfig, true);
	       
	        Canvas canvas = new Canvas(bitmap);
	        // new antialised Paint
	        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	        // text color - #3D3D3D
	        paint.setColor(Color.rgb(110,110, 110));
	        // text size in pixels
	        paint.setTextSize((int) (12 * scale));
	        // text shadow
	        paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
	       
	        // draw text to the Canvas center
	        Rect bounds = new Rect();
	        paint.getTextBounds(gText, 0, gText.length(), bounds);
	        int x = (bitmap.getWidth() - bounds.width())/6;
	        int y = (bitmap.getHeight() + bounds.height())/5;
	       
	        canvas.drawText(gText, x+6,y+7, paint);
	        //this.newBitmap.createBitmap(bitmap.copy(bitmapConfig, false));
	        //bitmap = bitmap.copy(bitmapConfig, false);
	        return bitmap;
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
	 }
	 
	 public void caseI(Canvas canvas){
		 for(int j =0; j<initImagesID.length;j++){			 
			  mBitmapBlue = BitmapFactory.decodeResource(getResources(),initImagesID[j]);				  
			  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
			 canvas.drawBitmap(mBitmapBlue,initImagesX[j],initImagesY[j],mPaint);				  
		  }			
		 
	 }
/*
	public boolean onTouch(View view,MotionEvent event) {
		boolean eventConsumed = true;
		int x = (int)event.getX();
		int y = (int)event.getY();		
		int action = event.getAction();		
		if (action == MotionEvent.ACTION_DOWN) {
			
			
			
		} else if (action == MotionEvent.ACTION_UP) {				

		} else if (action == MotionEvent.ACTION_MOVE) {
		//Toast.makeText(getBaseContext(),"inside blue case",Toast.LENGTH_LONG).show();
			
					
		}
		return false;
	}*/


/*	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}*/
	
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

	
/*	protected void onDraw(Canvas canvas) {
		int i = 0;

		Paint p = new Paint();
		
		while (i < nImages.length){

		canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(),

		nImages[i]), 5,5,p);

		//mX = mX + dX;

		i += 1;

		
	}

	}*/
	 
	
	
	
	 public void drawCircle(){
		 	drawCircle = true;
		 	invalidate();
	 }
	 
	 public void drawRect(){
		 	drawRect = true;
		 	operation = 's';
		 	invalidate();
	 }
	 
	private void setViewPosition(View view, MotionEvent event)
    {
          int leftdist =(int) event.getRawX();
          int topdist =(int) event.getRawY();
          /*if(view == brownView){
        	  params1.x = (int)event.getX()-(view.getWidth()/2);
        	  params1.y = (int)event.getY()-(view.getWidth()/2);
        	  view.setLayoutParams(params1);
        	  Toast.makeText(getBaseContext(),"inside brown",Toast.LENGTH_LONG).show();
        	  
          }
          else if(view == redView){
        	  params2.x = (int)event.getX()-(view.getWidth()/2);
        	  params2.y = (int)event.getY()-(view.getWidth()/2);
        	  view.setLayoutParams(params2);
        	  Toast.makeText(getBaseContext(),"inside red",Toast.LENGTH_LONG).show();
          }
          else 
          {
        	  params.x = leftdist-(view.getWidth()/2);
        	  params.y = topdist-(view.getHeight()/2);
        	  view.setLayoutParams(params);
        	  //Toast.makeText(getBaseContext(),"inside blue",Toast.LENGTH_LONG).show();
          }*/
          
    }

	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int)event.getX();
		int y = (int)event.getY();
		Toast.makeText(context, "inside touch",Toast.LENGTH_LONG).show();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN :			
			boolean res = checkIfCircle(x,y);
			if(res)
			shape = "circle";
			else
				shape = "no";
			Toast.makeText(context, "shape :"+shape,Toast.LENGTH_LONG).show();
			break;
		case MotionEvent.ACTION_MOVE:
			if(shape.equalsIgnoreCase("circle")){
				movingx = x;
				movingy = y;
				drawmovingcircle = true;
				drawCircle = false;
			}
			
			break;
		}
		invalidate();
		return false;
	}
	
	public boolean checkIfCircle(float x,float y){
		int dx = x - 30;
		int dy = y -30;
		int point = (int) Math.sqrt((dx * dx)+ (dy * dy));
		if(point == 30 || point < 30)		
		return true;		
		else
		return false;
	}*/
	
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
	
	public void drawline(float[] l){
		lines = new float[]{};
		System.arraycopy(l, 0,lines,0,l.length);
		operation = 'l';
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
	
	public void checkPicture(float x,float y){
		/*float dx ,dy;
		if(x > 30){
			dx = x - 30;
		}
		else{
			dx = 30 - x;
		}
		if(y > 30){
			dy = y - 30;
		}
		else{
			dy = 30 - y;
		}
		double res = Math.sqrt((dx * dx) + (dy * dy));
		if(res <= 30){
			itsCircle = "circle";
		}
		else{
			if(x > 45 && x <60){
				if(y < 100 && y >30)
					itsCircle = "rect";
			}
			
		}
		return itsCircle;*/
		//for(int i=0;i<ima)
		
	}
	   
	
}	
