package com.mapsapp.domain;

import java.sql.Time;
import java.util.Date;

public class VehicleCoordinate {
	private long id;
	private double latitude;
	private double longitude;	
	private Date date;
	private Time time;
	private long waitTime;
	private boolean FirstPoint;
	private statusVehicle stat;
	private int distance;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "VehicleCoordinate [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", date=" + date
				+ ", time=" + time + ", waitTime=" + waitTime + ", FirstPoint=" + FirstPoint + ", stat=" + stat 
				+ ", distance=" + distance +  "]";
	}
	
}
