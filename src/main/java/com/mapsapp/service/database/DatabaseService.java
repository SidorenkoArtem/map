package com.mapsapp.service.database;

import com.mapsapp.domain.*;

import java.util.List;

public interface DatabaseService {
	List<BelEnergoCompany> getAllBelEnergoCompanies();
    List<BelEnergoCompany> getAllOrderedBelEnergoCompanies();
    //List<VehicleCoordinate> getAllCoordinatesVihicle(long[] codes);
    List<VehicleCoordinate> getAllCoordinatesVihicle(long[] codes, String dates, String lastDate);
    List<Integer> getDayWhenVehicleMove(String month, String year, long id);
    List<Vehicle> getVehicleByCode(long[] codes);
    List<DistrictPowerStation> getDistrictPowerStationsByCodes(long[] codes);
    List<Sector> getSectorsByCodes(long[] codes);
    List<AirLine10> getAirLines10ByCodes(long[] codes);
    List<AirLine> getAirLinesByCodes(long[] codes);
    List<Vehicle> getVehicleByCodes(long[] codes);
    List<Coordinates> getCoorditatesTrunkLinesByCodes(long[] codes);
    List<Coordinates> getCoordinatesAirLines10ByCodes(long[] codes);
    byte[] getSubstationSchemeByCode(long id);
    byte[] getAirLine10SchemeByCode(long id);
    List<DataAvailability> checkForExistenceDifferentPillarsData(long[] codes);
    byte[] getPillarMediaData(long id, String type);
    byte[] getPillarInfoData(long id, String type);
}