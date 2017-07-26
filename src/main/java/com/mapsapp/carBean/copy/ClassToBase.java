package com.mapsapp.carBean.copy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mapsapp.domain.*;

public class ClassToBase {
	public List<BelEnergoCompany> getAllBelEnergoCompanies(){
		List<BelEnergoCompany> companies = new ArrayList<>();
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = null;
			try{
				conn = DriverManager.getConnection("jdbc:sqlserver://192.168.51.161:1433;databaseName=gis_db", "sa",
						"123");
				Statement st = null;
				try{
					st = conn.createStatement();
					ResultSet rs = null;
					try{
						rs = 	st.executeQuery("Select id, name from company order by 1");
						while(rs.next()){
							//BelEnergoCompany company = new BelEnergoCompany();
							//company.setId();
						}
					}catch(Exception e){					
					}
				}catch(Exception e){
				}
			}catch(Exception e){
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
