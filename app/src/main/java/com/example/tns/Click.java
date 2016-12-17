package com.example.tns;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Click extends SurfaceView implements SurfaceHolder.Callback{
	
	 private SurfaceHolder mSurfaceHolder;
	     private Camera mCamera;


	public Click(Context context,Camera camera) {
		super(context);
		this.mCamera = camera;
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		try{
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		}catch(IOException ex){
			
		}
		
	}
	
	public void startAgain(Camera camera){
		this.mCamera = camera;
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		try{
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();
		}catch(IOException ex){
			
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {			
			            mCamera.setPreviewDisplay(holder);
			
			            mCamera.startPreview();
			
			        } catch (IOException e) {
			
			          // left blank for now
			
			        }
			
			 
			
			    }

		
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera.stopPreview();
		mCamera.release();
	}

}
