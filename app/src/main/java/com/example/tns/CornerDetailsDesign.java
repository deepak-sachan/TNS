package com.example.tns;

public class CornerDetailsDesign {

	 private int cornerNumber;
	  private float latitude;
	  private float longitude;
	  private float distance;
	  
	  
	  public CornerDetailsDesign(){
		  
	  }
	  
	  public CornerDetailsDesign(int num,float lat,float longi,float dist){
		  this.cornerNumber = num;
		  this.latitude = lat;
		  this.latitude = longi;
		  this.distance = dist;
	  }

	  public float getlatitude() {
	    return latitude;
	  }

	  public void setlatitude(float latitude) {
	    this.latitude = latitude;
	  }
	  
	  public float getlongitude() {
		    return longitude;
	  }

	  public void setlongitude(float longitude) {
		    this.longitude = longitude;
	  }
	  
	  public float getdistance() {
		    return distance;
		  }

		  public void setdistance(float distance) {
		    this.distance = distance;
		  }
	  
	  
	  public int getcornerNumber() {
		    return cornerNumber;
		  }

		  public void setcornerNumber(int cornerNumber) {
		    this.cornerNumber = cornerNumber;
		  }
		  
			
	
}

