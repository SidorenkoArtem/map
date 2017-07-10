package com.mapsapp.domain;

public class Vehicle {
	private long id;
	private long company_id;
	private String name;
	private String model;
	private String regNumber;
	private String driverName;
	private String phoneNumber;
	private float lastPositionX;
	private float lastPositionY;

	public Vehicle() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public float getLastPositionX() {
		return lastPositionX;
	}

	public void setLastPositionX(float lastPositionX) {
		this.lastPositionX = lastPositionX;
	}

	public float getLastPositionY() {
		return lastPositionY;
	}

	public void setLastPositionY(float lastPositionY) {
		this.lastPositionY = lastPositionY;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String reg_number) {
		this.regNumber = reg_number;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driver_name) {
		this.driverName = driver_name;
	}
}