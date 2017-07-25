package com.mapsapp.domain;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VehicleCoordinate {
	private long id;
	private double latitude;
	private double longitude;	
	private String date;
	private Time time;
	private String waitTime;
	private boolean FirstPoint;
	private statusVehicle stat;
	private int distance;
	private int srSpeed;

	public VehicleCoordinate(){
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDate() {
		return date;
	}

	public void setDate(Date date) {
		SimpleDateFormat oldDateFormat =new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
		try {
			String dateee = date.toString();
			System.out.println(dateee);
			date = oldDateFormat.parse(dateee);
			System.out.println(date);
			this.date = newDateFormat.format(date);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Date getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public boolean isFirstPoint() {
		return FirstPoint;
	}

	public void setFirstPoint(boolean firstPoint) {
		FirstPoint = firstPoint;
	}

	public statusVehicle getStat() {
		return stat;
	}

	public void setStat(statusVehicle stat) {
		this.stat = stat;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		//long t = time.getTime();
		//this.timeTemp = 
		this.waitTime = String.format("%02d:%02d:%02d", waitTime / 1000 / 3600, waitTime / 1000 / 60 % 60, waitTime / 1000 % 60);
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSrSpeed() {
		return srSpeed;
	}

	public void setSrSpeed(int srSpeed) {
		this.srSpeed = srSpeed;
	}

	@Override
	public String toString() {
		return "VehicleCoordinate [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", date=" + date
				+ ", time=" + time + ", waitTime=" + waitTime + ", FirstPoint=" + FirstPoint + ", stat=" + stat 
				+ ", distance=" + distance +  "]";
	}
	
}
