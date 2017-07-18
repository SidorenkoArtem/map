package com.mapsapp.service.database;

import com.mapsapp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DatabaseServiceImpl implements DatabaseService {
	@Autowired
	private DataSource dataSource;
	//@Autowired
	//private DataSource dataSource1;

	@Override
	public List<BelEnergoCompany> getAllBelEnergoCompanies() {
		List<BelEnergoCompany> companies = new ArrayList<>();

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=gis_db",
					"sa", "123");
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select id, name from company");
			while (resultSet.next()) {
				BelEnergoCompany belEnergoCompany = new BelEnergoCompany();
				belEnergoCompany.setId(resultSet.getLong(1));
				belEnergoCompany.setName(resultSet.getString(2));
				companies.add(belEnergoCompany);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return companies;
	}

	@Override
	public List<Vehicle> getVehicleByCodes(long[] codes) {
		List<Vehicle> vehicles = new ArrayList<>();
		if (codes == null)
			return vehicles;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=gis_db",
					"sa", "123");
			Statement statement = connection.createStatement();
			String in = conversion(codes);
			ResultSet resultSet = statement.executeQuery("Select id, model, last_position.x, "
					+ "last_position.y, vehicle.reg_number, vehicle.driver_name, vehicle.phone_number "
					+ "from vehicle " + "left join last_position on (last_position.vehicle_id = vehicle.id) "
					+ "where company_id =" + in + "Order by model");
			//double previousX, previousY;
			while (resultSet.next()) {
				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong(1));
				vehicle.setName(resultSet.getString(2));
				vehicle.setLastPositionX(resultSet.getFloat(3));
				vehicle.setLastPositionY(resultSet.getFloat(4));
				vehicle.setRegNumber(resultSet.getString(5));
				vehicle.setDriverName(resultSet.getString(6));
				vehicle.setPhoneNumber(resultSet.getString(7));
				vehicles.add(vehicle);
				/*
				 * AirLine airLine = new AirLine();
				 * airLine.setId(resultSet.getLong(1));
				 * airLine.setId1(resultSet.getLong(1));
				 * airLine.setName(resultSet.getString(2));
				 * airLines.add(airLine);
				 */
			}
		} catch (Exception e) {
		}
		return vehicles;
	}

	@Override
	public List<VehicleCoordinate> getAllCoordinatesVihicle(long[] codes, String dates) {
		List<VehicleCoordinate> coordinates = new ArrayList<>();
		if (codes == null) {
			return coordinates;
		}
		boolean isWait = false;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=gis_db",
					"sa", "123");
			Statement statement = connection.createStatement();
			String in = conversion(codes);
			String s = dates.replace('-', '.');
			ResultSet resultSet = statement
					.executeQuery("Select timestamp, x, y from position " + " where (vehicle_id = " + in
							+ ") and convert(varchar, timestamp, 104) = '" + s + "' order by timestamp");
			Time prevTime = null;
			double allDistance = 0;
			double distance;
			VehicleCoordinate prevVehicleCoordinate = null;
			while (resultSet.next()) {
				VehicleCoordinate vehicleCoordinate = new VehicleCoordinate();
				vehicleCoordinate.setId(codes[0]);
				if (prevTime != null) {
					distance = Calculation.getDistance(prevVehicleCoordinate.getLatitude(), prevVehicleCoordinate.getLongitude(), 
							resultSet.getDouble(3), resultSet.getDouble(2));
					allDistance +=distance;
					if (resultSet.getTime(1).getTime() - prevTime.getTime() > 600000) {
						vehicleCoordinate.setStat(statusVehicle.DISCONNECT);
					}
					if (allDistance > 100){
						vehicleCoordinate.setStat(statusVehicle.SHOWPOINT);
						vehicleCoordinate.setDistance((int)allDistance);
						allDistance = 0;	
					}
					if (isWait == true) {
						prevVehicleCoordinate.setWaitTime(resultSet.getTime(1).getTime() - prevVehicleCoordinate.getTime().getTime());
						prevVehicleCoordinate.setStat(statusVehicle.WAIT);
					}

					if (prevVehicleCoordinate.getLatitude() == resultSet.getDouble(3)
							&& prevVehicleCoordinate.getLongitude() == resultSet.getDouble(2))
						isWait = true;
					else 
					    isWait = false;
					
					prevTime = resultSet.getTime(1);
				} else {
					prevTime = resultSet.getTime(1);
					vehicleCoordinate.setDate(resultSet.getTime(1));
					vehicleCoordinate.setTime(resultSet.getTime(1));
				}
				if (isWait != true) {
					vehicleCoordinate.setDate(resultSet.getDate(1));
					vehicleCoordinate.setTime(resultSet.getTime(1));
					vehicleCoordinate.setLongitude(resultSet.getDouble(2));
					vehicleCoordinate.setLatitude(resultSet.getDouble(3));
					prevVehicleCoordinate = vehicleCoordinate;
					coordinates.add(vehicleCoordinate);
				}
			}
			for (VehicleCoordinate v : coordinates) {
				System.out.println(v);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return coordinates;

	}

	@Override
	@Transactional(readOnly = true)
	public List<BelEnergoCompany> getAllOrderedBelEnergoCompanies() {
		List<BelEnergoCompany> companies = new ArrayList<>();
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			ResultSet resultSet = statement.executeQuery(
					"SELECT id_spredpri, naimp FROM SPREDPRI where id_spredpri IN (Select distinct id_spredpri From oc) "
							+ "or id_spredpri IN (SELECT distinct id_spredpri FROM S_RES WHERE id_res IN"
							+ " (SELECT id_res FROM S_uch WHERE id_uch IN (SELECT distinct id_uch FROM oc10) )) ORDER BY naimp");
			while (resultSet.next()) {
				BelEnergoCompany belEnergoCompany = new BelEnergoCompany();
				belEnergoCompany.setId(resultSet.getLong(1));
				belEnergoCompany.setName(resultSet.getString(2));
				companies.add(belEnergoCompany);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return companies;
	}

	private String conversion(long[] array) {
		String result = "";
		for (int i = 0; i < array.length - 1; ++i) {
			result += array[i] + ", ";
		}
		result += array[array.length - 1];
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DistrictPowerStation> getDistrictPowerStationsByCodes(long[] codes) {
		List<DistrictPowerStation> companies = new ArrayList<>();

		if (codes == null)
			return companies;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery(
					"SELECT id_res, nampr, id_spredpri FROM S_RES WHERE id_spredpri IN (" + in + ") ORDER BY nampr");
			while (resultSet.next()) {
				DistrictPowerStation districtPowerStation = new DistrictPowerStation();
				districtPowerStation.setId(resultSet.getLong(1));
				districtPowerStation.setName(resultSet.getString(2));
				districtPowerStation.setIdBelEnergoCompany(resultSet.getLong(3));
				companies.add(districtPowerStation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return companies;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sector> getSectorsByCodes(long[] codes) {
		List<Sector> sectors = new ArrayList<>();

		if (codes == null)
			return sectors;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery(
					"SELECT id_uch, namuch, id_res FROM S_UCH WHERE id_res IN (SELECT id_res FROM S_RES WHERE id_spredpri IN ("
							+ in + ")) ORDER BY namuch");
			while (resultSet.next()) {
				Sector sector = new Sector();
				sector.setId(resultSet.getLong(1));
				sector.setName(resultSet.getString(2));
				sector.setIdDistrictPowerStation(resultSet.getLong(3));
				sectors.add(sector);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sectors;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AirLine10> getAirLines10ByCodes(long[] codes) {
		List<AirLine10> airLines10 = new ArrayList<>();

		if (codes == null)
			return airLines10;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery(
					"SELECT id_oc10, indfig, oc10.nnn FROM oc10 WHERE oc10.kodot IS NULL AND indfig IS NOT NULL AND id_uch IN ("
							+ in + ") ORDER BY oc10.nnn");
			while (resultSet.next()) {
				AirLine10 airLine10 = new AirLine10();
				airLine10.setId(resultSet.getLong(1));
				airLine10.setId1(resultSet.getLong(2));
				airLine10.setName(resultSet.getString(3));
				airLines10.add(airLine10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return airLines10;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AirLine> getAirLinesByCodes(long[] codes) {
		List<AirLine> airLines = new ArrayList<>();

		if (codes == null)
			return airLines;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery(
					"SELECT id_oc, indfig, oc.nnn FROM oc WHERE oc.kodot IS NULL AND indfig IS NOT NULL AND id_spredpri IN ("
							+ in + ") ORDER BY oc.nnn");
			while (resultSet.next()) {
				AirLine airLine = new AirLine();
				airLine.setId(resultSet.getLong(1));
				airLine.setId1(resultSet.getLong(2));
				airLine.setName(resultSet.getString(3));
				airLines.add(airLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return airLines;
	}

	private Coordinates getCoordinates(ResultSet resultSet) throws SQLException {
		Coordinates coordinates = new Coordinates();
		coordinates.setId(resultSet.getLong(1));
		coordinates.setName(resultSet.getString(7));
		coordinates.setLatitude1(resultSet.getDouble(2));
		coordinates.setLongitude1(resultSet.getDouble(3));
		coordinates.setLatitude2(resultSet.getDouble(4));
		coordinates.setLongitude2(resultSet.getDouble(5));
		coordinates.setCode(resultSet.getInt(6));
		coordinates.setIdLine(resultSet.getLong(8));
		coordinates.setIdLineParent(resultSet.getLong(9));
		return coordinates;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coordinates> getCoorditatesTrunkLinesByCodes(long[] codes) {
		List<Coordinates> coordinates = new ArrayList<>();

		if (codes == null)
			return coordinates;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery("SELECT id_oc FROM oc where id_oc IN (" + in
					+ ") or id_oc IN (SELECT id_oc FROM oc WHERE id_ocr IN(" + in + "))");
			String newIN = "";
			while (resultSet.next()) {
				if (!resultSet.isLast())
					newIN += resultSet.getLong(1) + ", ";
				else
					newIN += resultSet.getLong(1);
			}

			resultSet = statement
					.executeQuery("SELECT gis$objid, gis$start_lat, gis$start_lon, gis$end_lat, gis$end_lon,"
							+ "gis$kind, gis$caption, id_oc, case when id_ocr is null then id_oc else id_ocr end\n"
							+ "from (\n" + "SELECT\n" + "gis.gis$objid,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS gis$start_lat ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS gis$start_lon,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS gis$end_lat,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS gis$end_lon,\n" + "gis.gis$kind,\n"
							+ "gis.gis$caption,\n" + "o.id_oc,\n" + "o.id_ocr\n"
							+ "FROM oc o, gis$geodatastore gis WHERE gis.gis$objid=o.indfig AND gis.gis$objid in (select o.indfig from oc where o.id_oc IN ("
							+ newIN + "))\n" + "UNION\n" + "\n" + "SELECT\n" + "gis.gis$objid,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS gis$start_lat ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS gis$start_lon,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS gis$end_lat,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS gis$end_lon,\n" + "gis.gis$kind,\n"
							+ "gis.gis$caption,\n" + "o.id_oc,\n" + "o.id_ocr\n"
							+ "FROM oc o, opor op, gis$geodatastore gis where o.id_oc=op.id_oc and gis.gis$objid=op.indfig and op.id_oc IN ("
							+ newIN + ")\n" + "UNION\n" + "SELECT\n" + "gis.gis$objid,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS gis$start_lat ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS gis$start_lon,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS gis$end_lat,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS gis$end_lon,\n" + "gis.gis$kind,\n"
							+ "gis.gis$caption,\n" + "o.id_oc,\n" + "o.id_ocr\n"
							+ "FROM  oc o, fprolet f, gis$geodatastore gis WHERE o.id_oc=f.id_oc and gis.gis$objid=f.indfig and f.id_oc IN ("
							+ newIN + ")\n" + "UNION\n" + "SELECT\n" + "gis.gis$objid,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS gis$start_lat ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS gis$start_lon,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS gis$end_lat,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS gis$end_lon,\n" + "gis.gis$kind,\n"
							+ "gis.gis$caption,\n" + "o.id_oc,\n" + "o.id_ocr\n"
							+ "FROM  oc o, podst p, vl35 vl, gis$geodatastore gis WHERE o.id_vl35=vl.id_vl35 and (vl.id_podstn=p.id_podst or vl.id_podstk=p.id_podst) and gis.gis$objid=p.indfig and o.id_oc IN ("
							+ newIN + ")\n" + ")\n" + "results\n"
							+ "ORDER BY id_ocr, id_oc, case when (gis$kind=1 or (gis$kind>215 and gis$kind<220) or (gis$kind=230) or (gis$kind=231) or (gis$kind=239)) then gis$kind-1000 else gis$kind end");

			while (resultSet.next()) {
				coordinates.add(getCoordinates(resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return coordinates;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coordinates> getCoordinatesAirLines10ByCodes(long[] codes) {
		List<Coordinates> coordinates = new ArrayList<>();

		if (codes == null)
			return coordinates;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
			String in = conversion(codes);
			String newIN = "";

			ResultSet resultSet = statement.executeQuery("SELECT indfig FROM opor10 WHERE id_oc10 IN (" + in
					+ ") OR id_oc10 IN (SELECT id_oc10 FROM oc10 WHERE id_ocr IN(" + in + "))\n"
					+ "UNION SELECT indfig FROM oc10 WHERE id_oc10 IN (" + in + ") OR id_ocr IN(" + in + ")\n"
					+ "UNION SELECT indfig FROM fprolet10 WHERE id_konop IN (SELECT id_opor10 FROM opor10 WHERE id_oc10 IN ("
					+ in + ") OR id_oc10 IN (SELECT id_oc10 FROM oc10 WHERE id_ocr IN(" + in + ")))\n"
					+ "UNION select indfig from podst where id_podst IN (select ID_PODST from vl10 where id_vl10 in (select id_vl10 from oc10 where id_oc10 IN ("
					+ in + ")  ))");
			while (resultSet.next()) {
				if (!resultSet.isLast())
					newIN += resultSet.getLong(1) + ", ";
				else
					newIN += resultSet.getLong(1);
			}

			resultSet = statement.executeQuery(
					"SELECT id, latitude1, longitude1, latitude2, longitude2, code, name, id_oc, case when id_ocr is null then id_oc else id_ocr end AS id_line_parent\n"
							+ "FROM (\n" + "SELECT\n" + "gis.gis$objid AS id,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS latitude1 ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS longitude1,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS latitude2,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS longitude2,\n"
							+ "gis.gis$kind AS code,\n" + "gis.gis$caption AS name,\n" + "o.id_oc10 AS id_oc,\n"
							+ "o.id_ocr as id_ocr\n"
							+ "FROM gis$geodatastore gis left join oc10 o on o.indfig=gis.gis$objid\n"
							+ "where o.id_oc10 IN(" + in + ")\n" + "and gis.gis$objid IN (" + newIN + ")\n" + "UNION\n"
							+ "select \n" + "gis.gis$objid AS id,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS latitude1 ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS longitude1,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS latitude2,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS longitude2,\n"
							+ "gis.gis$kind AS code,\n" + "gis.gis$caption AS name,\n" + "o.id_oc10 AS id_oc,\n"
							+ "o.id_ocr as id_ocr\n"
							+ "from gis$geodatastore gis left join opor10 op on op.indfig=gis.gis$objid left join oc10 o on o.id_oc10=op.id_oc10\n"
							+ "where op.id_oc10 IN (" + in + ")\n" + "and gis.gis$objid IN (" + newIN + ")\n"
							+ "UNION\n" + "select \n" + "gis.gis$objid AS id,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS latitude1 ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS longitude1,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS latitude2,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS longitude2,\n"
							+ "gis.gis$kind AS code,\n" + "gis.gis$caption AS name,\n" + "o.id_oc10 AS id_oc,\n"
							+ "o.id_ocr as id_ocr\n"
							+ "from gis$geodatastore gis left join fprolet10 fp on fp.indfig=gis.gis$objid left join opor10 op10 on op10.id_opor10=fp.id_konop left join oc10 o on o.id_oc10=op10.id_oc10\n"
							+ "where o.id_oc10 IN (" + in + ")\n" + "and  gis.gis$objid IN (" + newIN + ")\n"
							+ "UNION\n" + "select \n" + "gis.gis$objid AS id,\n"
							+ "CAST (gis.gis$start_lat AS numeric(16,6))/1000000 AS latitude1 ,\n"
							+ "CAST (gis.gis$start_lon AS numeric(16,6))/1000000 AS longitude1,\n"
							+ "CAST (gis.gis$end_lat AS numeric(16,6))/1000000 AS latitude2,\n"
							+ "CAST (gis.gis$end_lon AS numeric(16,6))/1000000 AS longitude2,\n"
							+ "gis.gis$kind AS code,\n" + "gis.gis$caption AS name,\n" + "o.id_oc10 AS id_oc,\n"
							+ "o.id_ocr as id_ocr\n"
							+ "from gis$geodatastore gis left join podst p on p.indfig=gis.gis$objid left join vl10 vl on p.id_podst=vl.id_podst left join oc10 o on o.id_vl10=vl.id_vl10\n"
							+ "where o.id_oc10 IN (" + in + ")\n" + "and  gis.gis$objid IN (" + newIN + ")\n"
							+ " ) results\n"
							+ "ORDER BY id_ocr, id_oc, case when (code=1 or (code>215 and code<220) or (code=230) or (code=231) or (code=239)) then code-1000 else code end\n");
			while (resultSet.next()) {
				coordinates.add(getCoordinates(resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return coordinates;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getSubstationSchemeByCode(long id) {
		byte[] substationScheme = null;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT shema FROM podst WHERE indfig = " + id);
			substationScheme = getBytesFromBlob(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return substationScheme;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getAirLine10SchemeByCode(long id) {
		byte[] airLine10Scheme = null;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT shema FROM oc10 WHERE indfig = " + id);
			airLine10Scheme = getBytesFromBlob(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return airLine10Scheme;
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<DataAvailability> checkForExistenceDifferentPillarsData(long[] codes) {
		HashMap<Long, DataAvailability> result = new HashMap<>();

		if (codes == null)
			return new ArrayList<>(result.values());

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			String in = conversion(codes);

			ResultSet resultSet = statement.executeQuery(
					"SELECT indfig, photo1, photo2, photo3, voice, video1, video2 FROM session where indfig IN (" + in
							+ ")");
			while (resultSet.next()) {
				long id = resultSet.getLong(1);
				DataAvailability dataAvailability = new DataAvailability(id);

				dataAvailability.addValue("photo1",
						(resultSet.getBlob(2) == null) ? "" : "/pillar/media/" + id + "/photo1");
				dataAvailability.addValue("photo2",
						(resultSet.getBlob(3) == null) ? "" : "/pillar/media/" + id + "/photo2");
				dataAvailability.addValue("photo3",
						(resultSet.getBlob(4) == null) ? "" : "/pillar/media/" + id + "/photo3");
				dataAvailability.addValue("voice",
						(resultSet.getBlob(5) == null) ? "" : "/pillar/media/" + id + "/voice");
				dataAvailability.addValue("video1",
						(resultSet.getBlob(6) == null) ? "" : "/pillar/media/" + id + "/video1");
				dataAvailability.addValue("video2",
						(resultSet.getBlob(7) == null) ? "" : "/pillar/media/" + id + "/video2");

				result.put(id, dataAvailability);
			}

			resultSet = statement.executeQuery(
					"SELECT opor10.indfig, s_shifrop.shema, s_shifrop.ima1, s_shifrop.shema1, s_shifrop.shema2 "
							+ "FROM s_shifrop, opor10 WHERE opor10.id_sshifrop = s_shifrop.id_sshifrop and opor10.indfig IN ("
							+ in + ")");

			while (resultSet.next()) {
				long id = resultSet.getLong(1);

				if (result.containsKey(id)) {
					DataAvailability dataAvailability = result.get(id);
					dataAvailability.addValue("scheme",
							(resultSet.getBlob(2) == null) ? "" : "/pillar/info/" + id + "/scheme");
					dataAvailability.addValue("image1",
							(resultSet.getBlob(3) == null) ? "" : "/pillar/info/" + id + "/image1");
					dataAvailability.addValue("image2",
							(resultSet.getBlob(4) == null) ? "" : "/pillar/info/" + id + "/scheme1");
					dataAvailability.addValue("image3",
							(resultSet.getBlob(5) == null) ? "" : "/pillar/info/" + id + "/scheme2");
				} else {
					DataAvailability dataAvailability = new DataAvailability(id);
					dataAvailability.addValue("scheme",
							(resultSet.getBlob(2) == null) ? "" : "/pillar/info/" + id + "/scheme");
					dataAvailability.addValue("image1",
							(resultSet.getBlob(3) == null) ? "" : "/pillar/info/" + id + "/image1");
					dataAvailability.addValue("image2",
							(resultSet.getBlob(4) == null) ? "" : "/pillar/info/" + id + "/scheme1");
					dataAvailability.addValue("image3",
							(resultSet.getBlob(5) == null) ? "" : "/pillar/info/" + id + "/scheme2");
					dataAvailability.addValue("photo1", "");
					dataAvailability.addValue("photo2", "");
					dataAvailability.addValue("photo3", "");
					dataAvailability.addValue("voice", "");
					dataAvailability.addValue("video1", "");
					dataAvailability.addValue("video2", "");
					result.put(id, dataAvailability);
				}
			}

			ArrayList<DataAvailability> preResult = new ArrayList<>(result.values());
			for (DataAvailability data : preResult) {
				if (!data.containsKey("scheme")) {
					data.addValue("scheme", "");
					data.addValue("image1", "");
					data.addValue("image2", "");
					data.addValue("image3", "");
				}
			}
			return preResult;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(result.values());
	}

	private byte[] getBytesFromBlob(ResultSet resultSet) throws SQLException {
		byte[] result = null;
		if (resultSet.next()) {
			Blob blob = resultSet.getBlob(1);
			if (blob != null) {
				result = blob.getBytes(1, (int) blob.length());
				blob.free();
			}
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getPillarMediaData(long id, String type) {
		byte[] pillarMedia = null;

		if ((type == null) || (type.length() < 3))
			return null;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT " + type + " FROM session where indfig = " + id);

			pillarMedia = getBytesFromBlob(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pillarMedia;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getPillarInfoData(long id, String type) {
		byte[] pillarInfo = null;

		if ((type == null) || (type.length() < 3))
			return null;

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT " + type + " FROM s_shifrop, opor10 "
					+ "WHERE opor10.id_sshifrop = s_shifrop.id_sshifrop and opor10.indfig = " + id);

			pillarInfo = getBytesFromBlob(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pillarInfo;
	}
}

// image2 = scheme1, image3 = scheme2