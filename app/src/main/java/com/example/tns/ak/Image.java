package com.example.tns.ak;

public class Image {

	int id,uniqueID;
	float x , y;
	int widthI , heightI;
	String name;
	//constructor
	public Image(int id , float x , float y,int wI,int hI,int uniqueID,String name){
		this.id = id;
		this.x = x;
		this.y = y;
		this.widthI = wI;
		this.heightI = hI;
		this.uniqueID = uniqueID;
		this.name = name;
	}
	
	//get methods...
	public int getID(){
		return id;
	}
	
	public void setID(int iD){
		this.id = iD;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name  = name;
	}
	
	
	public int getuniqueID(){
		return uniqueID;
	}
	
	public void setuniqueID(int uniqueID){
		this.uniqueID= uniqueID;
	}
	
	public float getXCoordinate(){
		return x;
	}
	
	
	public float getYCoordinate(){
		return y;
	}
	
	public void setXCoordinate(float xI){
		this.x = xI;
	}
	
	public void setYCoordinate(float yI){
		this.y = yI;
	}
	
	public int getW(){
		return widthI;
	}
	
	public void setW(int wI){
		this.widthI = wI;
	}
	
	public int getH(){
		return heightI;
	}
	
	public void setH(int hI){
		this.heightI = hI;
	}
	
}	