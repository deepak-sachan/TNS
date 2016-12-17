package com.example.tns;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CompassView extends View {
	Paint paint = new Paint();
	float azimut;
	Context context;
	public CompassView(Context context) {
		super(context);
		this.context = context;
		/* Set Paint as White */
		paint.setColor(Color.BLACK);
		/* Set Stroke Width of 2 */
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		/* Set Anitalias */
		paint.setAntiAlias(true);
		/* Set Text Size of 20f */
		paint.setTextSize(20f);
	}
	
	public CompassView(Context context, AttributeSet attrs){
		super(context,attrs);
		this.context = context;
		/* Set Paint as White */
		paint.setColor(Color.BLACK);
		/* Set Stroke Width of 2 */
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		/* Set Anitalias */
		paint.setAntiAlias(true);
		/* Set Text Size of 20f */
		paint.setTextSize(20f);
	}

	protected void onDraw(Canvas canvas) {
		/* Get Width and Height of the canvas */
		int width = 85;
		int height = 85;
		Log.w("width",String.valueOf(getWidth()/2));
		Log.w("height",String.valueOf(getHeight()/2));
		/* Get center point of the canvas */
		//int centerx = width / 2;
		//int centery = height / 2;
		canvas.rotate(-azimut * 360 / (2 * 3.14159f), 20,((getHeight()/2)+20));
		canvas.drawLine(25,getHeight()/2,25,(getHeight()/2) +60, paint);
		canvas.drawLine(5,(getHeight()/2)+20,45,(getHeight()/2)+20, paint);
		canvas.drawText("N",0,(getHeight()/2)+20,paint);
		//Toast.makeText(context, "A:"+String.valueOf(azimut),Toast.LENGTH_SHORT).show();
		/* Rotate the canvas based on the angle using azimuth */
		//canvas.rotate(-azimut * 360 / (2 * 3.14159f), centerx, centery);
		// draw north - south line
		//canvas.drawLine(420 ,25, 420, 65,
			//	paint);
		// draw east - west line
		//canvas.drawLine(375,50,415,50,
			//	paint);
		// draw names
		/*canvas.drawText("N", 420,25, paint);
		canvas.drawText("South", 420,70, paint);
		canvas.drawText("East", 415,50, paint);
		canvas.drawText("West", 375,50, paint);*/
	}
	
	public void takeAzi(float orient){
		this.azimut = orient;
		invalidate();
	}
	public void letdraw(){
		invalidate();
	}
}