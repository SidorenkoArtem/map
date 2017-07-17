package com.mapsapp.domain;

public class Calculation {
	public static double getDistance(double latPrev, double lngPrev, double lat, double lng){
		long r = 6372795;
		//перевод координат в радианы
        latPrev *= Math.PI / 180;
        lat *= Math.PI / 180;
        lngPrev *= Math.PI / 180;
        lng *= Math.PI / 180;
        //вычисление синусов и косинусов широт и разницы долгот
        double clPrev = Math.cos(latPrev);
        double cl = Math.cos(lat);
        double slPrev = Math.sin(latPrev);
        double sl = Math.sin(lat);
        double delta = lng - lngPrev;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);
        //вычисления длины большого круга
        double y = Math.sqrt(Math.pow(cl * sdelta, 2) + Math.pow(clPrev * sl - slPrev * cl * cdelta, 2));
        double x = slPrev * sl + clPrev * cl * cdelta;
        double ad = Math.atan2(y, x);
        double dist = ad * r; //расстояние между двумя координатами в метрах
		return dist;
	}

}
