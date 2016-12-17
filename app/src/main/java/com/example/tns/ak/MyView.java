package com.example.tns.ak;

import java.util.ArrayList;
import java.util.List;

import com.example.tns.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View{

	Paint mPaint;
	ArrayList<Image> movingList;
	ArrayList<WriteTextLayout> writeList;
	int mainWidth , mainHeight;
	float movingx,movingy;
	int imageMoving,imgRemoved;
	String shape;
	Context context;
	public char operation='a';
	String writeT ="";
	Boolean drawCircle=false,drawRect=false,drawmovingcircle=false;
	float initImagesX[] = new float[]{} , initImagesY[]  = new float[]{}, imagesRight[] , imagesBottom[];
	Bitmap mBitmap[];
	String itsCircle="";
	ArrayList<Float> linesDraw = new ArrayList<Float>();
	float runningX=0 , runningY=0;
	boolean actionMove = false;
	int curImgPos;
	public static Boolean boolLineDraw=false;
	public static float x1,y1,x2,y2;
	String curImgName="";
	float[] lines = null;
	int initImagesID[] = new int[]{};
	
   Bitmap bitmap;
   List<Image> imageIdList=new ArrayList<Image>();
	
	float x,y;
	
	public MyView(Context context){
		super(context);
		this.context = context;
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(20);
		mPaint.setStrokeWidth(1);
	}
	
	 @Override
	 public void onDraw(Canvas canvas)
	 {
	 Paint paint = new Paint();
	 paint.setStyle(Paint.Style.FILL);
	 paint.setColor(Color.BLUE);
	 paint.setStrokeWidth(3);
	switch (operation) {
	case 'm':
		caseMoveImg(canvas);
		break;
		
	case 'l':
	    caseDrawLine(canvas);
		break;
	case 'n':
	    caseDrawNewImg(canvas);
		break;
		
	case 'w':
	    caseWriteText(canvas);
		break;

	default:
		break;
	}
		 // Toast.makeText(context,"in draw",Toast.LENGTH_SHORT).show();
//		  canvas.drawLine(MainActivity.x1, MainActivity.y1, MainActivity.x2, MainActivity.y2, paint);
//		  MainActivity.boolLineDraw=false;
		//  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildingsrc);
			// canvas.drawBitmap(bitmap, MainActivity.x1, MainActivity.y1, paint);  //originally bitmap draw at x=o and y=0
	 
	 }

	 public boolean onTouchEvent(MotionEvent event)
		{
			return true;/*
		
			 runningX = event.getX();
			 runningY = event.getY();
		 
		 switch(event.getAction())
		    {
		      case MotionEvent.ACTION_DOWN: {
		    	  
		    		  if(boolLineDraw)
		  			{
		  			Toast.makeText(context,"line start",Toast.LENGTH_SHORT).show();
		  			x1 = event.getX();
		  			y1 = event.getY();
		  			
		  			}  
		    	  actionMove = false;
		    	  checkMoveImg();
		    	  
		    }
		   break;
		
		   case MotionEvent.ACTION_MOVE: 
		   {
			   
			   if(runningX != 0 && runningY!=0 )
			   {
				   
				actionMove = true;	
				drawImage(runningX, runningY, curImgPos, MainActivity.imgList);
				//invalidate();
			   }
		      x=(int)event.getX();
		      y=(int)event.getY();
		
		 }
		
		 break;
		   case MotionEvent.ACTION_UP: 
		
		       x=(int)event.getX();
		       y=(int)event.getY();
		       System.out.println(".................."+x+"......"+y); //x= 345 y=530 
		       if(boolLineDraw)
				{
		    	   x2=event.getX();
		    	   y2=event.getY();
				boolLineDraw=false;
				operation='l';
				invalidate();
				Toast.makeText(context,"Up",Toast.LENGTH_SHORT).show();
				}
		       
		    break; 
		   }
		  return true;
		 */}
	
	 
	 public void drawImage(float x ,float y ,int imgPosID,ArrayList<Image>maintainList){
			movingList = new ArrayList<Image>(maintainList);
			movingx = x;
			movingy = y;
			imageMoving = imgPosID;
			operation = 'm';
			//this.movingList = maintainList;
			invalidate();
		}
	 
/*	 public void drawLine(Canvas canvas,Paint paint)
	 {
		 linesDraw.add(x1);
		 linesDraw.add(y1);
		 linesDraw.add(x2);
		 linesDraw.add(y2);
		 canvas.drawLine(x1,y1,x2,y2, paint);
		  invalidate();
	 }*/
	 
	 public void drawline(ArrayList<Float> l){
			linesDraw = new ArrayList<Float>(l);
			imageMoving = -1;
			//System.arraycopy(l, 0,lines,0,l.length);
			operation = 'l';
			//fpoints = new ArrayList<Float>(pnts);
			invalidate();
		}

		public void checkMoveImg()
		{
			//Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
			for(int i=0;i<imageIdList.size();i++){
				if(i!=0){
				 Image img = imageIdList.get(i);
//				 if((img.getuniqueID()!= img.getID()) && (img.getName().isEmpty())){
//				 Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),img.getID());
//				 }
				 float x = img.getXCoordinate();
				 float y = img.getYCoordinate();
				 float dx = img.getW() + x;
				 float dy = img.getH() + y;
				 if(runningX < dx && runningX > x){
					 if(runningY < dy && runningY > y){
						// shpFocused = img.getID();
						 curImgPos = img.getuniqueID();
						 img.setXCoordinate(runningX);
						 img.setYCoordinate(runningY);
						 
					 }
				 }
				} 
				 
			}
			
			
		}
		
		public void moveImage(Canvas canvas,Paint paint)
		{
			for(int j =0; j<imageIdList.size();j++){			 
						 Image listImg = imageIdList.get(j);
						 
						 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
						  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0)
						    canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
						  else
						 
							  if(listImg.getuniqueID()==curImgPos)
							  canvas.drawBitmap(bitmap,movingx,movingy,mPaint);
					  }
			
		}
		
		
		
 public void caseDrawLine(Canvas canvas){
			 
			 if(movingList == null || movingList.size() <= 0 ){		 
				 for(int j =0; j<initImagesID.length;j++){			 
					  bitmap = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
					  
					  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
					 //canvas.drawBitmap(bitmap,initImagesX[j],initImagesY[j],mPaint);				  
				  }
				}
			 
				else{
					for(int j =0; j<movingList.size();j++){
					Image listImg = movingList.get(j);
					 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){						 
						 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());
						 canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
						 
					 }
				  }
				}
			 
			 canvas.drawLine(MainualDrawActivity.ln1x1, MainualDrawActivity.ln1y1, MainualDrawActivity.ln1x2, MainualDrawActivity.ln1y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln2x1, MainualDrawActivity.ln2y1, MainualDrawActivity.ln2x2, MainualDrawActivity.ln2y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln3x1, MainualDrawActivity.ln3y1, MainualDrawActivity.ln3x2, MainualDrawActivity.ln3y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln4x1, MainualDrawActivity.ln4y1, MainualDrawActivity.ln4x2, MainualDrawActivity.ln4y2, mPaint);
			 
			 
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
		
		
		 public void caseMoveImg(Canvas canvas){
			 for(int j =0; j<movingList.size();j++){			 
				 Image listImg = movingList.get(j);
				  bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());				  
				  if(imageMoving != listImg.getuniqueID() && listImg.getuniqueID()!= -1 && listImg.getuniqueID()!= 0 )
				    canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
				  else
					  if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1)
					  {
					     canvas.drawBitmap(bitmap,movingx,movingy,mPaint);
					     if(listImg.getuniqueID()== 1 || imageMoving==1)
					     {
					    	 
					    if(MainualDrawActivity.twrType.equalsIgnoreCase("Triangle"))
						    {
							     MainualDrawActivity.ln1x2=movingx+(listImg.getW()/2); MainualDrawActivity.ln1y2=movingy;
								 MainualDrawActivity.ln2x2=movingx; MainualDrawActivity.ln2y2=movingy+listImg.getH();
								 MainualDrawActivity.ln3x2=movingx+(listImg.getW()/2); MainualDrawActivity.ln3y2=movingy;
								 MainualDrawActivity.ln4x2=movingx+listImg.getW(); MainualDrawActivity.ln4y2=movingy+listImg.getH();
					    	 }
					    	 else
					    	 {
					    		 MainualDrawActivity.ln1x2=movingx; MainualDrawActivity.ln1y2=movingy;
								 MainualDrawActivity.ln2x2=movingx; MainualDrawActivity.ln2y2=movingy+listImg.getH();
								 MainualDrawActivity.ln3x2=movingx+listImg.getW(); MainualDrawActivity.ln3y2=movingy;
								 MainualDrawActivity.ln4x2=movingx+listImg.getW(); MainualDrawActivity.ln4y2=movingy+listImg.getH();
						     }
					    	 
					     }
					     
					  }
				  
			   }
		     	 canvas.drawLine(MainualDrawActivity.ln1x1, MainualDrawActivity.ln1y1, MainualDrawActivity.ln1x2, MainualDrawActivity.ln1y2, mPaint);
				 canvas.drawLine(MainualDrawActivity.ln2x1, MainualDrawActivity.ln2y1, MainualDrawActivity.ln2x2, MainualDrawActivity.ln2y2, mPaint);
				 canvas.drawLine(MainualDrawActivity.ln3x1, MainualDrawActivity.ln3y1, MainualDrawActivity.ln3x2, MainualDrawActivity.ln3y2, mPaint);
				 canvas.drawLine(MainualDrawActivity.ln4x1, MainualDrawActivity.ln4y1, MainualDrawActivity.ln4x2, MainualDrawActivity.ln4y2, mPaint);
				 
			 
			 
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
		
	
		public void caseDrawNewImg(Canvas canvas){
			 
			 for(int j =0; j<movingList.size();j++){			 
				 Image listImg = movingList.get(j);
				 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1 ){
					 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());			  
					 canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
				 }	 
			  }	
			 
			 canvas.drawLine(MainualDrawActivity.ln1x1, MainualDrawActivity.ln1y1, MainualDrawActivity.ln1x2, MainualDrawActivity.ln1y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln2x1, MainualDrawActivity.ln2y1, MainualDrawActivity.ln2x2, MainualDrawActivity.ln2y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln3x1, MainualDrawActivity.ln3y1, MainualDrawActivity.ln3x2, MainualDrawActivity.ln3y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln4x1, MainualDrawActivity.ln4y1, MainualDrawActivity.ln4x2, MainualDrawActivity.ln4y2, mPaint);
			 
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
	 
		public void drawNewImage(ArrayList<Image>maintainList){
			movingList = new ArrayList<Image>(maintainList);
			//movingList = maintainList;
			operation = 'n';
			invalidate();
		}
		
		
		public void caseWriteText(Canvas canvas){
			 Log.w("write","text");		 
			if(movingList == null || movingList.size() <= 0){		 
			 for(int j =0; j<initImagesID.length;j++){			 
				  bitmap = BitmapFactory.decodeResource(getResources(),initImagesID[j]);
				  
				  //canvas.drawBitmap(mBitmap[j], initImagesX[j],initImagesY[j], mPaint);
				 canvas.drawBitmap(bitmap,initImagesX[j],initImagesY[j],mPaint);				  
			  }
			}
			else{
				for(int j =0; j<movingList.size();j++){
				Image listImg = movingList.get(j);
				 if(listImg.getuniqueID()!= 0 && listImg.getuniqueID()!= -1){
						 bitmap = BitmapFactory.decodeResource(getResources(),listImg.getID());					 
						 canvas.drawBitmap(bitmap,listImg.getXCoordinate(),listImg.getYCoordinate(),mPaint);
					 	}
			    }
			}
			
			 canvas.drawLine(MainualDrawActivity.ln1x1, MainualDrawActivity.ln1y1, MainualDrawActivity.ln1x2, MainualDrawActivity.ln1y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln2x1, MainualDrawActivity.ln2y1, MainualDrawActivity.ln2x2, MainualDrawActivity.ln2y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln3x1, MainualDrawActivity.ln3y1, MainualDrawActivity.ln3x2, MainualDrawActivity.ln3y2, mPaint);
			 canvas.drawLine(MainualDrawActivity.ln4x1, MainualDrawActivity.ln4y1, MainualDrawActivity.ln4x2, MainualDrawActivity.ln4y2, mPaint);
			 
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
//			System.arraycopy(imageID, 0,initImagesID,0,imageID.length);
//			System.arraycopy(x, 0,initImagesX,0,x.length);
//			System.arraycopy(y, 0,initImagesY,0,y.length);
			
			invalidate();

		}
		
}
