package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//  created by Ankit Tyagi on 22 nov 2014
@SuppressLint("NewApi")
public class CamTestActivity extends Activity implements SensorEventListener {
	private static final String TAG = "CamTestActivity";
	Preview preview;
	Button buttonClick;
	Camera camera;
	Activity act;
	Context ctx;
	private static String[] imagesList = new String[] {};
	byte[] dataImg;

	private ProgressDialog dialog;
	private float currentDegree = 0f;

	private SensorManager mSensorManager;

	private TextView tvHeading;
	//private ImageView image;
	private Spinner spinner;
	private static String imgName = "";
	static String ipid = "test123";

	ArrayAdapter<String> adapter;
	AccessData objAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		act = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		tvHeading = (TextView) findViewById(R.id.tvHeading);
		//image = (ImageView) findViewById(R.id.imageViewCompass);

		spinner = (Spinner) findViewById(R.id.spnImages);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		preview = new Preview(this,
				(SurfaceView) findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		((FrameLayout) findViewById(R.id.layout)).addView(preview);
		preview.setKeepScreenOn(true);

		preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imagesList = getBaseContext().getResources().getStringArray(
						R.array.Tower);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CamTestActivity.this);
				// Title
				builder.setTitle("Choose image name :");
				builder.setSingleChoiceItems(imagesList, -1,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								imgName = imagesList[which];
								camera.takePicture(shutterCallback, rawCallback, jpegCallback);
								//saveimg();
								dialog.dismiss();

							}
						});

				builder.create();
				builder.show();
				
				
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		int numCams = Camera.getNumberOfCameras();
		if (numCams > 0) {
			try {
				camera = Camera.open(0);
				camera.startPreview();
				preview.setCamera(camera);
			} catch (RuntimeException ex) {

			}
		}

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		if (camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
		
	}

	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		
		
		
		
		public void onPictureTaken(byte[] data, Camera camera) {

			saveimg();

			// new SaveImageTask().execute(data);
			resetCam();
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

	@SuppressLint("NewApi")
	private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

		@TargetApi(Build.VERSION_CODES.CUPCAKE)
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CamTestActivity.this);
			dialog.setMessage("Saving...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(byte[]... data) {
			FileOutputStream outStream = null;

			// Write to SD Card
			try {

				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File(sdCard.getAbsolutePath() + "/TNS");
				dir.mkdirs();

				String fileName = String.format("%d.jpg",
						System.currentTimeMillis());
				File outFile = new File(dir, fileName);

				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();

				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length
						+ " to " + outFile.getAbsolutePath());

				refreshGallery(outFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

		}

	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

//		if (degree <= 270) {
//			degree = degree + 90;
			tvHeading.setText("" + Float.toString(degree) + " degrees");
//		} else {
//			tvHeading.setText("" + Float.toString(degree) + " from north");
//		}

		// create a rotation animation (reverse turn degree degrees)
	/*	RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);*/
		currentDegree = degree;

	}

	@SuppressLint("NewApi")
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}

	public void saveimg() {
		try {
			String imgType = "";
			if (imgName.startsWith("T")) {
				imgType = "TOWER";
				Log.w("tw", "T");
			} else if (imgName.startsWith("S")) {
				imgType = "GROUND";
				Log.w("gr", "S");
			} else {
				imgType = "BUILDING";
				Log.w("blsg", "B");
			}
			File f = new File(Environment.getExternalStorageDirectory() + ipid
					+ "_" + imgName + "_" + imgName + ".png");

			objAdapter = new AccessData(getBaseContext());
			objAdapter.openDataBase();

			long i = objAdapter.saveImagePathToDatabase(f.getAbsolutePath(),
					ipid, imgName, imgType);
			Toast.makeText(this, String.valueOf(i), Toast.LENGTH_SHORT).show();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(dataImg);
			fos.close();
			objAdapter.close();
			Log.w("done", "camera");
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

	}
}
