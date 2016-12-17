package com.example.tns;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AutoDrawBuildingActivity extends Activity implements
		OnClickListener, OnTouchListener {

	AutoDrawBuildingCustomView customView;
	float runningX = 0, runningY = 0;
	float textx = 0, texty = 0;
	String ipid, towershape;
	boolean iswrite = false;
	ArrayList<Image> imgList;
	ArrayList<Integer> imagesFinal;
	boolean actionMove = false;
	ArrayList<WriteTextLayout> writeList;
	ImageButton onUndoText, onWriteText, onSave;
	int numBldg = 0;
	int startx = 150, starty = 40;
	AccessData objAdapter;
	int[] finalImageArr;
	float[] finaldx, finaldy, bldgdx, bldgdy;
	int[] imagesRect = new int[] { R.drawable.autodrawbldg, R.drawable.blue };
	int[] imagesCircle = new int[] { R.drawable.autodrawbldg,
			R.drawable.towercauto };
	int[] imagesTria = new int[] { R.drawable.autodrawbldg,
			R.drawable.towertauto };
	int[] images = new int[] { R.drawable.draw, R.drawable.blue };
	int[] bldgImg = new int[] {};
	float[] imagesdx = new float[] { 2, 150 };
	float[] imagesdy = new float[] { 2, 135 };
	int shpFocused = -1;
	String textoncanvas = "";
	String curLayout = "adjbldglayout";
	int imgposID = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_custom_autoadjbldg_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);
		ipid = getIntent().getStringExtra("ipid");
		numBldg = getIntent().getIntExtra("bldgnum", 0);
		towershape = getIntent().getStringExtra("towershape");
		onWriteText = (ImageButton) findViewById(R.id.btnText);
		onWriteText.setOnClickListener(this);
		onUndoText = (ImageButton) findViewById(R.id.btnUndoText);
		onUndoText.setOnClickListener(this);
		onSave = (ImageButton) findViewById(R.id.btnSave);
		onSave.setOnClickListener(this);
		// ipid = "DU-1116";
		// towershape = "Rectangle";
		// numBldg = 2;

		customView = (AutoDrawBuildingCustomView) findViewById(R.id.autocustomView);

		customView.setOnTouchListener(this);
		customView.setDrawingCacheEnabled(true);

		objAdapter = new AccessData(this);
		// getNumberOfBuildingDetails();
		if (towershape.equalsIgnoreCase("Rectangle")) {
			System.arraycopy(imagesRect, 0, images, 0, imagesRect.length);
			createBuildingArray();
			customView.initialiseImages(finalImageArr, finaldx, finaldy);
			initList();
		} else if (towershape.equalsIgnoreCase("Triangle")) {
			System.arraycopy(imagesTria, 0, images, 0, imagesTria.length);
			createBuildingArray();
			customView.initialiseImages(finalImageArr, finaldx, finaldy);
			initList();
		} else {
			System.arraycopy(imagesCircle, 0, images, 0, imagesCircle.length);
			createBuildingArray();
			customView.initialiseImages(finalImageArr, finaldx, finaldy);
			initList();
		}

	}

	public void addtoArray() {

	}

	public void createBuildingArray() {
		int len = images.length + numBldg;
		Log.w("len", String.valueOf(len));
		finalImageArr = new int[len];
		finaldx = new float[len];
		finaldy = new float[len];
		bldgImg = new int[numBldg];
		bldgdx = new float[numBldg];
		bldgdy = new float[numBldg];
		int i = 0;
		for (i = 0; i < images.length; i++) {
			finalImageArr[i] = images[i];
			finaldx[i] = imagesdx[i];
			finaldy[i] = imagesdy[i];
		}
		Log.w("copied images", String.valueOf(finalImageArr.length));

		float dx = 280, dy = 80;
		for (int k = 0; k < numBldg; k++) {
			bldgImg[k] = R.drawable.building;
			bldgdx[k] = dx;
			bldgdy[k] = dy;
			dx -= 30;
			if (k > 8) {
				dx = 280;
				dy += 15;
			}

			// dy -= 20;
		}
		Log.w("copied bldg img", String.valueOf(bldgImg.length));
		int j = 0;
		Log.w("ii", String.valueOf(i));
		while (j < numBldg) {
			finalImageArr[i] = bldgImg[j];
			finaldx[i] = bldgdx[j];
			finaldy[i] = bldgdy[j];
			i++;
			j++;
		}
		// System.arraycopy(bldgImg,0,finalImageArr,images.length,bldgImg.length);
		// System.arraycopy(bldgdx,0,finaldx,imagesdx.length,bldgdx.length);
		// System.arraycopy(bldgdy,0,finaldy,imagesdy.length,bldgdy.length);

	}

	// Maintain list of images.
	public void initList() {
		imgList = new ArrayList<Image>();
		for (int i = 0; i < finalImageArr.length; i++) {
			Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),
					finalImageArr[i]);
			Image item = new Image(finalImageArr[i], finaldx[i], finaldy[i],
					bitMoved.getWidth(), bitMoved.getHeight(), i + 1, "");
			imgList.add(item);
		}

	}

	@SuppressLint("NewApi")
	public void checkList() {
		// Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
		for (int i = 0; i < imgList.size(); i++) {
			if (i > 1) {
				Image img = imgList.get(i);
				if ((img.getuniqueID() != img.getID())
						&& (img.getName().isEmpty())) {
					Bitmap bitMoved = BitmapFactory.decodeResource(
							getResources(), img.getID());
				}
				float x = img.getXCoordinate();
				float y = img.getYCoordinate();
				float dx = img.getW() + x;
				float dy = img.getH() + y;
				if (runningX < dx && runningX > x) {
					if (runningY < dy && runningY > y) {
						shpFocused = img.getID();
						imgposID = img.getuniqueID();
						img.setXCoordinate(runningX);
						img.setYCoordinate(runningY);

					}
				}
			}

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		runningX = event.getX();
		runningY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			checkList();
			actionMove = false;

		}

		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (runningX != 0 && runningY != 0)
				customView.drawImage(runningX, runningY, imgposID, imgList);
			actionMove = true;
		}

		else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (runningX != 0 && runningY != 0) {
				if (actionMove) {
					for (int i = 0; i < imgList.size(); i++) {
						Image imgUP = imgList.get(i);
						if (imgUP.getuniqueID() == imgposID) {
							imgUP.setXCoordinate(runningX);
							imgUP.setYCoordinate(runningY);
						}
					}
					// 9thfeb
					imgposID = -1;
				}

				/*
				 * if(!actionMove && drwline){
				 * //Toast.makeText(this,"in list",Toast.LENGTH_SHORT).show();
				 * 
				 * if(tempx ==0 && tempy == 0){ tempx = runningX; tempy =
				 * runningY; } else{ Toast.makeText(this,"line stop",
				 * Toast.LENGTH_SHORT).show(); linesdraw.add(runningX);
				 * linesdraw.add(runningY); linesdraw.add(tempx);
				 * linesdraw.add(tempy); customView.drawline(linesdraw); tempx
				 * =0; tempy =0; drwline = false; }
				 * 
				 * }
				 */
				if (!actionMove && iswrite) {
					WriteTextLayout wobj = new WriteTextLayout(
							writeList.size() + 1, runningX, runningY,
							textoncanvas);
					writeList.add(wobj);
					customView.writeText(writeList);
					iswrite = false;
				}

				actionMove = false;
			}
		}

		return true;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnUndoText:
			if (writeList != null) {
				if (writeList.size() > 0) {
					writeList.remove(writeList.size() - 1);
				}
			}
			customView.writeText(writeList);

			break;
		case R.id.btnSave:
			saveBitmapFile();
			break;

		case R.id.btnText:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Title
			builder.setTitle("Please enter text:");
			LayoutInflater inflater = this.getLayoutInflater();
			final View mychildview = inflater.inflate(R.layout.writetextdialog,
					null);
			builder.setView(mychildview);
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

							iswrite = true;
							EditText ed = (EditText) mychildview
									.findViewById(R.id.textlayout);
							textoncanvas = ed.getText().toString();

							if (writeList == null) {
								writeList = new ArrayList<WriteTextLayout>();
							}
							arg0.dismiss();
						}
					});
			// To show negative button
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();

						}
					});
			builder.create();
			builder.show();
			Log.w("outof", "switchmain");
			break;

		}

	}

	@TargetApi(Build.VERSION_CODES.DONUT)
	public void saveBitmapFile() {
		String imgName = ipid + "_" + curLayout;

		FileOutputStream outStream;
		customView.buildDrawingCache(true);
		File imgFile = new File(Environment.getExternalStorageDirectory() + "/"
				+ imgName + ".png");
		// imgTest = (ImageView)findViewById(R.id.test);
		Bitmap bmp = customView.getDrawingCache();
		try {
			// outStream = openFileOutput(imgName, MODE_PRIVATE);
			outStream = new FileOutputStream(imgFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
			outStream.flush();
			outStream.close();

			// File imgFile = new
			// File(Environment.getExternalStorageDirectory()+
			// "/"+imgName+".png");

			objAdapter = new AccessData(this);
			objAdapter.openDataBase();
			// long v= objAdapter.saveImagePathToDatabase("TOWER_BL", ipid,
			// imgName,"TOWER_BL");
			long v = objAdapter.saveImagePathToDatabase(
					imgFile.getAbsolutePath(), ipid, imgName, "TOWER_BL");
			Toast.makeText(this, "L:" + String.valueOf(v), Toast.LENGTH_LONG)
					.show();

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		Toast.makeText(this, "Layout stored", Toast.LENGTH_SHORT).show();
//	 Intent iAdjBldg = new Intent(this,CameraMainActivity.class);
		Intent iAdjBldg = new Intent(this, CamTestActivity.class);
		iAdjBldg.putExtra("ipid", ipid);
		startActivity(iAdjBldg);

	}

}