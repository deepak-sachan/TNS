package com.example.tns.ak;

public class WriteTextLayout {

	float x , y;
	int id;
	String name;
	//constructor
	public WriteTextLayout(int id , float x , float y,String name){
		this.id = id;
		this.x = x;
		this.y = y;		
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
}
