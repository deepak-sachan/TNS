package com.example.tns;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Rose extends ImageView {
  Paint paint;
  int direction = 0;
  Context context;
  
  public Rose(Context context) {
	    super(context);

	   	  }
	
	public Rose(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		 paint = new Paint();
		    paint.setColor(Color.WHITE);
		    paint.setStrokeWidth(2);
		    paint.setStyle(Style.STROKE);

		    this.setImageResource(R.drawable.compassrose);

	}
  


  @Override
  public void onDraw(Canvas canvas) {
    int height = this.getHeight();
    int width = this.getWidth();

    canvas.rotate(direction, width / 2, height / 2);
    super.onDraw(canvas);
  }

  public void setDirection(int direction) {
    this.direction = direction;
    this.invalidate();
  }

}
