package com.example.tns;

public class BuildingDetailsDesign {

	 private int bldgNumber;
	  private float latitude;
	  private float longitude;
	  private float distance;
	  private float azimuth;
	  private float height;
	  private int floors;
	  
	  
	  public BuildingDetailsDesign(){
		  
	  }
	  
	  public BuildingDetailsDesign(int num,float lat,float longi,float dist,float azi , int floors,float height){
		  this.bldgNumber = num;
		  this.latitude = lat;
		  this.latitude = longi;
		  this.distance = dist;
		  this.azimuth = azi;
		  this.floors = floors;
		  this.height = height;
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
	  
	  
	  public int getbldgNumber() {
		    return bldgNumber;
		  }

		  public void setbldgNumber(int cornerNumber) {
		    this.bldgNumber = cornerNumber;
		  }
		  
		  public float getazimuth() {
			    return azimuth;
			  }

	    public void setazimuth(float azi) {
			    this.azimuth =azi;
			  }
	    
		  public float getheight() {
			    return height;
			  }

	    public void setheight(float height) {
			    this.height =height;
			  }
		
	    public int getfloors() {
		    return floors;
		  }

    public void setfloors(int floors) {
		    this.floors =floors;
		  }

	
}

