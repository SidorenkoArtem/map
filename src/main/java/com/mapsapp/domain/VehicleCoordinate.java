package com.mapsapp.domain;

import java.sql.Time;
import java.util.Date;

public class VehicleCoordinate {
	private long id;
	
	private double latitude;
	private double longitude;	
	private Date date;
	private Time time;

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
}
